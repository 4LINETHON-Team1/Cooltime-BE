package com.likelion.fourthlinethon.team1.cooltime.stats.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostponedRateChangeResponse {
    private int changePercent;

    /** UP, DOWN, SAME, NO_DATA */
    private ChangeStatus changeStatus;

    public enum ChangeStatus { UP, DOWN, SAME, NO_DATA }

    public static PostponedRateChangeResponse of(int currentPercent, Integer previousPercent) {
        if (previousPercent == null) {
            return noData();
        }
        int diff = currentPercent - previousPercent;
        ChangeStatus status = diff > 0 ? ChangeStatus.UP : diff < 0 ? ChangeStatus.DOWN : ChangeStatus.SAME;
        return PostponedRateChangeResponse.builder()
                .changePercent(diff)
                .changeStatus(status)
                .build();
    }

    public static PostponedRateChangeResponse noData() {
        return PostponedRateChangeResponse.builder()
                .changePercent(0)
                .changeStatus(ChangeStatus.NO_DATA)
                .build();
    }
}