package com.money.security.custom;

import com.money.security.model.RbacUser;

/**
 * @author : money
 * @version : 1.0.0
 * @description : RBAC Security配置
 * @createTime : 2022-01-03 14:33:47
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
