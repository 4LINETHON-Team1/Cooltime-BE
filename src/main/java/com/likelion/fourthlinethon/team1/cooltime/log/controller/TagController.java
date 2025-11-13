package com.likelion.fourthlinethon.team1.cooltime.log.controller;

import com.likelion.fourthlinethon.team1.cooltime.global.response.BaseResponse;
import com.likelion.fourthlinethon.team1.cooltime.log.dto.TagResponse;
import com.likelion.fourthlinethon.team1.cooltime.log.service.TagService;
import com.likelion.fourthlinethon.team1.cooltime.user.entity.User;
import com.likelion.fourthlinethon.team1.cooltime.global.security.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Tag", description = "활성화된 활동 및 이유 태그 조회 API")
@RestController
@RequestMapping("/api/tag")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @Operation(summary = "활성화된 활동 및 이유 태그 조회", description = "JWT 토큰 기반으로 해당 사용자의 활성화된 활동과 이유를 모두 반환합니다.")
    @GetMapping
    public ResponseEntity<BaseResponse<TagResponse>> getActiveTags() {
        User user = SecurityUtil.getCurrentUser();
        TagResponse response = tagService.getActiveTags(user);
        return ResponseEntity.ok(BaseResponse.success("활성화된 활동 및 이유 태그 조회 성공", response));
    }
}
