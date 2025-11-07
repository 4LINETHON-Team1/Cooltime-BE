package com.likelion.fourthlinethon.team1.cooltime.badge.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
public enum BadgeCode {
    STREAK_10("STREAK_10", 10),
    STREAK_30("STREAK_30", 30),
    STREAK_50("STREAK_50", 50),
    STREAK_100("STREAK_100", 100),
    STREAK_365("STREAK_365", 365),
    STREAK_500("STREAK_500", 500);

    private final String code;
    private final int days;

    public static Optional<BadgeCode> bestByStreak(int streak) {
        return Arrays.stream(values())
                .filter(b -> streak >= b.days)
                .max(Comparator.comparingInt(BadgeCode::getDays));
    }

    public static Optional<BadgeCode> nextAfter(BadgeCode current) {
        return Arrays.stream(values())
                .filter(b -> b.days > (current == null ? Integer.MIN_VALUE : current.days))
                .min(Comparator.comparingInt(BadgeCode::getDays));
    }

    public static BadgeCode first() {
        return Arrays.stream(values())
                .min(Comparator.comparingInt(BadgeCode::getDays))
                .orElseThrow();
    }

    public static BadgeCode last() {
        return Arrays.stream(values())
                .max(Comparator.comparingInt(BadgeCode::getDays))
                .orElseThrow();
    }
}