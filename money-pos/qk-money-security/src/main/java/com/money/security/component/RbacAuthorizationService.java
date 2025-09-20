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
 * RBAC 授权服务（SecurityExpressionRoot）
 *
 * @author : money
 * @since : 1.0.0
 */
@Slf4j
@Component("rbac")
public class RbacAuthorizationService {

    /**
     * 是否拥有角色
     *
     * @param roles 角色
     * @return boolean
     */
    public boolean hasRole(String... roles) {
        Set<String> userAuthorities = this.getUserAuthorities();
        // todo 超级管理员放行所有权限
        if (userAuthorities.contains("ROLE_SUPER_ADMIN")) {
            return true;
        }
        // 判断当前用户的所有权限是否包含接口上定义的权限
        return Arrays.stream(roles).map(role -> "ROLE_" + role).anyMatch(userAuthorities::contains);
    }

    /**
     * 是否拥有权限
     *
     * @param permissions 权限码
     * @return boolean
     */
    public boolean hasPermission(String... permissions) {
        Set<String> userAuthorities = this.getUserAuthorities();
        // todo 超级管理员放行所有权限
        if (userAuthorities.contains("ROLE_SUPER_ADMIN")) {
            return true;
        }
        // 判断当前用户的所有权限是否包含接口上定义的权限
        return Arrays.stream(permissions).anyMatch(userAuthorities::contains);
    }

    /**
     * 获取当前用户所有权限
     *
     * @return {@link Set }<{@link String }>
     */
    private Set<String> getUserAuthorities() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityUserDetail userDetail = (SecurityUserDetail) authentication.getPrincipal();
        return userDetail.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
    }

}

