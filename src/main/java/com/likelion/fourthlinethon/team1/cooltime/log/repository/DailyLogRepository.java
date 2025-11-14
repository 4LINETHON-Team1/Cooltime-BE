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

    Optional<DailyLog> findByUserAndDate(User user, LocalDate date);
    boolean existsByUserAndDate(User user, LocalDate date);

    List<DailyLog> findAllByUserAndDateBetween(User user, LocalDate startDate, LocalDate endDate);

    long countByUser(User user);

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
