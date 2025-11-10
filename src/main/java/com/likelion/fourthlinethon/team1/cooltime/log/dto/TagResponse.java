package com.likelion.fourthlinethon.team1.cooltime.log.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Schema(title = "활동 및 이유 태그 통합 응답 DTO", description = "활성화된 활동 및 이유 목록을 전부 반환합니다.")
public class TagResponse {

    @Schema(description = "활성화된 활동 목록")
    private List<ActivityTagResponse> activities;

    @Schema(description = "활성화된 이유 목록")
    private List<ReasonTagResponse> reasons;
}
