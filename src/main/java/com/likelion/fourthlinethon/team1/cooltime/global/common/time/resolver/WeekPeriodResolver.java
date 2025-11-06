package com.likelion.fourthlinethon.team1.cooltime.global.common.time.resolver;


import com.likelion.fourthlinethon.team1.cooltime.global.common.time.period.WeekPeriod;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * 주간 전용 리졸버:
 * - date(YYYY-MM-DD)
 * - calendar week: (year, month, weekOfMonth)
 * - ISO week: (isoYear, isoWeek)
 */
@Component
@RequiredArgsConstructor
public class WeekPeriodResolver {

    /** date → WeekPeriod (월~일, inclusive) */
    public WeekPeriod fromDate(LocalDate date) {
        if (date == null) throw new IllegalArgumentException("date must not be null");
        return WeekPeriod.of(date);
    }

    /** (year, month, weekOfMonth) → WeekPeriod */
    public WeekPeriod fromCalendarWeek(int year, int month, int weekOfMonth) {
        return WeekPeriod.ofCalendarWeek(year, month, weekOfMonth);
    }

    /** (isoYear, isoWeek) → WeekPeriod */
    public WeekPeriod fromIsoWeek(int isoYear, int isoWeek) {
        return WeekPeriod.ofIsoWeek(isoYear, isoWeek);
    }
}