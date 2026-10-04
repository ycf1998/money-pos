package com.money.security.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * RBAC 用户模型
 * <p>包含用户的基本信息、角色列表和权限列表，用于 Spring Security 认证和授权。</p>
 *
 * @author money
 * @since 1.0.0
 * @see com.money.security.component.SecurityUserDetail
 */
@Data
public class RbacUser {

    /**
     * 用户唯一标识
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码（加密后）
     */
    private String password;

    /**
     * 账户是否启用
     */
    private Boolean enabled = Boolean.TRUE;

    /**
     * 角色列表
     */
    private List<String> roles = new ArrayList<>();

    /**
     * 权限标识列表
     */
    private List<String> permissions = new ArrayList<>();

}
