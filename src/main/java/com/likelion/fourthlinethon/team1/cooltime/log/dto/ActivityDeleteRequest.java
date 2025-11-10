package com.likelion.fourthlinethon.team1.cooltime.log.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
@Schema(title = "활동 삭제 요청 DTO", description = "삭제할 활동의 이름을 담는 요청입니다.")
public class ActivityDeleteRequest {

    @Schema(description = "삭제할 활동 이름", example = "등교하기")
    @NotBlank(message = "삭제할 활동 이름은 필수입니다.")
    private String name;
}
