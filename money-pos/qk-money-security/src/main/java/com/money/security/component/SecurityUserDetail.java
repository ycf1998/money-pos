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
 * @author : money
 * @version : 1.0.0
 * @description : 安全用户细节
 * @createTime : 2022-01-01 15:03:17
 */
@Data
@AllArgsConstructor
public class SecurityUserDetail implements UserDetails {

    private static final long serialVersionUID = 2129468533758586139L;

    private RbacUser user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Stream.concat(user.getRoles().stream(), user.getPermissions().stream())
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
