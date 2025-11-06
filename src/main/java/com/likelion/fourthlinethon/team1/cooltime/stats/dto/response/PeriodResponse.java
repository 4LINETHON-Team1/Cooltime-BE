package com.likelion.fourthlinethon.team1.cooltime.stats.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PeriodResponse {

    private LocalDate startDate;
    private LocalDate endDate;

    /** 시작일과 종료일로 생성 */
    public static PeriodResponse of(LocalDate start, LocalDate end) {
        return PeriodResponse.builder()
                .startDate(start)
                .endDate(end)
                .build();
    }

}