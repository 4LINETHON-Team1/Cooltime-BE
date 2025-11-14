package com.likelion.fourthlinethon.team1.cooltime.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(title = "비밀번호 유효성 검사 요청 DTO", description = "비밀번호 형식 검증을 위한 요청 바디입니다.")
public class PasswordCheckRequest {

    @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    @Schema(description = "검사할 비밀번호 (8~20자, 영문/숫자/특수문자 2종 이상)", example = "abc123!@#")
    private String password;
}
