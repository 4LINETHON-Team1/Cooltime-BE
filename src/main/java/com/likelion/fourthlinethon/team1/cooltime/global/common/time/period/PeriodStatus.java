package com.likelion.fourthlinethon.team1.cooltime.global.common.time.period;

public enum PeriodStatus {
    PRE_SIGNUP_INVALID,     // 가입 이전 (invalid)
    SIGNUP_PERIOD_ADJUSTED, // 가입 기간 포함(시작일 보정)
    PAST_VALID,             // 과거 유효 기간(그대로 사용)
    CURRENT_TRUNCATED,      // 현재 기간 포함(종료일 보정)
    FUTURE_INVALID          // 미래 기간 (invalid)
}