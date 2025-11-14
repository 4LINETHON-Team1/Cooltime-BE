package com.likelion.fourthlinethon.team1.cooltime.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import com.likelion.fourthlinethon.team1.cooltime.user.entity.User;
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

    @Schema(description = "사용자 아이디 (username)")
    private String username;

    @Schema(description = "유형 테스트 완료 여부 (true=완료, false=미실시)")
    private Boolean hasTested;

    @Schema(description = "사용자의 미룸유형 (PERFECTION, MOTIVATION, STRESS 등)")
    private String myType;

    public static LoginResponse from(User user, String accessToken, String refreshToken) {
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .nickname(user.getNickname())
                .username(user.getUsername())
                .hasTested(user.isHasTested())
                .myType(user.getMytype() == null ? null : user.getMytype().name())
                .build();
    }
}
