package com.likelion.fourthlinethon.team1.cooltime.report.dto.response;

import com.likelion.fourthlinethon.team1.cooltime.report.entity.AiWeeklyReport;
import com.likelion.fourthlinethon.team1.cooltime.report.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(title = "AI 주간 보고서 응답 DTO", description = "AI가 생성한 주간 보고서 정보를 담고 있는 응답 바디입니다.")
public class AiWeeklyReportResponse {

    /** 상태 정보 */
    @Schema(description = "보고서 상태", example = "OK")
    private String status;

    /** UI 라벨 */
    @Schema(description = "보고서가 해당하는 연도", example = "2025")
    private Integer labelYear;
    @Schema(description = "보고서가 해당하는 월", example = "11")
    private Integer labelMonth;
    @Schema(description = "보고서가 해당하는 달의 몇 번째 주차", example = "2")
    private Integer weekOfMonth;

    /** 레포트 본문 */
    @Schema(description = "미룸 패턴 분석")
    private String patternAnalysis;
    @Schema(description = "개선 사항 및 조언")
    private String solution;
    @Schema(description = "저번 주와의 비교")
    private String weeklyComparison;

    public static AiWeeklyReportResponse of(AiWeeklyReport report) {
        return AiWeeklyReportResponse.builder()
                .status(Status.OK.name())
                .labelYear(report.getLabelYear())
                .labelMonth(report.getLabelMonth())
                .weekOfMonth(report.getWeekOfMonth())
                .patternAnalysis(report.getPatternAnalysis())
                .solution(report.getSolution())
                .weeklyComparison(report.getWeeklyComparison())
                .build();
    }

    public static AiWeeklyReportResponse outOfRange(String labelYear, String labelMonth, String weekOfMonth) {
        return AiWeeklyReportResponse.builder()
                .status(Status.OUT_OF_RANGE.name())
                .labelYear(Integer.valueOf(labelYear))
                .labelMonth(Integer.valueOf(labelMonth))
                .weekOfMonth(Integer.valueOf(weekOfMonth))
                .patternAnalysis(null)
                .solution(null)
                .weeklyComparison(null)
                .build();

    }
}
