package com.likelion.fourthlinethon.team1.cooltime.log.service;

import com.likelion.fourthlinethon.team1.cooltime.global.exception.CustomException;
import com.likelion.fourthlinethon.team1.cooltime.log.dto.ReasonTagRequest;
import com.likelion.fourthlinethon.team1.cooltime.log.dto.ReasonTagResponse;
import com.likelion.fourthlinethon.team1.cooltime.log.entity.ReasonTag;
import com.likelion.fourthlinethon.team1.cooltime.log.exception.DailyLogErrorCode;
import com.likelion.fourthlinethon.team1.cooltime.log.repository.ReasonTagRepository;
import com.likelion.fourthlinethon.team1.cooltime.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReasonTagService {

    private final ReasonTagRepository reasonTagRepository;

    /**
     * 이유 추가 (중복 불가, 추가 후 전체 활성 이유 반환)
     */
    @Transactional
    public List<ReasonTagResponse> createReason(User user, ReasonTagRequest request) {
        // 1️⃣ 기존 동일 이름의 이유 존재 여부 확인
        Optional<ReasonTag> existingTag = reasonTagRepository
                .findAll()
                .stream()
                .filter(r -> r.getUser().getId().equals(user.getId()) && r.getName().equals(request.getName()))
                .findFirst();

        if (existingTag.isPresent()) {
            ReasonTag tag = existingTag.get();

            // 2️⃣ 이미 활성 상태면 예외 발생
            if (tag.getIsActive()) {
                throw new CustomException(DailyLogErrorCode.ACTIVITY_OR_REASON_ALREADY_EXISTS);
            }

            // 3️⃣ 비활성화 상태라면 재활성화
            tag.setIsActive(true);
            reasonTagRepository.save(tag);

            return getActiveReasons(user);
        }

        // 4️⃣ 존재하지 않으면 새로 생성
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
     * ✅ 유저의 활성화된 전체 이유 목록 조회
     */
    private List<ReasonTagResponse> getActiveReasons(User user) {
        return reasonTagRepository.findAll().stream()
                .filter(r -> r.getUser().getId().equals(user.getId()) && r.getIsActive())
                .map(ReasonTagResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
