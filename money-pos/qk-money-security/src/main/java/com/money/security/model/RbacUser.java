package com.money.security.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * RBAC 用户
 *
 * @author : money
 * @since : 1.0.0
 */
@Data
public class RbacUser {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 启用
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
