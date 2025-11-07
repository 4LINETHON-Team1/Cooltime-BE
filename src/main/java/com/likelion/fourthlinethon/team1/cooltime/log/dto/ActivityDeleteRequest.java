package com.likelion.fourthlinethon.team1.cooltime.log.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "활동 삭제 요청 DTO")
public class ActivityDeleteRequest {

    @NotNull(message = "삭제할 활동 ID는 필수입니다.")
    @Schema(description = "활동 ID", example = "2")
    private Long activityId;
}
