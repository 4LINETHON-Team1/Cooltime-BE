package com.likelion.fourthlinethon.team1.cooltime.report.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.fourthlinethon.team1.cooltime.global.common.time.period.ClampedPeriod;
import com.likelion.fourthlinethon.team1.cooltime.global.common.time.period.PeriodGuard;
import com.likelion.fourthlinethon.team1.cooltime.global.common.time.period.WeekPeriod;
import com.likelion.fourthlinethon.team1.cooltime.log.entity.DailyLog;
import com.likelion.fourthlinethon.team1.cooltime.log.entity.LogActivity;
import com.likelion.fourthlinethon.team1.cooltime.log.entity.LogReason;
import com.likelion.fourthlinethon.team1.cooltime.log.repository.DailyLogRepository;
import com.likelion.fourthlinethon.team1.cooltime.log.repository.LogActivityRepository;
import com.likelion.fourthlinethon.team1.cooltime.log.repository.LogReasonRepository;
import com.likelion.fourthlinethon.team1.cooltime.report.dto.request.PatternAnalysisRequest;
import com.likelion.fourthlinethon.team1.cooltime.report.dto.response.AiWeeklyReportResponse;
import com.likelion.fourthlinethon.team1.cooltime.report.dto.response.PatternAnalysisResponse;
import com.likelion.fourthlinethon.team1.cooltime.report.entity.AiWeeklyReport;
import com.likelion.fourthlinethon.team1.cooltime.report.repository.AiWeeklyReportRepository;
import com.likelion.fourthlinethon.team1.cooltime.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReportService {
    private final AiWeeklyReportRepository aiWeeklyReportRepository;
    private final DailyLogRepository dailyLogRepository;
    private final LogActivityRepository logActivityRepository;
    private final LogReasonRepository logReasonRepository;
    private final OpenAiService openAiService;
    private final ObjectMapper objectMapper;

    @Transactional
    public AiWeeklyReportResponse getAiWeeklyReport(User user, WeekPeriod period) {
        // 1) 회원 가입일 및 오늘 날짜 조회
        LocalDate signup = user.getCreatedAt().toLocalDate();
        LocalDate today = LocalDate.now();

        // 2) 기간 클램프 및 유효성 검사
        ClampedPeriod clamped = PeriodGuard.clamp(signup, period, today);

        if (clamped.invalid() || period.isCurrentWeek()) {
            log.warn("[서비스] AI 주간 보고서 요청 - 범위 벗어남: userId={}, requestedPeriod={}, clampedPeriod={}",
                    user.getId(), period, clamped);
            return AiWeeklyReportResponse.outOfRange(
                    String.valueOf(period.year()),
                    String.valueOf(period.month()),
                    String.valueOf(period.weekOfMonthAuto())
        }

        // 3) AI 레포트가 존재하면 반환, 없으면 새로 생성
        return aiWeeklyReportRepository
                .findByUserAndWeekStart(user, period.getStart())
                .map(AiWeeklyReportResponse::of)
                .orElseGet(() -> AiWeeklyReportResponse.of(createAiWeeklyReport(user, period)));
    }

    private AiWeeklyReport createAiWeeklyReport(User user, WeekPeriod weekPeriod) {
        LocalDate weekStart = weekPeriod.getStart();
        LocalDate weekEnd = weekPeriod.getEnd();

        // 1) 현재 주 데이터 조회
        List<DailyLog> currentWeekLogs = dailyLogRepository.findAllByUserAndDateBetween(user, weekStart, weekEnd);

        // 2) 기록이 2일 이하이면 레포트 본문 null로 저장
        if (currentWeekLogs.size() <= 2) {
            log.info("[서비스] 이번 주 기록이 2일 이하입니다. userId={}, weekStart={}, logCount={}",
                    user.getId(), weekStart, currentWeekLogs.size());
            AiWeeklyReport report = AiWeeklyReport.createReport(user, weekStart, null, null, null);
            return aiWeeklyReportRepository.save(report);
        }

        // 3) 현재 주 데이터 구조 생성
        PatternAnalysisRequest.WeekData currentWeekData = toWeekData(currentWeekLogs);

        // 4) 지난 주 데이터 조회
        WeekPeriod lastWeekPeriod = (WeekPeriod) weekPeriod.prev();
        LocalDate lastWeekStart = lastWeekPeriod.getStart();
        LocalDate lastWeekEnd = lastWeekPeriod.getEnd();
        List<DailyLog> lastWeekLogs = dailyLogRepository.findAllByUserAndDateBetween(user, lastWeekStart, lastWeekEnd);

        // 5) 지난 주 데이터 구조 생성 (2일 이하이면 null)
        PatternAnalysisRequest.WeekData lastWeekData = null;
        if (lastWeekLogs.size() > 2) {
            lastWeekData = toWeekData(lastWeekLogs);
        }

        // 6) PatternAnalysisRequest 생성
        PatternAnalysisRequest request = PatternAnalysisRequest.builder()
                .patternType(user.getMytype())
                .currentWeek(currentWeekData)
                .lastWeek(lastWeekData)
                .build();

        // 7) JSON으로 변환
        String requestJson;
        try {
            requestJson = objectMapper.writeValueAsString(request);
            log.info("[서비스] OpenAI 요청 JSON: {}", requestJson);
        } catch (JsonProcessingException e) {
            log.error("[서비스] JSON 변환 실패", e);
            throw new RuntimeException("AI 분석 요청 생성 중 오류가 발생했습니다.", e);
        }

        // 8) OpenAI API 호출
        String responseJson = openAiService.analyzePostponePattern(requestJson);

        // 9) 응답 파싱
        PatternAnalysisResponse response;
        try {
            response = objectMapper.readValue(responseJson, PatternAnalysisResponse.class);
            log.info("[서비스] OpenAI 응답 파싱 완료");
        } catch (JsonProcessingException e) {
            log.error("[서비스] OpenAI 응답 파싱 실패: {}", responseJson, e);
            throw new RuntimeException("AI 분석 응답 처리 중 오류가 발생했습니다.", e);
        }

        // 10) AiWeeklyReport 생성 및 저장
        AiWeeklyReport report = AiWeeklyReport.createReport(
                user,
                weekStart,
                response.getPatternAnalysis(),
                response.getSolution(),
                response.getWeeklyComparison()
        );

        return aiWeeklyReportRepository.save(report);
    }

    /**
     * DailyLog 리스트를 기반으로 WeekData 생성
     */
    private PatternAnalysisRequest.WeekData toWeekData(List<DailyLog> logs) {
        int totalLogCount = logs.size();

        // 미룸 기록만 필터링
        List<DailyLog> postponedLogs = logs.stream()
                .filter(DailyLog::isPostponed)
                .toList();

        int postponedLogCount = postponedLogs.size();

        // 카테고리 통계 생성
        Map<String, Integer> categoryCountMap = new HashMap<>();
        for (DailyLog log : postponedLogs) {
            List<LogActivity> activities = logActivityRepository.findByLog(log);
            for (LogActivity activity : activities) {
                String categoryName = activity.getActivity().getName();
                categoryCountMap.put(categoryName, categoryCountMap.getOrDefault(categoryName, 0) + 1);
            }
        }

        List<PatternAnalysisRequest.CategoryStat> categoryStats = categoryCountMap.entrySet().stream()
                .map(entry -> PatternAnalysisRequest.CategoryStat.builder()
                        .category(entry.getKey())
                        .count(entry.getValue())
                        .build())
                .collect(Collectors.toList());

        // 이유 통계 생성
        Map<String, Integer> reasonCountMap = new HashMap<>();
        for (DailyLog log : postponedLogs) {
            List<LogReason> reasons = logReasonRepository.findByLog(log);
            for (LogReason reason : reasons) {
                String reasonName = reason.getReason().getName();
                reasonCountMap.put(reasonName, reasonCountMap.getOrDefault(reasonName, 0) + 1);
            }
        }

        List<PatternAnalysisRequest.ReasonStat> reasonStats = reasonCountMap.entrySet().stream()
                .map(entry -> PatternAnalysisRequest.ReasonStat.builder()
                        .reason(entry.getKey())
                        .count(entry.getValue())
                        .build())
                .collect(Collectors.toList());

        return PatternAnalysisRequest.WeekData.builder()
                .totalLogCount(totalLogCount)
                .postponedLogCount(postponedLogCount)
                .categoryStats(categoryStats)
                .reasonStats(reasonStats)
                .build();
    }
}
