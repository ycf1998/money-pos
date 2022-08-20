package com.money.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.money.dto.SysRoleDTO;
import com.money.dto.query.SysRoleQueryDTO;
import com.money.entity.SysRole;
import com.money.entity.SysUserRoleRelation;
import com.money.common.vo.PageVO;
import com.money.vo.SysRoleVO;

import java.util.List;
import java.util.Set;

/**
 * 角色表(SysRole)表服务接口
 *
 * @author money
 * @since 2021-11-28 22:50:29
 */
public interface SysRoleService extends IService<SysRole> {

    /**
     * 获取用户最高角色的级别
     *
     * @param userId 用户id
     * @return {@link Integer}
     */
    Integer getLevel(Long userId);

    /**
     * 获取用户所有角色
     *
     * @param userId 用户id
     * @return {@link List}<{@link SysRole}>
     */
    List<SysRole> getRoles(Long userId);

    /**
     * 获取所有角色
     *
     * @return {@link List}<{@link SysRole}>
     */
    List<SysRole> getAllRoles();

    /**
     * 查询角色列表
     *
     * @param queryDTO 查询dto
     * @return {@link PageVO}<{@link SysRoleVO}>
     */
    PageVO<SysRoleVO> list(SysRoleQueryDTO queryDTO);

    /**
     * 添加角色
     *
     * @param sysRoleDTO 系统角色dto
     */
    void add(SysRoleDTO sysRoleDTO);

    /**
     * 修改角色
     *
     * @param sysRoleDTO 系统角色dto
     */
    void updateById(SysRoleDTO sysRoleDTO);

    /**
     * 删除角色
     *
     * @param ids ids
     */
    void deleteById(Set<Long> ids);

    /**
     * 配置权限
     *
     * @param id          id
     * @param permissions 权限
     */
    void configurePermissions(Long id, Set<Long> permissions);

    /**
     * 修改可用状态
     *
     * @param id      id
     * @param enabled 启用
     */
    void changeStatus(Long id, Boolean enabled);

    /**
     * 关联角色
     *
     * @param userRoleRelations 用户角色关系
     */
    void relate(List<SysUserRoleRelation> userRoleRelations);

    /**
     * 解除用户所有角色
     *
     * @param userId 用户id
     */
    void relevanceByUser(Long userId);

    /**
     * 更新角色人数
     *
     * @param id   id
     * @param step 步
     */
    void updateRoleCount(List<Long> id, long step);

    /**
     * 检查角色级别
     *
     * @param userId     用户id
     * @param checkLevel 检查级别
     */
    void checkLevel(Long userId, Integer checkLevel);

    /**
     * 检查角色级别
     *
     * @param userId  用户id
     * @param roleIds 角色id
     */
    void checkLevelByRoleId(Long userId, Set<Long> roleIds);

    /**
     * 检查角色级别
     *
     * @param userId        用户id
     * @param checkUserIds 检查的用户
     */
    void checkLevelByUserId(Long userId, Set<Long> checkUserIds);

}
