package com.likelion.fourthlinethon.team1.cooltime.global.common.time.period;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClampedPeriod {

    private LocalDate start;
    private LocalDate end;
    private PeriodStatus status;

    public boolean invalid() {
        return status == PeriodStatus.PRE_SIGNUP_INVALID
                || status == PeriodStatus.FUTURE_INVALID;
    }
}