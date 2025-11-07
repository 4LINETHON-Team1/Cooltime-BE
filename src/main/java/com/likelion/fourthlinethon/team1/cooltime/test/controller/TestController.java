package com.likelion.fourthlinethon.team1.cooltime.test.controller;

import com.likelion.fourthlinethon.team1.cooltime.badge.entity.UserStreak;
import com.likelion.fourthlinethon.team1.cooltime.badge.repository.UserStreakRepository;
import com.likelion.fourthlinethon.team1.cooltime.badge.service.UserStreakService;
import com.likelion.fourthlinethon.team1.cooltime.global.response.BaseResponse;
import com.likelion.fourthlinethon.team1.cooltime.global.security.CustomUserDetails;
import com.likelion.fourthlinethon.team1.cooltime.user.entity.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "Test", description = "테스트 관련 API")
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {
    // 테스트용
    private final UserStreakService userStreakService;
    private final UserStreakRepository userStreakRepository;

    @PostMapping("/record-trigger")
    public ResponseEntity<BaseResponse<String>> awardTestBadge(
            @RequestParam int days,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        User user = userDetails.getUser();
        // 유저 streak 조회 또는 신규 생성
        UserStreak streak  = userStreakRepository.findByUserId(user.getId())
                .orElseGet(() -> userStreakRepository.save(UserStreak.newOf(user)));
        if(streak.getLastRecordDate()==null){
            streak.updateLastRecordDate(java.time.LocalDate.now());
        }
        streak.updateLastRecordDate(streak.getLastRecordDate().minusDays(days));
        userStreakRepository.save(streak);
        userStreakService.updateStreakOnRecord(user.getId());

        return ResponseEntity.ok(
                BaseResponse.success("테스트용 연속 기록 갱신 성공", "currentStreak: " + streak.getCurrentStreak()+ "longestStreak: " + streak.getLongestStreak())
        );
    }
}
