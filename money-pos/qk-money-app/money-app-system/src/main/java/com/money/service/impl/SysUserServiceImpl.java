package com.money.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.money.common.exception.BaseException;
import com.money.common.vo.PageVO;
import com.money.constant.ErrorStatus;
import com.money.dto.ChangePasswordDTO;
import com.money.dto.SysUserDTO;
import com.money.dto.UpdateProfileDTO;
import com.money.dto.query.SysUserQueryDTO;
import com.money.entity.SysUser;
import com.money.entity.SysUserRoleRelation;
import com.money.mapper.SysUserMapper;
import com.money.oss.OSSDelegate;
import com.money.oss.core.FileNameStrategy;
import com.money.oss.core.FolderPath;
import com.money.oss.local.LocalOSS;
import com.money.service.SysRoleService;
import com.money.service.SysUserService;
import com.money.util.PageUtil;
import com.money.vo.SysUserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@CacheConfig(cacheNames = "user")
@RequiredArgsConstructor
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private final PasswordEncoder passwordEncoder;
    private final OSSDelegate<LocalOSS> localOSS;

    private final SysRoleService sysRoleService;

    @Override
    public SysUser getByUsername(String username) {
        return this.lambdaQuery().eq(SysUser::getUsername, username).one();
    }

    // ============================================================

    @Override
    public String uploadAvatar(String username, MultipartFile file) {
        SysUser sysUser = this.getByUsername(username);
        String oldAvatar = sysUser.getAvatar();
        if (StrUtil.isNotBlank(oldAvatar)) {
            localOSS.delete(oldAvatar);
        }
        String avatar = localOSS.upload(file, FolderPath.builder().cd("user").build(), FileNameStrategy.TIMESTAMP);
        sysUser.setAvatar(avatar);
        this.updateById(sysUser);
        return avatar;
    }

    @Override
    public void updateProfile(String username, UpdateProfileDTO updateProfileDTO) {
        SysUser sysUser = new SysUser();
        BeanUtil.copyProperties(updateProfileDTO, sysUser);
        this.lambdaUpdate().eq(SysUser::getUsername, username).update(sysUser);
    }

    @Override
    public void changePassword(String username, ChangePasswordDTO changePasswordDTO) {
        SysUser sysUser = this.getByUsername(username);
        if (sysUser == null || !passwordEncoder.matches(changePasswordDTO.getOldPassword(), sysUser.getPassword())) {
            throw new BaseException(ErrorStatus.OLD_PASSWORD_ERROR);
        }
        sysUser.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        this.updateById(sysUser);
    }

    // ============================================================

    @Override
    public PageVO<SysUserVO> list(SysUserQueryDTO queryDTO) {
        IPage<SysUser> page = this.lambdaQuery()
                .eq(ObjectUtil.isNotNull(queryDTO.getEnabled()), SysUser::getEnabled, queryDTO.getEnabled())
                .like(StrUtil.isNotBlank(queryDTO.getPhone()), SysUser::getPhone, queryDTO.getPhone())
                .and(StrUtil.isNotBlank(queryDTO.getName()), wrapper -> wrapper.like(SysUser::getUsername, queryDTO.getName())
                        .or(orWrapper -> orWrapper.like(SysUser::getNickname, queryDTO.getName())))
                .orderByDesc(StrUtil.isBlank(queryDTO.getSort()), SysUser::getLastTime)
                .last(StrUtil.isNotBlank(queryDTO.getSort()), queryDTO.getOrderBySql())
                .page(PageUtil.toPage(queryDTO));
        return PageUtil.toPageVO(page, sysUser -> {
            SysUserVO sysUserVO = new SysUserVO();
            BeanUtil.copyProperties(sysUser, sysUserVO);
            sysUserVO.setRoles(sysRoleService.getByUser(sysUserVO.getId()));
            return sysUserVO;
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(SysUserDTO sysUserDTO) {
        // 唯一性判断
        if (this.getByUsername(sysUserDTO.getUsername()) != null) {
            throw new BaseException(ErrorStatus.DATA_ALREADY_EXIST, "用户");
        }
        SysUser sysUser = new SysUser();
        BeanUtil.copyProperties(sysUserDTO, sysUser);
        // 加密密码
        sysUser.setPassword(passwordEncoder.encode("123456"));
        this.save(sysUser);
        // 关联角色
        List<SysUserRoleRelation> userRoleRelations = sysUserDTO.getRoles().stream().map(roleId -> {
            SysUserRoleRelation relation = new SysUserRoleRelation();
            relation.setUserId(sysUser.getId());
            relation.setRoleId(roleId);
            return relation;
        }).collect(Collectors.toList());
        sysRoleService.relate(userRoleRelations);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateById(SysUserDTO sysUserDTO) {
        SysUser sysUser = new SysUser();
        BeanUtil.copyProperties(sysUserDTO, sysUser);
        // 用户名不允许修改
        sysUser.setUsername(null);
        this.updateById(sysUser);
        Optional.ofNullable(sysUserDTO.getRoles()).ifPresent(roles -> {
            // 重新关联角色
            sysRoleService.relevanceByUser(sysUserDTO.getId());
            List<SysUserRoleRelation> userRoleRelations = roles.stream().map(roleId -> {
                SysUserRoleRelation relation = new SysUserRoleRelation();
                relation.setUserId(sysUser.getId());
                relation.setRoleId(roleId);
                return relation;
            }).collect(Collectors.toList());
            sysRoleService.relate(userRoleRelations);
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Set<Long> ids) {
        this.removeBatchByIds(ids);
        // 删除角色关联
        ids.forEach(sysRoleService::relevanceByUser);
    }

}
