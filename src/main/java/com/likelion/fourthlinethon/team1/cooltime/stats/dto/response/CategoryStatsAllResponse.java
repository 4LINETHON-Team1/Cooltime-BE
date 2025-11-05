package com.likelion.fourthlinethon.team1.cooltime.stats.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@Schema(title = "CategoryStatsAllResponse", description = "카테고리별 통계 전체 응답")
public class CategoryStatsAllResponse {
    /** 카테고리별 미룸 횟수 순위 목록 */
    @Schema(description = "카테고리별 미룸 횟수 순위 목록")
    private List<CategoryRankItem> categoryRankItems;
}
