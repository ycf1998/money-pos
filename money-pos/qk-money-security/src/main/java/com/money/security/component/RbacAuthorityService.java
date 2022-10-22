package com.money.security.component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : money
 * @version : 1.0.0
 * @description : rbac授权服务
 * @createTime : 2022-01-01 14:27:42
 */
@Slf4j
@Component("rbac")
@RequiredArgsConstructor
public class RbacAuthorityService {

    /**
     * 权限检测
     *
     * @param permissions 许可
     * @return boolean
     */
    public boolean hasPermission(String... permissions) {
        // 获取当前用户的所有权限
        SecurityUserDetail userDetail = (SecurityUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<String> userPermissions = userDetail.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        // 判断当前用户的所有权限是否包含接口上定义的权限
        return userPermissions.contains("SUPER_ADMIN") || Arrays.stream(permissions).anyMatch(userPermissions::contains);
    }

}

