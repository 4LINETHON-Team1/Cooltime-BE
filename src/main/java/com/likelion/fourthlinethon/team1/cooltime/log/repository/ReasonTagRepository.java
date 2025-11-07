package com.likelion.fourthlinethon.team1.cooltime.log.repository;

import com.likelion.fourthlinethon.team1.cooltime.log.entity.ReasonTag;
import com.likelion.fourthlinethon.team1.cooltime.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReasonTagRepository extends JpaRepository<ReasonTag, Long> {
    boolean existsByUserAndName(User user, String name);
    Optional<ReasonTag> findByIdAndUser(Long id, User user);
}
