package com.likelion.fourthlinethon.team1.cooltime.log.dto;

import com.likelion.fourthlinethon.team1.cooltime.log.entity.ActivityTag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ActivityTagResponse {

    @Schema(description = "활동 ID")
    private Long id;

    @Schema(description = "활동명")
    private String name;

    @Schema(description = "활성화 여부")
    private Boolean isActive;

    @Schema(description = "기본값 여부")
    private Boolean isDefault;

    public static ActivityTagResponse fromEntity(ActivityTag tag) {
        return ActivityTagResponse.builder()
                .id(tag.getId())
                .name(tag.getName())
                .isActive(tag.getIsActive())
                .isDefault(tag.getIsDefault())
                .build();
    }
}
