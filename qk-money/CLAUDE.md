# 后端

Spring Boot 3 多模块工程。下列命令均在本目录执行。

## 模块

| 模块 | 内容 |
|---|---|
| `qk-money-common` | 通用能力：`money-common-{web,mybatis,cache,oss,mail,schedule,swagger}` |
| `qk-money-security` | Spring Security + JWT + RBAC |
| `qk-money-tenant` | 多租户 |
| `qk-money-app` | 应用层，见下 |
| `qk-money-parent` | BOM，管理依赖版本 |

`qk-money-app` 分三层：

| 模块 | 内容 |
|---|---|
| `money-app-api` | Entity、DTO、VO |
| `money-app-biz` | Controller、Service、Mapper；启动类 `com.money.QkMoneyApplication` |
| `money-app-system` | 框架内置的系统管理：用户、角色、权限、租户、字典 |

**业务代码写入 `money-app-api` 与 `money-app-biz`，与框架代码并存，不新建模块。**

## 命令

| 命令 | 说明 |
|---|---|
| `mvn clean install -DskipTests` | 编译 |
| `mvn spring-boot:run -pl qk-money-app/money-app-biz -Dspring-boot.run.profiles=dev` | 启动 |
| `mvn test` | 测试 |

版本号集中定义于根 `pom.xml` 的 `<revision>`，由各模块继承，**命令行无须传 `-Drevision`**。

## 测试

基类 `ControllerTestBase`（配置 `application-test.yml`）负责创建与清理测试账号；用例以 `@DisplayName` 描述场景。

## 文档

| 内容 | 位置 |
|---|---|
| 某个框架模块的能力与配置 | `../doc/backend/<模块名>.md` —— `money-common-*`、`qk-money-security`、`qk-money-tenant`、`qk-money-parent` 各一篇 |
| 分页、排序、异常、i18n、时区的写法 | `../.claude/skills/qk-dev/references/utilities.md` |
| 建表与字段规范 | `../.claude/skills/qk-dev/references/database.md` |
| Spring Boot 3 / 4 迁移参考（通用资料，与本仓库版本无关） | `../doc/notes/` |

后端各文件的写法规范见 `../.claude/skills/qk-dev/references/backend-crud.md`。

代码生成器 `MybatisPlusGenerator` 位于 `money-common-mybatis`，交互式生成 Controller / Service / Mapper / Entity / DTO / VO。
