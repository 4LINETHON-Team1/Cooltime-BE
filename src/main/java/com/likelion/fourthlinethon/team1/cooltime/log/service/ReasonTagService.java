package com.likelion.fourthlinethon.team1.cooltime.log.service;

import com.likelion.fourthlinethon.team1.cooltime.global.exception.CustomException;
import com.likelion.fourthlinethon.team1.cooltime.log.dto.request.ReasonTagRequest;
import com.likelion.fourthlinethon.team1.cooltime.log.dto.response.ReasonTagResponse;
import com.likelion.fourthlinethon.team1.cooltime.log.entity.ReasonTag;
import com.likelion.fourthlinethon.team1.cooltime.log.exception.DailyLogErrorCode;
import com.likelion.fourthlinethon.team1.cooltime.log.repository.ReasonTagRepository;
import com.likelion.fourthlinethon.team1.cooltime.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReasonTagService {

    private final ReasonTagRepository reasonTagRepository;

    /**
     * 이유 추가 (중복 불가, 추가 후 전체 활성 이유 반환)
     */
    @Transactional
    public List<ReasonTagResponse> createReason(User user, ReasonTagRequest request) {
        Optional<ReasonTag> existingTag = reasonTagRepository.findByUserAndName(user, request.getName());

        if (existingTag.isPresent()) {
            ReasonTag tag = existingTag.get();
            if (tag.getIsActive()) {
                throw new CustomException(DailyLogErrorCode.ACTIVITY_OR_REASON_ALREADY_EXISTS);
            }
            tag.setIsActive(true);
            reasonTagRepository.save(tag);

            return getActiveReasons(user);
        }

        ReasonTag tag = ReasonTag.builder()
                .user(user)
                .name(request.getName())
                .isDefault(false)
                .isActive(true)
                .build();

        reasonTagRepository.save(tag);

        return getActiveReasons(user);
    }

    /**
     * 이유 삭제 (default 불가, isActive=false 후 전체 활성 이유 반환)
     */
    @Transactional
    public List<ReasonTagResponse> deleteReasonByName(User user, String name) {
        ReasonTag reason = reasonTagRepository.findByUserAndName(user, name)
                .orElseThrow(() -> new CustomException(DailyLogErrorCode.REASON_NOT_FOUND));

        if (reason.getIsDefault()) {
            throw new CustomException(DailyLogErrorCode.CANNOT_DELETE_DEFAULT_TAG);
        }
        if (!reason.getIsActive()) {
            throw new CustomException(DailyLogErrorCode.ALREADY_INACTIVE_TAG);
        }

        reason.setIsActive(false);
        reasonTagRepository.save(reason);

        return getActiveReasons(user);
    }

    /**
     * 유저의 활성화된 전체 이유 목록 조회
     */
    private List<ReasonTagResponse> getActiveReasons(User user) {
        return reasonTagRepository.findByUserAndIsActiveOrderByIsDefaultDescUpdatedAtAsc(user, true)
                .stream()
                .map(ReasonTagResponse::fromEntity)
                .toList();
    }
}
