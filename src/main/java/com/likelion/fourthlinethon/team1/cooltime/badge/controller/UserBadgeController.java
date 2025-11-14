package com.likelion.fourthlinethon.team1.cooltime.badge.controller;

import com.likelion.fourthlinethon.team1.cooltime.badge.dto.response.BadgeListResponse;
import com.likelion.fourthlinethon.team1.cooltime.badge.dto.response.BadgeProgressResponse;
import com.likelion.fourthlinethon.team1.cooltime.badge.repository.UserStreakRepository;
import com.likelion.fourthlinethon.team1.cooltime.badge.service.UserBadgeService;
import com.likelion.fourthlinethon.team1.cooltime.badge.service.UserStreakService;
import com.likelion.fourthlinethon.team1.cooltime.global.response.BaseResponse;
import com.likelion.fourthlinethon.team1.cooltime.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "UserBadge", description = "사용자 배지 관련 API")
@RequestMapping("/api/badges")
@RequiredArgsConstructor
public class UserBadgeController {

    private final UserBadgeService userBadgeService;

    @Operation(
            summary = "배지 진행 현황 조회",
            description = "사용자의 배지 진행 현황을 조회합니다."
    )
    @GetMapping("/progress")
    public ResponseEntity<BaseResponse<BadgeProgressResponse>> getBadgeProgress(
            @AuthenticationPrincipal CustomUserDetails userDetails
            ) {
        return
                ResponseEntity.ok(
                        BaseResponse.success(
                                "배지 진행 현황 조회 성공",
                                userBadgeService.getBadgeProgress(userDetails.getUser().getId())
                        )
                );
    }

    @Operation(summary = "획득 배지 목록 조회", description = "전체 배지 목록과 각 배지의 획득 여부를 반환합니다.")
    @GetMapping
    public ResponseEntity<BaseResponse<BadgeListResponse>> getBadgeList(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return
                ResponseEntity.ok(
                        BaseResponse.success(
                                "획득 배지 목록 조회 성공",
                                userBadgeService.getBadgeList(userDetails.getUser().getId())
                        )
                );
    }



}
