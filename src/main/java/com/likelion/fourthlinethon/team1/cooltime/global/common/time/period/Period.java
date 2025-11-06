package com.likelion.fourthlinethon.team1.cooltime.global.common.time.period;

import java.time.LocalDate;
/** 정규 기간 VO 공통 인터페이스 (inclusive: [start, end]) */
public interface Period {
    /** 포함 시작일 */
    LocalDate getStart();

    /** 포함 종료일 */
    LocalDate getEnd();

    /** 동일 단위의 이전 기간 */
    Period prev();

    /** 동일 단위의 다음 기간 */
    Period next();

}
