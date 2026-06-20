# 后端工具类与公共机制

## 分页相关

### PageRequest / PageQueryRequest

包：`com.money.web.dto`

- `PageRequest` — 基类，字段 `page`（默认 1）、`size`（默认 10）
- `PageQueryRequest` — 继承 `PageRequest` + `ISortRequest`，加入 `orderBy` 字段

### ISortRequest

包：`com.money.web.dto`

分页排序接口。关键方法：
- `sortKeyMap()` — 返回 `Map<String, String>`，key 为前端传的排序参数，value 为数据库列名（under_score）
- `getOrderBySql()` — 校验 `orderBy` 中的字段是否在 `sortKeyMap()` 白名单中，生成 `ORDER BY col ASC, col2 DESC` 片段
- `getSortOptions()` — 解析后的排序选项集合

### MoneyCommUtil.sortFieldMap()

包：`com.money.web.util`

快捷构造 sortKeyMap：
```java
return MoneyCommUtil.sortFieldMap("createTime", "updateTime", "name");
// 自动转 createTime→create_time, updateTime→update_time
```

### PageVO

包：`com.money.web.vo`

分页响应体：`current`、`size`、`total`、`records`、`pages`（计算属性）。

### PageUtil

包：`com.money.util`（在 money-app-api 模块）

核心方法：
```java
// PageRequest → MyBatis-Plus Page
PageUtil.toPage(PageRequest request)

// IPage → PageVO（无转换）
PageUtil.toPageVO(IPage<T> page)

// IPage → PageVO（通过 Supplier 转换，内部用 BeanMapUtil）
PageUtil.toPageVO(IPage<T> page, Supplier<R> supplier)

// IPage → PageVO（通过 Function 自定义转换）
PageUtil.toPageVO(IPage<T> page, Function<T, R> mapper)
```

---

## Bean 转换

### BeanMapUtil

包：`com.money.web.util`

```java
// 单个对象转换：DTO → Entity（新建）
Entity entity = BeanMapUtil.to(dto, Entity::new)

// 单个对象转换：DTO → 已存在 Entity（覆盖属性）
BeanMapUtil.to(dto, existingEntity)

// 集合转换
List<VO> vos = BeanMapUtil.to(entities, VO::new)
```

内部使用 Hutool `BeanUtil.copyProperties`。

---

## 全局响应

### R\<T\>

包：`com.money.web.response`

统一响应体。框架通过 `DefaultResponseHandler` 自动包装 Controller 返回值，**无需手动返回 R**。

```java
// 自动包装：Controller 返回 PageVO → 自动变成 R.success(pageVO)
// 自动包装：Controller 返回 void   → 自动变成 R.success()
```

常用方法（仅 Service 层手动构造时用）：
- `R.success()` / `R.success(data)`
- `R.fail()` / `R.fail(message)` / `R.fail(code, message)`

### DefaultResponseHandler

包：`com.money.web.response`

`@RestControllerAdvice`，拦截所有 Controller 返回值自动包装。
- 跳过已返回 `R` 的
- 跳过标注 `@IgnoreGlobalResponse` 的
- 开关配置：`money.web.response-handler=true`

---

## 异常处理

### BaseException

包：`com.money.web.exception`

```java
// 抛出业务异常
throw new BaseException(BizErrorStatus.DATA_ALREADY_EXIST, "供应商名称已存在");
throw new BaseException(BizErrorStatus.DATA_NOT_FOUND, "供应商不存在");
throw new BaseException("自定义简单消息");
```

构造函数：
- `BaseException(String message)` — 无错误码
- `BaseException(IStatus status)` — 从枚举取 code + message
- `BaseException(IStatus status, String detail)` — code 用枚举，message 叠加详情
- `BaseException(int code, String message)` — 自定义 code

### IStatus

包：`com.money.web.response`

接口，定义 `int getCode()` 和 `String getMessage()`。业务错误枚举实现此接口。

### DefaultExceptionHandler

包：`com.money.web.exception`

