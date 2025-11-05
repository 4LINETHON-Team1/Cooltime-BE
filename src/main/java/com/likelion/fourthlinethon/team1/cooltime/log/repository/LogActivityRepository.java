package com.likelion.fourthlinethon.team1.cooltime.log.repository;

import com.likelion.fourthlinethon.team1.cooltime.log.entity.LogActivity;
import com.likelion.fourthlinethon.team1.cooltime.stats.projection.ActivityStatsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LogActivityRepository extends JpaRepository<LogActivity, Long> {

    @Query(value = """
      SELECT at.id AS activityId,
             at.name AS activityName,
             COUNT(*) AS cnt
      FROM logs_activities la
      JOIN daily_logs dl ON dl.id = la.log_id
      JOIN activities_tags at ON at.id = la.activity_id
      WHERE dl.user_id = :userId
        AND dl.is_postponed = TRUE
      GROUP BY at.id, at.name
      ORDER BY cnt DESC, at.name ASC, at.id ASC
      LIMIT 1
      """, nativeQuery = true)
    Optional<ActivityStatsProjection> findTopPostponedActivityByUserId(@Param("userId") Long userId);

    @Query(value = """
        SELECT at.id AS activityId,
               at.name AS activityName,
               COUNT(*) AS cnt
        FROM logs_activities la
        JOIN daily_logs dl ON dl.id = la.log_id
        JOIN activities_tags at ON at.id = la.activity_id
        WHERE dl.user_id = :userId
          AND dl.is_postponed = TRUE
        GROUP BY at.id, at.name
        ORDER BY cnt DESC, at.name ASC, at.id ASC
        """, nativeQuery = true)
    List<ActivityStatsProjection> findAllPostponedActivityStatsByUserId(@Param("userId") Long userId);
}
