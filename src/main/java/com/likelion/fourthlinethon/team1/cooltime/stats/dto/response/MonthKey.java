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
public class MonthKey {
    private int year;
    private int month;

    public static MonthKey of(int year, int month) {
        return MonthKey.builder()
                .year(year)
                .month(month)
                .build();
    }

    public static MonthKey from(LocalDate date) {
        return of(date.getYear(), date.getMonthValue());
    }

}
