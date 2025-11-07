package com.likelion.fourthlinethon.team1.cooltime.test.service;

import com.likelion.fourthlinethon.team1.cooltime.user.entity.MyType;
import com.likelion.fourthlinethon.team1.cooltime.user.entity.User;
import com.likelion.fourthlinethon.team1.cooltime.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.likelion.fourthlinethon.team1.cooltime.global.exception.CustomException;
import com.likelion.fourthlinethon.team1.cooltime.auth.exception.AuthErrorCode;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TestService {

    private final UserRepository userRepository;

    public MyType calculateAndSaveResult(String username, List<Integer> answers) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(AuthErrorCode.USER_NOT_FOUND));
        if (answers == null || answers.size() != 7) {
            throw new CustomException(AuthErrorCode.INVALID_REQUEST);
        }

        int p = 0, m = 0, s = 0;

        // Q2, Q5 가중치 2점
        for (int i = 0; i < answers.size(); i++) {
            int answer = answers.get(i);
            int weight = (i == 1 || i == 4) ? 2 : 1; // Q2(index 1), Q5(index 4)만 2점

            switch (answer) {
                case 1 -> p += weight;
                case 2 -> m += weight;
                case 3 -> s += weight;
            }
        }

        // 유형 판정
        MyType result = decideType(p, m, s, answers);

        // User 업데이트
        user.updateMyType(result);
        userRepository.save(user);

        return result;
    }

    private MyType decideType(int p, int m, int s, List<Integer> answers) {
        int max = Math.max(p, Math.max(m, s));
        List<MyType> top = new ArrayList<>();

        if (p == max) top.add(MyType.PERFECTION);
        if (m == max) top.add(MyType.MOTIVATION);
        if (s == max) top.add(MyType.STRESS);

        if (top.size() == 1) return top.get(0);

        // 동점자 우선순위: Q2 > Q5 > Q4
        int[] priority = {2, 5, 4};
        for (int qNum : priority) {
            int idx = qNum - 1;
            int ans = answers.get(idx);
            if (ans == 1 && top.contains(MyType.PERFECTION)) return MyType.PERFECTION;
            if (ans == 2 && top.contains(MyType.MOTIVATION)) return MyType.MOTIVATION;
            if (ans == 3 && top.contains(MyType.STRESS)) return MyType.STRESS;
        }

        // 여전히 동점일 경우: P > M > S 순서
        if (top.contains(MyType.PERFECTION)) return MyType.PERFECTION;
        if (top.contains(MyType.MOTIVATION)) return MyType.MOTIVATION;
        return MyType.STRESS;
    }
}
