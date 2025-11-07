package com.likelion.fourthlinethon.team1.cooltime.log.controller;

import com.likelion.fourthlinethon.team1.cooltime.global.response.BaseResponse;
import com.likelion.fourthlinethon.team1.cooltime.global.security.SecurityUtil;
import com.likelion.fourthlinethon.team1.cooltime.log.dto.DailyLogRequest;
import com.likelion.fourthlinethon.team1.cooltime.log.dto.DailyLogResponse;
import com.likelion.fourthlinethon.team1.cooltime.log.service.DailyLogService;
import com.likelion.fourthlinethon.team1.cooltime.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "DailyLog", description = "미룸 기록 관리 API")
@RestController
@RequestMapping("/api/log")
@RequiredArgsConstructor
public class DailyLogController {

    private final DailyLogService dailyLogService;

    @Operation(summary = "미룸 기록하기", description = "오늘 날짜의 미룸 기록을 작성합니다.")
    @PostMapping
    public ResponseEntity<BaseResponse<DailyLogResponse>> createLog(@RequestBody DailyLogRequest request) {
        User user = SecurityUtil.getCurrentUser();
        DailyLogResponse response = dailyLogService.createDailyLog(user, request);
        return ResponseEntity.ok(BaseResponse.success("미룸 기록이 저장되었습니다.", response));
    }

    @Operation(summary = "미룸 기록 수정하기", description = "오늘 날짜의 미룸 기록을 수정합니다.")
    @PutMapping
    public ResponseEntity<BaseResponse<DailyLogResponse>> updateDailyLog(
            @RequestBody DailyLogRequest request) {
        User user = SecurityUtil.getCurrentUser();
        DailyLogResponse response = dailyLogService.updateDailyLog(user, request);
        return ResponseEntity.ok(BaseResponse.success("미룸 기록이 수정되었습니다.", response));
    }

}
