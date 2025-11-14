package com.likelion.fourthlinethon.team1.cooltime.stats.exception;

import com.likelion.fourthlinethon.team1.cooltime.global.exception.model.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum StatsErrorCode implements BaseErrorCode {
    // 유효하지 않은 기간
    PERIOD_OUT_OF_RANGE("STATS_422", "유효하지 않은 기간입니다.", HttpStatus.UNPROCESSABLE_ENTITY);

    private final String code;
    private final String message;
    private final HttpStatus status;
}
