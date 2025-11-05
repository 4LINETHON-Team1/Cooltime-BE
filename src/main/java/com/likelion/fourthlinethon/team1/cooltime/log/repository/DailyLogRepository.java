package com.likelion.fourthlinethon.team1.cooltime.log.repository;

import com.likelion.fourthlinethon.team1.cooltime.log.entity.DailyLog;
import com.likelion.fourthlinethon.team1.cooltime.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyLogRepository extends JpaRepository<DailyLog, Long> {

    // 특정 유저의 총 기록일수(레코드 수)를 반환
    long countByUser(User user);

}
