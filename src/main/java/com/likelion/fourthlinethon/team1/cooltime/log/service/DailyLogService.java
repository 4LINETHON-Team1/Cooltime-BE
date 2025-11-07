package com.likelion.fourthlinethon.team1.cooltime.log.service;

import com.likelion.fourthlinethon.team1.cooltime.global.exception.CustomException;
import com.likelion.fourthlinethon.team1.cooltime.log.dto.DailyLogRequest;
import com.likelion.fourthlinethon.team1.cooltime.log.dto.DailyLogResponse;
import com.likelion.fourthlinethon.team1.cooltime.log.entity.ActivityTag;
import com.likelion.fourthlinethon.team1.cooltime.log.entity.DailyLog;
import com.likelion.fourthlinethon.team1.cooltime.log.entity.LogActivity;
import com.likelion.fourthlinethon.team1.cooltime.log.entity.LogReason;
import com.likelion.fourthlinethon.team1.cooltime.log.entity.ReasonTag;
import com.likelion.fourthlinethon.team1.cooltime.log.exception.DailyLogErrorCode;
import com.likelion.fourthlinethon.team1.cooltime.log.repository.ActivityTagRepository;
import com.likelion.fourthlinethon.team1.cooltime.log.repository.DailyLogRepository;
import com.likelion.fourthlinethon.team1.cooltime.log.repository.LogActivityRepository;
import com.likelion.fourthlinethon.team1.cooltime.log.repository.LogReasonRepository;
import com.likelion.fourthlinethon.team1.cooltime.log.repository.ReasonTagRepository;
import com.likelion.fourthlinethon.team1.cooltime.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DailyLogService {

    private final DailyLogRepository dailyLogRepository;
    private final ActivityTagRepository activityTagRepository;
    private final ReasonTagRepository reasonTagRepository;
    private final LogActivityRepository logActivityRepository;
    private final LogReasonRepository logReasonRepository;

    /**
     * ✏️ 미룸 기록 작성 (POST /api/logs)
     */
    @Transactional
    public DailyLogResponse createDailyLog(User user, DailyLogRequest request) {
        LocalDate today = LocalDate.now();

        if (dailyLogRepository.existsByUserAndDate(user, today)) {
            throw new CustomException(DailyLogErrorCode.INVALID_DATE);
        }

        DailyLog log = DailyLog.builder()
                .user(user)
                .date(today)
                .isPostponed(request.getIsPostponed())
                .type(request.getMyType())
                .build();

        DailyLog savedLog = dailyLogRepository.save(log);

        // 활동 매핑
        for (Long activityId : request.getActivityIds()) {
            ActivityTag activity = activityTagRepository.findById(activityId)
                    .orElseThrow(() -> new CustomException(DailyLogErrorCode.ACTIVITY_NOT_FOUND));

            if (!activity.getUser().getId().equals(user.getId())) {
                throw new CustomException(DailyLogErrorCode.ACTIVITY_NOT_FOUND);
            }

            logActivityRepository.save(
                    LogActivity.builder()
                            .log(savedLog)
                            .activity(activity)
                            .build()
            );
        }

        // 이유 매핑
        for (Long reasonId : request.getReasonIds()) {
            ReasonTag reason = reasonTagRepository.findById(reasonId)
                    .orElseThrow(() -> new CustomException(DailyLogErrorCode.REASON_NOT_FOUND));

            if (!reason.getUser().getId().equals(user.getId())) {
                throw new CustomException(DailyLogErrorCode.REASON_NOT_FOUND);
            }

            logReasonRepository.save(
                    LogReason.builder()
                            .log(savedLog)
                            .reason(reason)
                            .build()
            );
        }

        return DailyLogResponse.fromEntity(savedLog);
    }

    /**
     * 🧩 미룸 기록 수정 (PUT /api/logs)
     */
    @Transactional
    public DailyLogResponse updateDailyLog(User user, DailyLogRequest request) {
        LocalDate today = LocalDate.now();

        DailyLog existingLog = dailyLogRepository.findByUserAndDate(user, today)
                .orElseThrow(() -> new CustomException(DailyLogErrorCode.LOG_NOT_FOUND));

        // 기존 연결 삭제
        logActivityRepository.deleteAllByLog(existingLog);
        logReasonRepository.deleteAllByLog(existingLog);

        // 값 수정
        existingLog.update(request.getIsPostponed(), request.getMyType());

        // 활동 다시 매핑
        for (Long activityId : request.getActivityIds()) {
            ActivityTag activity = activityTagRepository.findById(activityId)
                    .orElseThrow(() -> new CustomException(DailyLogErrorCode.ACTIVITY_NOT_FOUND));

            if (!activity.getUser().getId().equals(user.getId())) {
                throw new CustomException(DailyLogErrorCode.ACTIVITY_NOT_FOUND);
            }

            logActivityRepository.save(
                    LogActivity.builder()
                            .log(existingLog)
                            .activity(activity)
                            .build()
            );
        }

        // 이유 다시 매핑
        for (Long reasonId : request.getReasonIds()) {
            ReasonTag reason = reasonTagRepository.findById(reasonId)
                    .orElseThrow(() -> new CustomException(DailyLogErrorCode.REASON_NOT_FOUND));

            if (!reason.getUser().getId().equals(user.getId())) {
                throw new CustomException(DailyLogErrorCode.REASON_NOT_FOUND);
            }

            logReasonRepository.save(
                    LogReason.builder()
                            .log(existingLog)
                            .reason(reason)
                            .build()
            );
        }

        dailyLogRepository.save(existingLog);
        return DailyLogResponse.fromEntity(existingLog);
    }
}
