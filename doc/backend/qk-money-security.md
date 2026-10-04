# qk-money-security - 安全模块

## 概述

安全模块基于 Spring Security 实现认证授权功能，提供基于 JWT 的 Token 认证和基于 RBAC 模型的权限控制。

**设计优势**：
- **开箱即用**：封装 Spring Security 复杂配置，简化使用
- **JWT 认证**：基于 JWT 的无状态 Token 认证
- **RBAC 授权**：基于角色的访问控制模型
- **双 Token 机制**：Access Token + Refresh Token
- **灵活策略**：支持 JWT 和 Redis 两种 Token 策略（可扩展）
- **便捷获取**：支持上下文和注解两种方式获取当前用户

## 依赖

```xml
<!-- 安全模块 -->
<dependency>
    <groupId>com.money</groupId>
    <artifactId>qk-money-security</artifactId>
</dependency>
```

## 使用方式

### 1. 配置用户详情加载

实现 `RbacSecurityConfig` 接口提供用户详情加载逻辑：

```java
@Bean
public RbacSecurityConfig rbacSecurityConfig() {
    return username -> {
        // 从数据库加载用户信息
        SysUser user = sysUserService.getByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        
        // 加载角色和权限
        List<String> roles = sysUserService.getRoles(user.getId())
                .stream().map(SysRole::getRoleCode).collect(Collectors.toList());
        List<String> permissions = sysUserService.getPermissions(user.getId())
                .stream().map(SysPermission::getPermission).collect(Collectors.toList());
        
        // 构建 RbacUser
        RbacUser rbacUser = new RbacUser();
        rbacUser.setUserId(user.getId());
        rbacUser.setUsername(user.getUsername());
        rbacUser.setPassword(user.getPassword());
        rbacUser.setEnabled(user.getEnabled());
        rbacUser.setRoles(roles);
        rbacUser.setPermissions(permissions);
        return rbacUser;
    };
}
```

### 2. 登录接口

```java
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final SecurityTokenSupport tokenSupport;

    @PostMapping("/login")
    public R<Map<String, String>> login(@RequestBody LoginDTO dto) {
        // 认证
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
        );
        
        // 生成 Token
        String username = authentication.getName();
        String accessToken = tokenSupport.generateToken(username);
        String refreshToken = tokenSupport.generateRefreshToken(username);
        
        Map<String, String> tokens = Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken
        );
        return R.success(tokens);
    }
}
```

### 3. 权限控制

```java
@RestController
@RequestMapping("/users")
public class UserController {

    // 需要特定权限
    @PostMapping
    @PreAuthorize("hasAuthority('system:user:add')")
    public R<UserVO> create(@RequestBody UserCreateDTO dto) { }

    // 需要特定角色
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public R<Void> delete(@PathVariable Long id) { }
}
```

### 4. 获取当前用户信息

```java
// 方式 1：通过上下文
RbacUser user = SecurityGuard.getRbacUser();

// 方式 2：通过注解
@GetMapping("/me")
public R<UserVO> me(@CurrentUser RbacUser user) {
    return R.success(userService.getVO(user.getUserId()));
}
```

## 配置说明

```yaml
money:
  security:
    token:
      header: Authorization       # Token 请求头键名
      token-type: Bearer          # 令牌类型
      secret: your-secret         # 密钥（生产环境请修改）
      ttl: 28800000               # Access Token 过期时间（ms），默认 8 小时
      refresh-ttl: 18144000000    # Refresh Token 过期时间（ms），默认 210 天
      strategy: jwt               # 策略：jwt（默认）、redis
      cache-key: "security:token:"
    
    # 忽略的 URL（无需认证）
    ignore:
      pattern:
        - /error/**
        - /swagger**/**
        - /auth/login
```

## 核心类说明

| 类名/接口 | 说明 |
|-----------|------|
| `RbacUser` | 用户模型（userId、username、roles、permissions） |
| `RbacSecurityConfig` | 用户详情加载配置接口 |
| `SecurityTokenSupport` | Token 支持类（生成、解析、刷新、失效） |
| `TokenStrategy` | Token 策略接口（JWT/Redis） |
| `SecurityGuard` | 安全上下文持有者 |
| `@CurrentUser` | 用户信息注入注解 |

## Token 策略

| 策略 | 说明 | 适用场景 |
|------|------|----------|
| JWT | Token 自包含过期时间，无法手动失效 | 一般场景 |
| Redis | 可手动失效 Token | 需要强制下线场景 |

可通过实现 `TokenStrategy` 接口自定义策略。

## 权限表达式

| 表达式 | 说明 |
|--------|------|
| `hasAuthority('xxx')` | 是否有指定权限 |
| `hasAnyAuthority('a', 'b')` | 是否有任一权限 |
| `hasRole('ADMIN')` | 是否有指定角色 |
| `hasAnyRole('ADMIN', 'USER')` | 是否有任一角色 |
| `isAuthenticated()` | 是否已认证 |

## 注意事项

1. **密钥安全**：生产环境需修改 `secret` 配置
2. **HTTPS**：生产环境建议使用 HTTPS 传输 Token
3. **Token 过期时间**：根据业务需求合理设置

## 相关链接

- [Spring Security 官方文档](https://spring.io/projects/spring-security)
- [JWT 官方文档](https://jwt.io/)
