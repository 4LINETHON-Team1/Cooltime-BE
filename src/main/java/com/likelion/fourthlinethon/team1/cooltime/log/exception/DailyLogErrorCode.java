package com.likelion.fourthlinethon.team1.cooltime.log.exception;

import com.likelion.fourthlinethon.team1.cooltime.global.exception.model.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum DailyLogErrorCode implements BaseErrorCode {

    ACTIVITY_OR_REASON_ALREADY_EXISTS("ACTIVITY_400", "이미 활동/이유가 활성화 되어있습니다.", HttpStatus.BAD_REQUEST),
    LOG_NOT_FOUND("LOG_404", "오늘 기록이 아직 없습니다.", HttpStatus.NOT_FOUND),
    ACTIVITY_NOT_FOUND("ACTIVITY_404", "해당 활동을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    REASON_NOT_FOUND("REASON_404", "해당 이유를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    ALREADY_INACTIVE_TAG("ALREADY_INACTIVE_TAG", "이미 비활성화된 항목입니다.", HttpStatus.BAD_REQUEST),
    INVALID_DATE("LOG_400", "중복 기록은 불가합니다.", HttpStatus.BAD_REQUEST),
    CANNOT_DELETE_DEFAULT_TAG("CANNOT_DELETE_DEFAULT_TAG", "기본값(default) 항목은 삭제할 수 없습니다.", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus status;
}
