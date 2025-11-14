package com.likelion.fourthlinethon.team1.cooltime.global.common.time.resolver;



import com.likelion.fourthlinethon.team1.cooltime.global.common.time.exception.TimeErrorCode;
import com.likelion.fourthlinethon.team1.cooltime.global.common.time.period.MonthPeriod;
import com.likelion.fourthlinethon.team1.cooltime.global.common.time.period.WeekPeriod;
import com.likelion.fourthlinethon.team1.cooltime.global.common.time.period.YearPeriod;
import com.likelion.fourthlinethon.team1.cooltime.global.exception.CustomException;

import java.time.LocalDate;

public interface PeriodResolver {

    // Week
    WeekPeriod resolveWeek(LocalDate anyDate);
    WeekPeriod resolveWeek(int year, int month, int weekOfMonth);
    WeekPeriod resolveWeekByIso(int isoYear, int isoWeek);

    // Month
    MonthPeriod resolveMonth(LocalDate anyDate);
    MonthPeriod resolveMonth(int year, int month);

    // Year
    YearPeriod resolveYear(LocalDate anyDate);
    YearPeriod resolveYear(int year);

    // 유틸
    default LocalDate requireDate(LocalDate date) {
        if (date == null) throw new CustomException(TimeErrorCode.LOCAL_DATE_NULL);
        return date;
    }
}