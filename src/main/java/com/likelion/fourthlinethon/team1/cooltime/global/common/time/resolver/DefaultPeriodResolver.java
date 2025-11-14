package com.likelion.fourthlinethon.team1.cooltime.global.common.time.resolver;

import com.likelion.fourthlinethon.team1.cooltime.global.common.time.period.MonthPeriod;
import com.likelion.fourthlinethon.team1.cooltime.global.common.time.period.WeekPeriod;
import com.likelion.fourthlinethon.team1.cooltime.global.common.time.period.YearPeriod;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * 기간 리졸버의 기본 구현체:
 * - 주간, 월간, 연간 기간을 다양한 방식으로 해석하여 반환
 */

@Component
public class DefaultPeriodResolver implements PeriodResolver {

    // ---------- Week ----------
    @Override
    public WeekPeriod resolveWeek(LocalDate anyDate) {
        return WeekPeriod.of(requireDate(anyDate));
    }

    @Override
    public WeekPeriod resolveWeek(int year, int month, int weekOfMonth) {
        return WeekPeriod.ofCalendarWeek(year, month, weekOfMonth);
    }

    @Override
    public WeekPeriod resolveWeekByIso(int isoYear, int isoWeek) {
        return WeekPeriod.ofIsoWeek(isoYear, isoWeek);
    }

    // ---------- Month ----------
    @Override
    public MonthPeriod resolveMonth(LocalDate anyDate) {
        return MonthPeriod.of(requireDate(anyDate));
    }

    @Override
    public MonthPeriod resolveMonth(int year, int month) {
        return MonthPeriod.of(year, month);
    }

    // ---------- Year ----------
    @Override
    public YearPeriod resolveYear(LocalDate anyDate) {
        return YearPeriod.of(requireDate(anyDate));
    }

    @Override
    public YearPeriod resolveYear(int year) {
        return YearPeriod.of(year);
    }

}
