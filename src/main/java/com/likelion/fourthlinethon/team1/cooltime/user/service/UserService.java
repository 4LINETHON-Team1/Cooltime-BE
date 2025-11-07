package com.likelion.fourthlinethon.team1.cooltime.user.service;

import com.likelion.fourthlinethon.team1.cooltime.log.entity.ActivityTag;
import com.likelion.fourthlinethon.team1.cooltime.log.repository.ActivityTagRepository;
import com.likelion.fourthlinethon.team1.cooltime.user.dto.SignUpRequest;
import com.likelion.fourthlinethon.team1.cooltime.user.dto.UserResponse;
import com.likelion.fourthlinethon.team1.cooltime.user.entity.Role;
import com.likelion.fourthlinethon.team1.cooltime.user.entity.User;
import com.likelion.fourthlinethon.team1.cooltime.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ActivityTagRepository activityTagRepository;

    @Transactional
    public UserResponse signUp(SignUpRequest request) {
        log.info("[회원가입 시도] username = {}", request.getUsername());

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        if (userRepository.existsByNickname(request.getNickname())) {
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = User.builder()
                .username(request.getUsername())
                .password(encodedPassword)
                .nickname(request.getNickname())
                .mytype(null)
                .role(Role.User)
                .build();

        User savedUser = userRepository.save(user);
        log.info("[회원가입 성공] id={}, username={}", savedUser.getId(), savedUser.getUsername());

        // ✅ 기본 활동 자동 생성
        List<String> defaultActivities = List.of("공부", "운동", "독서");
        defaultActivities.forEach(activity ->
                activityTagRepository.save(
                        ActivityTag.builder()
                                .name(activity)
                                .isActive(true)
                                .isDefault(true)
                                .user(savedUser)
                                .build()
                )
        );

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
