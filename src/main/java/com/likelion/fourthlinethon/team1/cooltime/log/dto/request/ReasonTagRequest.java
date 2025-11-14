package com.likelion.fourthlinethon.team1.cooltime.log.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReasonTagRequest {

    @Schema(description = "이유명", example = "피곤해서")
    private String name;
}
