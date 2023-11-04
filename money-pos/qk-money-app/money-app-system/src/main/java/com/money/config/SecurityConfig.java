package com.money.config;

import com.money.entity.SysPermission;
import com.money.entity.SysRole;
import com.money.entity.SysUser;
import com.money.security.custom.RbacSecurityConfig;
import com.money.security.model.RbacUser;
import com.money.service.SysAuthService;
import com.money.vo.UserInfoVO;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : money
 * @version : 1.0.0
 * @description : Security配置
 * @createTime : 2022-01-01 16:23:35
 */
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class SecurityConfig {

    private final SysAuthService sysAuthService;

    @Bean
    public RbacSecurityConfig rbacSecurityConfig() {
        return username -> {
            UserInfoVO userInfo = sysAuthService.getUserInfo(username);
            SysUser sysUser = userInfo.getInfo();
            List<String> roleCodes = userInfo.getRoles()
                    .stream().map(SysRole::getRoleCode).collect(Collectors.toList());
            List<String> permissions = userInfo.getPermissions()
                    .stream().map(SysPermission::getPermission).collect(Collectors.toList());
            // 返回装填的rbac user
            RbacUser rbacUser = new RbacUser();
            rbacUser.setUserId(sysUser.getId());
            rbacUser.setUsername(sysUser.getUsername());
            rbacUser.setPassword(sysUser.getPassword());
            rbacUser.setEnabled(sysUser.getEnabled());
            rbacUser.setRoles(roleCodes);
            rbacUser.setPermissions(permissions);
            return rbacUser;
        };
    }

}
