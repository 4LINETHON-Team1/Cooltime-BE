package com.likelion.fourthlinethon.team1.cooltime.badge.service;

import com.likelion.fourthlinethon.team1.cooltime.badge.entity.Badge;
import com.likelion.fourthlinethon.team1.cooltime.badge.enums.BadgeCode;
import com.likelion.fourthlinethon.team1.cooltime.badge.exception.BadgeErrorCode;
import com.likelion.fourthlinethon.team1.cooltime.badge.repository.BadgeRepository;
import com.likelion.fourthlinethon.team1.cooltime.global.exception.CustomException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BadgeService {
    private final BadgeRepository badgeRepository;

    public List<Badge> findAll() {
        return badgeRepository.findAll();
    }

    public Badge findByCode(String code) {
        return badgeRepository.findByCode(code)
                .orElseThrow(() -> new CustomException(BadgeErrorCode.BADGE_NOT_FOUND));

    }

}
