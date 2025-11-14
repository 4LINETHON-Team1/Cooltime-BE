package com.likelion.fourthlinethon.team1.cooltime.log.controller;

import com.likelion.fourthlinethon.team1.cooltime.global.response.BaseResponse;
import com.likelion.fourthlinethon.team1.cooltime.global.security.SecurityUtil;
import com.likelion.fourthlinethon.team1.cooltime.log.dto.response.DailyLogDetailResponse;
import com.likelion.fourthlinethon.team1.cooltime.log.dto.request.DailyLogRequest;
import com.likelion.fourthlinethon.team1.cooltime.log.dto.response.DailyLogResponse;
import com.likelion.fourthlinethon.team1.cooltime.log.dto.response.MonthlyLogSummaryResponse;
import com.likelion.fourthlinethon.team1.cooltime.log.service.DailyLogService;
import com.likelion.fourthlinethon.team1.cooltime.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Tag(name = "DailyLog", description = "미룸 기록 관리 API")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DailyLogController {

    private final DailyLogService dailyLogService;

    /** 미룸 기록 작성 */
    @Operation(summary = "미룸 기록하기", description = "오늘 날짜의 미룸 기록을 작성합니다.")
    @PostMapping("/log")
    public ResponseEntity<BaseResponse<DailyLogResponse>> createLog(@RequestBody DailyLogRequest request) {
        User user = SecurityUtil.getCurrentUser();
        DailyLogResponse response = dailyLogService.createDailyLog(user, request);
        return ResponseEntity.ok(BaseResponse.success("미룸 기록이 저장되었습니다.", response));
    }

    /** 미룸 기록 수정 */
    @Operation(summary = "미룸 기록 수정하기", description = "오늘 날짜의 미룸 기록을 수정합니다.")
    @PutMapping("/log")
    public ResponseEntity<BaseResponse<DailyLogResponse>> updateDailyLog(@RequestBody DailyLogRequest request) {
        User user = SecurityUtil.getCurrentUser();
        DailyLogResponse response = dailyLogService.updateDailyLog(user, request);
        return ResponseEntity.ok(BaseResponse.success("미룸 기록이 수정되었습니다.", response));
    }

    /** 미룸 기록 조회 */
    @Operation(summary = "미룸 기록 조회", description = "특정 날짜의 미룸 기록을 조회합니다.")
    @GetMapping("/log")
    public ResponseEntity<BaseResponse<DailyLogDetailResponse>> getDailyLog(@RequestParam String date) {
        User user = SecurityUtil.getCurrentUser();
        LocalDate targetDate = LocalDate.parse(date);
        DailyLogDetailResponse response = dailyLogService.getDailyLog(user, targetDate);
        return ResponseEntity.ok(BaseResponse.success("미룸 기록 조회 성공", response));
    }

    /** 캘린더 조회 */
    @Operation(summary = "캘린더 조회", description = "특정 월의 미룸 기록 요약을 조회합니다.")
    @GetMapping("/calendar")
    public ResponseEntity<BaseResponse<MonthlyLogSummaryResponse>> getMonthlyLogs(
            @RequestParam int year,
            @RequestParam int month) {

        User user = SecurityUtil.getCurrentUser();
        MonthlyLogSummaryResponse response = dailyLogService.getMonthlyLogs(user, year, month);
        return ResponseEntity.ok(BaseResponse.success("캘린더 조회 성공", response));
    }
}
