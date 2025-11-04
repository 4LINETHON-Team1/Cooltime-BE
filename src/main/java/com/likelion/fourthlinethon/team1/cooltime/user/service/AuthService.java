package com.likelion.fourthlinethon.team1.cooltime.user.service;

import com.likelion.fourthlinethon.team1.cooltime.auth.exception.AuthErrorCode;
import com.likelion.fourthlinethon.team1.cooltime.global.exception.CustomException;
import com.likelion.fourthlinethon.team1.cooltime.global.jwt.JwtProvider;
import com.likelion.fourthlinethon.team1.cooltime.user.dto.LoginRequest;
import com.likelion.fourthlinethon.team1.cooltime.user.dto.LoginResponse;
import com.likelion.fourthlinethon.team1.cooltime.user.entity.User;
import com.likelion.fourthlinethon.team1.cooltime.user.exception.UserErrorCode;
import com.likelion.fourthlinethon.team1.cooltime.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Transactional
    public LoginResponse signIn(LoginRequest request) {
        log.info("[로그인 시도] username = {}", request.getUsername());

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException(UserErrorCode.PASSWORD_MISMATCH);
        }

        String accessToken = jwtProvider.createAccessToken(user.getUsername());
        String refreshToken = jwtProvider.createRefreshToken(user.getUsername(), String.valueOf(user.getId()));

        user.updateRefreshToken(refreshToken); // refresh token 저장
        userRepository.save(user);

        log.info("[로그인 성공] username = {}", user.getUsername());

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .nickname(user.getNickname())
                .username(user.getUsername())
                .build();
    }
    /**
     * ✅ 토큰 재발급
     */
    @Transactional
    public LoginResponse refreshAccessToken(String refreshToken) {
        log.info("[토큰 재발급 요청] refreshToken = {}", refreshToken);

        // RefreshToken 유효성 검증
        if (!jwtProvider.validateToken(refreshToken)) {
            throw new CustomException(AuthErrorCode.JWT_TOKEN_INVALID);
        }

        // DB에서 사용자 조회
        User user = userRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new CustomException(AuthErrorCode.JWT_TOKEN_INVALID));

        // 새 AccessToken 발급
        String newAccessToken = jwtProvider.createAccessToken(user.getUsername());

        // RefreshToken이 곧 만료될 경우 새로 발급 (optional)
        long remain = jwtProvider.getExpiration(refreshToken);
        if (remain < 1000 * 60 * 60 * 24) { // 남은 시간이 1일 이하라면 새로 발급
            String newRefreshToken = jwtProvider.createRefreshToken(user.getUsername(), String.valueOf(user.getId()));
            user.updateRefreshToken(newRefreshToken);
            userRepository.save(user);
            refreshToken = newRefreshToken;
        }

        return LoginResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .nickname(user.getNickname())
                .username(user.getUsername())
                .build();
    }

    /**
     * ✅ 로그아웃
     */
    @Transactional
    public void logout(String username) {
        log.info("[로그아웃 요청] username = {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

        user.updateRefreshToken(null); // refresh_token 제거
        userRepository.save(user);

        log.info("[로그아웃 완료] username = {}", username);
    }
}
