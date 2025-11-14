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
    name = "reason_tags",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_user_reason_name", columnNames = {"user_id", "name"})
    },
    indexes = {
        @Index(name = "idx_user_default", columnList = "user_id, isDefault")
    }
)
public class ReasonTag extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

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

    public void setIsDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }
}
