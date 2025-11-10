package com.likelion.fourthlinethon.team1.cooltime.log.dto;

import com.likelion.fourthlinethon.team1.cooltime.user.entity.MyType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@Schema(title = "DailyLogRequest", description = "미룸 기록 작성/수정 요청 DTO")
public class DailyLogRequest {

    @Schema(description = "미룸 여부", example = "true")
    private Boolean isPostponed;

    @Schema(description = "유형", example = "PERFECTION")
    private MyType myType;

    @Schema(description = "활동 이름 배열", example = "[\"공부하기\", \"운동하기\"]")
    private List<String> activities;

    @Schema(description = "이유 이름 배열", example = "[\"귀찮아서\", \"의욕이 없어서\"]")
    private List<String> reasons;
}
