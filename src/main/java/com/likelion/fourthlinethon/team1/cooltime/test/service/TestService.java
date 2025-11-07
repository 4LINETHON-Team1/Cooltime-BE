package com.likelion.fourthlinethon.team1.cooltime.test.service;

import com.likelion.fourthlinethon.team1.cooltime.auth.exception.AuthErrorCode;
import com.likelion.fourthlinethon.team1.cooltime.global.exception.CustomException;
import com.likelion.fourthlinethon.team1.cooltime.log.entity.ReasonTag;
import com.likelion.fourthlinethon.team1.cooltime.log.repository.ReasonTagRepository;
import com.likelion.fourthlinethon.team1.cooltime.user.entity.MyType;
import com.likelion.fourthlinethon.team1.cooltime.user.entity.User;
import com.likelion.fourthlinethon.team1.cooltime.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TestService {

    private final UserRepository userRepository;
    private final ReasonTagRepository reasonTagRepository;

    /**
     * 🔹 미룸유형 테스트 결과 계산 및 저장
     */
    @Transactional
    public MyType calculateAndSaveResult(String username, List<Integer> answers) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(AuthErrorCode.USER_NOT_FOUND));

        if (answers == null || answers.size() != 7) {
            throw new CustomException(AuthErrorCode.INVALID_REQUEST);
        }

        int p = 0, m = 0, s = 0;

        // Q2, Q5는 가중치 2점
        for (int i = 0; i < answers.size(); i++) {
            int answer = answers.get(i);
            int weight = (i == 1 || i == 4) ? 2 : 1;

            switch (answer) {
                case 1 -> p += weight;
                case 2 -> m += weight;
                case 3 -> s += weight;
            }
        }

        MyType newType = decideType(p, m, s, answers);
        MyType oldType = user.getMytype();

        // 유형이 바뀐 경우에만 기본 이유 갱신
        if (oldType == null || !oldType.equals(newType)) {
            updateDefaultReasons(user, newType);
        }

        // user 엔티티의 내장 메서드로 mytype/hasTested/lastTestedAt 동시 갱신
        user.updateMyType(newType);
        userRepository.save(user);

        return newType;
    }

    /**
     * 🔹 유형별 기본 이유 자동 갱신
     * - 기존 default는 해제(isDefault=false, isActive=true 유지)
     * - 새 유형 3개 이유는 default로 설정
     */
    private void updateDefaultReasons(User user, MyType newType) {
        Map<MyType, List<String>> reasonMap = Map.of(
                MyType.PERFECTION, List.of("완벽하게 하려다", "준비만 하다가", "결과가 두려워"),
                MyType.MOTIVATION, List.of("의욕이 없어서", "자신이 없어서", "귀찮아서"),
                MyType.STRESS, List.of("머리가 복잡해서", "집중이 안 돼서", "너무 피곤해서")
        );

        // 기존 default 이유 해제 (isDefault=false, isActive는 true 유지)
        reasonTagRepository.findByUserAndIsDefaultTrue(user)
                .forEach(reason -> {
                    reason.setIsDefault(false);
                    reasonTagRepository.save(reason);
                });

        // 새로운 유형의 기본 이유 추가 또는 갱신
        for (String reasonName : reasonMap.get(newType)) {
            ReasonTag existing = reasonTagRepository.findByUserAndName(user, reasonName).orElse(null);
            if (existing != null) {
                existing.setIsActive(true);
                existing.setIsDefault(true);
                reasonTagRepository.save(existing);
            } else {
                reasonTagRepository.save(ReasonTag.builder()
                        .user(user)
                        .name(reasonName)
                        .isActive(true)
                        .isDefault(true)
                        .build());
            }
        }
    }

    /**
     * 🔹 유형 판정 로직
     */
    private MyType decideType(int p, int m, int s, List<Integer> answers) {
        int max = Math.max(p, Math.max(m, s));
        List<MyType> top = new ArrayList<>();

        if (p == max) top.add(MyType.PERFECTION);
        if (m == max) top.add(MyType.MOTIVATION);
        if (s == max) top.add(MyType.STRESS);

        // 단일 최대값이면 바로 반환
        if (top.size() == 1) return top.get(0);

        // 동점자 우선순위: Q2 > Q5 > Q4
        int[] priority = {2, 5, 4};
        for (int qNum : priority) {
            int ans = answers.get(qNum - 1);
            if (ans == 1 && top.contains(MyType.PERFECTION)) return MyType.PERFECTION;
            if (ans == 2 && top.contains(MyType.MOTIVATION)) return MyType.MOTIVATION;
            if (ans == 3 && top.contains(MyType.STRESS)) return MyType.STRESS;
        }

        // 여전히 동점 → P > M > S 우선순위
        if (top.contains(MyType.PERFECTION)) return MyType.PERFECTION;
        if (top.contains(MyType.MOTIVATION)) return MyType.MOTIVATION;
        return MyType.STRESS;
    }
}
