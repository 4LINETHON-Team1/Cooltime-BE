package com.likelion.fourthlinethon.team1.cooltime.user.controller;

import com.likelion.fourthlinethon.team1.cooltime.user.dto.SignUpRequest;
import com.likelion.fourthlinethon.team1.cooltime.user.dto.UserResponse;
import com.likelion.fourthlinethon.team1.cooltime.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
     * 🧩 회원가입 API
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

    /**
     * 🧩 아이디 중복 확인 API
     */
    @Operation(
            summary = "아이디 중복 확인 API",
            description = "회원가입 전, 입력한 아이디가 이미 사용 중인지 확인합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "true = 이미 존재 / false = 사용 가능")
            }
    )
    @GetMapping("/check-username")
    public ResponseEntity<Boolean> checkUsername(
            @Parameter(description = "중복 확인할 아이디", example = "user1234")
            @RequestParam String username) {
        boolean exists = userService.checkUsername(username);
        return ResponseEntity.ok(exists);
    }

    /**
     * 🧩 닉네임 중복 확인 API
     */
    @Operation(
            summary = "닉네임 중복 확인 API",
            description = "회원가입 전, 입력한 닉네임이 이미 사용 중인지 확인합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "true = 이미 존재 / false = 사용 가능")
            }
    )
    @GetMapping("/check-nickname")
    public ResponseEntity<Boolean> checkNickname(
            @Parameter(description = "중복 확인할 닉네임", example = "민정")
            @RequestParam String nickname) {
        boolean exists = userService.checkNickname(nickname);
        return ResponseEntity.ok(exists);
    }
}
