package com.likelion.fourthlinethon.team1.cooltime.global.security;

import com.likelion.fourthlinethon.team1.cooltime.user.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 현재 로그인한 사용자의 정보를 SecurityContext에서 가져오는 유틸 클래스
 * - JWT 인증 필터(JwtAuthenticationFilter)에서 주입된 CustomUserDetails를 사용함
 */
public class SecurityUtil {

    private SecurityUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 현재 인증된 사용자(User) 엔티티 반환
     * @return 현재 로그인한 사용자 (없을 경우 null)
     */
    public static User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            return null;
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getUser();
    }

    /**
     * 현재 인증된 사용자의 username 반환
     * @return username (없을 경우 null)
     */
    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            return null;
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }
}
