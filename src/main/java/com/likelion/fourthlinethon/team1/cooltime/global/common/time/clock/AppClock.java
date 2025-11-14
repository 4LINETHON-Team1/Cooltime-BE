package com.likelion.fourthlinethon.team1.cooltime.global.common.time.clock;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class AppClock {

    /** 애플리케이션 기본 타임존 (운영환경과 동일하게 고정) */
    private static final ZoneId ZONE_ID = ZoneId.of("Asia/Seoul");

    /** 현재 날짜 (KST 기준) */
    public LocalDate today() {
        return LocalDate.now(ZONE_ID);
    }

    /** 현재 시각 (KST 기준) */
    public LocalDateTime now() {
        return LocalDateTime.now(ZONE_ID);
    }

    /** Instant (UTC 기준) */
    public Instant instant() {
        return Instant.now();
    }

    /** Zone 정보 */
    public ZoneId zone() {
        return ZONE_ID;
    }
}