package com.likelion.fourthlinethon.team1.cooltime.log.service;

import com.likelion.fourthlinethon.team1.cooltime.log.entity.ActivityTag;
import com.likelion.fourthlinethon.team1.cooltime.log.repository.ActivityTagRepository;
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

}
