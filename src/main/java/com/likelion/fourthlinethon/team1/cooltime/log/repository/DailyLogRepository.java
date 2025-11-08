package com.likelion.fourthlinethon.team1.cooltime.log.repository;

import com.likelion.fourthlinethon.team1.cooltime.log.entity.DailyLog;
import com.likelion.fourthlinethon.team1.cooltime.stats.projection.PostponeRatioCounts;
import com.likelion.fourthlinethon.team1.cooltime.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyLogRepository extends JpaRepository<DailyLog, Long> {

    // ✅ 특정 날짜의 기록 존재 여부
    Optional<DailyLog> findByUserAndDate(User user, LocalDate date);
    boolean existsByUserAndDate(User user, LocalDate date);

    // ✅ 기간별 기록 조회
    List<DailyLog> findAllByUserAndDateBetween(User user, LocalDate startDate, LocalDate endDate);

    // ✅ 특정 유저의 총 기록 수 반환
    long countByUser(User user);

    // ✅ 통계용: 특정 기간의 미룸/성공 기록 수 집계
    @Query("""
            SELECT
                count(dl) as total,
                coalesce(sum(case when dl.isPostponed = true  then 1 else 0 end), 0) as postponed,
                coalesce(sum(case when dl.isPostponed = false then 1 else 0 end), 0) as done
            FROM DailyLog dl
            WHERE dl.user.id = :userId
                and dl.date between :startDate and :endDate
    """)
    PostponeRatioCounts getPostponeRatioCounts(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    // ✅ [추가] 특정 연도와 월 기준으로 일별 로그 조회 (캘린더용)
    @Query("""
        select dl
        from DailyLog dl
        where dl.user = :user
          and YEAR(dl.date) = :year
          and MONTH(dl.date) = :month
        order by dl.date asc
    """)
    List<DailyLog> findByUserAndMonth(
            @Param("user") User user,
            @Param("year") int year,
            @Param("month") int month
    );
}
