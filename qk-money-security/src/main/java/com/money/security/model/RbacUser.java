package com.money.security.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 安全用户信息
 * @createTime : 2022-01-06 22:35:16
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
