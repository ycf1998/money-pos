package com.money.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.money.entity.SysPermission;
import com.money.entity.SysRolePermissionRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色资源权限关联表(SysRolePermissionRelation)表数据库访问层
 *
 * @author money
 * @since 2021-09-08 22:34:42
 */
public interface SysRolePermissionRelationMapper extends BaseMapper<SysRolePermissionRelation> {

    /**
     * 通过角色获取权限信息
     *
     * @param roleIds 角色id
     * @return {@link List}<{@link SysPermission}>
     */
    List<SysPermission> selectPermissionByRole(@Param("roleIds") List<Long> roleIds);

    List<SysPermission> selectPermissionByUser(@Param("userId") Long userId);

}
