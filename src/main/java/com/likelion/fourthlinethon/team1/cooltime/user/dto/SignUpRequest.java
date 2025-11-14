package com.likelion.fourthlinethon.team1.cooltime.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(title = "회원가입 요청 DTO", description = "사용자 회원가입 시 필요한 요청 정보입니다.")
public class SignUpRequest {

    @Schema(
            description = "사용자 아이디 (4~12자, 영문과 숫자 조합)",
            example = "user1234"
    )
    @NotBlank(message = "아이디는 필수 입력 항목입니다.")
    @Pattern(
            regexp = "^[A-Za-z\\d]{4,12}$",
            message = "아이디는 4~12자의 영문과 숫자 조합이어야 합니다."
    )
    private String username;

    @Schema(
            description = "비밀번호 (8~20자, 영문/숫자/특수문자 2종 이상 조합)",
            example = "abc123!@#"
    )
    @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    @Pattern(
            regexp = "^(?=(?:.*[A-Za-z])?.*(?:\\d.*[!@#$%^&*()_+=\\-]|[!@#$%^&*()_+=\\-].*\\d)|(?=.*[A-Za-z])(?=.*\\d)|(?=.*[A-Za-z])(?=.*[!@#$%^&*()_+=\\-])).{8,20}$",
            message = "비밀번호는 8~20자의 영문, 숫자, 특수문자 중 2종 이상을 포함해야 합니다."
    )
    private String password;

    @Schema(
            description = "사용자 닉네임 (한글 12자 이내)",
            example = "민정"
    )
    @NotBlank(message = "닉네임은 필수 입력 항목입니다.")
    @Pattern(
            regexp = "^[가-힣]{1,12}$",
            message = "닉네임은 한글 12자 이내로 입력해주세요."
    )
    private String nickname;
}
