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
        Optional<ActivityTag> existingTag = activityTagRepository
                .findAll()
                .stream()
                .filter(t -> t.getUser().getId().equals(user.getId()) && t.getName().equals(request.getName()))
                .findFirst();

        if (existingTag.isPresent()) {
            ActivityTag tag = existingTag.get();
            if (tag.getIsActive()) {
                throw new CustomException(DailyLogErrorCode.ACTIVITY_OR_REASON_ALREADY_EXISTS);
            }
            tag.setIsActive(true);
            activityTagRepository.save(tag);

            return getActiveTags(user);
        }

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
     * 활동 삭제
     */
    @Transactional
    public List<ActivityTagResponse> deleteActivityByName(User user, String name) {
        ActivityTag activity = activityTagRepository.findByUserAndName(user, name)
                .orElseThrow(() -> new CustomException(DailyLogErrorCode.ACTIVITY_NOT_FOUND));

        if (activity.getIsDefault()) {
            throw new CustomException(DailyLogErrorCode.CANNOT_DELETE_DEFAULT_TAG);
        }
        if (!activity.getIsActive()) {
            throw new CustomException(DailyLogErrorCode.ALREADY_INACTIVE_TAG);
        }

        activity.setIsActive(false);
        activityTagRepository.save(activity);

        return getActiveTags(user);
    }

    /**
     * 유저의 활성화된 전체 활동 목록 조회
     */
    private List<ActivityTagResponse> getActiveTags(User user) {
        return activityTagRepository.findAll().stream()
                .filter(t -> t.getUser().getId().equals(user.getId()) && t.getIsActive())
                .map(ActivityTagResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
