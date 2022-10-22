package com.money.security;

import com.money.security.component.SecurityUserDetail;
import com.money.security.model.RbacUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 保安
 * @createTime : 2022-01-07 22:28:13
 */
@Slf4j
public class SecurityGuard {

    public static boolean isLoggedState() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof SecurityUserDetail;
    }

    public static RbacUser getRbacUser() {
        if (!isLoggedState()) {
            log.warn("检查是否是经过认证后的接口里的操作，放行的接口无法获取登录信息");
            throw new RuntimeException("无法获取登录信息");
        }
        SecurityUserDetail userDetail = (SecurityUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetail.getUser();
    }
}
