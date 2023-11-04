package com.money.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.money.entity.SysUserRoleRelation;

import java.util.List;

/**
 * 用户角色关联表(SysUserRoleRelation)表服务接口
 *
 * @author money
 * @since 2021-09-08 22:35:10
 */
public interface SysUserRoleRelationService extends IService<SysUserRoleRelation> {

    /**
     * 通过用户获取关联关系
     *
     * @param userId 用户id
     * @return {@link List}<{@link SysUserRoleRelation}>
     */
    List<SysUserRoleRelation> getRelationByUser(Long userId);
}
