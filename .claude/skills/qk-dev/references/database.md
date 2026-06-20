# 数据库表设计规范

## 命名

| 规则 | 示例 |
|------|------|
| 表名用 `模块前缀_实体名` | `sys_user`、`gms_goods`、`gms_goods_category` |
| 模块前缀按功能取简写（2-4 字母） | `sys` 系统、`gms` 商品管理、`pms` 采购管理 |
| 字段名用小写 + 下划线 | `create_time`、`supplier_id` |
| 主键统一用 `id` | |
| 外键用 `关联表_id` | `supplier_id`、`category_id` |
| 唯一索引 `uk_字段名` | `uk_name`、`uk_username` |
| 普通索引 `idx_字段名` | `idx_status` |

> 模块前缀在需求澄清阶段和用户确认。

## 基础字段

每张表必须包含，由 BaseEntity 自动填充：

```sql
`id`            BIGINT    NOT NULL  COMMENT '主键ID',
`create_by`     VARCHAR(50)     NULL  COMMENT '创建人',
`create_time`   DATETIME   NOT NULL  COMMENT '创建时间',
`update_by`     VARCHAR(50)     NULL  COMMENT '更新人',
`update_time`   DATETIME   NOT NULL  COMMENT '更新时间',
PRIMARY KEY (`id`)
```

## 类型选择

| 场景 | 类型 | 说明 |
|------|------|------|
| 主键/外键 | `BIGINT` | Snowflake ID，与 BaseEntity 一致 |
| 名称、标题、用户名 | `VARCHAR(50)` | 大多数短标识用 50 够用 |
| 手机号 | `VARCHAR(20)` | 不用数值类型 |
| 邮箱 | `VARCHAR(50)` | |
| 地址 | `VARCHAR(200)` | |
| 描述、备注 | `VARCHAR(500)` | 超过 500 用 `TEXT` |
| 长文本（正文、富文本） | `TEXT` | 不需要指定长度 |
| 状态/类型枚举 | `TINYINT` 或 `VARCHAR(20)` | 布尔用 `TINYINT(1)`，多状态用 `VARCHAR(20)` |
| 金额 | `DECIMAL(10,2)` | 不用浮点数 |
| 日期时间 | `DATETIME` | 不用 TIMESTAMP |

> VARCHAR 长度按业务语义选，不需要凑 2 的倍数。

## 索引

- 每个表必须有 `PRIMARY KEY (id)`
- 需要唯一约束的字段加 `UNIQUE KEY`
- 外键字段加普通索引 `INDEX`
- 高频搜索/筛选字段根据场景加 `INDEX`

## 多租户

新业务表默认加 `tenant_id`，除非设计文档明确是跨租户共享数据。

```sql
`tenant_id` BIGINT NULL COMMENT '租户ID'
```

值由框架 `TenantContextHolder` 自动注入，查询条件由 MP 多租户插件自动过滤。

## 注释

每张表加 `COMMENT`，每个字段加 `COMMENT`。
```sql
) COMMENT '供应商';
```

## N:N 关联

用中间表，命名：`关联表1_关联表2`，只放两个外键 + 联合主键：
```sql
CREATE TABLE `user_role` (
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `role_id` BIGINT NOT NULL COMMENT '角色ID',
    PRIMARY KEY (`user_id`, `role_id`)
) COMMENT '用户角色关联';
```
