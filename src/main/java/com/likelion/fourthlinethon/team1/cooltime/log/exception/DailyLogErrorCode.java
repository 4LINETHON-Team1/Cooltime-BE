package com.likelion.fourthlinethon.team1.cooltime.log.exception;

import com.likelion.fourthlinethon.team1.cooltime.global.exception.model.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum DailyLogErrorCode implements BaseErrorCode {

    ACTIVITY_ALREADY_EXISTS("ACTIVITY_400", "이미 동일한 활동이 존재합니다.", HttpStatus.BAD_REQUEST),
    LOG_NOT_FOUND("LOG_404", "해당 일일 기록을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    ACTIVITY_NOT_FOUND("ACTIVITY_404", "해당 활동을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    REASON_NOT_FOUND("REASON_404", "해당 이유를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_DATE("LOG_400", "오늘 날짜에만 수정 및 등록이 가능합니다.", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus status;
}
