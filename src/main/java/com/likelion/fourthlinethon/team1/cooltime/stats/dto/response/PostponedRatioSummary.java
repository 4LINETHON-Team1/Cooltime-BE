package com.likelion.fourthlinethon.team1.cooltime.stats.dto.response;

import com.likelion.fourthlinethon.team1.cooltime.stats.projection.PostponeRatioCounts;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostponedRatioSummary {

    private int totalCount;
    private int postponedCount;
    private int doneCount;
    private int postponedPercent;

    /** 단순 생성용 */
    public static PostponedRatioSummary of(int total, int postponed, int done) {
        int percent = total == 0 ? 0 : (int) Math.round((double) postponed / total * 100);
        return PostponedRatioSummary.builder()
                .totalCount(total)
                .postponedCount(postponed)
                .doneCount(done)
                .postponedPercent(percent)
                .build();
    }

    public static PostponedRatioSummary from(@NonNull PostponeRatioCounts postponeRatioCounts) {
        return of(
                postponeRatioCounts.getTotal().intValue(),
                postponeRatioCounts.getPostponed().intValue(),
                postponeRatioCounts.getDone().intValue()
        );
    }

    public static PostponedRatioSummary empty() {
        return PostponedRatioSummary.builder()
                .totalCount(0)
                .postponedCount(0)
                .doneCount(0)
                .postponedPercent(0)
                .build();
    }

}