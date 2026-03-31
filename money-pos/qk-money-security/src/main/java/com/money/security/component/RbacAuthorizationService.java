package com.money.security.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * RBAC 授权服务
 * <p>提供基于角色和权限的访问控制，用于 {@code @PreAuthorize} 注解。</p>
 * <p>超级管理员（{@code ROLE_SUPER_ADMIN}）自动放行所有权限。</p>
 *
 * @author money
 * @since 1.0.0
 */
@Slf4j
@Component("rbac")
public class RbacAuthorizationService {

    /**
     * 检查当前用户是否拥有指定角色（满足任一即可）
     *
     * @param roles 角色列表
     * @return {@code true} 表示拥有指定角色
     */
    public boolean hasRole(String... roles) {
        Set<String> userAuthorities = this.getUserAuthorities();
        // 超级管理员放行所有权限
        if (userAuthorities.contains("ROLE_SUPER_ADMIN")) {
            return true;
        }
        // 判断当前用户的所有权限是否包含接口上定义的权限
        return Arrays.stream(roles)
                .map(role -> "ROLE_" + role)
                .anyMatch(userAuthorities::contains);
    }

    /**
     * 检查当前用户是否拥有指定权限（满足任一即可）
     *
     * @param permissions 权限列表
     * @return {@code true} 表示拥有指定权限
     */
    public boolean hasPermission(String... permissions) {
        Set<String> userAuthorities = this.getUserAuthorities();
        // 超级管理员放行所有权限
        if (userAuthorities.contains("ROLE_SUPER_ADMIN")) {
            return true;
        }
        // 判断当前用户的所有权限是否包含接口上定义的权限
        return Arrays.stream(permissions).anyMatch(userAuthorities::contains);
    }

    /**
     * 获取当前用户的所有权限标识
     *
     * @return 权限标识集合
     */
    private Set<String> getUserAuthorities() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityUserDetail userDetail = (SecurityUserDetail) authentication.getPrincipal();
        return userDetail.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
    }

}
