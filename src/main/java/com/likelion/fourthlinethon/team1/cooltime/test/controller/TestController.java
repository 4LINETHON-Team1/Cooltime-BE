package com.likelion.fourthlinethon.team1.cooltime.test.controller;

import com.likelion.fourthlinethon.team1.cooltime.global.response.BaseResponse;
import com.likelion.fourthlinethon.team1.cooltime.test.dto.TestRequest;
import com.likelion.fourthlinethon.team1.cooltime.test.dto.TestResultResponse;
import com.likelion.fourthlinethon.team1.cooltime.test.service.TestService;
import com.likelion.fourthlinethon.team1.cooltime.user.entity.MyType;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

    @Operation(summary = "미룸유형 테스트 결과 계산 및 저장")
    @PostMapping
    public ResponseEntity<BaseResponse<TestResultResponse>> calculateResult(
            @AuthenticationPrincipal String username,
            @RequestBody TestRequest request) {

        MyType result = testService.calculateAndSaveResult(username, request.getAnswers());
        TestResultResponse response = new TestResultResponse(result);
        return ResponseEntity.ok(BaseResponse.success(response));
    }
}
