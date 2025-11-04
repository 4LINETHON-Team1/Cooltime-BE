package com.likelion.fourthlinethon.team1.cooltime.auth.exception;

import com.likelion.fourthlinethon.team1.cooltime.global.exception.model.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
public enum AuthErrorCode implements BaseErrorCode {
  LOGIN_FAIL("AUTH_4001", "로그인 처리 중 오류 발생", HttpStatus.BAD_REQUEST),
  TOKEN_FAIL("AUTH_4002", "액세스 토큰 요청 실패", HttpStatus.UNAUTHORIZED),
  USER_INFO_FAIL("AUTH_4003", "사용자 정보 요청 실패", HttpStatus.UNAUTHORIZED),
  INVALID_ACCESS_TOKEN("AUTH_4004", "유효하지 않은 액세스 토큰입니다.", HttpStatus.UNAUTHORIZED),
  ACCESS_TOKEN_EXPIRED("AUTH_4005", "액세스 토큰이 만료되었습니다.", HttpStatus.UNAUTHORIZED),
  REFRESH_TOKEN_REQUIRED("AUTH_4006", "리프레시 토큰이 필요합니다.", HttpStatus.FORBIDDEN),

  JWT_TOKEN_INVALID("AUTH_4001", "유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED),
  JWT_TOKEN_EXPIRED("AUTH_4002", "만료된 토큰입니다.", HttpStatus.UNAUTHORIZED),
  UNSUPPORTED_TOKEN("AUTH_4003", "지원하지 않는 토큰 형식입니다.", HttpStatus.BAD_REQUEST),
  MALFORMED_JWT_TOKEN("AUTH_4004", "JWT 토큰 구조가 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
  INVALID_SIGNATURE("AUTH_4005", "서명 검증에 실패했습니다.", HttpStatus.UNAUTHORIZED),
  ILLEGAL_ARGUMENT("AUTH_4006", "잘못된 JWT 인자입니다.", HttpStatus.BAD_REQUEST);

  private final String code;
  private final String message;
  private final HttpStatus status;
}
