package com.likelion.fourthlinethon.team1.cooltime.user.entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 미룸유형")
public enum MyType {
    @Schema(description = "완벽주의형")
    PERFECTION,
    @Schema(description = "동기저하형")
    MOTIVATION,
    @Schema(description = "스트레스형")
    STRESS;
}
