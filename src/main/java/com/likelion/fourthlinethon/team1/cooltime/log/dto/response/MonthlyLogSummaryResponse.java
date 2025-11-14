package com.likelion.fourthlinethon.team1.cooltime.log.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class MonthlyLogSummaryResponse {
    private Summary summary;
    private List<DailyLogCalendarResponse> logs;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Summary {
        private long postponedCount;
        private long completedCount;
    }
}
