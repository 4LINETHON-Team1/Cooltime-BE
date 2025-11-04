package com.likelion.fourthlinethon.team1.cooltime.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@Schema(title = "LoginResponse DTO", description = "로그인 성공 시 반환되는 응답 DTO")
public class LoginResponse {

    @Schema(description = "Access Token")
    private String accessToken;

    @Schema(description = "Refresh Token")
    private String refreshToken;

    @Schema(description = "사용자 닉네임")
    private String nickname;

    @Schema(description = "사용자 아이디 (이메일)")
    private String username;
}
