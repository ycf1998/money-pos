package com.money.security.custom;

import com.money.security.model.RbacUser;
import com.money.security.component.SecurityUserService;

/**
 * RBAC 安全配置接口
 * <p>用于从业务系统加载用户信息，实现此接口将安全模块与业务系统解耦。</p>
 * <p>使用示例：
 * <pre>{@code
 * @Configuration
 * public class MySecurityConfig implements RbacSecurityConfig {
 *
 *     @Autowired
 *     private UserService userService;
 *
 *     @Override
 *     public RbacUser loadRbacUser(String username) {
 *         User user = userService.getByUsername(username);
 *         if (user == null) return null;
 *
 *         RbacUser rbacUser = new RbacUser();
 *         rbacUser.setUserId(user.getId());
 *         rbacUser.setUsername(user.getUsername());
 *         rbacUser.setPassword(user.getPassword());
 *         rbacUser.setEnabled(user.getStatus() == 1);
 *         rbacUser.setRoles(roleService.getRolesByUserId(user.getId()));
 *         rbacUser.setPermissions(permissionService.getPermissionsByUserId(user.getId()));
 *         return rbacUser;
 *     }
 * }
 * }</pre>
 * </p>
 *
 * @author money
 * @since 1.0.0
 * @see SecurityUserService
 */
public interface RbacSecurityConfig {

    /**
     * 根据用户名加载 RBAC 用户信息
     *
     * @param username 用户名
     * @return RBAC 用户信息，不存在返回 {@code null}
     */
    RbacUser loadRbacUser(String username);
}
