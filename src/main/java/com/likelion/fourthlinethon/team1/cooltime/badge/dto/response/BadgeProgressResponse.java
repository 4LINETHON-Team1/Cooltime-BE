package com.likelion.fourthlinethon.team1.cooltime.badge.dto.response;

import com.likelion.fourthlinethon.team1.cooltime.badge.enums.BadgeCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(title = "BadgeProgressResponse", description = "배지 진행 상황 응답 DTO")
public class BadgeProgressResponse {
    @Schema(description = "최대 연속 기록일", example = "25")
    private int longestStreak;
    @Schema(description = "다음 목표 배지 코드", example = "STREAK_30")
    private String badgeCode;
    @Schema(description = "다음 목표 배지의 필요 일수", example = "30")
    private Integer requiredDays;
    @Schema(description = "모든 배지 획득 여부", example = "false")
    private boolean allEarned;

    public static BadgeProgressResponse of(int longestStreak, BadgeCode highest) {
        boolean allEarned = (highest != null && highest == BadgeCode.last());
        BadgeCode target = allEarned
                ? highest                   // 전부 획득이면 highest 그대로 사용
                : BadgeCode.nextAfter(highest).orElseThrow();

        return BadgeProgressResponse.builder()
                .longestStreak(longestStreak)
                .badgeCode(target.getCode())
                .requiredDays(target.getDays())
                .allEarned(allEarned)
                .build();
    }
}