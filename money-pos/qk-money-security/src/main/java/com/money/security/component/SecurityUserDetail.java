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
 * <p>实现 Spring Security 的 {@link UserDetails} 接口，将 {@link RbacUser} 适配为标准用户详情格式。</p>
 * <p>权限格式：
 * <ul>
 *     <li>角色：{@code ROLE_ADMIN}、{@code ROLE_USER} 等（带 {@code ROLE_} 前缀）</li>
 *     <li>权限码：{@code user:add}、{@code user:edit} 等（无前缀）</li>
 * </ul>
 * </p>
 *
 * @author money
 * @since 1.0.0
 * @see UserDetails
 * @see RbacUser
 */
@Data
@AllArgsConstructor
public class SecurityUserDetail implements UserDetails {

    private static final long serialVersionUID = 2129468533758586139L;

    private RbacUser user;

    /**
     * 获取用户的所有权限（角色带 ROLE_ 前缀）
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 兼容 Spring Security 规范，角色追加 ROLE_ 前缀
        return Stream.concat(
                    user.getRoles().stream().map(role -> "ROLE_" + role),
                    user.getPermissions().stream())
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
