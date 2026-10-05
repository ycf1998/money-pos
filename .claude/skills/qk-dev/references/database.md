# 数据库表设计规范

## 命名

| 对象 | 规则 | 示例 |
|---|---|---|
| 表 | `模块前缀_实体名` | `sys_user`、`gms_goods`、`gms_goods_category` |
| 模块前缀 | 功能简写，2-4 字母 | `sys` 系统、`gms` 商品、`pms` 采购 |
| 字段 | 小写 + 下划线 | `create_time`、`supplier_id` |
| 主键 | 固定 `id` | |
| 外键 | `关联表_id` | `supplier_id`、`category_id` |
| 唯一索引 | `uk_描述` | `uk_tenant_username` |
| 普通索引 | `idx_描述` | `idx_tenant_parent_id` |

> 模块前缀在需求澄清阶段和用户确认。

## 字符集

库、表、字段一律 `utf8mb4` + `utf8mb4_0900_ai_ci`。建库时显式声明，表级和字段级不再重复写 `CHARACTER SET` / `COLLATE`。

**同一字段与其他表比较时（JOIN、按名字关联）排序规则必须一致**，否则走不了索引甚至直接报 `Illegal mix of collations`。这就是不在字段上单独指定排序规则的原因。

## 类型与长度

| 场景 | 类型 | 说明 |
|---|---|---|
| 主键、外键 | `bigint` | 与 Java `Long` 一一对应，**不用 `UNSIGNED`** |
| 名称、编码、标题 | `varchar(50)` | 大多数短标识 50 够用 |
| 手机号 | `varchar(20)` | 不用数值类型 |
| 邮箱 | `varchar(100)` | |
| URL、路径 | `varchar(255)` | |
| 描述、备注 | `varchar(500)` | 超过 500 用 `TEXT` |
| 长文本（正文、富文本） | `TEXT` | 不指定长度 |
| 布尔 | `tinyint(1)` | 0-否；1-是 |
| 枚举 | `varchar(20)` | 取值在代码里定义，不在库里加约束 |
| 金额 | `decimal(10,2)` | 不用浮点数 |
| 时间 | `datetime` | 不用 `TIMESTAMP` |

补充约定：

- **字符串一律 `NOT NULL DEFAULT ''`**，避免 NULL 和空串两种"空"混用。
- **时间字段不写默认值**，含 `DEFAULT CURRENT_TIMESTAMP` 与 `ON UPDATE CURRENT_TIMESTAMP` —— 时间由 `BaseEntity` 填充，库里再设默认值会出现两个来源互相覆盖。
- 语义上"从未发生"的时间允许为空（如 `last_login_time`），用 `datetime NULL`。

## 基础字段

业务表必须包含下列字段，由 `BaseEntity` 自动填充，填充时机见 `doc/backend/money-common-mybatis.md`：

```sql
`create_by`   varchar(50) NOT NULL COMMENT '创建人',
`create_time` datetime    NOT NULL COMMENT '创建时间',
`update_by`   varchar(50) NOT NULL COMMENT '更新人',
`update_time` datetime    NOT NULL COMMENT '更新时间',
PRIMARY KEY (`id`)
```

`create_by` / `update_by` 存用户名（本系统用户名不可修改）。

两类例外：

- **N:N 关联表** —— 只有主键和两个外键，不带基础字段。
- **导入的参照表**（如行政区划 `provinces`）—— 数据由外部整批导入、无业务身份，无主键与基础字段，只统一字符集并给查询列加索引。

## 多租户

按租户隔离的业务表加：

```sql
`tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID'
```

全局共享的表（如字典）**不要加**：多租户开关打开后，插件会给每张未忽略的表拼 `tenant_id` 条件，表里没有这列会直接报 `Unknown column`。新增或去掉 `tenant_id` 时，同步维护 `money.tenant.ignore-table` 配置。

值由 MP 多租户插件自动注入和过滤。**自定义 SQL 必须给表起别名**（XML、`@Select` 同），否则条件拼不上；多表查询每个表都要加别名：

```sql
-- 正确
SELECT u.* FROM sys_user u LEFT JOIN sys_user_role ur ON u.id = ur.user_id
-- 错误
SELECT * FROM sys_user WHERE status = 1
```

## 索引

- 每张表必须有 `PRIMARY KEY (id)`
- 唯一约束用 `UNIQUE KEY`，普通索引用 `KEY`
- 外键列、高频筛选列加索引
- 小表、低基数列不建索引

**带 `tenant_id` 的表，每个索引都以 `tenant_id` 为最左列** —— 租户条件出现在该表的每一次查询里，放最左才能被走到，也不必再单独建 `idx_tenant_id`。

```sql
UNIQUE KEY `uk_tenant_username` (`tenant_id`, `username`)
```

**可留空的列不要建唯一索引**：空串或 NULL 会互相冲突，反而不允许出现第二个。这类列的唯一性在服务端校验。

## 逻辑删除

需要逻辑删除的表加：

```sql
`deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除；1-已删除'
```

是否使用逻辑删除由业务选择，不是每张表都必须有。

## 注释

每张表加 `COMMENT`，每个字段加 `COMMENT`。

```sql
) COMMENT '供应商';
```

## N:N 关联

中间表命名为 `关联表1_关联表2`，只放主键、两个外键和 `tenant_id`，用联合唯一索引防重复：

```sql
CREATE TABLE `sys_user_role` (
    `id`        bigint NOT NULL             COMMENT '主键ID',
    `user_id`   bigint NOT NULL             COMMENT '用户ID',
    `role_id`   bigint NOT NULL             COMMENT '角色ID',
    `tenant_id` bigint NOT NULL DEFAULT 0   COMMENT '租户ID',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_role` (`tenant_id`, `user_id`, `role_id`)
) COMMENT '用户角色关联表';
```
