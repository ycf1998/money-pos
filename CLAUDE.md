# CLAUDE.md

## 项目

QK-Money 后台管理系统 — 前后端分离的快速开发框架。

| 目录 | 技术栈 |
|------|--------|
| `qk-money/` | Java 17、Spring Boot 3、MyBatis-Plus 3.5、Maven |
| `qk-money-ui/` | Vue 3、Vite、Element Plus、TailwindCSS、Pinia |

## 常用命令

```bash
# 后端编译（-Drevision 必须）
cd qk-money && mvn clean install -Drevision=1.0.0 -DskipTests

# 后端启动
cd qk-money && mvn spring-boot:run -pl qk-money-app/money-app-biz -Dspring-boot.run.profiles=dev

# 运行测试
cd qk-money && mvn test -Drevision=1.0.0

# 前端
cd qk-money-ui && npm install && npm run dev
```

> `-Drevision=1.0.0`：项目使用 CI Friendly Version，所有 Maven 命令必须带此参数。

## 开发方式

| 任务 | 方式 |
|------|------|
| 开发 CRUD 管理功能 | `/qk-dev` skill（需求→设计→编码） |
| 其他开发任务 | 直接对话 |

设计文档输出到 `.personal/specs/{name}/design.md`。

## 模块结构

```
qk-money/
├── qk-money-common/          # 通用模块（web、mybatis、cache、oss、mail、schedule）
├── qk-money-security/        # Spring Security + JWT + RBAC
├── qk-money-tenant/          # 多租户
└── qk-money-app/
    ├── money-app-api/        # Entity、DTO、VO
    ├── money-app-biz/        # Controller、Service、Mapper、启动类
    └── money-app-system/     # 系统管理（用户、角色、权限、租户、字典）
```

## 测试

- 基类：`ControllerTestBase`，配置：`application-test.yml`
- 测试账号由基类自动创建和清理
- 使用 `@DisplayName` 描述测试场景
