package com.likelion.fourthlinethon.team1.cooltime.test.repository;

import com.likelion.fourthlinethon.team1.cooltime.test.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findAllByOrderByOrderNumAsc();
}
