package com.money.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.money.entity.SysUserRoleRelation;
import com.money.mapper.SysUserRoleRelationMapper;
import com.money.service.SysUserRoleRelationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 管理员角色关联表(SysAdminRoleRelation)表服务实现类
 *
 * @author money
 * @since 2021-09-08 22:35:10
 */
@Service
@RequiredArgsConstructor
public class SysUserRoleRelationServiceImpl extends ServiceImpl<SysUserRoleRelationMapper, SysUserRoleRelation> implements SysUserRoleRelationService {
    @Override
    public List<SysUserRoleRelation> getRelationByUser(Long userId) {
        return this.lambdaQuery().eq(SysUserRoleRelation::getUserId, userId).list();
    }
}
