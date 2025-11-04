package com.likelion.fourthlinethon.team1.cooltime.user.controller;

import com.likelion.fourthlinethon.team1.cooltime.global.response.BaseResponse;
import com.likelion.fourthlinethon.team1.cooltime.user.dto.LoginRequest;
import com.likelion.fourthlinethon.team1.cooltime.user.dto.LoginResponse;
import com.likelion.fourthlinethon.team1.cooltime.user.dto.RefreshTokenRequest;
import com.likelion.fourthlinethon.team1.cooltime.user.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.core.Authentication;
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

    @Operation(summary = "토큰 재발급 API", description = "Request Body로 RefreshToken을 전달받아 새로운 AccessToken을 발급합니다.")
    @PostMapping("/refresh")
    public ResponseEntity<BaseResponse<LoginResponse>> refreshToken(@RequestBody RefreshTokenRequest request) {
        LoginResponse response = authService.refreshAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(BaseResponse.success("AccessToken 재발급 성공", response));
    }

    // 로그아웃 API (JWT 인증 필터를 통과한 사용자만 가능)
    @Operation(summary = "로그아웃 API", description = "AccessToken 인증 후 RefreshToken 무효화")
    @PostMapping("/sign-out")
    public ResponseEntity<BaseResponse<Void>> logout(Authentication authentication) {
        // JWT 필터를 통과하면 authentication.getName()에 username이 들어 있음
        String username = authentication.getName();
        authService.logout(username);
        return ResponseEntity.ok(BaseResponse.success("로그아웃 성공", null));
    }
}
