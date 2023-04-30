package com.money.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.money.common.exception.BaseException;
import com.money.common.vo.PageVO;
import com.money.constant.ErrorStatus;
import com.money.dto.SysRoleDTO;
import com.money.dto.query.SysRoleQueryDTO;
import com.money.entity.SysPermission;
import com.money.entity.SysRole;
import com.money.entity.SysRolePermissionRelation;
import com.money.entity.SysUserRoleRelation;
import com.money.exception.RoleRelatedException;
import com.money.mapper.SysRoleMapper;
import com.money.security.SecurityGuard;
import com.money.service.SysPermissionService;
import com.money.service.SysRolePermissionRelationService;
import com.money.service.SysRoleService;
import com.money.service.SysUserRoleRelationService;
import com.money.util.PageUtil;
import com.money.vo.SysRoleVO;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 角色表(SysRole)表服务实现类
 *
 * @author money
 * @since 2021-11-28 22:50:29
 */
@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    private final SysPermissionService sysPermissionService;
    private final SysUserRoleRelationService sysUserRoleRelationService;
    private final SysRolePermissionRelationService sysRolePermissionRelationService;

    @Override
    public Integer getLevel(Long userId) {
        return Optional.ofNullable(this.baseMapper.getLevel(userId)).orElse(Integer.MAX_VALUE);
    }

    @Override
    public List<SysRole> getRoles(Long userId) {
        return this.baseMapper.selectRoleList(userId);
    }

    @Override
    public List<SysRole> getAllRoles() {
        return this.list(new LambdaQueryWrapper<SysRole>().eq(SysRole::getEnabled, true).orderByAsc(SysRole::getLevel));
    }

    @Override
    public PageVO<SysRoleVO> list(SysRoleQueryDTO queryDTO) {
        IPage<SysRole> page = this.lambdaQuery()
                .eq(ObjectUtil.isNotNull(queryDTO.getEnabled()), SysRole::getEnabled, queryDTO.getEnabled())
                .like(StrUtil.isNotBlank(queryDTO.getRoleCode()), SysRole::getRoleCode, queryDTO.getRoleCode())
                .and(StrUtil.isNotBlank(queryDTO.getName()), wrapper -> wrapper.like(SysRole::getRoleName, queryDTO.getName())
                        .or(orWrapper -> orWrapper.like(SysRole::getDescription, queryDTO.getName())))
                .orderByAsc(SysRole::getLevel)
                .page(PageUtil.toPage(queryDTO));
        return PageUtil.toPageVO(page, sysRole -> {
            SysRoleVO sysRoleVO = new SysRoleVO();
            BeanUtil.copyProperties(sysRole, sysRoleVO);
            List<SysPermission> permissionByRole = sysPermissionService.getPermissionByRole(Collections.singletonList(sysRoleVO.getId()));
            sysRoleVO.setPermissions(permissionByRole);
            return sysRoleVO;
        });
    }

    @Override
    public void add(SysRoleDTO sysRoleDTO) {
        // 唯一性判断
        boolean exists = this.lambdaQuery().eq(SysRole::getRoleCode, sysRoleDTO.getRoleCode()).exists();
        if (exists) {
            throw new RoleRelatedException(ErrorStatus.ROLE_ALREADY_EXIST);
        }
        SysRole sysRole = new SysRole();
        BeanUtil.copyProperties(sysRoleDTO, sysRole);
        // 角色编码全大写
        sysRole.setRoleCode(sysRole.getRoleCode().toUpperCase());
        this.save(sysRole);
    }

    @Override
    public void updateById(SysRoleDTO sysRoleDTO) {
        SysRole sysRole = new SysRole();
        BeanUtil.copyProperties(sysRoleDTO, sysRole);
        // 不允许修改编码
        sysRole.setRoleCode(null);
        this.updateById(sysRole);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Set<Long> ids) {
        // 删除角色用户关联关系
        sysUserRoleRelationService.remove(new LambdaQueryWrapper<SysUserRoleRelation>().in(SysUserRoleRelation::getRoleId, ids));
        // 删除角色权限关联关系
        sysRolePermissionRelationService.remove(new LambdaQueryWrapper<SysRolePermissionRelation>().in(SysRolePermissionRelation::getRoleId, ids));
        this.removeBatchByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = "user", allEntries = true)
    public void configurePermissions(Long id, Set<Long> permissions) {
        // 先删除在重新关联
        sysRolePermissionRelationService.remove(new LambdaQueryWrapper<SysRolePermissionRelation>().eq(SysRolePermissionRelation::getRoleId, id));
        List<SysRolePermissionRelation> relations = permissions.stream().map(permission -> {
            SysRolePermissionRelation sysRolePermissionRelation = new SysRolePermissionRelation();
            sysRolePermissionRelation.setRoleId(id);
            sysRolePermissionRelation.setPermissionId(permission);
            return sysRolePermissionRelation;
        }).collect(Collectors.toList());
        sysRolePermissionRelationService.saveBatch(relations);
    }

    @Override
    public void changeStatus(Long id, Boolean enabled) {
        this.lambdaUpdate().eq(SysRole::getId, id).set(SysRole::getEnabled, enabled).update();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void relate(List<SysUserRoleRelation> userRoleRelations) {
        sysUserRoleRelationService.saveBatch(userRoleRelations);
        this.updateRoleCount(userRoleRelations.stream().map(SysUserRoleRelation::getRoleId).collect(Collectors.toList()), 1);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void relevanceByUser(Long userId) {
        List<Long> roleIds = sysUserRoleRelationService.listObjs(new LambdaQueryWrapper<SysUserRoleRelation>().select(SysUserRoleRelation::getRoleId)
                .eq(SysUserRoleRelation::getUserId, userId), e -> Long.valueOf(String.valueOf(e)));
        if (CollectionUtil.isNotEmpty(roleIds)) {
            sysUserRoleRelationService.remove(new LambdaQueryWrapper<SysUserRoleRelation>()
                    .eq(SysUserRoleRelation::getUserId, userId));
            this.updateRoleCount(roleIds, -1);
        }
    }

    @Override
    public void updateRoleCount(List<Long> id, long step) {
        this.lambdaUpdate().in(SysRole::getId, id).setSql("count = count + " + step).update();
    }

    @Override
    public void checkLevel(Long userId, Integer checkLevel) {
        Integer level = this.getLevel(userId);
        if (level > checkLevel) {
            throw new BaseException("权限不足，你的角色级别低于操作的角色级别");
        }
    }

    @Override
    public void checkLevelByRoleId(Long userId, Set<Long> roleIds) {
        Integer level = this.getLevel(userId);
        Integer min = this.listByIds(roleIds).stream().map(SysRole::getLevel).min(Integer::compare).orElse(Integer.MAX_VALUE);
        if (level > min) {
            throw new BaseException("权限不足，你的角色级别低于操作的角色级别");
        }
    }

    @Override
    public void checkLevelByUserId(Long userId, Set<Long> checkUserIds) {
        Integer level = this.getLevel(SecurityGuard.getRbacUser().getUserId());
        Integer min = checkUserIds.stream().map(this::getLevel).min(Integer::compare).orElse(Integer.MAX_VALUE);
        if (level > min) {
            throw new BaseException("角色权限不足，不能操作比自己级别高的用户");
        }
    }
}
