package com.likelion.fourthlinethon.team1.cooltime.stats.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PostponeRatioMonthResponse{

    /** 월 키 (연/월) */
    private MonthKey monthKey;

    /** 기간  */
    private PeriodResponse period;

    /** 전월 대비 변화 정보 */
    private PostponedRateChangeResponse change;

    /** 미룸 비율 정보 */
    private PostponedRatioSummary ratio;
    public static PostponeRatioMonthResponse of(
            LocalDate startDate,
            LocalDate endDate,
            PostponedRatioSummary current,
            PostponedRatioSummary previous
    ){
        MonthKey key = MonthKey.from(startDate);
        PeriodResponse period = PeriodResponse.of(startDate, endDate);
        PostponedRateChangeResponse change = PostponedRateChangeResponse.of(
                current.getPostponedPercent(),
                previous != null ? previous.getPostponedPercent() : null
        );

        return PostponeRatioMonthResponse.builder()
                .monthKey(key)
                .period(period)
                .change(change)
                .ratio(current)
                .build();
    }
}