package com.likelion.fourthlinethon.team1.cooltime.badge.dto.response;

import com.likelion.fourthlinethon.team1.cooltime.badge.enums.BadgeCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(title = "BadgeListResponse", description = "배지 목록 응답 DTO")
public class BadgeListResponse {
    @Schema(description = "배지 목록")
    private List<BadgeItem> badges;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BadgeItem {
        @Schema(description = "배지 코드", example = "STREAK_10")
        private String code;
        @Schema(description = "필요 연속 기록일", example = "10")
        private int requiredDays;
        @Schema(description = "획득 여부", example = "true")
        private boolean earned;
    }

    /** 정적 팩토리 메서드 */
    public static BadgeListResponse from(BadgeCode highestBadgeCode) {
        List<BadgeItem> items = Arrays.stream(BadgeCode.values())
                .map(code -> BadgeItem.builder()
                        .code(code.getCode())
                        .requiredDays(code.getDays())
                        .earned(highestBadgeCode != null && code.getDays() <= highestBadgeCode.getDays())
                        .build())
                .toList();

        return BadgeListResponse.builder()
                .badges(items)
                .build();
    }
}
