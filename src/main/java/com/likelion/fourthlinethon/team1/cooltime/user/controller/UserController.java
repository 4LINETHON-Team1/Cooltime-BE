package com.likelion.fourthlinethon.team1.cooltime.user.controller;

import com.likelion.fourthlinethon.team1.cooltime.global.security.CustomUserDetails;
import com.likelion.fourthlinethon.team1.cooltime.user.dto.SignUpRequest;
import com.likelion.fourthlinethon.team1.cooltime.user.dto.UserResponse;
import com.likelion.fourthlinethon.team1.cooltime.global.exception.CustomException;
import com.likelion.fourthlinethon.team1.cooltime.user.exception.UserErrorCode;
import com.likelion.fourthlinethon.team1.cooltime.user.dto.PasswordCheckRequest;
import com.likelion.fourthlinethon.team1.cooltime.user.service.UserService;
import com.likelion.fourthlinethon.team1.cooltime.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * 회원가입 및 사용자 관련 API
 */
@Tag(name = "Auth", description = "회원 인증/가입 관련 API")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 회원가입 API
     */
    @Operation(
            summary = "회원가입 API",
            description = "사용자로부터 아이디, 비밀번호, 닉네임을 입력받아 회원가입을 진행합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "회원가입 성공",
                            content = @Content(schema = @Schema(implementation = UserResponse.class))),
                    @ApiResponse(responseCode = "400", description = "입력값 검증 실패 또는 중복된 아이디/닉네임")
            }
    )
    @PostMapping("/sign-up")
    public ResponseEntity<UserResponse> signUp(
            @Valid @RequestBody SignUpRequest signUpRequest) {
        UserResponse response = userService.signUp(signUpRequest);
        return ResponseEntity.ok(response);
    }

    // 아이디 중복 확인 API
    @Operation(summary = "아이디 중복 확인 API", description = "아이디 형식 및 중복 여부를 확인합니다.")
    @GetMapping("/check-username")
    public ResponseEntity<BaseResponse<String>> checkUsername(@RequestParam String username) {
        userService.validateUsername(username);
        return ResponseEntity.ok(BaseResponse.success("사용 가능한 아이디입니다.", null));
    }

    // 닉네임 중복 확인 API
    @Operation(summary = "닉네임 중복 확인 API", description = "닉네임 형식 및 중복 여부를 확인합니다.")
    @GetMapping("/check-nickname")
    public ResponseEntity<BaseResponse<String>> checkNickname(@RequestParam String nickname) {
        userService.validateNickname(nickname);
        return ResponseEntity.ok(BaseResponse.success("사용 가능한 닉네임입니다.", null));
    }

    /**
     * 비밀번호 유효성 검사 API
     */
    @Operation(summary = "비밀번호 유효성 검사", description = "비밀번호가 8~20자, 영문·숫자·특수문자 2종 이상 조합인지 확인합니다.")
    @PostMapping("/check-password")
    public ResponseEntity<BaseResponse<String>> checkPassword(@RequestBody PasswordCheckRequest request) {
        String password = request.getPassword();

        if (!isValidPassword(password)) {
            throw new CustomException(UserErrorCode.INVALID_PASSWORD_FORMAT);
        }

        return ResponseEntity.ok(BaseResponse.success("사용 가능한 비밀번호입니다.", null));
    }

    /**
     * 비밀번호 2종 이상 조합(영문·숫자·특수문자) 검증 메서드
     */
    private boolean isValidPassword(String password) {
        if (password == null) return false;
        if (password.length() < 8 || password.length() > 20) return false;

        boolean hasLetter = password.matches(".*[A-Za-z].*");
        boolean hasDigit = password.matches(".*\\d.*");
        boolean hasSpecial = password.matches(".*[!@#$%^&*()_+=-].*");

        int count = 0;
        if (hasLetter) count++;
        if (hasDigit) count++;
        if (hasSpecial) count++;

        return count >= 2;
    }

    @Operation(summary = "내 정보 조회", description = "JWT 인증 후 자신의 정보를 반환합니다.")
    @GetMapping("/me")
    public ResponseEntity<BaseResponse<String>> getMyInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails
            ) {
        String username = userDetails.getUsername();
        return ResponseEntity.ok(BaseResponse.success("인증된 사용자: " + username, null));
    }



}
