package com.money.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.money.entity.SysRolePermissionRelation;

import java.util.List;

/**
 * 角色资源权限关联表(SysRolePermissionRelation)表服务接口
 *
 * @author money
 * @since 2021-09-08 22:35:10
 */
public interface SysRolePermissionRelationService extends IService<SysRolePermissionRelation> {

    /**
     * 根据角色获取关联关系
     *
     * @param roleIds 角色id
     * @return {@link List}<{@link SysRolePermissionRelation}>
     */
    List<SysRolePermissionRelation> getRelationByRole(List<Long> roleIds);

}
