package com.money.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.money.entity.SysRole;

import java.util.List;

/**
 * 角色表(SysRole)表数据库访问层
 *
 * @author money
 * @since 2021-09-07 22:19:31
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * 获取最高角色级别
     *
     * @param userId 用户id
     * @return {@link Integer}
     */
    Integer getLevel(Long userId);

    /**
     * 通过用户名查询角色列表
     *
     * @param userId 用户id
     * @return {@link List}<{@link SysRole}>
     */
    List<SysRole> selectRoleList(Long userId);
}