全局异常处理：
- `BindException` / `MethodArgumentNotValidException` → 400 + 校验消息
- `ConstraintViolationException` → 400 + 校验消息
- `BaseException` → 对应错误码 + 消息
- `Exception` → 500

---

## 校验

### ValidGroup

包：`com.money.web.dto`

```java
public class ValidGroup {
    public interface Save {}    // 新增场景
    public interface Update {}  // 编辑场景
}
```

用法：
```java
// DTO 中
@NotNull(groups = ValidGroup.Update.class) private Long id;
@NotBlank(groups = ValidGroup.Save.class) private String name;

// Controller 中
@Validated(ValidGroup.Save.class)   // 新增
@Validated(ValidGroup.Update.class) // 编辑
@Validated                          // 不触发分组校验
```

### ValidationUtil

包：`com.money.web.util`

编程式校验（非 Controller 层）：
```java
ValidationUtil.validateThrow(obj, groups);  // 校验失败抛异常
ConstraintViolation violation = ValidationUtil.validate(obj, groups);  // 返回第一个失败项
```

---

## 请求上下文

### WebRequestContextHolder

包：`com.money.web.context`

ThreadLocal 持有请求上下文：
```java
WebRequestContext context = WebRequestContextHolder.getContext();
String requestId = context.getRequestId();
String lang = context.getLang();
ZoneId timezone = context.getTimezone();
```

### WebRequestConstant

包：`com.money.web.constant`

请求头常量：
- `HEADER_REQUEST_ID = "X-qk-request"`
- `HEADER_LANG = "X-qk-lang"`
- `HEADER_TIMEZONE = "X-qk-timezone"`

---

## 当前用户

### SecurityGuard

包：`com.money.security`

```java
RbacUser user = SecurityGuard.getRbacUser();
Long userId = user.getUserId();
String username = user.getUsername();
```

### @CurrentUser

Controller 参数注解：
```java
public void add(@CurrentUser Long userId, @RequestBody DTO dto) {}
public void update(@CurrentUser String username, @RequestBody DTO dto) {}
public void profile(@CurrentUser RbacUser user) {}
```

---

## 多租户

### TenantContextHolder

包：`com.money.context`（在 qk-money-tenant 模块）

```java
Long tenantId = TenantContextHolder.getTenant();
```

实体中 `tenantId` 字段由 MyBatis-Plus 多租户插件自动注入条件，无需手动处理。

---

## 时区

### 注解

- `@TZProcess` — 类级别，启用时区自动转换
- `@TZParam` — 标记需要时区转换的入参字段
- `@TZRep` — 标记需要时区转换的响应字段

### TimeZoneAspect

AOP 自动处理带 `@TZProcess` 注解的 Controller 方法，根据客户端时区转换日期时间字段。

配置：`money.web.timezone.enabled`、`money.web.timezone.defaultTimeZone`（默认 GMT+08:00）

### TimeZoneUtil

包：`com.money.web.util`

手动时区转换（一般用注解处理，不直接调用）。

---

## I18n（多语言）

### 机制

中文消息本身作为 key 也是默认语言的值。启用多语言后，框架根据客户端 `X-qk-lang` 请求头自动查找对应语言文件中的翻译。

### 配置

`application.yml`：

```yaml
money:
  web:
    i18n:
      enabled: true
      support:
        - zh
        - en
```

`basename` 默认为 `i18n/messages`，无需修改。

### 语言文件

放在 `money-app-biz/src/main/resources/i18n/` 目录：

```
resources/i18n/
├── messages_zh.properties   # 中文（key 本身即中文，可不建）
└── messages_en.properties   # 英文
```

文件格式（key = value）：
```properties
# messages_en.properties
供应商名称已存在=Supplier name already exists
数据不存在=Data not found
```

### I18nSupport

包：`com.money.web.i18n`

```java
String msg = I18nSupport.get("供应商名称已存在");
String msg = I18nSupport.get("数据不存在");
```

语言从 `WebRequestContextHolder.getContext().getLang()` 获取。
未启用或 bean 不存在时，直接返回 key 本身（中文）。
