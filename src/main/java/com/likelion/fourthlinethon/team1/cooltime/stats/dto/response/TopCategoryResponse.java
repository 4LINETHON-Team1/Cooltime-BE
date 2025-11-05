package com.likelion.fourthlinethon.team1.cooltime.stats.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@Schema(title = "TopCategoryResponse", description = "최고 카테고리 응답")
public class TopCategoryResponse {
    /** 카테고리 이름 */
    @Schema(description = "카테고리 이름", example = "공부")
    private String categoryName;

    public static TopCategoryResponse of(String categoryName) {
        return TopCategoryResponse.builder()
                .categoryName(categoryName)
                .build();
    }

    /** 미룸 기록 없음 */
    public static TopCategoryResponse empty() {
        return new TopCategoryResponse("");
    }
}
