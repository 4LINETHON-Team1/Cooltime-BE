package com.likelion.fourthlinethon.team1.cooltime.log.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
@Schema(title = "이유 삭제 요청 DTO", description = "삭제할 이유의 이름을 담는 요청입니다.")
public class ReasonDeleteRequest {

    @Schema(description = "삭제할 이유 이름", example = "귀찮아서")
    @NotBlank(message = "삭제할 이유 이름은 필수입니다.")
    private String name;
}
