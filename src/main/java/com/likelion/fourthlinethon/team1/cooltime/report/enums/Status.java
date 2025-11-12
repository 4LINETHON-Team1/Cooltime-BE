package com.likelion.fourthlinethon.team1.cooltime.report.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Status {
    /** 유효한 요청 (가용 기간 내) */
    OK("요청 기간이 가용 범위 내에 있습니다."),

    /** 완전히 무효 (가입 전 or 미래만 포함) */
    OUT_OF_RANGE("요청 기간이 가용 범위를 벗어났습니다.");

    private final String description;
}