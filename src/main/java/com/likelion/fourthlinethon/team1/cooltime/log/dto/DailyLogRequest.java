package com.likelion.fourthlinethon.team1.cooltime.log.dto;

import com.likelion.fourthlinethon.team1.cooltime.user.entity.MyType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
public class DailyLogRequest {

    @Schema(description = "오늘 일을 미뤘는지 여부")
    private Boolean isPostponed;

    @Schema(description = "미룸 활동 ID 목록")
    private List<Long> activityIds;

    @Schema(description = "미룸 이유 ID 목록")
    private List<Long> reasonIds;

    @Schema(description = "기록 시점의 미룸유형")
    private MyType myType;
}
