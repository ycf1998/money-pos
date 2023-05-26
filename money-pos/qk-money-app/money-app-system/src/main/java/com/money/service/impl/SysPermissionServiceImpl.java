package com.money.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.money.common.exception.BaseException;
import com.money.constant.ErrorStatus;
import com.money.constant.PermissionType;
import com.money.dto.SysPermissionDTO;
import com.money.dto.query.SysPermissionQueryDTO;
import com.money.entity.SysPermission;
import com.money.entity.SysRolePermissionRelation;
import com.money.mapper.SysPermissionMapper;
import com.money.service.SysPermissionService;
import com.money.service.SysRolePermissionRelationService;
import com.money.vo.SysPermissionVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 资源权限表(SysPermission)表服务实现类
 *
 * @author money
 * @since 2021-09-08 22:35:10
 */
@Service
@RequiredArgsConstructor
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements SysPermissionService {

    private final SysRolePermissionRelationService sysRolePermissionRelationService;

    @Override
    public List<SysPermission> getByRole(List<Long> roleIds) {
        if (CollectionUtil.isEmpty(roleIds)) {
            return Collections.emptyList();
        }
        List<Long> permissionIdList = sysRolePermissionRelationService.getRelationByRole(roleIds)
                .stream().map(SysRolePermissionRelation::getPermissionId).collect(Collectors.toList());
        return permissionIdList.isEmpty() ? Collections.emptyList() : this.listByIds(permissionIdList);
    }

    @Override
    public List<SysPermissionVO> list(SysPermissionQueryDTO queryDTO) {
        // 权限界面不分页
        boolean notSearch = StrUtil.isAllBlank(queryDTO.getCondition(), queryDTO.getPermissionType());
        List<SysPermission> sysPermissionList = this.lambdaQuery()
                .eq(notSearch, SysPermission::getParentId, 0)
                .eq(ObjectUtil.isNotNull(queryDTO.getParentId()), SysPermission::getParentId, queryDTO.getParentId())
                .eq(StrUtil.isNotBlank(queryDTO.getPermissionType()), SysPermission::getPermissionType, queryDTO.getPermissionType())
                .and(StrUtil.isNotBlank(queryDTO.getCondition()), wrapper -> wrapper.like(SysPermission::getPermissionName, queryDTO.getCondition())
                        .or(orWrapper -> orWrapper.like(SysPermission::getPermission, queryDTO.getCondition())))
                .orderByAsc(SysPermission::getSort).list();
        return sysPermissionList.stream().map(sysPermission -> {
            SysPermissionVO vo = new SysPermissionVO();
            BeanUtil.copyProperties(sysPermission, vo);
            if (notSearch) {
                this.recursionFillChildren(vo);
            }
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(SysPermissionDTO permissionDTO) {
        this.checkDTO(permissionDTO);
        if (StrUtil.isNotBlank(permissionDTO.getPermission())) {
            boolean exists = this.lambdaQuery().eq(SysPermission::getPermission, permissionDTO.getPermission()).exists();
            if (exists) {
                throw new BaseException(ErrorStatus.DATA_ALREADY_EXIST, "权限");
            }
        }
        SysPermission sysPermission = new SysPermission();
        BeanUtil.copyProperties(permissionDTO, sysPermission);
        this.save(sysPermission);
        // 更新子节点数
        this.updateSubCount(permissionDTO.getParentId(), 1);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateById(SysPermissionDTO permissionDTO) {
        this.checkDTO(permissionDTO);
        if (StrUtil.isNotBlank(permissionDTO.getPermission())) {
            boolean exists = this.lambdaQuery().eq(SysPermission::getPermission, permissionDTO.getPermission())
                    .ne(SysPermission::getId, permissionDTO.getId()).exists();
            if (exists) {
                throw new BaseException(ErrorStatus.DATA_ALREADY_EXIST, "权限");
            }
        }
        SysPermission sysPermission = this.getById(permissionDTO.getId());
        BeanUtil.copyProperties(permissionDTO, sysPermission);
        this.updateById(sysPermission);
        // 更新子节点数
        this.updateSubCount(permissionDTO.getParentId(), 1);
        this.updateSubCount(sysPermission.getParentId(), -1);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Set<Long> ids) {
        // 非根节点，更新父节点的子节点数
        this.listByIds(ids).stream().filter(sysPermission -> sysPermission.getParentId() != 0L)
                .forEach(sysPermission -> this.updateSubCount(sysPermission.getParentId(), -1));
        // 获取包括子级id，一并删除
        List<Long> temp = new ArrayList<>();
        ids.forEach(id -> {
            List<Long> allSubIds = this.getAllSubIds(id);
            temp.addAll(allSubIds);
        });
        // 删除角色权限关联关系
        sysRolePermissionRelationService.lambdaUpdate().in(SysRolePermissionRelation::getPermissionId, temp).remove();
        this.removeBatchByIds(temp);
    }

    @Override
    public List<Long> getAllSubIds(Long id) {
        List<Long> allSubIds = new ArrayList<>();
        allSubIds.add(id);
        recursionFillSubIds(id, allSubIds);
        return allSubIds;
    }

    /**
     * 更新子节点数
     *
     * @param parentId 父id
     * @param step     步
     */
    private void updateSubCount(Long parentId, int step) {
        this.lambdaUpdate().setSql("sub_count = sub_count + " + step).eq(SysPermission::getId, parentId).update();
    }

    /**
     * 递归填充子节点id
     *
     * @param id     id
     * @param subIds 子id
     */
    private void recursionFillSubIds(Long id, List<Long> subIds) {
        List<SysPermission> sysPermissionList = this.lambdaQuery().select(SysPermission::getId).eq(SysPermission::getParentId, id).list();
        sysPermissionList.forEach(sysPermission -> {
            Long subId = sysPermission.getId();
            subIds.add(subId);
            recursionFillSubIds(subId, subIds);
        });
    }

    /**
     * 递归填补子节点
     *
     * @param sysPermissionVO 系统许可VO
     */
    private void recursionFillChildren(SysPermissionVO sysPermissionVO) {
        List<SysPermission> children = this.lambdaQuery().eq(SysPermission::getParentId, sysPermissionVO.getId()).list();
        if (CollectionUtil.isNotEmpty(children)) {
            sysPermissionVO.setChildren(children.stream().map(sysPermission -> {
                SysPermissionVO vo = new SysPermissionVO();
                BeanUtil.copyProperties(sysPermission, vo);
                this.recursionFillChildren(vo);
                return vo;
            }).collect(Collectors.toList()));
        }
    }

    /**
     * 检查dto
     *
     * @param permissionDTO 许可dto
     */
    private void checkDTO(SysPermissionDTO permissionDTO) {
        String permissionType = permissionDTO.getPermissionType();
        if (PermissionType.DIR.name().equals(permissionType)) {
            if (StrUtil.isBlank(permissionDTO.getRouterPath())) {
                throw new BaseException("路由地址不允许为空");
            }
        } else if (PermissionType.MENU.name().equals(permissionType)) {
            if (StrUtil.isBlank(permissionDTO.getPermission())) {
                throw new BaseException("权限标识不允许为空");
            }
            if (!permissionDTO.getIframe()) {
                if (StrUtil.isBlank(permissionDTO.getComponentName())) {
                    throw new BaseException("组件名称不允许为空");
                }
                if (StrUtil.isBlank(permissionDTO.getComponentPath())) {
                    throw new BaseException("组件路径不允许为空");
                }
            }
        } else if (PermissionType.BUTTON.name().equals(permissionType)
                && StrUtil.isBlank(permissionDTO.getPermission())) {
            throw new BaseException("权限标识不允许为空");
        }
    }
}
