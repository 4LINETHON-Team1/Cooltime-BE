package com.likelion.fourthlinethon.team1.cooltime.log.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivityTagRequest {

    @Schema(description = "활동명", example = "과제 미루기")
    @NotBlank(message = "활동명은 필수 입력입니다.")
    private String name;
}
