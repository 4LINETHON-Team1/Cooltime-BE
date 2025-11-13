package com.likelion.fourthlinethon.team1.cooltime.log.service;

import com.likelion.fourthlinethon.team1.cooltime.badge.service.UserStreakService;
import com.likelion.fourthlinethon.team1.cooltime.global.exception.CustomException;
import com.likelion.fourthlinethon.team1.cooltime.log.dto.DailyLogCalendarResponse;
import com.likelion.fourthlinethon.team1.cooltime.log.dto.DailyLogDetailResponse;
import com.likelion.fourthlinethon.team1.cooltime.log.dto.DailyLogRequest;
import com.likelion.fourthlinethon.team1.cooltime.log.dto.DailyLogResponse;
import com.likelion.fourthlinethon.team1.cooltime.log.dto.MonthlyLogSummaryResponse;
import com.likelion.fourthlinethon.team1.cooltime.log.entity.ActivityTag;
import com.likelion.fourthlinethon.team1.cooltime.log.entity.DailyLog;
import com.likelion.fourthlinethon.team1.cooltime.log.entity.LogActivity;
import com.likelion.fourthlinethon.team1.cooltime.log.entity.LogReason;
import com.likelion.fourthlinethon.team1.cooltime.log.entity.ReasonTag;
import com.likelion.fourthlinethon.team1.cooltime.log.exception.DailyLogErrorCode;
import com.likelion.fourthlinethon.team1.cooltime.log.repository.ActivityTagRepository;
import com.likelion.fourthlinethon.team1.cooltime.log.repository.DailyLogRepository;
import com.likelion.fourthlinethon.team1.cooltime.log.repository.LogActivityRepository;
import com.likelion.fourthlinethon.team1.cooltime.log.repository.LogReasonRepository;
import com.likelion.fourthlinethon.team1.cooltime.log.repository.ReasonTagRepository;
import com.likelion.fourthlinethon.team1.cooltime.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DailyLogService {

    private final DailyLogRepository dailyLogRepository;
    private final ActivityTagRepository activityTagRepository;
    private final ReasonTagRepository reasonTagRepository;
    private final LogActivityRepository logActivityRepository;
    private final LogReasonRepository logReasonRepository;
    private final UserStreakService userStreakService;

    /**
     * ✏️ 미룸 기록 작성 (POST /api/logs)
     */
    @Transactional
    public DailyLogResponse createDailyLog(User user, DailyLogRequest request) {
        LocalDate today = LocalDate.now();

        if (dailyLogRepository.existsByUserAndDate(user, today)) {
            throw new CustomException(DailyLogErrorCode.INVALID_DATE);
        }

        DailyLog log = DailyLog.builder()
                .user(user)
                .date(today)
                .isPostponed(request.getIsPostponed())
                .type(request.getMyType())
                .build();

        DailyLog savedLog = dailyLogRepository.save(log);

        // ✅ 활동 매핑 (이름 기반)
        for (String activityName : request.getActivities()) {
            ActivityTag activity = activityTagRepository.findByUserAndName(user, activityName)
                    .orElseThrow(() -> new CustomException(DailyLogErrorCode.ACTIVITY_NOT_FOUND));
            logActivityRepository.save(
                    LogActivity.builder()
                            .log(savedLog)
                            .activity(activity)
                            .build()
            );
        }

        // ✅ 이유 매핑 (이름 기반)
        for (String reasonName : request.getReasons()) {
            ReasonTag reason = reasonTagRepository.findByUserAndName(user, reasonName)
                    .orElseThrow(() -> new CustomException(DailyLogErrorCode.REASON_NOT_FOUND));
            logReasonRepository.save(
                    LogReason.builder()
                            .log(savedLog)
                            .reason(reason)
                            .build()
            );
        }

        // 배지 및 연속 기록 처리
        userStreakService.updateStreakOnRecord(user.getId());
        return DailyLogResponse.fromEntity(savedLog);
    }

    /**
     * 🧩 미룸 기록 수정 (PUT /api/logs)
     */
    @Transactional
    public DailyLogResponse updateDailyLog(User user, DailyLogRequest request) {
        LocalDate today = LocalDate.now();

        DailyLog existingLog = dailyLogRepository.findByUserAndDate(user, today)
                .orElseThrow(() -> new CustomException(DailyLogErrorCode.LOG_NOT_FOUND));

        // 1. 기존 관계 삭제 + 즉시 DB 반영
        logActivityRepository.deleteAllByLog(existingLog);
        logReasonRepository.deleteAllByLog(existingLog);
        logActivityRepository.flush();
        logReasonRepository.flush();

        // 2. 오늘만 수정 가능하도록 체크
        if (!existingLog.getDate().isEqual(today)) {
            throw new CustomException(DailyLogErrorCode.INVALID_DATE);
        }

        // 3. 로그 상태 업데이트
        existingLog.update(request.getIsPostponed(), request.getMyType());

        // 4. 중복 없는 활동 추가
        for (String activityName : new HashSet<>(request.getActivities())) {
            ActivityTag activity = activityTagRepository.findByUserAndName(user, activityName)
                    .orElseThrow(() -> new CustomException(DailyLogErrorCode.ACTIVITY_NOT_FOUND));
            logActivityRepository.save(
                    LogActivity.builder()
                            .log(existingLog)
                            .activity(activity)
                            .build()
            );
        }

        // 5. 중복 없는 이유 추가
        for (String reasonName : new HashSet<>(request.getReasons())) {
            ReasonTag reason = reasonTagRepository.findByUserAndName(user, reasonName)
                    .orElseThrow(() -> new CustomException(DailyLogErrorCode.REASON_NOT_FOUND));
            logReasonRepository.save(
                    LogReason.builder()
                            .log(existingLog)
                            .reason(reason)
                            .build()
            );
        }

        dailyLogRepository.save(existingLog);
        return DailyLogResponse.fromEntity(existingLog);
    }


    // 미룸 기록 조회
    @Transactional(readOnly = true)
    public DailyLogDetailResponse getDailyLog(User user, LocalDate date) {
        DailyLog log = dailyLogRepository.findByUserAndDate(user, date)
                .orElseThrow(() -> new CustomException(DailyLogErrorCode.LOG_NOT_FOUND));

        List<String> activities = logActivityRepository.findByLog(log)
                .stream()
                .map(la -> la.getActivity().getName())
                .toList();

        List<String> reasons = logReasonRepository.findByLog(log)
                .stream()
                .map(lr -> lr.getReason().getName())
                .toList();

        return DailyLogDetailResponse.fromEntity(log, activities, reasons);
    }

    @Transactional(readOnly = true)
    public MonthlyLogSummaryResponse getMonthlyLogs(User user, int year, int month) {
        List<DailyLog> logs = dailyLogRepository.findByUserAndMonth(user, year, month);

        long postponedCount = logs.stream().filter(DailyLog::isPostponed).count();
        long completedCount = logs.size() - postponedCount;

        List<DailyLogCalendarResponse> logResponses = logs.stream()
                .map(DailyLogCalendarResponse::from)
                .toList();

        MonthlyLogSummaryResponse.Summary summary = MonthlyLogSummaryResponse.Summary.builder()
                .postponedCount(postponedCount)
                .completedCount(completedCount)
                .build();

        return MonthlyLogSummaryResponse.builder()
                .summary(summary)
                .logs(logResponses)
                .build();
    }



}
