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

    /**
     * ✅ 로그인
     */
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

        user.updateRefreshToken(refreshToken);
        userRepository.save(user);

        log.info("[로그인 성공] username = {}", user.getUsername());

        return LoginResponse.from(user, accessToken, refreshToken);
    }

    /**
     * ✅ 토큰 재발급
     */
    @Transactional
    public LoginResponse refreshAccessToken(String refreshToken) {
        log.info("[토큰 재발급 요청] refreshToken = {}", refreshToken);

        if (!jwtProvider.validateToken(refreshToken)) {
            throw new CustomException(AuthErrorCode.JWT_TOKEN_INVALID);
        }

        User user = userRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new CustomException(AuthErrorCode.JWT_TOKEN_INVALID));

        String newAccessToken = jwtProvider.createAccessToken(user.getUsername());

        long remain = jwtProvider.getExpiration(refreshToken);
        if (remain < 1000 * 60 * 60 * 24) { // 남은 유효기간이 1일 이하일 경우
            String newRefreshToken = jwtProvider.createRefreshToken(user.getUsername(), String.valueOf(user.getId()));
            user.updateRefreshToken(newRefreshToken);
            userRepository.save(user);
            refreshToken = newRefreshToken;
        }

        return LoginResponse.from(user, newAccessToken, refreshToken);
    }

    /**
     * ✅ 로그아웃
     */
    @Transactional
    public void logout(String username) {
        log.info("[로그아웃 요청] username = {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

        user.updateRefreshToken(null);
        userRepository.save(user);

        log.info("[로그아웃 완료] username = {}", username);
    }
}
