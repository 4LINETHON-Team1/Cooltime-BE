package com.likelion.fourthlinethon.team1.cooltime.log.service;

import com.likelion.fourthlinethon.team1.cooltime.log.dto.ActivityTagResponse;
import com.likelion.fourthlinethon.team1.cooltime.log.dto.ReasonTagResponse;
import com.likelion.fourthlinethon.team1.cooltime.log.dto.TagResponse;
import com.likelion.fourthlinethon.team1.cooltime.log.repository.ActivityTagRepository;
import com.likelion.fourthlinethon.team1.cooltime.log.repository.ReasonTagRepository;
import com.likelion.fourthlinethon.team1.cooltime.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {

    private final ActivityTagRepository activityTagRepository;
    private final ReasonTagRepository reasonTagRepository;

    @Transactional(readOnly = true)
    public TagResponse getActiveTags(User user) {
        List<ActivityTagResponse> activities = activityTagRepository
                .findByUserAndIsActiveOrderByIsDefaultDescUpdatedAtAsc(user, true)
                .stream()
                .map(ActivityTagResponse::fromEntity)
                .toList();

        List<ReasonTagResponse> reasons = reasonTagRepository
                .findByUserAndIsActiveOrderByIsDefaultDescUpdatedAtAsc(user, true)
                .stream()
                .map(ReasonTagResponse::fromEntity)
                .toList();

        return new TagResponse(activities, reasons);
    }
}
