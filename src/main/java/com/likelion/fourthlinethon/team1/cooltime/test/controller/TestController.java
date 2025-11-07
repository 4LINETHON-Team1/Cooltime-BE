package com.likelion.fourthlinethon.team1.cooltime.test.controller;

import com.likelion.fourthlinethon.team1.cooltime.global.response.BaseResponse;
import com.likelion.fourthlinethon.team1.cooltime.global.security.SecurityUtil;
import com.likelion.fourthlinethon.team1.cooltime.test.dto.TestRequest;
import com.likelion.fourthlinethon.team1.cooltime.test.dto.TestResultResponse;
import com.likelion.fourthlinethon.team1.cooltime.test.service.TestService;
import com.likelion.fourthlinethon.team1.cooltime.user.entity.MyType;
import com.likelion.fourthlinethon.team1.cooltime.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Test", description = "유형 테스트 API")
@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

    @Operation(summary = "미룸유형 테스트 결과 계산 및 저장")
    @PostMapping
    public ResponseEntity<BaseResponse<TestResultResponse>> calculateResult(
            @RequestBody TestRequest request) {

        User user = SecurityUtil.getCurrentUser(); // JWT 인증된 사용자 가져오기
        MyType result = testService.calculateAndSaveResult(user.getUsername(), request.getAnswers());
        TestResultResponse response = new TestResultResponse(result);

        return ResponseEntity.ok(BaseResponse.success("테스트 결과 저장 성공", response));
    }
}
