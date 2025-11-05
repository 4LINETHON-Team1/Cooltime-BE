package com.likelion.fourthlinethon.team1.cooltime.stats.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(title = "CategoryRankItem", description = "카테고리별 미룸 횟수 순위 항목")
public class CategoryRankItem {
    /** activity_tags.id */
    @Schema(description = "카테고리 ID", example = "1")
    private Long categoryId;

    /** 카테고리 표시명 (예: 공부) */
    @Schema(description = "카테고리 표시명", example = "공부")
    private String categoryName;

    /** 순위(1부터) - 미룸 횟수 DESC 정렬 기준 */
    @Schema(description = "순위(1부터) - 미룸 횟수 DESC 정렬 기준", example = "1")
    private int rank;

    /** 전체 기록 횟수(사용자 기준, 카테고리 무관 분모) */
    @Schema(description = "전체 기록 횟수(사용자 기준, 카테고리 무관 분모)", example = "100")
    private int totalCount;

    /** 전체 기록 중 해당 카테고리로 '미뤘어요' 한 횟수 */
    @Schema(description = "전체 기록 중 해당 카테고리로 '미뤘어요' 한 횟수", example = "40")
    private int postponedCount;
}