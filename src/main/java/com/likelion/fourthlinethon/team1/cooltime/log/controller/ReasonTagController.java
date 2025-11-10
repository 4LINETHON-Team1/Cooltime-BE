package com.likelion.fourthlinethon.team1.cooltime.log.controller;

import com.likelion.fourthlinethon.team1.cooltime.global.response.BaseResponse;
import com.likelion.fourthlinethon.team1.cooltime.log.dto.ReasonDeleteRequest;
import com.likelion.fourthlinethon.team1.cooltime.log.dto.ReasonTagRequest;
import com.likelion.fourthlinethon.team1.cooltime.log.dto.ReasonTagResponse;
import com.likelion.fourthlinethon.team1.cooltime.log.service.ReasonTagService;
import com.likelion.fourthlinethon.team1.cooltime.user.entity.User;
import com.likelion.fourthlinethon.team1.cooltime.global.security.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "ReasonTag", description = "이유 태그 관리 API")
@RestController
@RequestMapping("/api/reason")
@RequiredArgsConstructor
public class ReasonTagController {

    private final ReasonTagService reasonTagService;

    @Operation(summary = "이유 추가", description = "사용자가 직접 이유 항목을 추가합니다.")
    @PostMapping
    public ResponseEntity<BaseResponse<List<ReasonTagResponse>>> createReason(
            @Valid @RequestBody ReasonTagRequest request) {

        User user = SecurityUtil.getCurrentUser();
        List<ReasonTagResponse> response = reasonTagService.createReason(user, request);

        return ResponseEntity.ok(BaseResponse.success("이유가 추가되었습니다.", response));
    }

    @Operation(summary = "이유 삭제", description = "이유명을 기준으로 비활성화 처리합니다.")
    @DeleteMapping
    public ResponseEntity<BaseResponse<List<ReasonTagResponse>>> deleteReason(
            @Valid @RequestBody ReasonDeleteRequest request) {

        User user = SecurityUtil.getCurrentUser();
        List<ReasonTagResponse> response = reasonTagService.deleteReasonByName(user, request.getName());

        return ResponseEntity.ok(BaseResponse.success("이유가 비활성화되었습니다.", response));
    }
}
