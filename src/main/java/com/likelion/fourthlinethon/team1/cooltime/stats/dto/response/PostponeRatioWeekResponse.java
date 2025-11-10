package com.likelion.fourthlinethon.team1.cooltime.stats.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.Nullable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostponeRatioWeekResponse {

    /** 주차 키 (연/월/주차) */
    private WeekKey weekKey;

    /** 요청 기간 */
    private PeriodResponse requestedPeriod;

    /** 실제 유효 기간 */
    private PeriodResponse effectivePeriod;

    /** 상태  */
    private String status;

    /** 전주 대비 변화 정보 */
    private PostponedRateChangeResponse change;

    /** 미룸 비율 정보 */
    private PostponedRatioSummary ratio;

    public static PostponeRatioWeekResponse of(
            PeriodResponse requestedPeriod,
            PeriodResponse effectivePeriod,
            PostponedRatioSummary current,
            @Nullable PostponedRatioSummary previous
    ){
        WeekKey key = WeekKey.from(requestedPeriod.getStartDate());
        PostponedRateChangeResponse change = PostponedRateChangeResponse.of(
                current.getPostponedPercent(),
                previous != null ? previous.getPostponedPercent() : null
        );

        return PostponeRatioWeekResponse.builder()
                .weekKey(key)
                .requestedPeriod(requestedPeriod)
                .effectivePeriod(effectivePeriod)
                .status(EffectivePeriodStatus.OK.name())
                .change(change)
                .ratio(current)
                .build();
    }

    public static PostponeRatioWeekResponse empty(
            PeriodResponse requestedPeriod,
            PeriodResponse effectivePeriod
    ){
        WeekKey key = WeekKey.from(requestedPeriod.getStartDate());

        return PostponeRatioWeekResponse.builder()
                .weekKey(key)
                .requestedPeriod(requestedPeriod)
                .effectivePeriod(effectivePeriod)
                .status(EffectivePeriodStatus.OUT_OF_RANGE.name())
                .change(PostponedRateChangeResponse.noData())
                .ratio(PostponedRatioSummary.empty())
                .build();
    }

}