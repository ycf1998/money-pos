# qk-money-tenant - 多租户模块

## 概述

多租户模块基于 MyBatis-Plus 多租户插件实现**基于表字段的多租户隔离**。开启多租户后，所有 MyBatis-Plus 方法 SQL 都会自动添加租户条件过滤。

**设计优势**：
- **自动过滤**：MyBatis-Plus 方法自动添加租户条件，无需显式编写
- **SQL 解析**：自定义 SQL 按规范书写也能自动处理
- **灵活配置**：支持配置忽略的表
- **上下文获取**：通过 `TenantContextHolder` 获取当前租户 ID

## 依赖

```xml
<!-- 多租户模块 -->
<dependency>
    <groupId>com.money</groupId>
    <artifactId>qk-money-tenant</artifactId>
</dependency>
```

## 使用方式

### 1. 数据库表设计

为需要租户隔离的表添加 `tenant_id` 字段（建议添加索引）：

```sql
CREATE TABLE `sys_user` (
  `id` bigint UNSIGNED NOT NULL,
  `tenant_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '租户 ID',
  -- 其他字段...
  KEY `idx_tenant_id` (`tenant_id`)
);
```

### 2. 配置多租户

```yaml
money:
  tenant:
    enabled: true                   # 是否启用
    header: Y-tenant                # 请求头键名
    default-tenant-id: "0"          # 默认租户 ID
    ignore-table:                   # 忽略的表
      - sys_tenant
      - sys_dict
```

### 3. 前端请求

前端在请求头中传递租户 ID：

```
Y-tenant: 1
```

### 4. 使用示例

#### 自动过滤租户

```java
// 以下方法都会自动添加 tenant_id = ? 条件
userMapper.selectById(1L);
userService.getById(1L);
userService.list(new LambdaQueryWrapper<User>().eq(User::getStatus, 1));
```

#### 自动填充租户 ID

```java
User user = new User();
user.setUsername("test");
userService.save(user);  // 自动填充 tenant_id
```

#### 自定义 SQL

自定义 SQL 需使用表别名才能自动处理：

```xml
<!-- ✅ 正确：使用表别名 -->
<select id="selectUserWithRole" resultType="com.money.entity.User">
    SELECT u.*, r.role_name
    FROM sys_user u
    LEFT JOIN sys_user_role ur ON u.id = ur.user_id
    LEFT JOIN sys_role r ON ur.role_id = r.id
    WHERE u.status = 1
</select>

<!-- ❌ 错误：没有使用别名 -->
<select id="selectWrong" resultType="com.money.entity.User">
    SELECT * FROM sys_user WHERE status = 1
</select>
```

### 5. 获取当前租户 ID

```java
Long tenantId = TenantContextHolder.getTenant();
```

## 配置说明

| 配置项 | 默认值 | 说明 |
|--------|--------|------|
| `enabled` | true | 是否启用多租户 |
| `header` | Y-tenant | 请求头键名 |
| `default-tenant-id` | 0 | 默认租户 ID |
| `ignore-table` | [] | 忽略的表列表 |

## 核心类说明

| 类名 | 说明 |
|------|------|
| `TenantConfiguration` | 自动配置类，注册多租户拦截器 |
| `TenantProperties` | 配置属性类 |
| `TenantContextHolder` | 租户上下文持有者（ThreadLocal） |

## 忽略的表

以下类型的表建议配置为忽略表：

| 表类型 | 说明 | 示例 |
|--------|------|------|
| 系统表 | 系统级配置表 | `sys_tenant`, `sys_config` |
| 字典表 | 公共字典数据 | `sys_dict`, `sys_dict_detail` |
| 公共表 | 所有租户共享的数据 | `area`, `city` |

## 注意事项

1. **拦截器顺序**：多租户拦截器必须在分页拦截器**之前**添加
2. **自定义 SQL 规范**：多表查询时每个表都需要使用别名

## 相关链接

- [MyBatis-Plus 多租户插件](https://baomidou.com/pages/aef2f2/#tenantlineinnerinterceptor)
- [MyBatis-Plus 官方文档](https://baomidou.com/)
