# money-common-web - 通用 Web 模块

## 概述

通用 Web 模块是 QK-MONEY 框架的核心基础模块，提供 Web 开发的通用功能和配置。包含全局响应处理、全局异常处理、日志链路追踪、多语言、多时区等核心功能。

## 依赖

```xml
<!-- Web 模块 -->
<dependency>
    <groupId>com.money</groupId>
    <artifactId>money-common-web</artifactId>
</dependency>
```

## 包结构

```
com.money.web
├── config/              # 配置类（Jackson、MVC 等）
├── constant/            # 常量定义
├── context/             # 请求上下文
├── dto/                 # 通用 DTO
├── exception/           # 异常处理
├── i18n/                # 多语言支持
├── log/                 # 日志切面
├── response/            # 响应处理
├── timezone/            # 多时区处理
├── util/                # 工具类
└── vo/                  # 通用 VO
```

## 核心功能

### 1. 全局响应处理

默认全局响应处理器 `DefaultResponseHandler` 将返回值统一包装为 `R<T>` 格式：

```json
{
    "code": 200,
    "data": T,
    "message": "操作成功"
}
```

**接口无需手动包装 `R<T>`**，全局响应处理会自动包装。

```java
// ✅ 正确：直接返回业务对象
@GetMapping("/user/{id}")
public UserVO getUser(@PathVariable Long id) {
    return userService.getById(id);
}

// ❌ 错误：无需手动包装
@GetMapping("/user/{id}")
public R<UserVO> getUser(@PathVariable Long id) {
    return R.success(userService.getById(id));  // 不要这样做
}
```

**返回自定义状态码**：直接返回 `R<T>` 或抛出业务异常。

```java
// 方式 1：返回 R<T>
@GetMapping("/check")
public R<Void> check() {
    if (someCondition) {
        return R.fail("自定义错误");
    }
    return R.success();
}

// 方式 2：抛出业务异常（推荐）
@GetMapping("/user/{id}")
public UserVO getUser(@PathVariable Long id) {
    User user = userService.getById(id);
    if (user == null) {
        throw new BaseException(RStatus.NOT_FOUND);  // 推荐
    }
    return user;
}
```

**忽略全局响应处理**：使用 `@IgnoreGlobalResponse` 注解，常用于导出接口。

```java
@IgnoreGlobalResponse
@GetMapping("/export")
public void export(HttpServletResponse response) {
    // 直接写入响应流（如导出 Excel）
}
```

### 2. 全局异常处理

默认全局异常处理器 `DefaultExceptionHandler` 拦截并处理各类异常：

| 异常类型 | HTTP 状态码 | 说明 |
|----------|------------|------|
| `Exception` | 500 | 兜底异常 |
| `BindException` / `MethodArgumentNotValidException` / `ConstraintViolationException` | 400 | 参数校验异常 |
| `BaseException` | 200 | 业务异常 |

**业务异常使用**：

```java
// 抛出业务异常
if (user == null) {
    throw new BaseException(RStatus.NOT_FOUND);
}
```

可继承 `BaseException` 创建更细粒度的业务异常。

### 3. 请求日志切面

`DefaultWebLogAspect` 记录请求日志，包含请求 IP、方法、URL、参数、返回结果和耗时。

**配置**：

```yaml
money:
  web:
    web-log-aspect:
      enabled: true
      mode: ignore_get_result
      # 脱敏字段列表（为空时不脱敏）
      desensitize-fields:
        - password
        - token
        - secret
        - accessToken
        - refreshToken
```

**mode 说明**：

| mode | 说明 |
|------|------|
| `all` | 记录所有请求的日志（包括请求和响应） |
| `ignore_get_result` | GET 请求不记录响应结果，其他请求正常记录 |
| `simple` | 简单模式，不记录请求体和响应结果 |

**日志输出示例**：

```
====================
POST /qk-money/users
{requestId:abc123, lang:zh_CN, timezone:GMT+08:00}
> 192.168.1.100
body: {"name":"test"}
result: {"code":200,"data":{}}
spend time: 15ms
```

日志单独输出到 `log/access.log` 文件，按日期滚动，保留 7 天。

### 4. 请求上下文 & 链路追踪

`WebRequestContextFilter` 过滤器处理请求上下文，传递请求 ID、语言、时区信息。

**请求头常量**：

```java
public interface WebRequestConstant {
    String HEADER_REQUEST_ID = "X-qk-request";   // 请求 ID（链路追踪）
    String HEADER_LANG = "X-qk-lang";            // 语言
    String HEADER_TIMEZONE = "X-qk-timezone";    // 时区
}
```

**获取上下文信息**：

```java
String requestId = WebRequestContextHolder.getContext().getRequestId();
String lang = WebRequestContextHolder.getContext().getLang();
ZoneId timezone = WebRequestContextHolder.getContext().getTimezone();
```

