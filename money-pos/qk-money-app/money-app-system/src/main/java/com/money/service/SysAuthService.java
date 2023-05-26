package com.money.service;

import com.money.dto.LoginDTO;
import com.money.vo.AuthTokenVO;
import com.money.vo.UserInfoVO;
import com.money.vo.VueRouterVO;

import java.util.Collection;
import java.util.List;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 系统身份验证服务
 * @createTime : 2023-05-23 22:58:34
 */
public interface SysAuthService {

    /**
     * 得到vue路由器
     *
     * @param userId 用户id
     * @return {@link List}<{@link VueRouterVO}>
     */
    List<VueRouterVO> getVueRouter(Long userId);

    /**
     * 登录
     *
     * @param loginDto 登录dto
     * @return {@link AuthTokenVO}
     */
    AuthTokenVO login(LoginDTO loginDto);

    /**
     * 获取个人信息
     *
     * @param username 用户名
     * @return {@link UserInfoVO}
     */
    UserInfoVO getUserInfo(String username);

    /**
     * 注销
     *
     * @param token 令牌
     */
    void logout(String token);

    /**
     * 刷新令牌
     *
     * @param username     用户名
     * @param refreshToken 刷新令牌
     * @return {@link AuthTokenVO}
     */
    AuthTokenVO refreshToken(String username, String refreshToken);

    /**
     * 等级检查
     *
     * @param userId     用户id
     * @param checkLevel 检查级别
     */
    void checkLevel(Long userId, Integer checkLevel);

    /**
     * 等级检查
     *
     * @param userId         用户id
     * @param targetUserList 目标用户列表
     */
    void checkLevelForUser(Long userId, Collection<Long> targetUserList);

    /**
     * 等级检查
     *
     * @param userId         用户id
     * @param targetRoleList 目标角色列表
     */
    void checkLevelForRole(Long userId, Collection<Long> targetRoleList);

}
