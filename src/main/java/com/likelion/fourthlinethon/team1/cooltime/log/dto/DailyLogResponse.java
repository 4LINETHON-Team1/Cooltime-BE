package com.likelion.fourthlinethon.team1.cooltime.log.dto;

import com.likelion.fourthlinethon.team1.cooltime.log.entity.DailyLog;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class DailyLogResponse {

    @Schema(description = "로그 ID")
    private Long id;

    @Schema(description = "작성 날짜")
    private LocalDate date;

    @Schema(description = "미룸 여부")
    private Boolean isPostponed;

    @Schema(description = "미룸유형")
    private String myType;

    public static DailyLogResponse fromEntity(DailyLog log) {
        return DailyLogResponse.builder()
                .id(log.getId())
                .date(log.getDate())
                .isPostponed(log.getIsPostponed())
                .myType(log.getType().toString())
                .build();
    }
}
