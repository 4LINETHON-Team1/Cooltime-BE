package com.likelion.fourthlinethon.team1.cooltime.global.common.time.period;

import java.time.LocalDate;
import java.util.Objects;

public final class PeriodGuard {
    private PeriodGuard() {}

    public static ClampedPeriod clamp(LocalDate signupDate, LocalDate periodStart, LocalDate periodEnd, LocalDate today) {
        Objects.requireNonNull(signupDate);
        Objects.requireNonNull(periodStart);
        Objects.requireNonNull(periodEnd);
        Objects.requireNonNull(today);

        if (periodStart.isAfter(periodEnd)) {
            throw new IllegalArgumentException("기간의 시작일이 종료일 이후입니다.");
        }

        // 전체가 가입 이전
        if (periodEnd.isBefore(signupDate)) {
            return new ClampedPeriod(periodStart, periodEnd, PeriodStatus.PRE_SIGNUP_INVALID);
        }
        // 전체가 미래
        if (periodStart.isAfter(today)) {
            return new ClampedPeriod(periodStart, periodEnd, PeriodStatus.FUTURE_INVALID);
        }

        boolean containsSignup = !signupDate.isBefore(periodStart) && !signupDate.isAfter(periodEnd); // start <= signup <= end
        boolean containsToday  = !today.isBefore(periodStart) && !today.isAfter(periodEnd);           // start <= today <= end

        LocalDate start = periodStart;
        LocalDate end   = periodEnd;

        if (containsSignup && containsToday) {
            // 가입 기간이면서 현재 기간까지 겹침: 시작은 가입일, 종료는 오늘
            start = signupDate.isAfter(start) ? signupDate : start;
            end   = today.isBefore(end) ? today : end;
            return new ClampedPeriod(start, end, PeriodStatus.CURRENT_TRUNCATED);
        }

        if (containsSignup) {
            // 가입 기간: 시작 보정
            start = signupDate.isAfter(start) ? signupDate : start;
            return new ClampedPeriod(start, end, PeriodStatus.SIGNUP_PERIOD_ADJUSTED);
        }

        if (containsToday) {
            // 현재 기간: 종료 보정
            end = today.isBefore(end) ? today : end;
            return new ClampedPeriod(start, end, PeriodStatus.CURRENT_TRUNCATED);
        }

        // 과거 유효 기간
        return new ClampedPeriod(start, end, PeriodStatus.PAST_VALID);
    }

    // 편의 오버로드: 주/월/년 공통 규칙
    public static ClampedPeriod clamp(LocalDate signupDate, WeekPeriod period, LocalDate today) {
        return clamp(signupDate, period.getStart(), period.getEnd(), today);
    }
    public static ClampedPeriod clamp(LocalDate signupDate, MonthPeriod period, LocalDate today) {
        return clamp(signupDate, period.getStart(), period.getEnd(), today);
    }
    public static ClampedPeriod clamp(LocalDate signupDate, YearPeriod period, LocalDate today) {
        return clamp(signupDate, period.getStart(), period.getEnd(), today);
    }
}