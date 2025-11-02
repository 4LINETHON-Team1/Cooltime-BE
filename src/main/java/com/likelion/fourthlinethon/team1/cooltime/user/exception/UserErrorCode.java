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
  USER_NOT_FOUND("USER_4041", "존재하지 않는 사용자입니다.", HttpStatus.NOT_FOUND),

  INVALID_USERNAME_FORMAT("USER_4003", "아이디는 4~12자의 영문과 숫자 조합이어야 합니다.", HttpStatus.BAD_REQUEST),
  INVALID_NICKNAME_FORMAT("USER_4004", "닉네임은 한글만 사용 가능하며, 1~12자 이내여야 합니다.", HttpStatus.BAD_REQUEST),
  INVALID_PASSWORD_FORMAT("USER_4005", "비밀번호는 8~20자, 영문·숫자·특수문자 2종 이상 조합해야 합니다.", HttpStatus.BAD_REQUEST);

  private final String code;
  private final String message;
  private final HttpStatus status;
}
