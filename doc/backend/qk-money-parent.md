# qk-money-parent - BOM 依赖清单

## 概述

`qk-money-parent` 作为整个工程的父模块，负责统一管理项目的第三方依赖版本。其他模块都直接或间接继承该模块，确保依赖版本一致性。

## 工程结构

```
qk-money/
├── pom.xml                    # 根 POM：声明项目版本和模块
└── qk-money-parent/
    └── pom.xml                # BOM POM：管理第三方依赖版本
```

> 项目模块版本声明在根目录 `pom.xml`，第三方依赖版本声明在 `qk-money-parent/pom.xml`

## 依赖管理方式

### 1. 主 POM（根目录 pom.xml）

声明项目模块版本，子模块引用时无需声明版本号。

### 2. BOM POM（qk-money-parent/pom.xml）

统一管理第三方依赖版本，通过 `<dependencyManagement>` 和 `<properties>` 进行版本控制。

主要管理以下类型的依赖：
- 框架依赖（Spring Boot、MyBatis-Plus 等）
- 工具库依赖（Hutool、Lombok 等）
- 安全认证（JJWT 等）
- 数据存储（MySQL 驱动等）
- 任务调度（XXL-JOB 等）
- 对象存储（七牛云 SDK 等）
- 接口文档（SpringDoc OpenAPI 等）
- 其他第三方库

### 3. 子模块使用

子模块引入依赖时**无需声明版本号**，由父 POM 统一管理。

## 公共依赖

父工程引入了项目必要的公共依赖，所有子模块自动继承：

- **Lombok**：通过注解简化代码
- **Hutool**：Java 工具类库，提供常用工具方法

## 版本属性

通过 `<properties>` 标签集中管理版本号，修改时只需在 BOM POM 中调整一处即可。

## 相关链接

- [Lombok 官方文档](https://www.projectlombok.org/features/all)
- [Hutool 官方文档](https://www.hutool.cn/docs/)
- [Maven BOM 最佳实践](https://maven.apache.org/guides/introduction/introduction-to-dependency-mechanism.html)
