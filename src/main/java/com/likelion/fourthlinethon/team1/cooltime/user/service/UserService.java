package com.likelion.fourthlinethon.team1.cooltime.user.service;

import com.likelion.fourthlinethon.team1.cooltime.global.exception.CustomException;
import com.likelion.fourthlinethon.team1.cooltime.log.entity.ActivityTag;
import com.likelion.fourthlinethon.team1.cooltime.log.repository.ActivityTagRepository;
import com.likelion.fourthlinethon.team1.cooltime.user.dto.SignUpRequest;
import com.likelion.fourthlinethon.team1.cooltime.user.dto.UserResponse;
import com.likelion.fourthlinethon.team1.cooltime.user.entity.Role;
import com.likelion.fourthlinethon.team1.cooltime.user.entity.User;
import com.likelion.fourthlinethon.team1.cooltime.user.exception.UserErrorCode;
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
            throw new CustomException(UserErrorCode.USERNAME_ALREADY_EXISTS);
        }
        if (userRepository.existsByNickname(request.getNickname())) {
            throw new CustomException(UserErrorCode.USERNAME_ALREADY_EXISTS);
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
    public void validateUsername(String username) {
        if (!username.matches("^[A-Za-z\\d]{4,12}$")) {
            throw new CustomException(UserErrorCode.INVALID_USERNAME_FORMAT);
        }
        if (userRepository.existsByUsername(username)) {
            throw new CustomException(UserErrorCode.USERNAME_ALREADY_EXISTS);
        }
    }

    @Transactional(readOnly = true)
    public void validateNickname(String nickname) {
        if (!nickname.matches("^[가-힣]{1,12}$")) {
            throw new CustomException(UserErrorCode.INVALID_NICKNAME_FORMAT);
        }

        if (userRepository.existsByNickname(nickname)) {
            throw new CustomException(UserErrorCode.USERNAME_ALREADY_EXISTS);
        }
    }

}
