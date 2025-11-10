package com.likelion.fourthlinethon.team1.cooltime.stats.dto.response;

import com.likelion.fourthlinethon.team1.cooltime.global.common.time.period.WeekPeriod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.temporal.WeekFields;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeekKey {
    private int year;
    private int month;
    private int weekOfMonth;

    public static WeekKey of(int year, int month, int weekOfMonth) {
        return WeekKey.builder()
                .year(year)
                .month(month)
                .weekOfMonth(weekOfMonth)
                .build();
    }

    /** 특정 날짜가 속한 주차 키 생성 */
    public static WeekKey from(LocalDate date) {
        WeekPeriod weekPeriod = WeekPeriod.of(date);
        LocalDate labelAnchor = weekPeriod.getStart().plusDays(3);
        return of(
                labelAnchor.getYear(),
                labelAnchor.getMonthValue(),
                weekPeriod.weekOfMonthAuto()
        );
    }
}