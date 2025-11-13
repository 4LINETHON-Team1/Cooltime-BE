package com.likelion.fourthlinethon.team1.cooltime.report.controller;

import com.likelion.fourthlinethon.team1.cooltime.global.common.time.period.WeekPeriod;
import com.likelion.fourthlinethon.team1.cooltime.global.common.time.resolver.DefaultPeriodResolver;
import com.likelion.fourthlinethon.team1.cooltime.global.response.BaseResponse;
import com.likelion.fourthlinethon.team1.cooltime.global.security.CustomUserDetails;
import com.likelion.fourthlinethon.team1.cooltime.report.dto.response.AiWeeklyReportResponse;
import com.likelion.fourthlinethon.team1.cooltime.report.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Tag(name = "AI-Report", description = "AI 보고서 관련 API")
@RestController
@RequestMapping("/api/ai-reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;
    private final DefaultPeriodResolver periodResolver;

    @Operation(summary = "AI 주간 보고서 조회 (날짜 기준)", description = "지정한 날짜가 속한 주의 AI 주간 보고서를 조회합니다.")
    @GetMapping("/by-date")
    public ResponseEntity<BaseResponse<AiWeeklyReportResponse>> getAiWeeklyReportByDate(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        WeekPeriod period = periodResolver.resolveWeek(date);
        return ResponseEntity.ok(
                BaseResponse.success(
                        "AI 주간 보고서 조회 성공 (날짜 기준)",
                        reportService.getAiWeeklyReport(userDetails.getUser(), period)
                )
        );
    }

    @Operation (summary = "AI 주간 보고서 조회 (캘린더 기준)", description = "지정한 연도, 월, 월의 몇 번째 주인지에 해당하는 AI 주간 보고서를 조회합니다.")
    @GetMapping("/by-calendar")
    public ResponseEntity<BaseResponse<AiWeeklyReportResponse>> getAiWeeklyReportByCalendar(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam int weekOfMonth
    ) {
        WeekPeriod period = periodResolver.resolveWeek(year, month, weekOfMonth);
        return ResponseEntity.ok(
                BaseResponse.success(
                        "AI 주간 보고서 조회 성공 (캘린더 기준)",
                        reportService.getAiWeeklyReport(userDetails.getUser(), period)
                )
        );
    }

    @Operation(summary = "AI 주간 보고서 조회 (ISO 기준)", description = "지정한 ISO 연도와 ISO 주차에 해당하는 AI 주간 보고서를 조회합니다.")
    @GetMapping("/by-iso")
    public ResponseEntity<BaseResponse<AiWeeklyReportResponse>> getAiWeeklyReportByIso(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam int isoYear,
            @RequestParam int isoWeek
    ) {
        WeekPeriod period = periodResolver.resolveWeekByIso(isoYear, isoWeek);
        return ResponseEntity.ok(
                BaseResponse.success(
                        "AI 주간 보고서 조회 성공 (ISO 기준)",
                        reportService.getAiWeeklyReport(userDetails.getUser(), period)
                )
        );
    }
}
