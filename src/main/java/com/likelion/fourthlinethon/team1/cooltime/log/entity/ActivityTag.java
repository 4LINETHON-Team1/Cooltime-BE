package com.likelion.fourthlinethon.team1.cooltime.log.entity;

import com.likelion.fourthlinethon.team1.cooltime.global.common.BaseTimeEntity;
import com.likelion.fourthlinethon.team1.cooltime.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(
    name = "activity_tags",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_user_activity_name", columnNames = {"user_id", "name"})
    }
)
public class ActivityTag extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 16자 제한
    @Column(name = "name", nullable = false, length = 48)
    private String name;

    @Column(name = "isActive", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "isDefault", nullable = false)
    @Builder.Default
    private Boolean isDefault = false;

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

}
