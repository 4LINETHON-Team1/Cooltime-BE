package com.likelion.fourthlinethon.team1.cooltime.global.common.time.period;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

/** 월간 기간: 해당 월 1일 ~ 말일 (inclusive) */
@Getter
@EqualsAndHashCode
@ToString
public class MonthPeriod implements Period {

    private final LocalDate start; // 해당 월 1일(포함)
    private final LocalDate end;   // 해당 월 말일(포함)

    private MonthPeriod(LocalDate anyDate) {
        this.start = anyDate.withDayOfMonth(1);
        this.end = this.start.with(TemporalAdjusters.lastDayOfMonth());
    }

    public static MonthPeriod of(int year, int month) {
        if (year < 1) throw new IllegalArgumentException("year must be >= 1");
        if (month < 1 || month > 12) throw new IllegalArgumentException("month must be 1..12");
        return new MonthPeriod(LocalDate.of(year, month, 1));
    }

    public static MonthPeriod of(@NonNull LocalDate anyDate) {
        return new MonthPeriod(anyDate);
    }

    /** 파생 값(표기/라벨용) */
    public int year()  { return start.getYear(); }
    public int month() { return start.getMonthValue(); }
    public LocalDate endExclusive() { return end.plusDays(1); }

    @Override public Period prev() { return new MonthPeriod(start.minusMonths(1)); }
    @Override public Period next() { return new MonthPeriod(start.plusMonths(1)); }
}