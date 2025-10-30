package com.likelion.fourthlinethon.team1.cooltime.user.exception;

import com.likelion.fourthlinethon.team1.cooltime.global.exception.model.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserErrorCode implements BaseErrorCode {
  USERNAME_ALREADY_EXISTS("USER_4001", "이미 존재하는 사용자 아이디입니다.", HttpStatus.BAD_REQUEST),
  PASSWORD_MISMATCH("USER_4002", "비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
  USER_NOT_FOUND("USER_4041", "존재하지 않는 사용자입니다.", HttpStatus.NOT_FOUND);

  private final String code;
  private final String message;
  private final HttpStatus status;
}
