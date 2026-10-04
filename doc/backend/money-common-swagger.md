# money-common-swagger - 接口文档模块

## 概述

接口文档模块集成 SpringDoc OpenAPI 3（Swagger 3），自动生成 RESTful API 接口文档。

**设计优势**：
- **开箱即用**：根据配置自动注入 Swagger 配置类
- **OpenAPI 3**：使用最新的 OpenAPI 3 规范
- **安全集成**：自动配置 JWT Bearer Token 认证
- **分组支持**：支持多模块分组展示

## 依赖

```xml
<!-- 接口文档模块 -->
<dependency>
    <groupId>com.money</groupId>
    <artifactId>money-common-swagger</artifactId>
</dependency>
```

## 使用方式

### 访问接口文档

启动应用后，访问：

```
http://{host}:{port}/{context-path}/swagger-ui.html
```

### 配置说明

```yaml
swagger:
  # 扫描包路径
  scan-package: com.money
  # 项目标题
  title: ${spring.application.name}
  # 项目描述
  description: ${spring.application.name}接口文档
  # 版本
  version: 1.0.0
  # 认证头名称
  auth-header: ${money.security.token.header}
  # 联系人信息
  contact:
    name: money
    url: www.money.com
    email: 374648769@qq.com
```

## 注解使用

### Swagger 3 注解

| 注解 | 说明 |
|------|------|
| `@Tag` | 类级别描述 |
| `@Operation` | 接口操作描述（summary、description） |
| `@Parameter` | 参数描述 |
| `@Parameters` | 多个参数 |
| `@Schema` | 模型描述 |
| `@Hidden` | 隐藏接口 |

### Controller 示例

```java
@Tag(name = "用户管理")
@RestController
@RequestMapping("/users")
public class UserController {

    @Operation(summary = "分页查询用户列表")
    @GetMapping
    public R<PageVO<UserVO>> page(PageQueryRequest request) {
        return R.success(userService.page(request));
    }

    @Operation(summary = "创建用户")
    @PostMapping
    public R<UserVO> create(@RequestBody @Validated UserCreateDTO dto) {
        return R.success(userService.create(dto));
    }

    @Operation(summary = "获取用户详情")
    @GetMapping("/{id}")
    public R<UserVO> detail(@Parameter(description = "用户 ID") @PathVariable Long id) {
        return R.success(userService.getById(id));
    }
}
```

### DTO/VO 示例

```java
@Schema(description = "用户信息")
@Data
public class UserVO {

    @Schema(description = "用户 ID", example = "1234567890")
    private Long id;

    @Schema(description = "用户名", example = "admin")
    private String username;
}
```

### 隐藏接口或参数

```java
// 隐藏整个接口
@Operation(hidden = true)
@GetMapping("/internal")
public R<String> internal() { }

// 隐藏特定参数
@GetMapping("/list")
public R<List<UserVO>> list(
        @Parameter(hidden = true) PageQueryRequest request) { }

// 隐藏整个 Controller
@Hidden
@RestController
@RequestMapping("/internal")
public class InternalController { }
```

## 安全认证

模块自动配置 JWT Bearer Token 认证，在 Swagger UI 中点击 **Authorize** 输入 Token：

```
Authorization: Bearer {accessToken}
```

## 核心类说明

| 类名 | 说明 |
|------|------|
| `Swagger3Configuration` | 自动配置类，注册 `GroupedOpenApi` 和 `OpenAPI` |
| `SwaggerProperties` | 配置属性类（标题、描述、扫描包等） |

## 分组配置

如有多个模块需要分组展示：

```java
@Bean
public GroupedOpenApi userApi() {
    return GroupedOpenApi.builder()
            .group("user")
            .packagesToScan("com.money.controller.user")
            .build();
}
```

访问时可在 Swagger UI 顶部切换分组。

## 注意事项

1. **生产环境关闭**：生产环境建议关闭 Swagger，避免暴露接口信息
2. **扫描范围**：建议限制扫描包范围，提升启动速度
3. **注解规范**：使用 Swagger 3 注解，避免混用 Swagger 2 注解

## 相关链接

- [SpringDoc 官方文档](https://springdoc.org/)
- [OpenAPI 规范](https://swagger.io/specification/)
