package com.likelion.fourthlinethon.team1.cooltime.badge.repository;

import com.likelion.fourthlinethon.team1.cooltime.badge.entity.Badge;
import com.likelion.fourthlinethon.team1.cooltime.badge.enums.BadgeCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BadgeRepository extends JpaRepository<Badge, Long> {
    Optional<Badge> findByCode(String badgeCode);
}
