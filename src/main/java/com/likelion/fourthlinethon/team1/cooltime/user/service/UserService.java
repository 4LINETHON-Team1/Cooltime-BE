package com.likelion.fourthlinethon.team1.cooltime.user.service;

import com.likelion.fourthlinethon.team1.cooltime.user.entity.Role;
import com.likelion.fourthlinethon.team1.cooltime.user.entity.User;
import com.likelion.fourthlinethon.team1.cooltime.user.repository.UserRepository;
import com.likelion.fourthlinethon.team1.cooltime.user.dto.SignUpRequest;
import com.likelion.fourthlinethon.team1.cooltime.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse signUp(SignUpRequest request) {
        log.info("[회원가입 시도] username = {}", request.getUsername());

        // 아이디 중복 검사
        if (userRepository.existsByUsername(request.getUsername())) {
            log.warn("[회원가입 실패] 이미 존재하는 아이디: {}", request.getUsername());
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        // 닉네임 중복 검사 추가
        if (userRepository.existsByNickname(request.getNickname())) {
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // User 엔티티 생성 (mytype은 null로 초기화)
        User user = User.builder()
                .username(request.getUsername())
                .password(encodedPassword)
                .nickname(request.getNickname())
                .mytype(null)
                .role(Role.User)
                .build();

        // 저장
        User savedUser = userRepository.save(user);
        log.info("[회원가입 성공] id={}, username={}", savedUser.getId(), savedUser.getUsername());

        // DTO로 변환 후 반환
        return new UserResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getNickname(),
                savedUser.getMytype() == null ? null : savedUser.getMytype().name()
        );
    }

    @Transactional(readOnly = true)
    public boolean checkUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Transactional(readOnly = true)
    public boolean checkNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }
}
