package com.likelion.fourthlinethon.team1.cooltime.log.exception;

import com.likelion.fourthlinethon.team1.cooltime.global.exception.model.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum DailyLogErrorCode implements BaseErrorCode {
    NOT_FOUND("LOG_404", "해당 일일 기록을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

  private final String code;
  private final String message;
  private final HttpStatus status;
}