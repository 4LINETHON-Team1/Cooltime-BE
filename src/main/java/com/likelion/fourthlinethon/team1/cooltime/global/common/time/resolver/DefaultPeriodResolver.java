package com.likelion.fourthlinethon.team1.cooltime.global.common.time.resolver;

import com.likelion.fourthlinethon.team1.cooltime.global.common.time.clock.AppClock;
import com.likelion.fourthlinethon.team1.cooltime.global.common.time.period.MonthPeriod;
import com.likelion.fourthlinethon.team1.cooltime.global.common.time.period.WeekPeriod;
import com.likelion.fourthlinethon.team1.cooltime.global.common.time.period.YearPeriod;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * 컨트롤러에서 가장 많이 주입해 쓰는 통합 리졸버.
 * - 주간: WeekPeriodResolver 위임
 * - 월/연: MonthPeriod/YearPeriod 정적 팩토리 사용
 * - date null 시 today() 기본값 적용(선택)
 */
@Component
@RequiredArgsConstructor
public class DefaultPeriodResolver implements PeriodResolver {

    private final WeekPeriodResolver weekResolver;
    private final AppClock clock;

    // ---------- Week ----------
    @Override
    public WeekPeriod resolveWeek(LocalDate date) {
        LocalDate d = (date != null) ? date : clock.today();
        return weekResolver.fromDate(d);
    }

    @Override
    public WeekPeriod resolveWeek(int year, int month, int weekOfMonth) {
        return weekResolver.fromCalendarWeek(year, month, weekOfMonth);
    }

    @Override
    public WeekPeriod resolveWeekByIso(int isoYear, int isoWeek) {
        return weekResolver.fromIsoWeek(isoYear, isoWeek);
    }

    // ---------- Month ----------
    @Override
    public MonthPeriod resolveMonth(int year, int month) {
        return MonthPeriod.of(year, month);
    }

    @Override
    public MonthPeriod resolveMonth(LocalDate anyDate) {
        return MonthPeriod.of(requireDate(anyDate));
    }

    // ---------- Year ----------
    @Override
    public YearPeriod resolveYear(int year) {
        return YearPeriod.of(year);
    }

    @Override
    public YearPeriod resolveYear(LocalDate anyDate) {
        return YearPeriod.of(requireDate(anyDate));
    }
}