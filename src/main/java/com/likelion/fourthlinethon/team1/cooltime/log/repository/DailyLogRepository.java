package com.likelion.fourthlinethon.team1.cooltime.log.repository;

import com.likelion.fourthlinethon.team1.cooltime.log.entity.DailyLog;
import com.likelion.fourthlinethon.team1.cooltime.stats.projection.PostponeRatioCounts;
import com.likelion.fourthlinethon.team1.cooltime.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface DailyLogRepository extends JpaRepository<DailyLog, Long> {
    Optional<DailyLog> findByUserAndDate(User user, LocalDate date);
    boolean existsByUserAndDate(User user, LocalDate date);

    // 특정 유저의 총 기록일수(레코드 수)를 반환
    long countByUser(User user);

    @Query("""
        select 
            count(dl) as total,
            sum(case when dl.isPostponed = true  then 1 else 0 end) as postponed,
            sum(case when dl.isPostponed = false then 1 else 0 end) as done
        from DailyLog dl
        where dl.user.id = :userId
          and dl.date between :startDate and :endDate
    """)
    PostponeRatioCounts getPostponeRatioCounts(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
