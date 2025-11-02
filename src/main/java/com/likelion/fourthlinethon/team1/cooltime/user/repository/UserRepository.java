package com.likelion.fourthlinethon.team1.cooltime.user.repository;

import com.likelion.fourthlinethon.team1.cooltime.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByNickname(String nickname);

    // 리프레시 토큰으로 사용자 조회 (JWT 토큰 재발급 시 사용)
    Optional<User> findByRefreshToken(String refreshToken);
}
