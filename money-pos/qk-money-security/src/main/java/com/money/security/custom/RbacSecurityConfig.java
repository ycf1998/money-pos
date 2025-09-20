package com.money.security.custom;

import com.money.security.model.RbacUser;

/**
 * RBAC 安全配置
 *
 * @author : money
 * @since : 1.0.0
 */
public interface RbacSecurityConfig {

    /**
     * 加载权限用户
     *
     * @param username 用户名
     * @return {@link RbacUser}
     */
    RbacUser loadRbacUser(String username);
}
