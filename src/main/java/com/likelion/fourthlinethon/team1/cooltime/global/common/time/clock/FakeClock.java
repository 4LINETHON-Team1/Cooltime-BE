package com.likelion.fourthlinethon.team1.cooltime.global.common.time.clock;


import java.time.*;

/**
 * 테스트에서 시간을 고정하거나 조작하기 위한 대체 Clock.
 */
public class FakeClock extends AppClock {

    private final LocalDateTime fixedDateTime;
    private final ZoneId zone;

    public FakeClock(LocalDateTime fixedDateTime) {
        this.fixedDateTime = fixedDateTime;
        this.zone = ZoneId.of("Asia/Seoul");
    }

    @Override
    public LocalDate today() {
        return fixedDateTime.toLocalDate();
    }

    @Override
    public LocalDateTime now() {
        return fixedDateTime;
    }

    @Override
    public Instant instant() {
        return fixedDateTime.atZone(zone).toInstant();
    }

    @Override
    public ZoneId zone() {
        return zone;
    }
}