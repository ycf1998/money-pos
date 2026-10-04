# 后端工具类与公共机制

> 本文只写「怎么写」的操作规则；类名、字段、配置等参考细节统一见 `doc/backend/money-common-web.md`，不在此重复。

## 分页相关

- `PageQueryDTO` 继承 `PageQueryRequest`，必须覆写 `sortKeyMap()` 作为排序白名单，用 `MoneyCommUtil.sortFieldMap("createTime", "updateTime", "name")` 快捷构造（自动转下划线）。
- Service 层用 `PageUtil.toPage(queryDTO)` 得到 MyBatis-Plus `Page`；结果用 `PageUtil.toPageVO(page, mapper)`（`Function` 自定义转换，或 `Supplier` + `BeanMapUtil`）转成 `PageVO` 返回。
- 排序 SQL 由 `queryDTO.getOrderBySql()` 生成（校验白名单后拼 `ORDER BY` 片段），自行拼入查询。

分页 DTO、`PageVO` 的类名与字段见 `doc/backend/money-common-web.md`；`PageUtil` 位于 money-app-api 的 `com.money.util`。

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

Controller 直接返回业务对象，框架自动包装为 `R<T>`，不要手动返回 `R`；需要绕过包装时加 `@IgnoreGlobalResponse`。Service 层需手动构造时用 `R.success(...)` / `R.fail(...)`。

响应格式与处理器细节见 `doc/backend/money-common-web.md`。

---

## 异常处理

业务异常用 `throw new BaseException(BizErrorStatus.XXX, "具体消息")`；无错误码时 `throw new BaseException("消息")`。业务错误码枚举实现 `IStatus`，定义见 `backend-crud.md`。

`BaseException`、`IStatus`、全局异常处理器见 `doc/backend/money-common-web.md`。

---

## 校验

DTO 字段按场景分组校验：`@NotNull(groups = ValidGroup.Update.class)`、`@NotBlank(groups = ValidGroup.Save.class)`。Controller 用 `@Validated(ValidGroup.Save.class)` / `@Validated(ValidGroup.Update.class)` 触发对应分组，不加则不做分组校验。非 Controller 层用 `ValidationUtil.validateThrow(obj, groups)`。

分组与校验工具定义见 `doc/backend/money-common-web.md`。

---

## 请求上下文

`WebRequestContextHolder.getContext()` 取请求 ID、语言、时区，对应 `getRequestId()` / `getLang()` / `getTimezone()`。

请求头常量与上下文细节见 `doc/backend/money-common-web.md`。

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

- `@TZProcess` 标在类上，开启该类的时区转换
- `@TZParam` 标注方法入参（或 Bean 字段），入参：客户时区 → 默认时区
- `@TZRep` 标注在类或方法上，出参：默认时区 → 客户时区

开关 `money.web.timezone.enabled`。切面、转换器与配置细节见 `doc/backend/money-common-web.md`。

---

## I18n（多语言）

中文消息本身即 key 与默认语言值。翻译放 `money-app-biz/src/main/resources/i18n/messages_{lang}.properties`，key 用中文原文、逐字符对齐。取用 `I18nSupport.get("中文消息")`。

配置与机制细节见 `doc/backend/money-common-web.md`。
