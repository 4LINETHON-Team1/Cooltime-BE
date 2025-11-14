package com.likelion.fourthlinethon.team1.cooltime.log.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class DailyLogCalendarResponse {

    @Schema(description = "날짜")
    private LocalDate date;

    @Schema(description = "미룸 여부 (true: 미룸, false: 해냄)")
    private Boolean isPostponed;

    public static DailyLogCalendarResponse from(com.likelion.fourthlinethon.team1.cooltime.log.entity.DailyLog log) {
        return DailyLogCalendarResponse.builder()
                .date(log.getDate())
                .isPostponed(log.isPostponed())
                .build();
    }
}
