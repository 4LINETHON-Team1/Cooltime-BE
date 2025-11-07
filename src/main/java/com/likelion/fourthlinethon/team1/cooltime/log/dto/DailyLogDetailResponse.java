package com.likelion.fourthlinethon.team1.cooltime.log.dto;

import com.likelion.fourthlinethon.team1.cooltime.log.entity.DailyLog;
import com.likelion.fourthlinethon.team1.cooltime.user.entity.MyType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class DailyLogDetailResponse {

    @Schema(description = "기록 ID")
    private Long id;

    @Schema(description = "날짜")
    private LocalDate date;

    @Schema(description = "오늘 할 일을 미뤘는지 여부")
    private Boolean isPostponed;

    @Schema(description = "미룸 유형")
    private MyType myType;

    @Schema(description = "활동 목록")
    private List<String> activities;

    @Schema(description = "이유 목록")
    private List<String> reasons;

    public static DailyLogDetailResponse fromEntity(DailyLog log, List<String> activities, List<String> reasons) {
        return DailyLogDetailResponse.builder()
                .id(log.getId())
                .date(log.getDate())
                .isPostponed(log.isPostponed())
                .myType(log.getType())
                .activities(activities)
                .reasons(reasons)
                .build();
    }
}
