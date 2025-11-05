package com.likelion.fourthlinethon.team1.cooltime.stats.controller;

import com.likelion.fourthlinethon.team1.cooltime.global.response.BaseResponse;
import com.likelion.fourthlinethon.team1.cooltime.global.security.CustomUserDetails;
import com.likelion.fourthlinethon.team1.cooltime.stats.dto.response.TotalRecordSummaryResponse;
import com.likelion.fourthlinethon.team1.cooltime.stats.service.StatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Stats", description = "미룸 기록 통계 관련 API")
@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;

    @Operation(summary = "총 기록 일수 조회", description = "사용자의 총 기록 일수를 조회합니다.")
    @GetMapping("/total-record-days")
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
}
