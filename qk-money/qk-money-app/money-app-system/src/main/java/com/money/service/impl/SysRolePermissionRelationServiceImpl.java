package com.money.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.money.entity.SysRolePermissionRelation;
import com.money.mapper.SysRolePermissionRelationMapper;
import com.money.service.SysRolePermissionRelationService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 角色资源权限关联表(SysRolePermissionRelation)表服务实现类
 *
 * @author money
 * @since 2021-09-08 22:35:10
 */
@Service
public class SysRolePermissionRelationServiceImpl extends ServiceImpl<SysRolePermissionRelationMapper, SysRolePermissionRelation> implements SysRolePermissionRelationService {

    @Override
    public List<SysRolePermissionRelation> getRelationByRole(List<Long> roleIds) {
        return this.lambdaQuery().in(SysRolePermissionRelation::getRoleId, roleIds).list();
    }
}
