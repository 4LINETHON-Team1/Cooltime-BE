package com.likelion.fourthlinethon.team1.cooltime.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.likelion.fourthlinethon.team1.cooltime.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "users") //user테이블 이름은 꼭 users로 하기
public class User extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // 4~12자 영문/숫자 조합 → DTO에서 정규식 검증 / DB에서는 길이 제약
  @Column(name = "username", nullable = false, unique = true, length = 12)
  private String username;

  // 8~20자 비밀번호 / BCrypt 해시를 감안해 60자로 저장
  @JsonIgnore
  @Column(name = "password", nullable = false, length = 60)
  private String password;

  // 닉네임: 한글 12자 이내 (UTF-8 3바이트 기준 → length=36)
  @Column(name = "nickname", nullable = false, length = 36)
  private String nickname;

  // 미룸유형
  @Enumerated(EnumType.STRING)
  @Column(name = "mytype", nullable = true)
  private MyType mytype;

  @JsonIgnore
  @Column(name = "refresh_token")
  private String refreshToken;

  @Enumerated(EnumType.STRING)
  @Column(name = "role", nullable = false)
  @Builder.Default
  private Role role = Role.User;

  @Column(name = "has_tested", nullable = false)
  @Builder.Default
  private boolean hasTested = false;

  @Column(name = "last_tested_at")
  private LocalDateTime lastTestedAt;

  public void updateRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public void updateMyType(MyType type) {
    this.mytype = type;
    this.hasTested = true;
    this.lastTestedAt = LocalDateTime.now();
  }
}

