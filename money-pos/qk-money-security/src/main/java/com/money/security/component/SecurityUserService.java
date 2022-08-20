package com.money.security.component;

import com.money.security.custom.RbacSecurityConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 安全用户服务
 * @createTime : 2022-01-01 15:03:20
 */
@Service
@RequiredArgsConstructor
public class SecurityUserService implements UserDetailsService {

    private final RbacSecurityConfig rbacSecurityConfig;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new SecurityUserDetail(rbacSecurityConfig.loadRbacUser(username));
    }

}
