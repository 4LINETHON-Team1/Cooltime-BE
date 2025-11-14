package com.likelion.fourthlinethon.team1.cooltime.report.repository;

import com.likelion.fourthlinethon.team1.cooltime.global.common.time.period.WeekPeriod;
import com.likelion.fourthlinethon.team1.cooltime.report.entity.AiWeeklyReport;
import com.likelion.fourthlinethon.team1.cooltime.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface AiWeeklyReportRepository extends JpaRepository<AiWeeklyReport, Long> {
    Optional<AiWeeklyReport> findByUserAndWeekStart(User user, LocalDate startDate);

    boolean existsByUser(User user);
}
