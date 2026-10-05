# money-common-mybatis - MyBatis 模块

## 概述

MyBatis 模块集成 MyBatis-Plus 增强框架，提供分页插件、自动填充功能和代码生成器。模块封装为独立模块，方便与其他框架解耦和替换。

**设计优势**：
- **开箱即用**：默认配置分页插件、主键生成策略，无需额外配置
- **自动填充**：审计字段（创建时间、更新时间、操作人）自动填充，减少重复代码
- **可扩展**：通过 `Operator` 接口灵活适配不同的用户信息获取方式
- **代码生成**：提供 CRUD 代码生成器，快速生成分层代码

## 依赖

```xml
<!-- MyBatis 模块 -->
<dependency>
    <groupId>com.money</groupId>
    <artifactId>money-common-mybatis</artifactId>
</dependency>

<!-- MySQL 驱动 -->
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
</dependency>
```

## 功能特性

### 1. 分页插件

默认配置 MyBatis-Plus 分页插件，无需额外配置即可使用。支持与其他插件（如多租户插件）灵活组合。

### 2. 主键生成策略

默认使用雪花算法（Snowflake）生成主键 ID，避免自增 ID 暴露业务信息。

### 3. 自动填充功能

配置元对象处理器，自动填充审计字段：

| 字段 | 类型 | 填充时机 | 值来源 |
|------|------|----------|--------|
| `create_time` | LocalDateTime | INSERT | 当前时间 |
| `update_time` | LocalDateTime | INSERT/UPDATE | 当前时间 |
| `create_by` | String | INSERT | Operator Bean |
| `update_by` | String | INSERT/UPDATE | Operator Bean |

主键 `id` 不走元对象处理器，由 `IdentifierGenerator`（雪花算法）在 INSERT 时生成。

**数据库端不要给这些字段设默认值**（如 `DEFAULT CURRENT_TIMESTAMP`、`ON UPDATE CURRENT_TIMESTAMP`），否则与这里的填充形成两个来源，互相覆盖。

**扩展性**：通过实现 `Operator` 接口，可灵活适配不同的用户信息获取方式（如从安全上下文、Session 等）。

### 4. 代码生成器

提供 CRUD 代码生成功能，快速生成 Entity、Mapper、Service、Controller 等代码，支持自定义模板和配置。

## 使用方式

### 实体类定义

继承 `BaseEntity` 获得审计字段自动填充能力：

```java
@TableName("sys_user")
@Schema(description = "用户信息")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUser extends BaseEntity {

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "密码")
    private String password;
    
    // 其他业务字段...
}
```

### Mapper/Service 定义

```java
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
    // 可添加自定义查询方法
}

public interface SysUserService extends IService<SysUser> {
    // 可添加自定义业务方法
}
```

### 分页查询

```java
// 使用 PageQueryRequest（封装分页 + 排序）
PageQueryRequest request = new PageQueryRequest();
request.setPage(1);
request.setSize(10);
request.setOrderBy("create_time,desc");

Page<SysUser> page = userService.page(PageUtil.toPage(request));

// 转换为 PageVO（统一分页响应格式）
PageVO<SysUser> pageVO = PageUtil.toPageVO(page);
```

### 自定义 Operator（扩展点）

与安全模块配合，实现审计字段自动填充：

```java
@Component
public class SecurityOperator implements Operator {

    @Override
    public String getUsername() {
        // 从安全上下文获取当前登录用户名
        return SecurityGuard.getRbacUser().getUsername();
    }
}
```

## 代码生成器

### 运行方式

运行 `com.money.mb.MybatisPlusGenerator` 类的 `main` 方法。

### 配置说明

代码生成器支持自定义配置：

- **数据源配置**：数据库连接信息
- **全局配置**：输出目录、作者等
- **包配置**：父包名、模块名、各层包名
- **策略配置**：需要生成的表、实体/映射/服务/控制器配置

## 配置说明

### 基础配置

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/qk_money?...
    username: root
    password: root
    type: com.zaxxer.hikari.HikariDataSource
```

### MyBatis-Plus 配置（可选）

```yaml
mybatis-plus:
  mapper-locations: classpath*:/mapper/**/*.xml
  type-aliases-package: com.money.**.entity
  configuration:
    map-underscore-to-camel-case: true
  global-config:
    db-config:
      logic-delete-field: deleted
      logic-delete-value: 1
      logic-not-delete-value: 0
```

## 核心类说明

| 类名 | 说明 | 扩展性 |
|------|------|--------|
| `MybatisPlusConfiguration` | 自动配置类，注册分页插件、主键生成器、元对象处理器 | 可通过 `@Bean` 覆盖默认配置 |
| `BaseEntity` | 基础实体类，包含审计字段 | 可继承获得自动填充能力 |
| `Operator` | 操作者接口，用于获取当前登录人信息 | **可扩展**，实现自定义用户信息获取逻辑 |
| `MybatisPlusMetaObjectHandler` | 元对象处理器，实现自动填充 | 可继承扩展填充逻辑 |

## 常用注解

| 注解 | 说明 |
|------|------|
| `@TableName` | 表名 |
| `@TableId` | 主键策略 |
| `@TableField(fill = FieldFill.INSERT)` | 字段填充 |
| `@TableLogic` | 逻辑删除 |
| `@Version` | 乐观锁 |

### 主键策略

| 策略 | 说明 |
|------|------|
| `ASSIGN_ID` | 雪花算法（推荐） |
| `ASSIGN_UUID` | UUID |
| `AUTO` | 数据库自增 |
| `NONE` | 无策略 |
| `INPUT` | 用户输入 |

## 注意事项

1. **实体类字段类型**：时间字段使用 `LocalDateTime` 类型
2. **Operator 实现**：建议与安全模块配合实现自动填充
3. **分页插件顺序**：多租户场景下，租户插件需在分页插件之前

## 相关链接

- [MyBatis 官方文档](https://mybatis.org/mybatis-3/)
- [MyBatis-Plus 官方文档](https://baomidou.com/)
