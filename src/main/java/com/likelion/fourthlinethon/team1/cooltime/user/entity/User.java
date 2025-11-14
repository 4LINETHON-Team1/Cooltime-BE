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
@Table(name = "users")
public class User extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "username", nullable = false, unique = true, length = 12)
  private String username;

  @JsonIgnore
  @Column(name = "password", nullable = false, length = 60)
  private String password;

  @Column(name = "nickname", nullable = false, length = 36)
  private String nickname;

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

