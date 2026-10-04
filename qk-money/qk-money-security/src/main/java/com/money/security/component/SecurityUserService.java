package com.money.security.component;

import com.money.security.custom.RbacSecurityConfig;
import com.money.security.model.RbacUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 安全用户服务
 * <p>实现 Spring Security 的 {@link UserDetailsService} 接口，负责加载用户详细信息。</p>
 * <p>认证流程：
 * <ol>
 *     <li>根据用户名加载 {@link RbacUser} 信息</li>
 *     <li>检查用户是否存在，不存在则抛出 {@link UsernameNotFoundException}</li>
 *     <li>检查用户是否启用，未启用则抛出 {@link DisabledException}</li>
 *     <li>将 {@link RbacUser} 包装为 {@link SecurityUserDetail} 返回</li>
 * </ol>
 * </p>
 *
 * @author money
 * @since 1.0.0
 * @see UserDetailsService
 * @see RbacSecurityConfig
 */
@Service
@RequiredArgsConstructor
public class SecurityUserService implements UserDetailsService {

    private final RbacSecurityConfig rbacSecurityConfig;

    /**
     * 根据用户名加载用户详细信息
     *
     * @param username 用户名
     * @return 用户详细信息
     * @throws UsernameNotFoundException 用户不存在时抛出
     * @throws DisabledException         用户已禁用时抛出
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        RbacUser rbacUser = rbacSecurityConfig.loadRbacUser(username);
        if (rbacUser == null) {
            throw new UsernameNotFoundException("Account [" + username + "] has not found");
        }
        if (!rbacUser.getEnabled()) {
            throw new DisabledException("Account [" + username + "] has been disabled");
        }
        return new SecurityUserDetail(rbacUser);
    }

}
