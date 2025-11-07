package com.likelion.fourthlinethon.team1.cooltime.log.repository;

import com.likelion.fourthlinethon.team1.cooltime.log.entity.ActivityTag;
import com.likelion.fourthlinethon.team1.cooltime.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ActivityTagRepository extends JpaRepository<ActivityTag, Long> {
    boolean existsByUserAndName(User user, String name);
    Optional<ActivityTag> findByIdAndUser(Long id, User user);
}
