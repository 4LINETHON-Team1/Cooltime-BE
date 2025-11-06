package com.likelion.fourthlinethon.team1.cooltime.stats.dto.response;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostponeRatioWeekResponse {

    /** 주차 키 (연/월/주차) */
    private WeekKey weekKey;

    /** 기간  */
    private PeriodResponse period;

    /** 전주 대비 변화 정보 */
    private PostponedRateChangeResponse change;

    /** 미룸 비율 정보 */
    private PostponedRatioSummary ratio;

    public static PostponeRatioWeekResponse of(
            LocalDate startDate,
            LocalDate endDate,
            PostponedRatioSummary current,
            @Nullable PostponedRatioSummary previous
    ){
        WeekKey key = WeekKey.from(startDate);
        PeriodResponse period = PeriodResponse.of(startDate, endDate);
        PostponedRateChangeResponse change = PostponedRateChangeResponse.of(
                current.getPostponedPercent(),
                previous != null ? previous.getPostponedPercent() : null
        );

        return PostponeRatioWeekResponse.builder()
                .weekKey(key)
                .period(period)
                .change(change)
                .ratio(current)
                .build();
    }


}