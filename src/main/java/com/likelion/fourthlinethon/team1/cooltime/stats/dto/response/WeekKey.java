package com.likelion.fourthlinethon.team1.cooltime.stats.dto.response;

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

    /** LocalDate로부터 ISO-8601 기준 주차 계산 */
    public static WeekKey from(LocalDate date) {
        WeekFields wf = WeekFields.ISO;
        return of(
                date.getYear(),
                date.getMonthValue(),
                date.get(wf.weekOfMonth())
        );
    }
}