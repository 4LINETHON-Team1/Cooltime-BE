package com.likelion.fourthlinethon.team1.cooltime.user.controller;

import com.likelion.fourthlinethon.team1.cooltime.global.response.BaseResponse;
import com.likelion.fourthlinethon.team1.cooltime.user.dto.LoginRequest;
import com.likelion.fourthlinethon.team1.cooltime.user.dto.LoginResponse;
import com.likelion.fourthlinethon.team1.cooltime.user.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth", description = "로그인 및 인증 관련 API")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "로그인 API", description = "사용자 아이디와 비밀번호를 통해 로그인하고 JWT 토큰을 발급합니다.")
    @PostMapping("/sign-in")
    public ResponseEntity<BaseResponse<LoginResponse>> signIn(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.signIn(request);
        return ResponseEntity.ok(BaseResponse.success("로그인 성공", response));
    }
}
