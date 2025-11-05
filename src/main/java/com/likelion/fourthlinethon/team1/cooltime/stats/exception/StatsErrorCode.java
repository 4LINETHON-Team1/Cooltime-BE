package com.likelion.fourthlinethon.team1.cooltime.stats.exception;

import com.likelion.fourthlinethon.team1.cooltime.global.exception.model.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum StatsErrorCode implements BaseErrorCode {
    STATS_NOT_FOUND("STATS_404", "통계 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    private final String code;
    private final String message;
    private final HttpStatus status;
}