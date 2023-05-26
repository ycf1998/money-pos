package com.money.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.money.common.exception.BaseException;
import com.money.common.vo.PageVO;
import com.money.constant.ErrorStatus;
import com.money.dto.SysRoleDTO;
import com.money.dto.query.SysRoleQueryDTO;
import com.money.entity.*;
import com.money.mapper.SysRoleMapper;
import com.money.service.SysPermissionService;
import com.money.service.SysRolePermissionRelationService;
import com.money.service.SysRoleService;
import com.money.service.SysUserRoleRelationService;
import com.money.util.PageUtil;
import com.money.vo.SysRoleVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
    public Integer getHighestLevel(Long userId) {
        List<Long> roleIdList = sysUserRoleRelationService.getRelationByUser(userId)
                .stream().map(SysUserRoleRelation::getRoleId).collect(Collectors.toList());
        return this.listByIds(roleIdList).stream().map(SysRole::getLevel).min(Integer::compareTo)
                .orElse(Integer.MAX_VALUE);
    }

    @Override
    public List<SysRole> getByUser(Long userId) {
        List<Long> relationRoleIdList = sysUserRoleRelationService.getRelationByUser(userId)
                .stream().map(SysUserRoleRelation::getRoleId).collect(Collectors.toList());
        return this.listByIds(relationRoleIdList)
                .stream().sorted(Comparator.comparing(SysRole::getLevel))
                .collect(Collectors.toList());
    }

    @Override
    public List<SysRole> getAll() {
        return this.lambdaQuery().eq(SysRole::getEnabled, true).orderByAsc(SysRole::getLevel).list();
    }

    @Override
    public PageVO<SysRoleVO> list(SysRoleQueryDTO queryDTO) {
        IPage<SysRole> page = this.lambdaQuery()
                .eq(ObjectUtil.isNotNull(queryDTO.getEnabled()), SysRole::getEnabled, queryDTO.getEnabled())
                .like(StrUtil.isNotBlank(queryDTO.getRoleCode()), SysRole::getRoleCode, queryDTO.getRoleCode())
                .and(StrUtil.isNotBlank(queryDTO.getName()), wrapper -> wrapper.like(SysRole::getRoleName, queryDTO.getName())
                        .or(orWrapper -> orWrapper.like(SysRole::getDescription, queryDTO.getName())))
                .orderByAsc(StrUtil.isBlank(queryDTO.getSort()), SysRole::getLevel)
                .last(StrUtil.isNotBlank(queryDTO.getSort()), queryDTO.getOrderBySql())
                .page(PageUtil.toPage(queryDTO));
        return PageUtil.toPageVO(page, sysRole -> {
            SysRoleVO sysRoleVO = new SysRoleVO();
            BeanUtil.copyProperties(sysRole, sysRoleVO);
            List<SysPermission> permissionByRole = sysPermissionService.getByRole(Collections.singletonList(sysRoleVO.getId()));
            sysRoleVO.setPermissions(permissionByRole);
            return sysRoleVO;
        });
    }

    @Override
    public void add(SysRoleDTO sysRoleDTO) {
        // 唯一性判断
        boolean exists = this.lambdaQuery().eq(SysRole::getRoleCode, sysRoleDTO.getRoleCode()).exists();
        if (exists) {
            throw new BaseException(ErrorStatus.DATA_ALREADY_EXIST, "角色");
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
    public void deleteById(Collection<Long> ids) {
        // 删除角色用户关联关系
        sysUserRoleRelationService.lambdaUpdate().in(SysUserRoleRelation::getRoleId, ids).remove();
        // 删除角色权限关联关系
        sysRolePermissionRelationService.lambdaUpdate().in(SysRolePermissionRelation::getRoleId, ids).remove();
        this.removeBatchByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void configurePermissions(Long id, Set<Long> permissions) {
        // 先删除在重新关联
        sysRolePermissionRelationService.lambdaUpdate().eq(SysRolePermissionRelation::getRoleId, id).remove();
        List<SysRolePermissionRelation> relations = permissions.stream().map(permission -> {
            SysRolePermissionRelation sysRolePermissionRelation = new SysRolePermissionRelation();
            sysRolePermissionRelation.setRoleId(id);
            sysRolePermissionRelation.setPermissionId(permission);
            return sysRolePermissionRelation;
        }).collect(Collectors.toList());
        sysRolePermissionRelationService.saveBatch(relations);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void relate(List<SysUserRoleRelation> userRoleRelations) {
        sysUserRoleRelationService.saveBatch(userRoleRelations);
        this.updateCount(userRoleRelations.stream().map(SysUserRoleRelation::getRoleId).collect(Collectors.toList()), 1);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void relevanceByUser(Long userId) {
        List<Long> roleIdList = sysUserRoleRelationService.getRelationByUser(userId)
                .stream().map(SysUserRoleRelation::getRoleId).collect(Collectors.toList());
        sysUserRoleRelationService.lambdaUpdate().eq(SysUserRoleRelation::getUserId, userId).remove();
        this.updateCount(roleIdList, -1);
    }

    /**
     * 更新角色关联用户数
     *
     * @param id   id
     * @param step 一步
     */
    private void updateCount(List<Long> id, long step) {
        this.lambdaUpdate().in(SysRole::getId, id).setSql("count = count + " + step).update();
    }

}
