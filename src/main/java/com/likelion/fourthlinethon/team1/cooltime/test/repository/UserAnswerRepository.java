package com.likelion.fourthlinethon.team1.cooltime.test.repository;

import com.likelion.fourthlinethon.team1.cooltime.test.entity.UserAnswer;
import com.likelion.fourthlinethon.team1.cooltime.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserAnswerRepository extends JpaRepository<UserAnswer, Long> {
    List<UserAnswer> findByUser(User user);
}
