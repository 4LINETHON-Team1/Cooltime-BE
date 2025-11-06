package com.likelion.fourthlinethon.team1.cooltime.stats.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PostponeRatioTotalResponse {
    private PostponedRatioSummary total;

    public static PostponeRatioTotalResponse of(int totalCount, int postponedCount, int doneCount) {
        return PostponeRatioTotalResponse.builder()
                .total(PostponedRatioSummary.of(totalCount, postponedCount, doneCount))
                .build();
    }

    public static PostponeRatioTotalResponse from(PostponedRatioSummary summary) {
        return PostponeRatioTotalResponse.builder()
                .total(summary)
                .build();
    }
}
