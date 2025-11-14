package com.likelion.fourthlinethon.team1.cooltime.log.controller;

import com.likelion.fourthlinethon.team1.cooltime.global.response.BaseResponse;
import com.likelion.fourthlinethon.team1.cooltime.log.dto.request.ActivityDeleteRequest;
import com.likelion.fourthlinethon.team1.cooltime.log.dto.request.ActivityTagRequest;
import com.likelion.fourthlinethon.team1.cooltime.log.dto.response.ActivityTagResponse;
import com.likelion.fourthlinethon.team1.cooltime.log.service.ActivityTagService;
import com.likelion.fourthlinethon.team1.cooltime.user.entity.User;
import com.likelion.fourthlinethon.team1.cooltime.global.security.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "ActivityTag", description = "활동 태그 관리 API")
@RestController
@RequestMapping("/api/activity")
@RequiredArgsConstructor
public class ActivityTagController {

    private final ActivityTagService activityTagService;

    @Operation(summary = "활동 추가", description = "사용자가 직접 활동 항목을 추가합니다.")
    @PostMapping
    public ResponseEntity<BaseResponse<List<ActivityTagResponse>>> createActivity(
            @Valid @RequestBody ActivityTagRequest request) {

        User user = SecurityUtil.getCurrentUser();
        List<ActivityTagResponse> response = activityTagService.createActivity(user, request);

        return ResponseEntity.ok(BaseResponse.success("활동이 추가되었습니다.", response));
    }

    @Operation(summary = "활동 삭제", description = "활동명을 기준으로 비활성화 처리합니다.")
    @DeleteMapping
    public ResponseEntity<BaseResponse<List<ActivityTagResponse>>> deleteActivity(
            @Valid @RequestBody ActivityDeleteRequest request) {

        User user = SecurityUtil.getCurrentUser();
        List<ActivityTagResponse> response = activityTagService.deleteActivityByName(user, request.getName());

        return ResponseEntity.ok(BaseResponse.success("활동이 비활성화되었습니다.", response));
    }
}
