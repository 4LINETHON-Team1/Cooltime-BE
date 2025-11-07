package com.likelion.fourthlinethon.team1.cooltime.log.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class ReasonDeleteRequest {

    @Schema(description = "삭제할 이유 ID")
    private Long reasonId;
}
