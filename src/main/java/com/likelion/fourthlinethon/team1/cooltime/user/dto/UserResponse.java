package com.likelion.fourthlinethon.team1.cooltime.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@Schema(title = "회원가입 응답 DTO", description = "회원가입 완료 후 반환되는 사용자 정보입니다.")
public class UserResponse {

    @Schema(description = "사용자 ID (DB PK)", example = "1")
    private Long id;

    @Schema(description = "사용자 아이디 (이메일 또는 로그인용 ID)", example = "user1234")
    private String username;

    @Schema(description = "사용자 닉네임", example = "민정")
    private String nickname;

    @Schema(description = "미룸유형 (회원가입 시 null, 검사 후 설정)", example = "null")
    private String mytype;
}
