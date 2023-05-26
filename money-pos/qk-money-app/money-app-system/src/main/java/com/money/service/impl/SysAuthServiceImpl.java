package com.money.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.money.common.exception.BaseException;
import com.money.common.response.RStatus;
import com.money.constant.ErrorStatus;
import com.money.constant.PermissionType;
import com.money.dto.LoginDTO;
import com.money.entity.SysPermission;
import com.money.entity.SysRole;
import com.money.entity.SysUser;
import com.money.security.component.SecurityTokenSupport;
import com.money.service.SysAuthService;
import com.money.service.SysPermissionService;
import com.money.service.SysRoleService;
import com.money.service.SysUserService;
import com.money.vo.AuthTokenVO;
import com.money.vo.UserInfoVO;
import com.money.vo.VueRouterVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 系统身份验证服务impl
 * @createTime : 2023-05-23 22:59:18
 */
@Service
@RequiredArgsConstructor
public class SysAuthServiceImpl implements SysAuthService {

    private final PasswordEncoder passwordEncoder;
    private final SecurityTokenSupport securityTokenSupport;

    private final SysUserService sysUserService;
    private final SysRoleService sysRoleService;
    private final SysPermissionService sysPermissionService;

    @Override
    public List<VueRouterVO> getVueRouter(Long userId) {
        // todo 超级管理员放行所有权限
        List<SysRole> roles = sysRoleService.getByUser(userId);
        boolean superAdmin = roles.stream().anyMatch(sysRole -> sysRole.getRoleCode().equals("SUPER_ADMIN"));
        List<SysPermission> sysPermissions;
        if (superAdmin) {
            sysPermissions = sysPermissionService.list();
        } else {
            sysPermissions = sysPermissionService.getByRole(roles.stream().map(SysRole::getId).collect(Collectors.toList()));
        }
        // 过滤目录和菜单
        sysPermissions = sysPermissions.stream()
                .filter(sysPermission -> ListUtil.of(PermissionType.DIR.name(), PermissionType.MENU.name()).contains(sysPermission.getPermissionType()))
                .sorted(Comparator.comparing(SysPermission::getSort))
                .collect(Collectors.toList());
        return this.recursionFillRouter(0L, sysPermissions);
    }

    /**
     * 递归填补路由器
     */
    private List<VueRouterVO> recursionFillRouter(Long parentId, List<SysPermission> sysPermissions) {
        List<VueRouterVO> routers = sysPermissions.stream().filter(sysPermission -> parentId.equals(sysPermission.getParentId()))
                .map(sysPermission -> {
                    // 对于空字符串得返回null，防止序列化回去
                    VueRouterVO routerVO = new VueRouterVO();
                    routerVO.setId(sysPermission.getId());
                    routerVO.setPath(sysPermission.getRouterPath());
                    routerVO.setName(sysPermission.getComponentName());
                    VueRouterVO.Meta meta = new VueRouterVO.Meta();
                    meta.setTitle(sysPermission.getPermissionName());
                    meta.setIcon(sysPermission.getIcon());
                    routerVO.setMeta(meta);
                    routerVO.setHidden(sysPermission.getHidden());
                    routerVO.setIframe(sysPermission.getIframe());
                    routerVO.setComponent(PermissionType.DIR.name().equals(sysPermission.getPermissionType()) ? "Layout" : sysPermission.getComponentPath());
                    return routerVO;
                }).collect(Collectors.toList());
        routers.forEach(router -> router.setChildren(this.recursionFillRouter(router.getId(), sysPermissions)));
        return routers;
    }

    @Override
    public AuthTokenVO login(LoginDTO loginDto) {
        SysUser sysUser = sysUserService.getByUsername(loginDto.getUsername());
        if (sysUser == null || !passwordEncoder.matches(loginDto.getPassword(), sysUser.getPassword())) {
            throw new BaseException(ErrorStatus.USER_NOT_FOUND);
        }
        AuthTokenVO authTokenVO = new AuthTokenVO();
        authTokenVO.setTokenType(securityTokenSupport.getTokenConfig().getTokenType());
        authTokenVO.setAccessToken(securityTokenSupport.generateToken(sysUser.getUsername()));
        authTokenVO.setTtl(securityTokenSupport.getTokenConfig().getTtl());
        authTokenVO.setRefreshToken(securityTokenSupport.generateRefreshToken(sysUser.getUsername()));
        authTokenVO.setRefreshTtl(securityTokenSupport.getTokenConfig().getRefreshTtl());
        // 更新登录时间
        sysUser.setLastTime(LocalDateTime.now());
        sysUserService.updateById(sysUser);
        return authTokenVO;
    }

    @Override
    public UserInfoVO getUserInfo(String username) {
        SysUser sysUser = sysUserService.getByUsername(username);
        if (sysUser == null) {
            throw new BaseException(ErrorStatus.USER_NOT_FOUND);
        }
        sysUser.setPassword(null);
        List<SysRole> roles = sysRoleService.getByUser(sysUser.getId());
        List<Long> roleIds = roles.stream().map(SysRole::getId).collect(Collectors.toList());
        List<SysPermission> permissions = sysPermissionService.getByRole(roleIds);
        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setInfo(sysUser);
        userInfoVO.setRoles(roles);
        userInfoVO.setPermissions(permissions);
        return userInfoVO;
    }

    @Override
    public void logout(String token) {
        if (StrUtil.isNotBlank(token)) {
            securityTokenSupport.invalidateToken(token);
        }
    }

    @Override
    public AuthTokenVO refreshToken(String username, String refreshToken) {
        String resolveUsername = securityTokenSupport.getUsername(refreshToken);
        if (!username.equals(resolveUsername)) {
            throw new BaseException(RStatus.FORBIDDEN);
        }
        AuthTokenVO authTokenVO = new AuthTokenVO();
        authTokenVO.setTokenType(securityTokenSupport.getTokenConfig().getTokenType());
        authTokenVO.setAccessToken(securityTokenSupport.generateToken(username));
        authTokenVO.setTtl(securityTokenSupport.getTokenConfig().getTtl());
        return authTokenVO;
    }

    @Override
    public void checkLevel(Long userId, Integer checkLevel) {
        if (checkLevel == null) {
            return;
        }
        Integer highestLevel = sysRoleService.getHighestLevel(userId);
        if (highestLevel >= checkLevel) {
            throw new BaseException(RStatus.FORBIDDEN);
        }
    }

    @Override
    public void checkLevelForUser(Long userId, Collection<Long> targetUserList) {
        if (CollUtil.isEmpty(targetUserList)) {
            return;
        }
        Integer highestLevel = sysRoleService.getHighestLevel(userId);
        targetUserList.stream().map(sysRoleService::getHighestLevel).min(Integer::compareTo)
                .ifPresent(highest -> {
                    if (highestLevel >= highest) {
                        throw new BaseException(RStatus.FORBIDDEN);
                    }
                });
    }

    @Override
    public void checkLevelForRole(Long userId, Collection<Long> targetRoleList) {
        if (CollUtil.isEmpty(targetRoleList)) {
            return;
        }
        Integer highestLevel = sysRoleService.getHighestLevel(userId);
        sysRoleService.listByIds(targetRoleList).stream().map(SysRole::getLevel).min(Integer::compare)
                .ifPresent(highest -> {
                    if (highestLevel >= highest) {
                        throw new BaseException(RStatus.FORBIDDEN);
                    }
                });
    }

}
