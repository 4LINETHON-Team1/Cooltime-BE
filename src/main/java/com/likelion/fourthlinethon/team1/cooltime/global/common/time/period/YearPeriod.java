package com.likelion.fourthlinethon.team1.cooltime.global.common.time.period;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.time.LocalDate;

/** 연간 기간: 해당 해 1/1 ~ 12/31 (inclusive) */
@Getter
@EqualsAndHashCode
@ToString
public class YearPeriod implements Period {

    private final LocalDate start; // 해당 해 1월 1일(포함)
    private final LocalDate end;   // 해당 해 12월 31일(포함)

    private YearPeriod(LocalDate firstDay) {
        this.start = LocalDate.of(firstDay.getYear(), 1, 1);
        this.end = this.start.plusYears(1).minusDays(1);
    }

    public static YearPeriod of(int year) {
        if (year < 1) throw new IllegalArgumentException("year must be >= 1");
        return new YearPeriod(LocalDate.of(year, 1, 1));
    }

    public static YearPeriod of(@NonNull LocalDate anyDate) {
        return new YearPeriod(anyDate);
    }

    /** 파생 값(표기/라벨용) */
    public int year() { return start.getYear(); }
    public LocalDate endExclusive() { return end.plusDays(1); }

    @Override public Period prev() { return new YearPeriod(start.minusYears(1)); }
    @Override public Period next() { return new YearPeriod(start.plusYears(1)); }
}