package com.likelion.fourthlinethon.team1.cooltime.report.service;

import com.likelion.fourthlinethon.team1.cooltime.global.common.time.period.ClampedPeriod;
import com.likelion.fourthlinethon.team1.cooltime.global.common.time.period.PeriodGuard;
import com.likelion.fourthlinethon.team1.cooltime.global.common.time.period.WeekPeriod;
import com.likelion.fourthlinethon.team1.cooltime.report.dto.response.AiWeeklyReportResponse;
import com.likelion.fourthlinethon.team1.cooltime.report.entity.AiWeeklyReport;
import com.likelion.fourthlinethon.team1.cooltime.report.repository.AiWeeklyReportRepository;
import com.likelion.fourthlinethon.team1.cooltime.stats.dto.response.PeriodResponse;
import com.likelion.fourthlinethon.team1.cooltime.stats.dto.response.PostponeRatioWeekResponse;
import com.likelion.fourthlinethon.team1.cooltime.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReportService {
    private final AiWeeklyReportRepository aiWeeklyReportRepository;

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
            );
        }


        // 임시 반환값
        return AiWeeklyReportResponse.of(AiWeeklyReport.createReport(user, period.getStart(),
                """
                        지난 일주일 동안, ‘공부’와 ‘정리정돈’ 카테고리에서 미룸이 가장 자주 나타났어요. 특히 ‘완벽하게 시작해야 한다’는 생각이 강할수록 실행이 늦어지는 경향이 보여요.
                        일정이 쌓이기보단, 준비 과정에서 스스로 피로를 느끼는 패턴이에요. AI가 분석한 결과, 현재 당신은 ‘완벽주의형 성향’에 가깝습니다. 집중력이 높고 책임감이 강하지만, 그만큼 결과에 대한 압박도 함께 커요.
                        """,
                """
                        완벽하게 하려는 마음이 강할수록 시작 자체가 어려워질 수 있어요. 이번 주에는 ‘결과보다 시도’를 기록하는 방식으로 루틴을 바꿔보세요. 예를 들어, 공부 계획을 세우기 전에 ‘10분만 하기’ 버튼을 눌러 기록하는 식이에요. 또한 미룸을 줄이는 것보다, ‘어떤 상황에서 미루는지’를 인식하는 것이 더 중요합니다. 쿨타임은 이 패턴을 바탕으로 다음 주 피드백을 조금 더 정교하게 조정할 거예요. 당신의 미룸은 실패가 아니라, 집중력과 책임감이 강한 사람에게 나타나는 회복 신호예요. 이번 주엔 ‘완벽한 하루’보다 ‘하루를 시작한 나’에게 초점을 맞춰보세요. 🌿
                        """,
                """
                        지난주보다 ‘공부’ 카테고리의 미룸 횟수는 줄었지만, ‘정리정돈’과 ‘운동’에서 새롭게 미룸이 나타났어요. 특히 주 초반엔 실행이 빠르지만, 주 후반으로 갈수록 피로감이나 집중 저하로 인해 루틴이 흐트러지는 경향이 보여요.
                        또한 지난주에는 ‘계획 수정’이 많았다면 이번 주에는 ‘시작 지연’이 주된 형태로 바뀌었어요. 즉, 계획을 세우는 시간은 확보되었지만 실제 행동으로 옮기는 데 더 많은 에너지가 필요한 상황이에요.
                        AI가 분석한 결과, 이번 주의 미룸은 ‘동기 저하형’보다는 ‘에너지 관리형’ 패턴에 가까워요. 집중력이 높을 때 몰입하지만, 회복 시간을 충분히 확보하지 못하면 다음 루틴으로 넘어가기 어려워지는 경향이 있습니다.
                        """));

        // 3)
        // AI 레포트가 존재하면 AiWeeklyReportResponse로 반환
        // 없으면 새로 생성하여 반환
//        return aiWeeklyReportRepository
//                .findByUserAndWeekStart(user, period.getStart())
//                .map(AiWeeklyReportResponse::of)
//                .orElseGet(()-> AiWeeklyReportResponse.of(createAiWeeklyReport(user)));
    }

    private AiWeeklyReport createAiWeeklyReport(User user){
        return new AiWeeklyReport();
    }

}
