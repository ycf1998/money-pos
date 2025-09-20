package com.money.security.component;

import cn.hutool.core.util.StrUtil;
import com.money.security.model.RbacUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 安全用户详细信息
 *
 * @author : money
 * @since : 1.0.0
 */
@Data
@AllArgsConstructor
public class SecurityUserDetail implements UserDetails {

    private static final long serialVersionUID = 2129468533758586139L;

    private RbacUser user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 兼容 Spring Security 规范，角色追加 ROLE_ 前缀，见 SecurityExpressionRoot.defaultRolePrefix
        return Stream.concat(user.getRoles().stream().map(role -> "ROLE_" + role), user.getPermissions().stream())
                .filter(StrUtil::isNotBlank)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getEnabled();
    }

}
