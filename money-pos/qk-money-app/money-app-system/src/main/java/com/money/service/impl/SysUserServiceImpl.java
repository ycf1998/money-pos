package com.money.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.money.common.vo.PageVO;
import com.money.constant.ErrorStatus;
import com.money.dto.ChangePasswordDTO;
import com.money.dto.LoginDTO;
import com.money.dto.SysUserDTO;
import com.money.dto.UpdateProfileDTO;
import com.money.dto.query.SysUserQueryDTO;
import com.money.entity.SysPermission;
import com.money.entity.SysRole;
import com.money.entity.SysUser;
import com.money.entity.SysUserRoleRelation;
import com.money.exception.UserRelatedException;
import com.money.mapper.SysUserMapper;
import com.money.oss.OSSDelegate;
import com.money.oss.core.FileNameStrategy;
import com.money.oss.core.FolderPath;
import com.money.oss.local.LocalOSS;
import com.money.security.component.SecurityTokenSupport;
import com.money.service.SysPermissionService;
import com.money.service.SysRoleService;
import com.money.service.SysUserRoleRelationService;
import com.money.service.SysUserService;
import com.money.util.*;
import com.money.vo.AuthTokenVO;
import com.money.vo.OwnInfoVO;
import com.money.vo.SysUserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CacheConfig(cacheNames = "user")
@RequiredArgsConstructor
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private final PasswordEncoder passwordEncoder;
    private final SecurityTokenSupport securityTokenSupport;

    private final SysRoleService sysRoleService;
    private final SysUserRoleRelationService sysUserRoleRelationService;
    private final SysPermissionService sysPermissionService;
    private final OSSDelegate<LocalOSS> localOSS;

    @Cacheable(key = "'basic.info:' + #username + ':object'")
    @Override
    public SysUser getByUsername(String username) {
        return this.lambdaQuery().eq(SysUser::getUsername, username).one();
    }

    @Cacheable(key = "'role:' + #userId + ':list'")
    @Override
    public List<SysRole> getRoles(Long userId) {
        return sysRoleService.getRoles(userId);
    }

    @Cacheable(key = "'permission:' + #userId + ':list'")
    @Override
    public List<SysPermission> getPermissions(Long userId) {
        List<SysUserRoleRelation> userRoleRelationList = sysUserRoleRelationService.lambdaQuery().select(SysUserRoleRelation::getRoleId).eq(SysUserRoleRelation::getUserId, userId).list();
        return sysPermissionService.getPermissionByRole(userRoleRelationList.stream().map(SysUserRoleRelation::getRoleId).collect(Collectors.toList()));
    }

    @Override
    public OwnInfoVO getOwnInfo(String username) {
        SysUser sysUser = getByUsername(username);
        sysUser.setPassword(null);
        List<SysRole> roles = getRoles(sysUser.getId());
        List<SysPermission> permissions = getPermissions(sysUser.getId());
        OwnInfoVO ownInfoVO = new OwnInfoVO();
        ownInfoVO.setInfo(sysUser);
        ownInfoVO.setRoles(roles);
        ownInfoVO.setPermissions(permissions);
        return ownInfoVO;
    }

    @Override
    public AuthTokenVO login(LoginDTO loginDto) {
        SysUser sysUser = this.getByUsername(loginDto.getUsername());
        if (sysUser == null || !passwordEncoder.matches(loginDto.getPassword(), sysUser.getPassword())) {
            throw new UserRelatedException(ErrorStatus.USER_NOT_FOUND);
        }
        AuthTokenVO authTokenVO = new AuthTokenVO();
        authTokenVO.setTokenType(securityTokenSupport.getTokenConfig().getTokenType());
        authTokenVO.setAccessToken(securityTokenSupport.generateToken(sysUser.getUsername()));
        authTokenVO.setTtl(securityTokenSupport.getTokenConfig().getTtl());
        authTokenVO.setRefreshToken(securityTokenSupport.generateRefreshToken(sysUser.getUsername()));
        authTokenVO.setRefreshTtl(securityTokenSupport.getTokenConfig().getRefreshTtl());
        // 更新登录时间
        sysUser.setLastTime(LocalDateTime.now());
        this.updateById(sysUser);
        return authTokenVO;
    }

    @Override
    public void logout(String token) {
        if (StrUtil.isNotBlank(token)) {
            securityTokenSupport.invalidateToken(token);
        }
    }

    /**
     * 刷新令牌
     * todo 选择令牌刷新策略
     * 1. 颁发新的accessToken和refreshToken。（无限续签，除非在refreshToken过期前都没刷新登录过）
     * 2. 仅颁发新的accessToken。（refreshToken过期就得重新登录）
     *
     * @param refreshToken 刷新令牌
     * @return {@link AuthTokenVO}
     */
    @Override
    public AuthTokenVO refreshToken(String refreshToken) {
        // 无论是否过期，请求刷新令牌就把原来的过期一下
        String username = securityTokenSupport.getUsername(refreshToken);
        AuthTokenVO authTokenVO = new AuthTokenVO();
        authTokenVO.setTokenType(securityTokenSupport.getTokenConfig().getTokenType());
        authTokenVO.setAccessToken(securityTokenSupport.generateToken(username));
        authTokenVO.setTtl(securityTokenSupport.getTokenConfig().getTtl());
//        authTokenVO.setRefreshToken(securityTokenSupport.generateRefreshToken(username));
//        authTokenVO.setRefreshTtl(securityTokenSupport.getTokenConfig().getRefreshTtl());
        return authTokenVO;
    }

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

    // ============================================================

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
            throw new UserRelatedException(ErrorStatus.OLD_PASSWORD_ERROR);
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
                .orderByDesc(SysUser::getUpdateTime)
                .page(PageUtil.toPage(queryDTO));
        return PageUtil.toPageVO(page, sysUser -> {
            SysUserVO sysUserVO = new SysUserVO();
            BeanUtil.copyProperties(sysUser, sysUserVO);
            sysUserVO.setRoles(sysRoleService.getRoles(sysUserVO.getId()));
            return sysUserVO;
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(SysUserDTO sysUserDTO) {
        // 唯一性判断
        if (getByUsername(sysUserDTO.getUsername()) != null) {
            throw new UserRelatedException(ErrorStatus.USER_ALREADY_EXIST);
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
    @Caching(evict = {
            @CacheEvict(key = "'basic.info:' + #sysUserDTO.id + ':object'"),
            @CacheEvict(key = "'role:' + #sysUserDTO.id + ':list'"),
            @CacheEvict(key = "'permission:' + #sysUserDTO.id + ':list'")
    })
    public void updateById(SysUserDTO sysUserDTO) {
        // 唯一性判断
        SysUser byUsername = getByUsername(sysUserDTO.getUsername());
        if (byUsername != null && !byUsername.getId().equals(sysUserDTO.getId())) {
            throw new UserRelatedException(ErrorStatus.USER_ALREADY_EXIST);
        }
        SysUser sysUser = new SysUser();
        BeanUtil.copyProperties(sysUserDTO, sysUser);
        this.updateById(sysUser);
        // 重新关联角色
        sysRoleService.relevanceByUser(sysUserDTO.getId());
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
    public void deleteById(Set<Long> ids) {
        this.removeBatchByIds(ids);
        // 删除角色关联
        ids.forEach(sysRoleService::relevanceByUser);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(key = "'basic.info:' + #id + ':object'"),
            @CacheEvict(key = "'role:' + #id + ':list'"),
            @CacheEvict(key = "'permission:' + #id + ':list'")
    })
    public void changeStatus(Long id, Boolean enabled) {
        this.lambdaUpdate().eq(SysUser::getId, id).set(SysUser::getEnabled, enabled).update();
    }

}
