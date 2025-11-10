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
    private static final WeekFields WF = WeekFields.ISO;

    private WeekPeriod(LocalDate start) {
        this.start = start;
        this.end = start.plusDays(6);
    }

    /** 임의의 날짜가 속한 주(월~일) */
    public static WeekPeriod of(@NonNull LocalDate anyDate) {
        LocalDate s = anyDate.with(TemporalAdjusters.previousOrSame(WEEK_START));
        return new WeekPeriod(s);
    }

    /** 주 기간 내에 특정 달의 날짜가 몇 일 포함되는지 계산 */
    private static int daysInTargetMonth(LocalDate start, int targetMonth) {
        int cnt = 0;
        for (int i = 0; i < 7; i++) {
            if (start.plusDays(i).getMonthValue() == targetMonth) cnt++;
        }
        return cnt;
    }

    /** 달의 ‘첫 유효 주’(해당 달 날짜가 ≥4일 포함되는 주)의 월요일 반환 */
    private static LocalDate firstValidWeekStart(int year, int month) {
        LocalDate firstDay = LocalDate.of(year, month, 1);
        LocalDate start = firstDay.with(TemporalAdjusters.previousOrSame(WEEK_START)); // 1일이 속한 주의 월요일
        if (daysInTargetMonth(start, month) < 4) {
            start = start.plusWeeks(1);  // 1주 뒤가 첫 유효 주
        }
        return start;
    }

    /** 달의 n주차 → 주 기간: ‘그 달 날짜가 ≥4일 포함되는 주’만 인정 */
    public static WeekPeriod ofCalendarWeek(int year, int month, int weekOfMonth) {
        // 유효성 검사
        validateYearMonth(year, month);
        if (weekOfMonth < 1) throw new IllegalArgumentException("weekOfMonth must be >= 1");

        // 달의 첫 유효 주의 월요일 계산
        LocalDate first = firstValidWeekStart(year, month);
        // n주차의 월요일 계산
        LocalDate start = first.plusWeeks(weekOfMonth - 1);

        // 해당 주가 그 달에 4일 이상 포함되는지 확인
        if (daysInTargetMonth(start, month) < 4) {
            throw new IllegalArgumentException("weekOfMonth out of range for " + year + "-" + month);
        }
        return new WeekPeriod(start);
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


    /** 파생 값(표기/라벨용) */
    public int isoYear()          { return start.get(IsoFields.WEEK_BASED_YEAR); }
    public int isoWeek()          { return start.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR); }
    public int year()             { return start.getYear(); }
    public int month()            { return start.getMonthValue(); }

    /** 이 주의 라벨 기준 연/월(주 대부분(≥4일) 포함 달) */
    public int labelYearAuto()  { return start.plusDays(3).getYear(); }
    public int labelMonthAuto() { return start.plusDays(3).getMonthValue(); }

    /** 라벨 달(자동) 기준 ‘≥4일 규칙’ week-of-month 반환 */
    public int weekOfMonthAuto() {
        int y = labelYearAuto();
        int m = labelMonthAuto();
        // 안전상 검사(이 주가 해당 달에 실제로 ≥4일 포함되는지)
        if (daysInTargetMonth(this.start, m) < 4) {
            throw new IllegalStateException("This week is not a valid week for " + y + "-" + m);
        }
        LocalDate first = firstValidWeekStart(y, m);
        long diff = java.time.temporal.ChronoUnit.WEEKS.between(first, this.start);
        return (int) diff + 1;
    }
    public int weekOfMonthWF()      { return start.get(WF.weekOfMonth()); }
    public LocalDate endExclusive() { return end.plusDays(1); } // 쿼리를 < endExclusive 로 쓰고 싶을 때

    @Override public Period prev() { return new WeekPeriod(start.minusWeeks(1)); }
    @Override public Period next() { return new WeekPeriod(start.plusWeeks(1)); }

    private static void validateYearMonth(int year, int month) {
        if (year < 1) throw new IllegalArgumentException("year must be >= 1");
        if (month < 1 || month > 12) throw new IllegalArgumentException("month must be 1..12");
    }
}