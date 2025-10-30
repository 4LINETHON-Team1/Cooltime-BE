package com.likelion.fourthlinethon.team1.cooltime.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.likelion.fourthlinethon.team1.cooltime.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

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

  @Column(name="nickname", nullable = false)
  private String nickname;

  @Column(name = "username", nullable = false, unique = true)
  private String username;

  @JsonIgnore
  @Column(name = "password", nullable = false)
  private String password;

  @JsonIgnore
  @Column(name = "refresh_token")
  private String refreshToken;

  @Column(name = "role", nullable = false)
  @Enumerated(EnumType.STRING)
  @Builder.Default
  private Role role = Role.User;

}