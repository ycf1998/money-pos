# Spring Boot 3.0 升级指南

> **通用参考**：本文为 Spring Boot 官方迁移指南的翻译，与本仓库（QK-Money）的版本无关。

> 原文：Spring Boot 3.0 Migration Guide
> 翻译版本：1.0
> 最后更新：2026-03-21

---

## 目录

1. [开始之前](#开始之前)
2. [系统要求](#系统要求)
3. [依赖项升级](#依赖项升级)
4. [核心功能升级](#核心功能升级)
5. [Web 功能升级](#web-功能升级)
6. [数据功能升级](#数据功能升级)
7. [安全功能升级](#安全功能升级)
8. [消息功能升级](#消息功能升级)
9. [Actuator 升级](#actuator-升级)
10. [测试功能升级](#测试功能升级)
11. [构建工具升级](#构建工具升级)

---

## 开始之前

### 升级到最新的 2.7.x 版本

在开始升级之前，请确保升级到可用的最新 2.7.x 版本。这将确保您基于该系列的最新依赖进行构建。

请花时间检查您可能调用的任何已弃用方法，因为这些方法将在 Spring Boot 3.0 中被移除。

### 检查依赖项

迁移到 Spring Boot 3 将升级许多依赖项，可能需要您进行一些工作。您可以对比 2.7.x 和 3.0.x 的依赖管理，评估您的项目受到的影响。

您可能还使用了 Spring Boot 未管理的依赖项（例如 Spring Cloud）。由于您的项目为这些依赖定义了显式版本，请在升级前确定兼容的版本。

### Spring Security 升级准备

Spring Boot 3.0 使用 Spring Security 6.0。Spring Security 团队发布了 Spring Security 5.8 以简化升级到 Spring Security 6.0 的过程。在升级到 Spring Boot 3.0 之前，建议先将 Spring Boot 2.7 应用程序升级到 Spring Security 5.8。

### Dispatcher 类型

在 Servlet 应用程序中，Spring Security 6.0 将授权应用于每种调度类型。为了与此保持一致，Spring Boot 现在配置 Spring Security 过滤器以针对每种调度类型调用。可以使用 `spring.security.filter.dispatcher-types` 属性配置这些类型。

---

## 系统要求

### Java 版本

Spring Boot 3.0 需要 **Java 17 或更高版本**。Java 8 不再被支持。同时需要 Spring Framework 6.0。

### Jakarta EE

每当 Spring Boot 依赖于 Jakarta EE 规范时，Spring Boot 3.0 已升级到 Jakarta EE 10 中包含的版本。例如，Spring Boot 3.0 使用 Servlet 6.0 和 JPA 3.1 规范。

**重要变更**：Jakarta EE 现在使用 `jakarta` 包而不是 `javax`。更新依赖后，您可能需要更新项目中的 import 语句。

迁移工具：
- [OpenRewrite recipes](https://docs.openrewrite.org/)
- [Spring Boot Migrator 项目](https://github.com/spring-projects-experimental/spring-boot-migrator)
- [IntelliJ IDEA 迁移支持](https://www.jetbrains.com/help/idea/spring-boot.html)

---

## 依赖项升级

### Spring Framework 6.0

Spring Boot 3.0 构建并需要 Spring Framework 6.0。建议继续升级前查看 [Spring Framework 6.0 升级指南](https://github.com/spring-projects/spring-framework/wiki/Upgrading-to-Spring-Framework-6.x)。

### 配置属性迁移

Spring Boot 3.0 中，一些配置属性被重命名/移除，开发者需要相应地更新 `application.properties`/`application.yml`。Spring Boot 提供了 `spring-boot-properties-migrator` 模块来帮助您：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-properties-migrator</artifactId>
    <scope>runtime</scope>
</dependency>
```

> **注意**：迁移完成后，请务必从项目依赖中移除此模块。

### 移除的依赖管理

以下依赖的支持已在 Spring Boot 3.0 中移除：

- Apache ActiveMQ
- Atomikos
- EhCache 2
- Hazelcast 3
- Apache Solr（其基于 Jetty 的客户端与 Jetty 11 不兼容）

### JSON-B 变更

Apache Johnzon 的依赖管理已被移除，取而代之的是 Eclipse Yasson。如果需要与 Jakarta EE 10 兼容的 Apache Johnzon 版本，您需要在依赖声明中指定版本。

---

## 核心功能升级

### 日志日期格式

Logback 和 Log4j2 的日志消息日期时间组件的默认格式已更改，以与 ISO-8601 标准保持一致。新的默认格式 `yyyy-MM-dd'T'HH:mm:ss.SSSXXX` 使用 `T` 分隔日期和时间，并在末尾添加时区偏移。

可以使用 `LOG_DATEFORMAT_PATTERN` 环境变量或 `logging.pattern.dateformat` 属性恢复之前的默认值 `yyyy-MM-dd HH:mm:ss.SSS`。

### @ConstructorBinding 变更

`@ConstructorBinding` 不再需要在类型级别上使用于 `@ConfigurationProperties` 类，应该移除。当类或记录有多个构造函数时，它仍可用于构造函数以指示应使用哪一个进行属性绑定。

### YamlJsonParser 移除

`YamlJsonParser` 已被移除，因为 SnakeYAML 的 JSON 解析与其他解析器实现不一致。

### Auto-configuration 文件变更

Spring Boot 2.7 引入了新的 `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` 文件来注册自动配置，同时保持与 `spring.factories` 注册的向后兼容性。

在此版本中，已移除对使用 `org.springframework.boot.autoconfigure.EnableAutoConfiguration` 键在 `spring.factories` 中注册自动配置的支持。

### 图像 Banner 支持移除

对基于图像的应用程序 banner 的支持已被移除。`banner.gif`、`banner.jpg` 和 `banner.png` 文件现在被忽略，应替换为基于文本的 `banner.txt` 文件。

---

## Web 功能升级

### Spring MVC 和 WebFlux URL 匹配变更

从 Spring Framework 6.0 开始，尾部斜杠匹配配置选项已被弃用，其默认值设置为 false。

以前，以下控制器会匹配 `"GET /some/greeting"` 和 `"GET /some/greeting/"`：

```java
@RestController
public class MyController {

    @GetMapping("/some/greeting")
    public String greeting() {
        return "Hello";
    }
}
```

现在 `"GET /some/greeting/"` 默认不再匹配，将导致 HTTP 404 错误。

**解决方案**：

```java
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setUseTrailingSlashMatch(true);
    }
}
```

### server.max-http-header-size 变更

`server.max-http-header-size` 之前在不同嵌入式 Web 服务器之间处理不一致。它已被弃用，取而代之的是 `server.max-http-request-header-size`。

### Jetty 支持

Jetty 尚不支持 Servlet 6.0。要在 Spring Boot 3.0 中使用 Jetty，需要将 Servlet API 降级到 5.0：

```xml
<jakarta-servlet.version>5.0.0</jakarta-servlet.version>
```

### Apache HttpClient 变更

Spring Framework 6.0 中已移除对 Apache HttpClient 的支持，立即替换为 `org.apache.httpcomponents.client5:httpclient5`（注意：此依赖具有不同的 groupId）。

### Spring Session 配置变更

Spring Session Data Redis 的属性已重命名：
- 以前以 `spring.session.redis` 开头的属性现在以 `spring.session.data.redis` 开头
- 以前以 `spring.session.mongodb` 开头的属性现在以 `spring.session.data.mongodb` 开头

### HttpMessageConverters 弃用

Spring Boot 的 `HttpMessageConverters` 已被弃用。如果您的应用程序声明了自定义的 `HttpMessageConverters` bean，这仍然受支持，但该类型本身已弃用。

---

## 数据功能升级

### 数据属性变更

`spring.data` 前缀已为 Spring Data 保留，该前缀下的任何属性都意味着类路径上需要 Spring Data。

- **Cassandra**: `spring.data.cassandra.*` → `spring.cassandra.*`
- **Redis**: `spring.redis.*` → `spring.data.redis.*`

### Hibernate 6.1

Spring Boot 3.0 默认使用 Hibernate 6.1。请参阅 [Hibernate 6.0](https://docs.jboss.org/hibernate/orm/6.0/migration-guide/migration-guide.html) 和 [6.1](https://docs.jboss.org/hibernate/orm/6.1/migration-guide/migration-guide.html) 迁移指南。

依赖管理和 `spring-boot-starter-data-jpa` starter 已更新为使用新的 `org.hibernate.orm` group ID。

`spring.jpa.hibernate.use-new-id-generator-mappings` 配置属性已被移除，因为 Hibernate 不再支持切换回旧的 ID 生成器映射。

### Flyway 升级

Spring Boot 3.0 默认使用 Flyway 9.0。请参阅 [Flyway 发行说明](https://documentation.red-gate.com/flyway/release-notes-and-older-versions/release-notes-for-flyway-engine)。

### Liquibase 升级

Spring Boot 3.0 默认使用 Liquibase 4.17.x。

### MySQL JDBC 驱动坐标变更

MySQL JDBC 驱动的坐标已从 `mysql:mysql-connector-java` 更改为 `com.mysql:mysql-connector-j`。

### R2DBC 1.0

Spring Boot 3.0 默认使用 R2DBC 1.0。R2DBC 不再发布 BOM（物料清单），因此 `r2dbc-bom.version` 属性不再可用。

### Elasticsearch 客户端变更

已弃用的低级 Elasticsearch `RestClient` 的支持已被替换为新的 Java 客户端的自动配置。

### 嵌入式 MongoDB

Flapdoodle 嵌入式 MongoDB 的自动配置和依赖管理已被移除。如果您在测试中使用嵌入式 MongoDB，请使用 Flapdoodle 项目提供的自动配置库，或修改测试以使用 [Testcontainers](https://www.testcontainers.org/) 项目。

---

## 安全功能升级

### Spring Security 6.0

Spring Boot 3.0 已升级到 Spring Security 6.0。请参阅 [Spring Security 6.0 迁移指南](https://docs.spring.io/spring-security/reference/5.8/migration/index.html)。

主要变更：
- 默认启用 CSRF
- 授权现在应用于每种调度类型
- 部分配置方法可能已更改

### SAML2 配置变更

`spring.security.saml2.relyingparty.registration.{id}.identity-provider` 下的属性支持已被移除。使用 `spring.security.saml2.relyingparty.registration.{id}.asserting-party` 下的新属性作为替代。

### ReactiveUserDetailsService

在存在 `AuthenticationManagerResolver` 时，不再自动配置 `ReactiveUserDetailsService`。

---

## 消息功能升级

### Spring AMQP 升级

Spring Boot 3.0 构建于 Spring AMQP 3.0 之上，该版本需要 `com.rabbitmq:amqp-client:5.x`。

### Spring for Apache Kafka 升级

Spring Boot 3.0 构建于 Spring for Apache Kafka 3.0 之上。

- `KafkaStreamsAnnotationDrivenConfiguration` 已重命名为 `StreamsBuilderFactoryBeanConfiguration`
- `KafkaStreamsAnnotationDrivenConfiguration` 中的 bean 名称已从 `kafkaStreamsAnnotationDrivenConfiguration` 更改为 `streamsBuilderFactoryBeanConfiguration`

### Spring Kafka 重试功能

Spring Kafka 已将其重试功能从 Spring Retry 迁移到 Spring Framework。

### Spring AMQP 重试功能

Spring AMQP 已将其重试功能从 Spring Retry 迁移到 Spring Framework。

---

## Actuator 升级

### JMX 端点暴露

默认情况下，现在只有 health 端点通过 JMX 暴露，以与默认的 Web 端点暴露保持一致。这可以通过配置 `management.endpoints.jmx.exposure.include` 和 `management.endpoints.jmx.exposure.exclude` 属性来更改。

### httptrace 端点重命名

`httptrace` 端点已重命名为 `httpexchanges`，以减少与 Micrometer Tracing 引入的混淆。

相关基础设施类也已重命名：`HttpTraceRepository` 现在命名为 `HttpExchangeRepository`，位于 `org.springframework.boot.actuate.web.exchanges` 包中。

### Actuator JSON 变更

Spring Boot 自带的 Actuator 端点响应现在使用隔离的 `ObjectMapper` 实例以确保结果一致。如果要恢复旧行为并使用应用程序的 `ObjectMapper`，可以设置 `management.endpoints.jackson.isolated-object-mapper` 为 `false`。

### Actuator 端点清理

由于 `/env` 和 `/configprops` 端点可能包含敏感值，所有值现在默认都被掩码。以前仅对被视为敏感的键才这样做。

可以使用以下属性配置是否显示未清理的值：
- `management.endpoint.env.show-values`
- `management.endpoint.configprops.show-values`
- `management.endpoint.quartz.show-values`

可选值：
- `NEVER` - 所有值都被清理（默认）
- `ALWAYS` - 所有值都显示
- `WHEN_AUTHORIZED` - 仅当用户被授权时显示值

---

## 测试功能升级

### @ConstructorBinding 在测试中的使用

`@ConstructorBinding` 的使用已从 `org.springframework.boot.context.properties` 重新定位到 `org.springframework.boot.context.properties.bind`。

### @AutoConfigureMockMvc 变更

`@AutoConfigureMockMvc` 注解现在可以配置 HtmlUnit 特定设置，这些设置已移至 `htmlUnit` 属性下。

例如，在 Spring Boot 2.7 中：
```java
@AutoConfigureMockMvc(webClientEnabled=false, webDriverEnabled=false)
```

在 Spring Boot 3.0 中：
```java
@AutoConfigureMockMvc(htmlUnit = @HtmlUnit(webClient = false, webDriver = false))
```

### TestRestTemplate 和 WebTestClient

`@SpringBootTest` 注解不再提供任何 `TestRestTemplate` 或 `WebTestClient` beans。如果要使用 `TestRestTemplate`，应在测试类上添加 `@AutoConfigureWebTestClient` 注解。

### @MockBean 和 @SpyBean

`@MockBean` 和 `@SpyBean` 注解已从 `org.springframework.boot.test.mock.mockito` 重新定位到 `org.springframework.test.context.bean.override.mockito`。

---

## 构建工具升级

### Maven 变更

#### 运行应用程序

`spring-boot:run` 和 `spring-boot:start` 的 `fork` 属性（在 Spring Boot 2.7 中已弃用）已被移除。

#### Git Commit ID Maven 插件

Git Commit ID Maven 插件已更新到版本 5，其坐标已更改：
- 旧坐标：`pl.project13.maven:git-commit-id-plugin`
- 新坐标：`io.github.git-commit-id:git-commit-id-maven-plugin`

### Gradle 变更

#### 配置 Gradle 任务

Spring Boot 的 Gradle 任务已更新为一致地使用 Gradle 的 `Property` 支持进行其配置。

例如，在 Spring Boot 2.x 中禁用 `bootJar` 任务的分层：
```groovy
tasks.named<BootJar>("bootJar") {
    layered {
        isEnabled = false
    }
}
```

在 3.0 中：
```groovy
tasks.named<BootJar>("bootJar") {
    layered {
        enabled.set(false)
    }
}
```

#### 排除 build-info.properties 中的属性

```groovy
springBoot {
    buildInfo {
        excludes = ['time']
    }
}
```

---

## 版本对照表

| 组件 | Spring Boot 2.7 | Spring Boot 3.0 |
|------|-----------------|-----------------|
| **Java** | 8 - 19 | **17+** |
| **Spring Framework** | 5.3.x | **6.0.x** |
| **Spring Security** | 5.7.x | **6.0.x** |
| **Spring Data** | 2021.x | **2022.x** |
| **Jakarta EE** |  javax.* | **jakarta.*** |
| **Servlet** | 4.0 | **6.0** |
| **Hibernate** | 5.6.x | **6.1.x** |
| **Flyway** | 8.x | **9.x** |
| **Liquibase** | 4.9.x | **4.17.x** |
| **Jackson** | 2.13.x | **2.14.x** |
| **Kotlin** | 1.6+ | **1.7+** |
| **Gradle** | 7.4+ | **7.5+** |

---

## 迁移检查清单

在开始迁移之前，请确保：

- [ ] ✅ 升级到最新的 Spring Boot 2.7.x 版本
- [ ] ✅ 检查并移除所有已弃用的 API 调用
- [ ] ✅ 确认 Java 17+ 可用
- [ ] ✅ 检查所有依赖项的兼容性
- [ ] ✅ 如果使用 Spring Security，先升级到 5.8
- [ ] ✅ 更新所有 `javax.*` import 为 `jakarta.*`
- [ ] ✅ 检查并更新配置属性
- [ ] ✅ 运行测试套件验证功能
- [ ] ✅ 检查相关的 Spring 项目发行说明

---

祝您升级顺利！
