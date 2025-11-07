package com.likelion.fourthlinethon.team1.cooltime.log.service;

import com.likelion.fourthlinethon.team1.cooltime.global.exception.CustomException;
import com.likelion.fourthlinethon.team1.cooltime.log.dto.ActivityTagRequest;
import com.likelion.fourthlinethon.team1.cooltime.log.dto.ActivityTagResponse;
import com.likelion.fourthlinethon.team1.cooltime.log.entity.ActivityTag;
import com.likelion.fourthlinethon.team1.cooltime.log.exception.DailyLogErrorCode;
import com.likelion.fourthlinethon.team1.cooltime.log.repository.ActivityTagRepository;
import com.likelion.fourthlinethon.team1.cooltime.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityTagService {

    private final ActivityTagRepository activityTagRepository;

    /**
     * 활동 추가 (중복 불가, 추가 후 전체 활성 활동 반환)
     */
    @Transactional
    public List<ActivityTagResponse> createActivity(User user, ActivityTagRequest request) {
        // 1️⃣ 기존 동일 이름의 활동 존재 여부 확인
        Optional<ActivityTag> existingTag = activityTagRepository
                .findAll()
                .stream()
                .filter(t -> t.getUser().getId().equals(user.getId()) && t.getName().equals(request.getName()))
                .findFirst();

        if (existingTag.isPresent()) {
            ActivityTag tag = existingTag.get();

            // 2️⃣ 이미 활성 상태면 예외 발생
            if (tag.getIsActive()) {
                throw new CustomException(DailyLogErrorCode.ACTIVITY_OR_REASON_ALREADY_EXISTS);
            }

            // 3️⃣ 비활성화 상태라면 재활성화
            tag.setIsActive(true);
            activityTagRepository.save(tag);

            return getActiveTags(user);
        }

        // 4️⃣ 존재하지 않으면 새로 생성
        ActivityTag tag = ActivityTag.builder()
                .user(user)
                .name(request.getName())
                .isDefault(false)
                .isActive(true)
                .build();

        activityTagRepository.save(tag);

        return getActiveTags(user);
    }

    /**
     * 활동 삭제 (default 불가, isActive=false 후 전체 활성 활동 반환)
     */
    @Transactional
    public List<ActivityTagResponse> deleteActivity(User user, Long activityId) {
        ActivityTag activity = activityTagRepository.findById(activityId)
                .orElseThrow(() -> new CustomException(DailyLogErrorCode.ACTIVITY_NOT_FOUND));

        if (!activity.getUser().getId().equals(user.getId())) {
            throw new CustomException(DailyLogErrorCode.ACTIVITY_NOT_FOUND);
        }

        if (activity.getIsDefault()) {
            throw new CustomException(DailyLogErrorCode.ACTIVITY_OR_REASON_ALREADY_EXISTS);
        }

        activity.setIsActive(false);
        activityTagRepository.save(activity);

        // ✅ 삭제 후에도 활성화된 전체 리스트 반환
        return getActiveTags(user);
    }

    /**
     * ✅ 유저의 활성화된 전체 활동 목록 조회
     */
    private List<ActivityTagResponse> getActiveTags(User user) {
        return activityTagRepository.findAll().stream()
                .filter(t -> t.getUser().getId().equals(user.getId()) && t.getIsActive())
                .map(ActivityTagResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