请求 ID 通过 MDC 集成到日志中，方便链路追踪。

### 5. 多语言支持

`I18nSupport` 提供多语言支持，根据请求头 `X-qk-lang` 自动切换语言。

**配置**：

```yaml
money:
  web:
    i18n:
      enabled: true
      support:
        - en
```

**语言文件**：位于 `resources/i18n/` 目录下，命名格式为 `messages_{语言}.properties`。

**文件示例**（`messages_en.properties`）：

```properties
操作成功=success
操作失败=failure
暂未登录或token已经过期=Not logged in yet or the token has expired
没有相关权限=No relevant permissions
```

**使用方式**：

```java
// 自动多语言（异常 message 自动翻译）
throw new BaseException(RStatus.FAILED);

// 手动获取多语言文本
String message = I18nSupport.get("操作成功");
```

### 6. 多时区支持

`TimeZoneAspect` 切面处理多时区，将客户时区与服务器时区进行转换。

**原理**：入参将客户时区转为默认时区，出参将默认时区转为客户时区，保证业务逻辑和数据库存储的时区一致性。

**配置**：

```yaml
money:
  web:
    timezone:
      enabled: true
      default-time-zone: GMT+08:00
```

**注解使用**：

`@TZProcess` 标注在类上，类中方法参与时区转换：

```java
@TZProcess
@RestController
public class UserController {

    // @TZParam 标注入参字段：客户时区 → 默认时区
    @PostMapping("/create")
    public R<UserVO> create(@RequestBody @TZParam UserDTO dto) {
        userService.create(dto);
        return R.success(userService.getById(dto.getId()));
    }

    // @TZRep 标注出参：默认时区 → 客户时区
    @TZRep
    @GetMapping("/{id}")
    public UserVO get(@PathVariable Long id) {
        return userService.getVO(id);
    }
}
```

**支持的数据类型**：

| 类型 | 说明 |
|------|------|
| `LocalDateTime` | 日期时间类 |
| `String` | 日期时间格式的字符串 |
| `Bean` | 递归处理标注 `@TZParam` 的字段 |
| `List` / `Collection` | 集合类型（递归转换） |
| `Map` | 键包含 `time` 或 `date` 的值 |
| `PageVO` | 分页响应 VO |

**自定义转换器**：

```java
// 实现 TimeZoneConverter 接口
public class CustomTimeZoneConverter implements TimeZoneConverter {
    @Override
    public Object convert(Object o, String format, ZoneId formZoneId, ZoneId toZoneId) {
        // 自定义转换逻辑
        return o;
    }
}

// 通过 @TZParam / @TZRep 的 converter 属性指定
@TZParam(format = "yyyy-MM-dd HH:mm:ss", converter = CustomTimeZoneConverter.class)
```

### 7. 常用工具类

| 工具类 | 说明 |
|--------|------|
| `BeanMapUtil` | 对象属性复制（基于 Hutool） |
| `IpUtil` | IP 工具类（获取真实 IP、地理位置） |
| `WebUtil` | Web 工具类（获取 Request/Response、直接响应等） |
| `ValidationUtil` | 手动参数校验 |
| `JacksonUtil` | JSON 工具类 |

## 通用 DTO/VO

| 类名 | 说明 |
|------|------|
| `PageRequest` | 分页请求参数（page、size） |
| `SortRequest` | 排序请求参数（orderBy） |
| `PageQueryRequest` | 分页查询请求（分页 + 排序） |
| `PageVO<T>` | 分页响应 VO |
| `ValidGroup` | 校验分组（Create、Update、Delete） |

## 配置总览

```yaml
money:
  web:
    # 全局响应处理器
    response-handler: true

    # 全局异常处理器
    exception-handler: true

    # 全局请求日志切面
    web-log-aspect:
      enabled: true
      mode: ignore_get_result
      # 脱敏字段列表（为空时不脱敏）
      desensitize-fields:
        - password
        - token
        - secret
        - accessToken
        - refreshToken

    # 多语言
    i18n:
      enabled: true
      support:
        - en

    # 多时区
    timezone:
      enabled: true
      default-time-zone: GMT+08:00
```

## 核心类说明

| 类名 | 说明 |
|------|------|
| `CommonWebConfiguration` | 自动配置类，注册各种 Web 组件 |
| `R<T>` | 统一响应类 |
| `BaseException` | 基础业务异常 |
| `IStatus` | 状态码接口 |
| `RStatus` | 内置状态码枚举 |

## 相关链接

- [Spring Boot 官方文档](https://spring.io/projects/spring-boot)
- [Spring MVC 官方文档](https://docs.spring.io/spring-framework/docs/current/reference/html/web.html)
