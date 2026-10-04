package com.money.security;

import com.money.security.component.SecurityUserDetail;
import com.money.security.model.RbacUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 安全守卫工具类
 * <p>提供获取当前登录用户信息的便捷方法。</p>
 *
 * @author money
 * @since 1.0.0
 */
@Slf4j
public class SecurityGuard {

    /**
     * 检查当前请求是否处于已登录状态
     *
     * @return {@code true} 表示用户已登录
     */
    public static boolean isLoggedState() {
        try {
            return SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof SecurityUserDetail;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取当前登录的用户信息
     *
     * @return 当前登录的 RBAC 用户信息
     * @throws RuntimeException 用户未登录时抛出
     */
    public static RbacUser getRbacUser() {
        if (!isLoggedState()) {
            log.warn("检查是否是经过认证后的接口里的操作，放行的接口无法获取登录信息");
            throw new RuntimeException("无法获取登录信息");
        }
        SecurityUserDetail userDetail = (SecurityUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetail.getUser();
    }
}
