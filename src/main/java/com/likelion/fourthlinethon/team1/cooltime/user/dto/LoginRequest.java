package com.likelion.fourthlinethon.team1.cooltime.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(title = "LoginRequest DTO", description = "로그인 요청 DTO")
public class LoginRequest {

    @NotBlank(message = "아이디는 필수 입력 항목입니다.")
    @Schema(description = "사용자 아이디", example = "user123")
    private String username;

    @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    @Schema(description = "사용자 비밀번호", example = "password123!")
    private String password;
}
