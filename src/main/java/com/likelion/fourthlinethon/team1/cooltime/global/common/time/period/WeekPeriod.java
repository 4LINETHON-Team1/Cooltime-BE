package com.likelion.fourthlinethon.team1.cooltime.global.common.time.period;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;

/** 주간 기간: 월요일 시작 ~ 일요일 종료 (inclusive) */
@Getter
@EqualsAndHashCode
@ToString
public class WeekPeriod implements Period {

    private final LocalDate start; // 월요일(포함)
    private final LocalDate end;   // 일요일(포함)

    private static final DayOfWeek WEEK_START = DayOfWeek.MONDAY;
    private static final WeekFields WF = WeekFields.of(WEEK_START, 1); // 최소 1일을 첫 주로

    private WeekPeriod(LocalDate start) {
        this.start = start;
        this.end = start.plusDays(6);
    }

    /** 임의의 날짜가 속한 주(월~일) */
    public static WeekPeriod of(@NonNull LocalDate anyDate) {
        LocalDate s = anyDate.with(TemporalAdjusters.previousOrSame(WEEK_START));
        return new WeekPeriod(s);
    }

    /** 캘린더 n주차(year/month/weekOfMonth) → 주 기간 */
    public static WeekPeriod ofCalendarWeek(int year, int month, int weekOfMonth) {
        validateYearMonth(year, month);
        int max = maxWeekOfMonth(year, month);
        if (weekOfMonth < 1 || weekOfMonth > max) {
            throw new IllegalArgumentException("weekOfMonth must be between 1 and " + max);
        }
        LocalDate base = LocalDate.of(year, month, 1).with(WF.weekOfMonth(), weekOfMonth);
        return of(base);
    }

    /** ISO 주차(week-based year) → 주 기간 */
    public static WeekPeriod ofIsoWeek(int isoYear, int isoWeek) {
        if (isoWeek < 1 || isoWeek > 53) throw new IllegalArgumentException("isoWeek must be 1..53");
        // ISO 기준: 1월 4일이 포함된 주가 1주차
        LocalDate start = LocalDate.of(isoYear, 1, 4)
                .with(IsoFields.WEEK_OF_WEEK_BASED_YEAR, isoWeek)
                .with(TemporalAdjusters.previousOrSame(WEEK_START));
        return new WeekPeriod(start);
    }

    /** 해당 월의 마지막 날짜 기준 월 내 최대 n주차(4~6) */
    public static int maxWeekOfMonth(int year, int month) {
        validateYearMonth(year, month);
        LocalDate last = LocalDate.of(year, month, 1).with(TemporalAdjusters.lastDayOfMonth());
        return last.get(WF.weekOfMonth());
    }

    /** 파생 값(표기/라벨용) */
    public int isoYear()          { return start.get(IsoFields.WEEK_BASED_YEAR); }
    public int isoWeek()          { return start.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR); }
    public int year()             { return start.getYear(); }
    public int month()            { return start.getMonthValue(); }
    public int weekOfMonth()      { return start.get(WF.weekOfMonth()); }
    public LocalDate endExclusive() { return end.plusDays(1); } // 쿼리를 < endExclusive 로 쓰고 싶을 때

    @Override public Period prev() { return new WeekPeriod(start.minusWeeks(1)); }
    @Override public Period next() { return new WeekPeriod(start.plusWeeks(1)); }

    private static void validateYearMonth(int year, int month) {
        if (year < 1) throw new IllegalArgumentException("year must be >= 1");
        if (month < 1 || month > 12) throw new IllegalArgumentException("month must be 1..12");
    }
}