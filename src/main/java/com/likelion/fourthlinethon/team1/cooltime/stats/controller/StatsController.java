package com.likelion.fourthlinethon.team1.cooltime.stats.controller;

import com.likelion.fourthlinethon.team1.cooltime.global.common.time.period.MonthPeriod;
import com.likelion.fourthlinethon.team1.cooltime.global.common.time.period.WeekPeriod;
import com.likelion.fourthlinethon.team1.cooltime.global.common.time.period.YearPeriod;
import com.likelion.fourthlinethon.team1.cooltime.global.common.time.resolver.DefaultPeriodResolver;
import com.likelion.fourthlinethon.team1.cooltime.global.response.BaseResponse;
import com.likelion.fourthlinethon.team1.cooltime.global.security.CustomUserDetails;
import com.likelion.fourthlinethon.team1.cooltime.stats.dto.response.*;
import com.likelion.fourthlinethon.team1.cooltime.stats.service.StatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Tag(name = "Stats", description = "미룸 기록 통계 관련 API")
@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;
    private final DefaultPeriodResolver periodResolver;

    @Operation(summary = "기본 통계 개요 조회", description = "사용자의 기본 통계 개요를 조회합니다.")
    @GetMapping("/overview")
    public ResponseEntity<BaseResponse<DashboardOverviewResponse>> getDashboardOverview(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(
                BaseResponse.success(
                        "기본 통계 개요 조회 성공",
                        statsService.getDashboardOverview(userDetails.getUser())
                )
        );
    }

    @Operation(summary = "총 기록 일수 조회", description = "사용자의 총 기록 일수를 조회합니다.")
    @GetMapping("/records/total-days")
    public ResponseEntity<BaseResponse<TotalRecordSummaryResponse>> getTotalRecordDays(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(
                BaseResponse.success(
                        "총 기록 일수 조회 성공",
                        statsService.getTotalRecordSummary(userDetails.getUser())
                )
        );
    }

    @Operation(summary = "최고 카테고리 조회", description = "미룸 횟수가 가장 많은 카테고리를 조회합니다.")
    @GetMapping("/categories/postponed/top")
    public ResponseEntity<BaseResponse<TopCategoryResponse>> getTopCategory(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(
                BaseResponse.success(
                        "최고 카테고리 조회 성공",
                        statsService.getTopCategory(userDetails.getUser())
                )
        );
    }

    @Operation(summary = "카테고리별 미룸 횟수 통계 조회", description = "모든 카테고리의 미룸 횟수 통계를 조회합니다.")
    @GetMapping("/categories/postponed")
    public ResponseEntity<BaseResponse<CategoryStatsAllResponse>> getCategoryStatsAll(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(
                BaseResponse.success(
                        "카테고리별 미룸 횟수 통계 조회 성공",
                        statsService.getCategoryStatsAll(userDetails.getUser())
                )
        );
    }


    @Operation(summary = "총 미룸 비율", description = "가입일 ~ 오늘까지의 미룸 비율 통계를 반환합니다.")
    @GetMapping("/postpone-ratio/total")
    public ResponseEntity<BaseResponse<PostponeRatioTotalResponse>> getPostponeRatioTotal(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(
                BaseResponse.success(
                        "총 미룸 비율 조회 성공",
                        statsService.getPostponeRatioTotal(userDetails.getUser())
                )
        );
    }

    @Operation(summary = "주간 미룸 비율(대표 날짜)", description = "date가 속한 주(월~일)의 미룸 비율을 반환합니다.")
    @GetMapping(value = "/postpone-ratio/week/by-date", params = "date")
    public ResponseEntity<BaseResponse<PostponeRatioWeekResponse>> getPostponeRatioWeekByDate(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        WeekPeriod period = periodResolver.resolveWeek(date);
        return ResponseEntity.ok(
                BaseResponse.success(
                        "주간 미룸 비율 조회 성공",
                        statsService.getPostponeRatioWeek(userDetails.getUser(), period)
                )
        );
    }

    @Operation(summary = "주간 미룸 비율(월 기준 n주차)", description = "year, month, weekOfMonth로 지정된 주의 미룸 비율을 반환합니다.")
    @GetMapping(value = "/postpone-ratio/week/by-calendar", params = {"year","month","weekOfMonth"})
    public ResponseEntity<BaseResponse<PostponeRatioWeekResponse>> getPostponeRatioWeekByCalendar(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam int weekOfMonth
    ) {
        WeekPeriod period = periodResolver.resolveWeek(year, month, weekOfMonth);
        return ResponseEntity.ok(
                BaseResponse.success(
                        "주간 미룸 비율 조회 성공(월 기준 n주차)",
                        statsService.getPostponeRatioWeek(userDetails.getUser(), period)
                )
        );
    }

    @Operation(summary = "주간 미룸 비율(ISO 주차)", description = "isoYear, isoWeek로 지정된 ISO 주의 미룸 비율을 반환합니다.")
    @GetMapping(value = "/postpone-ratio/week/by-iso", params = {"isoYear","isoWeek"})
    public ResponseEntity<BaseResponse<PostponeRatioWeekResponse>> getPostponeRatioWeekByIso(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam int isoYear,
            @RequestParam int isoWeek
    ) {
        WeekPeriod period = periodResolver.resolveWeekByIso(isoYear, isoWeek);
        return ResponseEntity.ok(
                BaseResponse.success(
                        "주간 미룸 비율 조회 성공(ISO)",
                        statsService.getPostponeRatioWeek(userDetails.getUser(), period)
                )
        );
    }
    @Operation(summary = "월간 미룸 비율(대표 날짜)", description = "date가 속한 월의 미룸 비율을 반환합니다.")
    @GetMapping(value = "/postpone-ratio/month/by-date", params = "date")
    public ResponseEntity<BaseResponse<PostponeRatioMonthResponse>> getPostponeRatioMonthByDate(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        MonthPeriod period = periodResolver.resolveMonth(date);
        return ResponseEntity.ok(
                BaseResponse.success(
                        "월간 미룸 비율 조회 성공(대표 날짜)",
                        statsService.getPostponeRatioMonth(userDetails.getUser(), period)
                )
        );
    }

    @Operation(summary = "월간 미룸 비율(연/월)", description = "year, month로 지정된 월의 미룸 비율을 반환합니다.")
    @GetMapping(value = "/postpone-ratio/month/by-ym", params = {"year","month"})
    public ResponseEntity<BaseResponse<PostponeRatioMonthResponse>> getPostponeRatioMonthByYm(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam int year,
            @RequestParam int month
    ) {
        MonthPeriod period = periodResolver.resolveMonth(year, month);
        return ResponseEntity.ok(
                BaseResponse.success(
                        "월간 미룸 비율 조회 성공",
                        statsService.getPostponeRatioMonth(userDetails.getUser(), period)
                )
        );
    }

    @Operation(summary = "연간 미룸 비율(연도)", description = "year로 지정된 연도의 미룸 비율을 반환합니다.")
    @GetMapping(value = "/postpone-ratio/year/by-year", params = "year")
    public ResponseEntity<BaseResponse<PostponeRatioYearResponse>> getPostponeRatioYearByYear(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam int year
    ) {
        YearPeriod period = periodResolver.resolveYear(year);
        return ResponseEntity.ok(
                BaseResponse.success(
                        "연간 미룸 비율 조회 성공",
                        statsService.getPostponeRatioYear(userDetails.getUser(), period)
                )
        );
    }

    @Operation(summary = "연간 미룸 비율(대표 날짜)", description = "date가 속한 연도의 미룸 비율을 반환합니다.")
    @GetMapping(value = "/postpone-ratio/year/by-date", params = "date")
    public ResponseEntity<BaseResponse<PostponeRatioYearResponse>> getPostponeRatioYearByDate(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        YearPeriod period = periodResolver.resolveYear(date);
        return ResponseEntity.ok(
                BaseResponse.success(
                        "연간 미룸 비율 조회 성공(대표 날짜)",
                        statsService.getPostponeRatioYear(userDetails.getUser(), period)
                )
        );
    }


}
