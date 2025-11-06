package com.likelion.fourthlinethon.team1.cooltime.global.common.time.exception;

import com.likelion.fourthlinethon.team1.cooltime.global.exception.model.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
public enum TimeErrorCode implements BaseErrorCode {

    // LocalDate가 null일 때 사용하는 에러 코드
    LOCAL_DATE_NULL("TIME_400", "LocalDate가 null입니다.", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus status;
}
