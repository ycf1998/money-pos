package com.money.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.money.common.exception.BaseException;
import com.money.dto.SysTenantDTO;
import com.money.dto.query.SysTenantQueryDTO;
import com.money.common.vo.PageVO;
import com.money.constant.ErrorStatus;
import com.money.entity.*;
import com.money.mapper.SysTenantMapper;
import com.money.oss.OSSDelegate;
import com.money.oss.core.FileNameStrategy;
import com.money.oss.core.FolderPath;
import com.money.oss.local.LocalOSS;
import com.money.service.*;
import com.money.util.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author money
 * @since 2022-03-04
 */
@Service
@RequiredArgsConstructor
public class SysTenantServiceImpl extends ServiceImpl<SysTenantMapper, SysTenant> implements SysTenantService {

    private final PasswordEncoder passwordEncoder;
    private final SysUserService sysUserService;
    private final SysUserRoleRelationService sysUserRoleRelationService;
    private final SysRoleService sysRoleService;
    private final SysRolePermissionRelationService sysRolePermissionRelationService;
    private final SysPermissionService sysPermissionService;
    private final OSSDelegate<LocalOSS> localOSS;


    @Override
    public SysTenant getTenantIdByCode(String code) {
        return this.lambdaQuery().eq(SysTenant::getTenantCode, code).eq(SysTenant::getDeleted, false).one();
    }

    @Override
    public PageVO<SysTenant> list(SysTenantQueryDTO queryDTO) {
        Page<SysTenant> page = this.lambdaQuery()
                .like(StrUtil.isNotBlank(queryDTO.getTenantCode()), SysTenant::getTenantCode, queryDTO.getTenantCode())
                .like(StrUtil.isNotBlank(queryDTO.getTenantName()), SysTenant::getTenantName, queryDTO.getTenantName())
                .eq(SysTenant::getDeleted, false)
                .orderByAsc(SysTenant::getSort).orderByDesc(SysTenant::getUpdateTime)
                .page(PageUtil.toPage(queryDTO));
        return PageUtil.toPageVO(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(SysTenantDTO sysTenantDTO, MultipartFile logo) {
        // 租户code唯一
        boolean exists = this.lambdaQuery()
                .eq(SysTenant::getTenantCode, sysTenantDTO.getTenantCode())
                .eq(SysTenant::getDeleted, false).exists();
        if (exists) {
            throw new BaseException(ErrorStatus.DATA_ALREADY_EXIST, "租户");
        }
        SysTenant sysTenant = new SysTenant();
        BeanUtil.copyProperties(sysTenantDTO, sysTenant);
        // 上传logo
        if (logo != null) {
            String logoUrl = localOSS.upload(logo, FolderPath.builder().cd("tenant").build(), FileNameStrategy.TIMESTAMP);
            sysTenant.setLogo(logoUrl);
        }
        this.save(sysTenant);
        this.newTenant(sysTenant.getId());
    }

    @Override
    public void updateById(SysTenantDTO sysTenantDTO, MultipartFile logo) {
        // 租户code唯一
        boolean exists = this.lambdaQuery()
                .ne(SysTenant::getId, sysTenantDTO.getId())
                .eq(SysTenant::getDeleted, false)
                .eq(SysTenant::getTenantCode, sysTenantDTO.getTenantCode()).exists();
        if (exists) {
            throw new BaseException(ErrorStatus.DATA_ALREADY_EXIST, "租户");
        }
        SysTenant sysTenant = this.getById(sysTenantDTO.getId());
        BeanUtil.copyProperties(sysTenantDTO, sysTenant, CopyOptions.create().ignoreNullValue());
        // 上传logo
        if (logo != null) {
            String logoUrl = localOSS.upload(logo, FolderPath.builder().cd("tenant").build(), FileNameStrategy.TIMESTAMP);
            localOSS.delete(sysTenant.getLogo());
            sysTenant.setLogo(logoUrl);
        }
        this.updateById(sysTenant);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Set<Long> ids) {
        // 主租户不可删除
        List<Long> filterIds = ids.stream().filter(id -> 0 != id).collect(Collectors.toList());
        if (CollUtil.isEmpty(filterIds)) {
            throw new BaseException("不可以删除主租户");
        }
        this.lambdaUpdate().set(SysTenant::getDeleted, true).in(SysTenant::getId, filterIds).update();
//        this.removeBatchByIds(ids);
//        sysUserService.lambdaUpdate().in(SysUser::getTenantId, ids).remove();
//        sysUserRoleRelationService.lambdaUpdate().in(SysUserRoleRelation::getTenantId, ids).remove();
//        sysRoleService.lambdaUpdate().in(SysRole::getTenantId, ids).remove();
//        sysRolePermissionRelationService.lambdaUpdate().in(SysRolePermissionRelation::getTenantId, ids).remove();
//        sysPermissionService.lambdaUpdate().in(SysPermission::getTenantId, ids).remove();
//        sysDictService.lambdaUpdate().in(SysDict::getTenantId, ids).remove();
//        sysDictDetailService.lambdaUpdate().in(SysDictDetail::getTenantId, ids).remove();
    }

    /**
     * 新租客初始化
     *
     * @param id id
     */
    private void newTenant(Long id) {
        // 新增管理员
        SysUser sysUser = new SysUser();
        sysUser.setUsername("admin");
        sysUser.setPassword(passwordEncoder.encode("admin"));
        sysUser.setNickname("管理员");
        sysUser.setTenantId(id);
        sysUserService.save(sysUser);
        // 新增管理员角色
        SysRole sysRole = new SysRole();
        sysRole.setRoleCode("ADMIN");
        sysRole.setRoleName("管理员");
        sysRole.setLevel(1);
        sysRole.setCount(1L);
        sysRole.setTenantId(id);
        sysRoleService.save(sysRole);
        // 关联用户角色
        SysUserRoleRelation sysUserRoleRelation = new SysUserRoleRelation();
        sysUserRoleRelation.setUserId(sysUser.getId());
        sysUserRoleRelation.setRoleId(sysRole.getId());
        sysUserRoleRelation.setTenantId(id);
        sysUserRoleRelationService.save(sysUserRoleRelation);
        // 新增权限
        List<SysPermission> sysPermissions = sysPermissionService.lambdaQuery()
                .notLike(SysPermission::getPermission, "tenant")
                .notLike(SysPermission::getPermission, "permission")
                .notLike(SysPermission::getPermission, "dict")
                .list();
        // id映射保证父子节点关系
        Map<Long, Long> permissionId = new HashMap<>(sysPermissions.size());
        sysPermissions.forEach(sysPermission -> {
            permissionId.put(sysPermission.getId(), IdUtil.getSnowflake().nextId());
        });
        sysPermissions.forEach(sysPermission -> {
            sysPermission.setId(permissionId.get(sysPermission.getId()));
            sysPermission.setParentId(permissionId.get(sysPermission.getParentId()));
            sysPermission.setTenantId(id);
        });
        sysPermissionService.saveBatch(sysPermissions);
        // 关联权限角色
        List<SysRolePermissionRelation> sysRolePermissionRelationList = sysPermissions.stream().map(sysPermission -> {
            SysRolePermissionRelation sysRolePermissionRelation = new SysRolePermissionRelation();
            sysRolePermissionRelation.setPermissionId(sysPermission.getId());
            sysRolePermissionRelation.setRoleId(sysRole.getId());
            sysRolePermissionRelation.setTenantId(id);
            return sysRolePermissionRelation;
        }).collect(Collectors.toList());
        sysRolePermissionRelationService.saveBatch(sysRolePermissionRelationList);
    }
}
