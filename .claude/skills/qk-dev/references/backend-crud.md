# 后端 CRUD 约定

每个 CRUD 功能涉及 8 种文件，以下列出每种文件的**项目特定规则**。

文件分两个模块存放：Entity/DTO/VO 在 `money-app-api`，Controller/Service/Mapper 在 `money-app-biz`。生成器会自动放到正确位置。

## Entity

路径：`money-app-api/src/main/java/com/money/entity/{Entity}.java`

- 继承 `BaseEntity`，自动获得 id/createBy/createTime/updateBy/updateTime
- 注解：`@Getter @Setter` + `@TableName("table_name")`
- 多租户表保留 `tenantId` 字段
- 字段用驼峰，MP 自动映射下划线

## Mapper

路径：`money-app-biz/src/main/java/com/money/mapper/{Entity}Mapper.java`

- 继承 `BaseMapper<Entity>`，空接口即可（标准 CRUD）
- 无需 `@Mapper`（`@MapperScan` 已配置）
- 自定义查询用 `@Select` / `@Update` 注解写在接口上，不需要 XML

## DTO

路径：`money-app-api/src/main/java/com/money/dto/{entity}/{Entity}DTO.java`

- `@Data`
- `id`：`@NotNull(groups = ValidGroup.Update.class)` — 仅编辑时校验
- 业务字段校验用 `ValidGroup.Save.class` — 覆盖新增和编辑
- message 写中文
- JSR303 覆盖不了的校验（如唯一性），不加注解，在 ServiceImpl 中处理
- 日期时间字段用 `LocalDateTime` / `LocalDate`，加 `@TZParam` 注解（入参时区转换）

## PageQueryDTO

路径：`money-app-api/src/main/java/com/money/dto/{entity}/{Entity}PageQueryDTO.java`

- `@Data @EqualsAndHashCode(callSuper = true)` 继承 `PageQueryRequest`
- 只放搜索条件字段
- 必须覆写 `sortKeyMap()`：
  ```java
  return MoneyCommUtil.sortFieldMap("createTime", "updateTime");
  ```

## VO

路径：`money-app-api/src/main/java/com/money/dto/{entity}/{Entity}VO.java`

- `@Data`
- 按设计文档响应字段来，不一定是 Entity 的全量字段
- 禁止包含密码等敏感字段
- 日期时间字段用 `LocalDateTime` / `LocalDate`，加 `@TZRep` 注解（出参时区转换）

## Service

路径：`money-app-biz/src/main/java/com/money/service/{Entity}Service.java`

继承 `IService<Entity>`，标准 4 方法：
```java
PageVO<XxxVO> list(XxxPageQueryDTO);
Long add(XxxDTO);
void update(XxxDTO);
void delete(Set<Long> ids);
```

## ServiceImpl

路径：`money-app-biz/src/main/java/com/money/service/impl/{Entity}ServiceImpl.java`

- 注解：`@Service @RequiredArgsConstructor @Transactional(rollbackFor = Exception.class)`
- 继承 `ServiceImpl<Mapper, Entity>` 实现 Service

**list**：
```java
return PageUtil.toPageVO(
    this.lambdaQuery()
        .like(StrUtil.isNotBlank(val), Entity::getField, val)
        .last(queryDTO.getOrderBySql())
        .page(PageUtil.toPage(queryDTO)),
    entity -> BeanMapUtil.to(entity, VO::new)
);
```

**add**：
```java
// 1. 唯一性检查
this.lambdaQuery().eq(Entity::getName, dto.getName()).exists();
// 2. DTO → Entity
Entity entity = BeanMapUtil.to(dto, Entity::new);
// 3. 保存
this.save(entity);
return entity.getId();
```

**update**：
```java
// 1. 取现有记录（注意：用 entity 变量，不是 Entity::new）
Entity entity = this.getById(dto.getId());
// 2. 业务校验（唯一性排除自身）
this.lambdaQuery().eq(...).ne(Entity::getId, dto.getId()).exists();
// 3. 复制并更新
BeanMapUtil.to(dto, entity);
this.updateById(entity);
```

**delete**：
```java
// 1. 关联检查（检查被其他表引用的记录，跳过或抛异常）
// 2. 删除
this.removeBatchByIds(ids);
```

**异常**：`throw new BaseException(BizErrorStatus.XXX, "具体消息")`

## Controller

路径：`money-app-biz/src/main/java/com/money/controller/{Entity}Controller.java`

- 注解：`@RequiredArgsConstructor @RestController @RequestMapping("/path")`
- 涉及日期时间字段展示的，加 `@TZProcess`（客户端时区自动转换）
- 4 端点无需改动，生成器已生成

## 错误码

路径：`money-app-api/src/main/java/com/money/constant/BizErrorStatus.java`（已创建，直接扩）

- 6 位编号：1 类型 + 2 模块 + 3 错误码
- 通用错误直接从已有枚举取值：
  - `DATA_NOT_FOUND(100000)` + 具体消息，如 `"供应商不存在"`
  - `DATA_ALREADY_EXIST(100001)` + 具体消息，如 `"供应商名称已存在"`
  - `STATUS_NOT_ALLOWED(200000)` — 状态不允许操作
  - `REF_EXISTS(200001)` — 存在关联数据
- 模块特有错误按号段增加（如商品模块 1/2 01000-01999）
- 禁止扩展 `SysErrorStatus`（那是框架系统模块的）

## 常用代码

| 场景 | 代码 |
|------|------|
| 等值条件 | `.eq(val != null, Entity::getField, val)` |
| 模糊搜索 | `.like(StrUtil.isNotBlank(str), Entity::getField, str)` |
| IN 条件 | `.in(CollUtil.isNotEmpty(list), Entity::getField, list)` |
| 获取排序 SQL | `queryDTO.getOrderBySql()` → 拼入 `.last(orderBy)` |
| 唯一性（新增） | `.eq(Entity::getName, name).exists()` |
| 唯一性（编辑排除自身） | `.eq(...).ne(Entity::getId, id).exists()` |
| DTO→新实体 | `BeanMapUtil.to(dto, Entity::new)` |
| DTO→已有实体 | `BeanMapUtil.to(dto, existingEntity)` |
| 分页+转换 | `PageUtil.toPageVO(page, e -> BeanMapUtil.to(e, VO::new))` |
| 当前用户 | `SecurityGuard.getRbacUser()` 或 Controller 参数 `@CurrentUser Long userId` |
