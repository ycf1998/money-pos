# Spring Boot 4.0 升级指南

> **通用参考**：本文为 Spring Boot 官方迁移指南的翻译，与本仓库（QK-Money）的版本无关。

> 原文：Spring Boot 4.0 Release Notes  
> 翻译版本：1.0  
> 最后更新：2026-03-17

---

## 目录

1. [开始之前](#开始之前)
2. [从本版本中移除的功能](#从本版本中移除的功能)
3. [模块依赖](#模块依赖)
4. [依赖项和构建插件升级](#依赖项和构建插件升级)
5. [Actuator 和生产就绪功能升级](#actuator 和生产就绪功能升级)
6. [Web 功能升级](#web 功能升级)
7. [数据功能升级](#数据功能升级)
8. [消息功能升级](#消息功能升级)
9. [IO 功能升级](#io 功能升级)
10. [测试功能升级](#测试功能升级)

---

## 开始之前

### 升级到最新的 3.5.x 版本

在开始升级之前，请确保升级到可用的最新 3.5.x 版本。这将确保您基于该系列的最新依赖进行构建。

请花时间检查您可能调用的任何已弃用方法，因为这些方法将在 Spring Boot 4.0 中被移除。

### 检查依赖项

迁移到 Spring Boot 4 将升级许多依赖项，可能需要您进行一些工作。您可以对比 3.5.x 和 4.0.x 的依赖管理，评估您的项目受到的影响。

您可能还使用了 Spring Boot 未管理的依赖项（例如 Spring Cloud）。由于您的项目为这些依赖定义了显式版本，请在升级前确定兼容的版本。

### 检查系统要求

- **Java**: Spring Boot 4.0 需要 Java 17 或更高版本。鼓励使用最新的 Java LTS 版本。

- **Kotlin**: Spring Boot 应用程序也可以使用 Kotlin 开发，必须使用 v2.2 或更高版本。

- **GraalVM**: 如果您使用 GraalVM 的 native-image，必须使用 v25 或更高版本。

- **Jakarta EE**: Spring Boot 4 基于 Jakarta EE 11，需要 Servlet 6.1 基线。

如果您直接管理依赖项，请确保相应地更新它们。特别是，您必须使用 Spring Framework 7.x。

### 检查 Spring Boot 3.x 的弃用内容

在 Spring Boot 3.x 中弃用的类、方法和属性已在此版本中移除。请在升级前确保您没有调用已弃用的方法。

### 升级到 Spring Boot 4

在检查了项目及其依赖项的状态后，升级到 Spring Boot 4.0 的最新维护版本。

---

## 从本版本中移除的功能

以下功能已从此版本中移除，不再可用。

### Undertow

Spring Boot 4 需要 Servlet 6.1 基线，而 Undertow 尚不兼容。因此，Undertow 支持已被放弃，包括 Undertow starter 以及使用 Undertow 作为嵌入式服务器的能力。

我们不建议将 Spring Boot 4.0 应用程序部署到不符合 Servlet 6.1 的容器。

### Pulsar Reactive

根据在 Spring Pulsar 中移除 reactor 支持的决策，Spring Boot 不再管理响应式 Pulsar 客户端。Spring Pulsar Reactive 的自动配置也已被移除。

### 嵌入式可执行 Uber Jar 启动脚本

用于创建"完全可执行"jar 文件的嵌入式启动脚本支持已被移除。该支持特定于类 Unix 操作系统，并且有许多限制，最显著的是与高效部署的建议相冲突。

如果您仍然需要类似功能，我们建议寻找替代方案，例如 Gradle 的 application 插件。

您仍然可以使用 Spring Boot 的构建插件创建 uber jars，并使用 `java -jar` 运行它们。

### Spring Session Hazelcast

Spring Session Hazelcast 现在由 Hazelcast 团队领导。因此，Spring Boot 本身已移除了对 Spring Session Hazelcast 的直接支持。

### Spring Session MongoDB

Spring Session MongoDB 现在由 MongoDB 团队领导。因此，Spring Boot 本身已移除了对 Spring Session MongoDB 的直接支持。

### Spock 集成

Spring Boot 的 Spock 集成已被移除，因为 Spock 尚不支持 Groovy 5。

---

## 模块依赖

Spring Boot 4.0 采用了新的模块化设计，现在提供更小的、专注于特定功能的模块，而不是几个大型 jar。因此，此版本拥有一组更一致的"starter"POM，并且仅依赖第三方依赖项才能工作的功能可能需要额外的 starter。

如果您的当前应用程序不使用 Spring Boot"starter"POM，您将需要进行依赖项修改。

### 主代码

新模块和"starter"POM 遵循一个约定，让您可以识别给定技术的支持来自何处：

- 所有 Spring Boot 模块命名为 `spring-boot-<technology>`
- 每个模块的根包是 `org.springframework.boot.<technology>`
- 所有"starter"POM 命名为 `spring-boot-starter-<technology>`

举例说明，GraphQL 的支持在 `spring-boot-graphql` 中，根包为 `org.springframework.boot.graphql`。"starter"POM 是 `spring-boot-starter-graphql`。

> **注意**  
> 根据您使用的技术查看下面的"starter"POM 列表，并检查您的依赖项。如果您使用以前没有"starter"POM 的技术，可能会遇到问题。
>
> 例如，如果您使用 Flyway 或 Liquibase，以前只有相关的第三方依赖。现在您需要分别将其替换为 `spring-boot-starter-flyway` 或 `spring-boot-starter-liquibase`。

### 测试代码

模块化也适用于测试基础设施。应用类似的约定：

- 所有 Spring Boot 测试模块命名为 `spring-boot-<technology>-test`
- 每个此类模块的根包是 `org.springframework.boot.<technology>.test`
- 所有测试"starter"POM 命名为 `spring-boot-starter-<technology>-test`

举例说明，GraphQL 的测试基础设施在 `spring-boot-graphql-test` 中，根包为 `org.springframework.boot.graphql.test`。"starter"POM 是 `spring-boot-starter-graphql-test`。

> **注意**  
> 如果您使用定义了自有测试基础设施的技术，您应该将其替换为等效的 `spring-boot-starter-<technology>-test`。在上面的例子中，`spring-boot-graphql-starter-test` 依赖于 `org.springframework.graphql:spring-graphql-test`。
>
> 不这样做可能会导致难以追踪的问题。例如，来自 `spring-security-test` 的 `@WithMockUser` 和 `@WithUserDetails` 现在需要 `spring-boot-starter-security-test` 才能正常运行。

> **提示**  
> 鉴于所有测试"starter"POM 都传递性地引入了 `spring-boot-starter-test`，您不再需要定义此 starter。相反，您应该列出模块下待测试技术的 starters。

### Starters 列表

此版本以两种方式统一了 starters 的使用：

- Spring Boot 涵盖的大多数技术都有专用的 starter
- 每个 starter 都有一个配套的测试 starter

检查您的测试依赖项很重要。下表显示了您应该使用哪些"starter"POM 来支持特定技术：

#### 核心 Starters

| 技术 | 主依赖 | 测试依赖 |
|------|--------|----------|
| AspectJ | `spring-boot-starter-aspectj` | `spring-boot-starter-aspectj-test` |
| Cloud Foundry 支持 | `spring-boot-starter-cloudfoundry` | `spring-boot-starter-cloudfoundry-test` |
| Jakarta Validation | `spring-boot-starter-validation` | `spring-boot-starter-validation-test` |
| Kotlinx Serialization JSON | `spring-boot-starter-kotlinx-serialization-json` | `spring-boot-starter-kotlinx-serialization-json-test` |

#### Web 服务器 Starters

| 技术 | 主依赖 | 测试依赖 |
|------|--------|----------|
| Jetty | `spring-boot-starter-jetty` | 无 |
| Reactor Netty | `spring-boot-starter-reactor-netty` | 无 |
| Tomcat | `spring-boot-starter-tomcat` | 无 |

#### Web 客户端 Starters

| 技术 | 主依赖 | 测试依赖 |
|------|--------|----------|
| Spring 命令式 RestClient 和 RestTemplate | `spring-boot-starter-restclient` | `spring-boot-starter-restclient-test` |
| Spring 响应式 WebClient | `spring-boot-starter-webclient` | `spring-boot-starter-webclient-test` |

#### Web Starters

| 技术 | 主依赖 | 测试依赖 |
|------|--------|----------|
| Jersey | `spring-boot-starter-jersey` | `spring-boot-starter-jersey-test` |
| Spring GraphQL | `spring-boot-starter-graphql` | `spring-boot-starter-graphql-test` |
| Spring HATEOAS | `spring-boot-starter-hateoas` | `spring-boot-starter-hateoas-test` |
| Spring REST Docs | `spring-boot-starter-restdocs` (自 Spring Boot 4.0.4 起) | - |
| Spring Session Data Redis | `spring-boot-starter-session-data-redis` | `spring-boot-starter-session-data-redis-test` |
| Spring Session JDBC | `spring-boot-starter-session-jdbc` | `spring-boot-starter-session-jdbc-test` |
| Spring Web MVC | `spring-boot-starter-webmvc` | `spring-boot-starter-webmvc-test` |
| Spring WebFlux | `spring-boot-starter-webflux` | `spring-boot-starter-webflux-test` |
| Spring Webservices | `spring-boot-starter-webservices` | `spring-boot-starter-webservices-test` |

#### 数据库 Starters

| 技术 | 主依赖 | 测试依赖 |
|------|--------|----------|
| Cassandra | `spring-boot-starter-cassandra` | `spring-boot-starter-cassandra-test` |
| Couchbase | `spring-boot-starter-couchbase` | `spring-boot-starter-couchbase-test` |
| Elasticsearch | `spring-boot-starter-elasticsearch` | `spring-boot-starter-elasticsearch-test` |
| Flyway | `spring-boot-starter-flyway` | `spring-boot-starter-flyway-test` |
| JDBC | `spring-boot-starter-jdbc` | `spring-boot-starter-jdbc-test` |
| jOOQ | `spring-boot-starter-jooq` | `spring-boot-starter-jooq-test` |
| Liquibase | `spring-boot-starter-liquibase` | `spring-boot-starter-liquibase-test` |
| LDAP | `spring-boot-starter-ldap` | `spring-boot-starter-ldap-test` |
| MongoDB | `spring-boot-starter-mongodb` | `spring-boot-starter-mongodb-test` |
| Neo4J | `spring-boot-starter-neo4j` | `spring-boot-starter-neo4j-test` |
| R2DBC | `spring-boot-starter-r2dbc` | `spring-boot-starter-r2dbc-test` |

#### Spring Data Starters

| 技术 | 主依赖 | 测试依赖 |
|------|--------|----------|
| Spring Data Cassandra | `spring-boot-starter-data-cassandra` 或 `spring-boot-starter-data-cassandra-reactive` | `spring-boot-starter-data-cassandra-test` 或 `spring-boot-starter-data-cassandra-reactive-test` |
| Spring Data Couchbase | `spring-boot-starter-data-couchbase` 或 `spring-boot-starter-data-couchbase-reactive` | `spring-boot-starter-data-couchbase-test` 或 `spring-boot-starter-data-couchbase-reactive-test` |
| Spring Data Elasticsearch | `spring-boot-starter-data-elasticsearch` | `spring-boot-starter-data-elasticsearch-test` |
| Spring Data JDBC | `spring-boot-starter-data-jdbc` | `spring-boot-starter-data-jdbc-test` |
| Spring Data JPA (使用 Hibernate) | `spring-boot-starter-data-jpa` | `spring-boot-starter-data-jpa-test` |
| Spring Data LDAP | `spring-boot-starter-data-ldap` | `spring-boot-starter-data-ldap-test` |
| Spring Data MongoDB | `spring-boot-starter-data-mongodb` 或 `spring-boot-starter-data-mongodb-reactive` | `spring-boot-starter-data-mongodb-test` 或 `spring-boot-starter-data-mongodb-reactive-test` |
| Spring Data Neo4J | `spring-boot-starter-data-neo4j` | `spring-boot-starter-data-neo4j-test` |
| Spring Data R2DBC | `spring-boot-starter-data-r2dbc` | `spring-boot-starter-data-r2dbc-test` |
| Spring Data Redis | `spring-boot-starter-data-redis` 或 `spring-boot-starter-data-redis-reactive` | `spring-boot-starter-data-redis-test` 或 `spring-boot-starter-data-redis-reactive-test` |
| Spring Data REST | `spring-boot-starter-data-rest` | `spring-boot-starter-data-rest-test` |

#### IO Starters

| 技术 | 主依赖 | 测试依赖 |
|------|--------|----------|
| Hazelcast | `spring-boot-starter-hazelcast` | `spring-boot-starter-hazelcast-test` |
| Mail | `spring-boot-starter-mail` | `spring-boot-starter-mail-test` |
| Quartz | `spring-boot-starter-quartz` | `spring-boot-starter-quartz-test` |
| SendGrid | `spring-boot-starter-sendgrid` | `spring-boot-starter-sendgrid-test` |
| Spring Caching 支持 | `spring-boot-starter-cache` | `spring-boot-starter-cache-test` |
| Spring Batch (带 JDBC) | `spring-boot-starter-batch-jdbc` | `spring-boot-starter-batch-jdbc-test` |
| Spring Batch (不带 JDBC) | `spring-boot-starter-batch` | `spring-boot-starter-batch-test` |

#### JSON Starters

| 技术 | 主依赖 | 测试依赖 |
|------|--------|----------|
| GSON | `spring-boot-starter-gson` | `spring-boot-starter-gson-test` |
| Jackson | `spring-boot-starter-jackson` | `spring-boot-starter-jackson-test` |
| JSONB | `spring-boot-starter-jsonb` | `spring-boot-starter-jsonb-test` |

#### 消息 Starters

| 技术 | 主依赖 | 测试依赖 |
|------|--------|----------|
| ActiveMQ | `spring-boot-starter-activemq` | `spring-boot-starter-activemq-test` |
| Artemis | `spring-boot-starter-artemis` | `spring-boot-starter-artemis-test` |
| JMS | `spring-boot-starter-jms` | `spring-boot-starter-jms-test` |
| RSocket | `spring-boot-starter-rsocket` | `spring-boot-starter-rsocket-test` |
| Spring AMQP | `spring-boot-starter-amqp` | `spring-boot-starter-amqp-test` |
| Spring Integration | `spring-boot-starter-integration` | `spring-boot-starter-integration-test` |
| Spring for Apache Kafka | `spring-boot-starter-kafka` | `spring-boot-starter-kafka-test` |
| Spring for Apache Pulsar | `spring-boot-starter-pulsar` | `spring-boot-starter-pulsar-test` |
| Websockets | `spring-boot-starter-websocket` | `spring-boot-starter-websocket-test` |

#### 安全 Starters

| 技术 | 主依赖 | 测试依赖 |
|------|--------|----------|
| Spring Security | `spring-boot-starter-security` | `spring-boot-starter-security-test` |
| Spring Security OAuth 授权服务器 | `spring-boot-starter-security-oauth2-authorization-server` | `spring-boot-starter-security-oauth2-authorization-server-test` |
| Spring Security OAuth 客户端 | `spring-boot-starter-security-oauth2-client` | `spring-boot-starter-security-oauth2-client-test` |
| Spring Security OAuth 资源服务器 | `spring-boot-starter-security-oauth2-resource-server` | `spring-boot-starter-security-oauth2-resource-server-test` |
| Spring Security SAML | `spring-boot-starter-security-saml2` | `spring-boot-starter-security-saml2-test` |

#### 模板 Starters

| 技术 | 主依赖 | 测试依赖 |
|------|--------|----------|
| Freemarker | `spring-boot-starter-freemarker` | `spring-boot-starter-freemarker-test` |
| Groovy Templates | `spring-boot-starter-groovy-templates` | `spring-boot-starter-groovy-templates-test` |
| Mustache | `spring-boot-starter-mustache` | `spring-boot-starter-mustache-test` |
| Thymeleaf | `spring-boot-starter-thymeleaf` | `spring-boot-starter-thymeleaf-test` |

#### 生产就绪 Starters

| 技术 | 主依赖 | 测试依赖 |
|------|--------|----------|
| Actuator | `spring-boot-starter-actuator` | `spring-boot-starter-actuator-test` |
| Micrometer Metrics | `spring-boot-starter-micrometer-metrics` | `spring-boot-starter-micrometer-metrics-test` |
| OpenTelemetry | `spring-boot-starter-opentelemetry` | `spring-boot-starter-opentelemetry-test` |
| Zipkin | `spring-boot-starter-zipkin` | `spring-boot-starter-zipkin-test` |

### Modules（模块方式）

如果您不想使用"starter"POM，可以改为声明直接的模块依赖：

#### 核心模块

| 技术 | 主依赖 | 测试依赖 |
|------|--------|----------|
| Cloud Foundry 支持 | `spring-boot-cloudfoundry` | 无 |
| Jakarta Validation | `spring-boot-validation` | 无 |
| Kotlin Serialization | `spring-boot-kotlin-serialization` | 无 |
| Reactor | `spring-boot-reactor` | 无 |

#### Web 服务器模块

| 技术 | 主依赖 | 测试依赖 |
|------|--------|----------|
| Jetty | `spring-boot-jetty` | 无 |
| Reactor Netty | `spring-boot-reactor-netty` | 无 |
| Tomcat | `spring-boot-tomcat` | 无 |

#### Web 客户端模块

| 技术 | 主依赖 | 测试依赖 |
|------|--------|----------|
| Spring 命令式 RestClient 和 RestTemplate | `spring-boot-restclient` | `spring-boot-restclient-test` |
| Spring 响应式 WebClient | `spring-boot-webclient` | `spring-boot-webclient-test` |

#### Web 模块

| 技术 | 主依赖 | 测试依赖 |
|------|--------|----------|
| Jersey | `spring-boot-jersey` | 无 |
| Spring GraphQL | `spring-boot-graphql` | `spring-boot-graphql-test` |
| Spring HATEOAS | `spring-boot-hateoas` | 无 |
| Spring REST Docs | `spring-boot-restdocs` | - |
| Spring Session Data Redis | `spring-boot-session-data-redis` | 无 |
| Spring Session JDBC | `spring-boot-session-jdbc` | 无 |
| Spring Web MVC | `spring-boot-webmvc` | `spring-boot-webmvc-test` |
| Spring WebFlux | `spring-boot-webflux` | `spring-boot-webflux-test` |
| Spring Webservices | `spring-boot-webservices` | `spring-boot-webservices-test` |

#### 数据库模块

| 技术 | 主依赖 | 测试依赖 |
|------|--------|----------|
| Cassandra | `spring-boot-cassandra` | 无 |
| Couchbase | `spring-boot-couchbase` | 无 |
| Elasticsearch | `spring-boot-elasticsearch` | 无 |
| Flyway | `spring-boot-flyway` | 无 |
| JDBC | `spring-boot-jdbc` | `spring-boot-jdbc-test` |
| jOOQ | `spring-boot-jooq` | `spring-boot-jooq-test` |
| Liquibase | `spring-boot-liquibase` | 无 |
| LDAP | `spring-boot-ldap` | 无 |
| MongoDB | `spring-boot-mongodb` | 无 |
| Neo4J | `spring-boot-neo4j` | 无 |
| R2DBC | `spring-boot-r2dbc` | 无 |

#### Spring Data 模块

| 技术 | 主依赖 | 测试依赖 |
|------|--------|----------|
| Spring Data Cassandra | `spring-boot-data-cassandra` | `spring-boot-data-cassandra-test` |
| Spring Data Couchbase | `spring-boot-data-couchbase` | `spring-boot-data-couchbase-test` |
| Spring Data Elasticsearch | `spring-boot-data-elasticsearch` | `spring-boot-data-elasticsearch-test` |
| Spring Data JDBC | `spring-boot-data-jdbc` | `spring-boot-data-jdbc-test` |
| Spring Data JPA (使用 Hibernate) | `spring-boot-data-jpa` | `spring-boot-data-jpa-test` |
| Spring Data LDAP | `spring-boot-data-ldap` | `spring-boot-data-ldap-test` |
| Spring Data MongoDB | `spring-boot-data-mongodb` | `spring-boot-data-mongodb-test` |
| Spring Data Neo4J | `spring-boot-data-neo4j` | `spring-boot-data-neo4j-test` |
| Spring Data R2DBC | `spring-boot-data-r2dbc` | `spring-boot-data-r2dbc-test` |
| Spring Data Redis | `spring-boot-data-redis` | `spring-boot-data-redis-test` |
| Spring Data REST | `spring-boot-data-rest` | 无 |

#### IO 模块

| 技术 | 主依赖 | 测试依赖 |
|------|--------|----------|
| Hazelcast | `spring-boot-hazelcast` | 无 |
| Mail | `spring-boot-mail` | 无 |
| Quartz | `spring-boot-quartz` | 无 |
| SendGrid | `spring-boot-sendgrid` | 无 |
| Spring Caching 支持 | `spring-boot-cache` | `spring-boot-cache-test` |
| Spring Batch (带 JDBC) | `spring-boot-batch-jdbc` | 无 |
| Spring Batch (不带 JDBC) | `spring-boot-batch` | 无 |

#### JSON 模块

| 技术 | 主依赖 | 测试依赖 |
|------|--------|----------|
| GSON | `spring-boot-gson` | 无 |
| Jackson | `spring-boot-jackson` | 无 |
| JSONB | `spring-boot-jsonb` | 无 |

#### 消息模块

| 技术 | 主依赖 | 测试依赖 |
|------|--------|----------|
| ActiveMQ | `spring-boot-activemq` | 无 |
| Artemis | `spring-boot-artemis` | 无 |
| JMS | `spring-boot-jms` | 无 |
| RSocket | `spring-boot-rsocket` | `spring-boot-rsocket-test` |
| Spring AMQP | `spring-boot-amqp` | 无 |
| Spring Integration | `spring-boot-integration` | 无 |
| Spring for Apache Kafka | `spring-boot-kafka` | 无 |
| Spring for Apache Pulsar | `spring-boot-pulsar` | 无 |
| Websockets | `spring-boot-websocket` | 无 |

#### 安全模块

| 技术 | 主依赖 | 测试依赖 |
|------|--------|----------|
| Spring Security | `spring-boot-security` | `spring-boot-security-test` |
| Spring Security OAuth 授权服务器 | `spring-boot-security-oauth2-authorization-server` | 无 |
| Spring Security OAuth 客户端 | `spring-boot-security-oauth2-client` | 无 |
| Spring Security OAuth 资源服务器 | `spring-boot-security-oauth2-resource-server` | 无 |
| Spring Security SAML | `spring-boot-security-saml2` | 无 |

#### 模板模块

| 技术 | 主依赖 | 测试依赖 |
|------|--------|----------|
| Freemarker | `spring-boot-freemarker` | 无 |
| Groovy Templates | `spring-boot-groovy-templates` | 无 |
| Mustache | `spring-boot-mustache` | 无 |
| Thymeleaf | `spring-boot-thymeleaf` | 无 |

#### 生产就绪模块

| 技术 | 主依赖 | 测试依赖 |
|------|--------|----------|
| Health | `spring-boot-health` | 无 |
| Micrometer Metrics | `spring-boot-micrometer-metrics` | `spring-boot-micrometer-metrics-test` |
| Micrometer Observation | `spring-boot-micrometer-observation` | 无 |
| Micrometer Tracing | `spring-boot-micrometer-tracing` | `spring-boot-micrometer-tracing-test` |
| Micrometer Tracing Brave | `spring-boot-micrometer-tracing-brave` | 无 |
| Micrometer Tracing OpenTelemetry | `spring-boot-micrometer-tracing-opentelemetry` | 无 |
| OpenTelemetry | `spring-boot-opentelemetry` | 无 |
| Zipkin | `spring-boot-zipkin` | 无 |

### 经典 Starters

如果您正在升级现有应用程序并且只是想快速运行，可以使用"经典 Starter POM"。经典 starters 提供所有模块，但排除其所有传递性依赖。

这提供了与上一代 Spring Boot 非常类似的设置，其中所有自动配置类都可用。

要使用经典 starters，请按如下方式更新您的构建：

| 以前的 Starter | 经典等效 |
|----------------|----------|
| `spring-boot-starter` | `spring-boot-starter-classic` |
| `spring-boot-starter-test` | `spring-boot-starter-test-classic` |

> **注意**  
> 我们建议您最终将应用程序从使用经典 starters 迁移出来。

### 已弃用的 Starters

几个 starter POM 已重命名，以更好地与其对应的模块保持一致。较旧的 starters 仍然存在，但应视为已弃用，并将在将来的版本中移除。

您应该按如下方式更新您的 POM：

| 已弃用的 Starter | 替换为 |
|------------------|--------|
| `spring-boot-starter-oauth2-authorization-server` | `spring-boot-starter-security-oauth2-authorization-server` |
| `spring-boot-starter-oauth2-client` | `spring-boot-starter-security-oauth2-client` |
| `spring-boot-starter-oauth2-resource-server` | `spring-boot-starter-security-oauth2-resource-server` |
| `spring-boot-starter-web` | `spring-boot-starter-webmvc` |
| `spring-boot-starter-web-services` | `spring-boot-starter-webservices` |

### 包组织

模块化对项目的包结构也有影响。每个模块现在以专用的 `org.springframework.boot.<module>` 开头。根据模块的范围，它可以包含 API、自动配置、与 Actuator 相关的支持等。

### 对构建自己 Starters 的项目的考虑

由于模块化工作，强烈不鼓励在同一 artifact 中同时支持 Spring Boot 3 和 Spring Boot 4。

### 迁移策略

根据您的应用程序规模，您可能希望直接适应新的模块化，或者分两步执行升级。

添加 `spring-boot-starter-classic`（以及用于测试的 `spring-boot-starter-test-classic`）可以让您回到所有基础设施都可用的类路径。您可以使用此中间状态来修复损坏的导入并验证应用程序是否正常工作。

迁移完成后，您可以移除经典 starters，并使用更新的导入来识别缺少的"starter"POM。

### 检查其他发行说明

此主要版本迁移到了 Spring 产品组合其余部分的主要版本。因此，请确保检查您的应用程序使用的其他项目的发行说明：

- Spring AMQP 4.0
- Spring Batch 6.0
- Spring Data 2025.1
- Spring GraphQL 2.0
- Spring Framework 7.0
- Spring Integration 7.0
- Spring for Apache Kafka 4.0
- Spring for Apache Pulsar 2.0
- Spring Security 7.0
- Spring REST Docs 4.0
- Spring Session 4.0
- Spring WS 5.0

---

## 核心功能升级

### JSpecify 空值注解

Spring Boot 4.0 添加了 JSpecify 空值注解。如果您在构建中使用空值检查器或使用 Kotlin，这可能会因为现在可空或不可空的类型而导致编译失败。

此外，如果您以前使用来自 `org.springframework.lang` 包的 Spring Framework 空值注解，您应该查看"从 Spring 空值安全注解迁移"。

### Logback 默认字符集

Logback 的默认字符集已与 Log4j2 的行为协调。默认情况下，日志文件的字符集是 UTF-8。对于控制台日志，如果可用，我们使用 `Console#charset()`，否则使用 UTF-8。

### BootstrapRegistry 和 EnvironmentPostProcessor 包变更

`BootstrapRegistry` 和相关类已从 `org.springframework.boot` 移动到 `org.springframework.boot.bootstrap`。`EnvironmentPostProcessor` 接口也已从 `org.springframework.boot.env` 移动到 `org.springframework.boot`。

如果您对 Spring Boot 有深度集成，可能需要更新您的代码和 `spring.factories` 文件。

> **注意**  
> `EnvironmentPostProcessor` 的弃用形式在 Spring Boot 4.0 中仍然可用，但将在以后移除。

### Property Mapper API 变更

`PropertyMapper` 类在源值为 null 时不再默认调用适配器或谓词方法。这移除了对 `alwaysApplyingNotNull()` 方法的需求，该方法已被移除。

如果您即使对于 null 值也需要执行映射，可以使用新的 `always()` 方法。

例如：

```java
map.from(source::method).to(destination::method);
```

如果 `source.method()` 返回 null，将不会调用 `destination.method(...)`。

而：

```java
map.from(source::method).always().to(destination::method);
```

如果 `source.method()` 返回 null，将调用 `destination.method(null)`。

如果您使用 `PropertyMapper`，可能需要查看 commit 239f384ac0，该提交显示了 Spring Boot 本身如何适应新的 API。

### DevTools Live Reload 支持

DevTools Live Reload 支持现在默认禁用。如果您想使用 live reload，请将 `spring.devtools.livereload.enabled` 设置为 `true`。

---

## 依赖项和构建插件升级

### Maven 中的可选依赖

可选依赖不再包含在 uber jars 中。如果您需要它们，可以使用配置设置 `<includeOptional>true</includeOptional>`。

### AOP Starter POM

为了澄清 `spring-boot-starter-aop` 的范围，它已重命名为 `spring-boot-starter-aspectj`。如果您在应用程序中显式添加了此 starter，请在使用替换之前检查您是否真的需要它。

要弄清楚这一点，请检查以下内容：

- 您的应用程序不使用 AspectJ，通常是 `org.aspectj.lang.annotation` 包中的注解
- 在应用程序的类路径上搜索 `org.aspectj.lang.annotation.Aspect` 的使用。它们通常记录触发功能的注解，例如 Micrometer 的 `@Timed` 或 `@Counted`

### Spring Retry 的依赖管理

随着产品组合从 Spring Retry 迁移到 Spring Framework 的新核心功能，Spring Retry 的依赖管理已被移除。

如果您的应用程序仍然依赖 Spring Retry，现在需要显式版本。请考虑将 Spring Retry 的使用迁移到 Spring Framework。

### Spring Authorization Server 的依赖管理

Spring Authorization Server 现在是 Spring Security 的一部分。支持已移除，以支持 Spring Security 已提供的依赖管理。

因此，您不再可以使用 `spring-authorization-server.version` 属性覆盖 Spring Authorization Server 的版本。如果您需要这样做，请使用 `spring-security.version`。

### 经典 Uber-Jar Loader 支持

经典的 uber-jar loader 已从此版本中移除。您应该从构建文件中移除任何 loader 实现配置。

对于 Maven，这意味着移除以下内容：

```xml
<loaderImplementation>CLASSIC</loaderImplementation>
```

对于 Gradle，它将是类似的行：

```groovy
loaderImplementation = org.springframework.boot.loader.tools.LoaderImplementation.CLASSIC
```

### Cyclone DX Gradle 插件

Cyclone DX Gradle 插件的最低支持版本现在是 3.0.0。

### Jackson 升级

Spring Boot 现在使用 Jackson 3 作为其首选 JSON 库。Jackson 3 使用新的组 ID 和包名，`com.fasterxml.jackson` 变为 `tools.jackson`。例外的是 `jackson-annotations` 模块，它继续使用 `com.fasterxml.jackson.core` 组 ID 和 `com.fasterxml.jackson.annotation` 包。要了解有关 Jackson 3 中更改的更多信息，请参阅 Jackson wiki。

对于需要 Jackson 2 的库，Jackson 2 的依赖管理仍然存在，如果需要，Jackson 2 ObjectMapper 可以与 Boot 的 Jackson 3 自动配置一起使用。

许多类已重命名以保持与 Jackson 3 的一致性：

- `JsonObjectSerializer` 重命名为 `ObjectValueSerializer`
- `JsonValueDeserializer` 重命名为 `ObjectValueDeserializer`
- `Jackson2ObjectMapperBuilderCustomizer` 重命名为 `JsonMapperBuilderCustomizer`

`@JsonComponent`、`@JsonMixin` 和相关支持类已重命名，以明确它们特定于 Jackson，并且不一定限于 JSON。`@JsonComponent` 现在是 `@JacksonComponent`，`@JsonMixin` 现在是 `@JacksonMixin`。支持类也以相同的方式重命名，将 `Json` 替换为 `Jackson`。

特定于 JSON 的 `spring.jackson.read.*` 和 `spring.jackson.write.*` 属性已分别移至 `spring.jackson.json.read` 和 `spring.jackson.json.write` 下。

当 Jackson 2 `JsonParser.Feature` 具有等效的 Jackson 3 `JsonReadFeature` 时，`spring.jackson.parser.*` 属性已替换为 `spring.jackson.json.read` 属性。当 Jackson 2 `JsonParser.Feature` 具有 Jackson 3 等效但不是 `JsonReadFeature` 时，请使用 `JsonMapperBuilderCustomizer` 以编程方式应用所需的配置。

从 Spring Boot 4 开始，Jackson 将检测类路径上存在的所有模块，并将它们注册到 mapper 实例中。在 Spring Boot 3 中，只有"知名"模块才会自动注册。您可以通过设置 `spring.jackson.find-and-add-modules=false` 属性来禁用此新行为。

### Jackson 2 兼容性

为了帮助从 Jackson 2 迁移到 Jackson 3，引入了 `spring.jackson.use-jackson2-defaults` 属性。当设置为 `true` 时，自动配置的 `JsonMapper` 将具有与 Spring Boot 3.x 中的 Jackson 2 尽可能一致 的默认值。

我们强烈建议尽可能采用 Jackson 3，但是，如果您无法这样做，Spring Boot 4.0 还提供了 `spring-boot-jackson2` 模块。此模块以弃用的形式发布，并将在将来的版本中移除。它旨在作为需要更多时间迁移到 Jackson 3 的用户的临时解决方案。

要使用 Jackson 2 模块，您可以将以下依赖项添加到您的 Maven POM：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-jackson2</artifactId>
</dependency>
```

或者如果您使用 Gradle：

```groovy
implementation("org.springframework.boot:spring-boot-jackson2")
```

Jackson 2 属性可在 `spring.jackson2` 下找到。这些等效于 Spring Boot 3.5 提供的 `spring.jackson` 属性。有关在 Spring Boot 4 中使用和配置 Jackson 2 的更多信息，请参阅参考文档。

---

## Actuator 和生产就绪功能升级

### 移除对 javax.annotations.NonNull 和 org.springframework.lang.Nullable 的支持

Actuator 端点参数不再可以使用 `org.springframework.lang.Nullable` 来声明参数是可选的。如果您使用此注解，应该迁移到 `org.jspecify.annotations.Nullable`。

### 存活和就绪探针

存活和就绪探针现在默认启用。这意味着 health 端点现在默认公开 liveness 和 readiness 组。

如果您不需要这些探针，可以使用 `management.endpoint.health.probes.enabled` 属性禁用它们。

---

## Web 功能升级

### 字体添加到常见静态资源位置

如果您使用 `PathRequest#toStaticResources`，请注意此版本添加了 `/fonts/**` 位置。如果您不进行更改，此路径将具有您之前配置的安全设置。

如果您不希望包含该位置，可以像排除其他任何位置一样排除它：

```java
pathRequest.toStaticResources().atCommonLocations().excluding(StaticResourceLocation.FONTS);
```

### Spring Session

Spring Session Data Redis 的属性已重命名，以反映对 Spring Data Redis 的依赖。以前以 `spring.session.redis` 开头的属性现在以 `spring.session.data.redis` 开头。同样，以前以 `spring.session.mongodb` 开头的属性现在以 `spring.session.data.mongodb` 开头。

### HttpMessageConverters 弃用

此版本弃用了 Spring Boot 的 `HttpMessageConverters`，因为 Spring Framework 在传统栈中改进了转换器配置。`HttpMessageConverters` 有几个问题，包括它混淆了客户端和服务器转换器。

如果您的应用程序声明了自定义的 `org.springframework.boot.http.converter.autoconfigure.HttpMessageConverters` bean，这仍然受支持，但该类型本身已弃用。如果您向上下文贡献 `HttpMessageConverter` beans（如 `JacksonJsonHttpMessageConverter`），这不再受支持，您将需要更新配置。

相反，您的应用程序可以声明一个或多个 `ClientHttpMessageConvertersCustomizer` 和 `ServerHttpMessageConvertersCustomizer`，这将让您以灵活的方式自定义转换器。每个自定义器可以选择将转换器贡献为"自定义"转换器（在默认转换器之前考虑），或者使用转换器实例替换自动检测到的默认转换器。您可以在参考文档中找到更多信息和示例。

### Jersey

Spring Boot 4.0 支持 Jersey 4.0，它尚不支持 Jackson 3。要使用 Jersey 处理 JSON，请使用 `spring-boot-jackson2`，可以替代或与 `spring-boot-jackson` 一起使用。

### Tomcat

如果您要将 war 文件部署到 Tomcat，您需要将依赖 `spring-boot-starter-tomcat` 切换为 `spring-boot-starter-tomcat-runtime`。

---

## 数据功能升级

### Elasticsearch 客户端

响应 Elasticsearch 中的更改，已弃用的低级 Elasticsearch RestClient 的自动配置已替换为新的 Rest5Client 的自动配置。如果您使用 Spring Boot 的 `RestClientBuilderCustomizer` 来自定义客户端，您现在必须使用 `Rest5ClientBuilderCustomizer`。

作为 Elasticsearch 中更改的一部分，客户端代码已合并到 `co.elastic.clients:elasticsearch-java` 模块中，包括内置的 sniffer 支持。`org.elasticsearch.client:elasticsearch-rest-client` 和 `org.elasticsearch.client:elasticsearch-rest-client-sniffer` 模块不再需要，Spring Boot 对它们的依赖管理已被移除。

对高级 `ElasticsearchClient` 和 Spring Data Elasticsearch 提供的 `ReactiveElasticsearchClient` 的支持仍然存在。它已更新为使用新的低级客户端。

### 持久化模块

已创建一个新的 `spring-boot-persistence` 模块来存放通用持久化相关代码和属性。`@EntityScan` 的用户应该将其导入调整为 `org.springframework.boot.persistence.autoconfigure.EntityScan`。

`spring.dao.exceptiontranslation.enabled` 属性不再受支持。请改用 `spring.persistence.exceptiontranslation.enabled`。

### MongoDB

配置 MongoDB 的一些属性已更新，因此它们的名称反映了是否需要 Spring Data MongoDB。许多以前以 `spring.data.mongodb` 开头的属性现在以 `spring.mongodb` 开头：

- `spring.mongodb.additional-hosts`
- `spring.mongodb.authentication-database`
- `spring.mongodb.database`
- `spring.mongodb.host`
- `spring.mongodb.password`
- `spring.mongodb.port`
- `spring.mongodb.protocol`
- `spring.mongodb.replica-set-name`
- `spring.mongodb.representation.uuid`
- `spring.mongodb.ssl.bundle`
- `spring.mongodb.ssl.enabled`
- `spring.mongodb.uri`
- `spring.mongodb.username`

此外，管理相关属性已重命名为使用 `mongodb` 而不是 `mongo`：

- `management.health.mongodb.enabled`
- `management.metrics.mongodb.command.enabled`
- `management.metrics.mongodb.connectionpool.enabled`

以下需要 Spring Data MongoDB 的属性保持不变：

- `spring.data.mongodb.auto-index-creation`
- `spring.data.mongodb.field-naming-strategy`
- `spring.data.mongodb.gridfs.bucket`
- `spring.data.mongodb.gridfs.database`
- `spring.data.mongodb.repositories.type`

### MongoDB UUID 和 BigDecimal 表示

Spring Data MongoDB 不再为 UUID 和 BigInteger/BigDecimal 表示提供默认值。这与驱动程序建议不偏向 UUID 或 BigInteger/BigDecimal 的特定表示以避免由升级到更新版本的 Spring Data 引起的表示更改保持一致。

需要显式配置，可以使用 `spring.mongodb.representation.uuid` 和 `spring.data.mongodb.representation.big-decimal` 属性分别设置表示。

### Hibernate 依赖管理

Hibernate 的依赖管理已协调以考虑重定位：

- `hibernate-jpamodelgen` 被 `hibernate-processor` 替换
- `hibernate-proxool` 和 `hibernate-vibur` 不再发布

---

## 消息功能升级

### Kafka Streams 自定义

Spring Boot 的 `StreamBuilderFactoryBeanCustomizer` 已移除，以支持 Spring Kafka 的 `StreamsBuilderFactoryBeanConfigurer`。迁移到新的配置器时，请注意它实现了 `Ordered`，默认值为 0。

### Spring Kafka 重试功能

Spring Kafka 已将其重试功能从 Spring Retry 迁移到 Spring Framework。

因此，`spring.kafka.retry.topic.backoff.random` 已移除，以支持 `spring.kafka.retry.topic.backoff.jitter`。后者比前者提供更大的灵活性。有关更多详细信息，请参阅文档。

### Spring AMQP 重试功能

Spring AMQP 已将其重试功能从 Spring Retry 迁移到 Spring Framework。

Spring Boot 为 `RetryTemplate` 和消息监听器使用的重试功能提供了自定义钩子点。为了更明确，引入了两个专用的自定义器：`RabbitTemplateRetrySettingsCustomizer` 和 `RabbitListenerRetrySettingsCustomizer`。

如果您使用 `RabbitRetryTemplateCustomizer` 根据目标自定义重试设置，您将需要迁移到这些接口中的任何一个。

---

## IO 功能升级

### Spring Batch

Spring Batch 现在可以在没有数据库的情况下运行（即内存中），常规的 `spring-boot-starter-batch` 使用这种简化模式。

升级后，Spring Batch 将不再在现有数据库中存储元数据。您可以简化配置并使用这种新模式，或者恢复以前的安排。要改回使用数据库，您需要更改为 `spring-boot-starter-batch-jdbc`。

---

## 测试功能升级

### Mockito Captor 和 Mock 注解

Spring Boot 3.4 中弃用的 `MockitoTestExecutionListener` 已在此版本中移除。这可能会让少数用户感到意外，因为监听器只是间接使用，所以弃用警告很容易被忽略。

如果您发现带有 `@Mock` 或 `@Captor` 注解的字段没有按预期工作，您应该直接使用 Mockito 本身的 `MockitoExtension`。

### 使用 MockMVC 和 @SpringBootTest

使用 `@SpringBootTest` 注解将不再提供任何 MockMVC 支持。如果您想在测试中使用 MockMVC，您现在应该在测试类上添加 `@AutoConfigureMockMvc` 注解。

此外，HtmlUnit 特定设置现在已移至 `htmlUnit` 属性下。例如，在 Spring Boot 3.5 中，您可能使用了 `@AutoConfigureMockMvc(webClientEnabled=false, webDriverEnabled=false)` 来禁用 HtmlUnit 功能。在 Spring Boot 4.0 中，您将使用 `@AutoConfigureMockMvc(htmlUnit = @HtmlUnit(webClient = false, webDriver = false))`。

### 使用 WebClient 或 TestRestTemplate 和 @SpringBootTest

使用 `@SpringBootTest` 注解将不再提供任何 `WebClient` 或 `TestRestTemplate` beans。如果您想使用 `TestRestTemplate`，您应该在测试类上添加 `@AutoConfigureTestRestTemplate` 注解。还需要对 `org.springframework.boot:spring-boot-resttestclient` 和 `org.springframework.boot:spring-boot-restclient` 的依赖。

此外，您可能希望考虑使用新的 `RestTestClient` 类替换任何 `TestRestTemplate` 的使用。要配置这个，在测试类上添加 `@AutoConfigureRestTestClient` 注解。

### TestRestTemplate 失败

如果您的 `TestRestTemplate` 测试出现编译失败，请添加 `org.springframework.boot:spring-boot-resttestclient` 的测试范围依赖。包含该类的包也需要更新为 `org.springframework.boot.resttestclient.TestRestTemplate`。

还需要 `org.springframework.boot:spring-boot-restclient` 的运行时依赖。

### @PropertyMapping 注解

`@PropertyMapping` 注解已从 `org.springframework.boot.test.autoconfigure.properties` 包重新定位到 `org.springframework.boot.test.context`。此外，`skip` 属性现在采用 `org.springframework.boot.test.context.PropertyMapping.Skip` 而不是 `org.springframework.boot.test.autoconfigure.properties.Skip`。

### @MockBean 和 @SpyBean 弃用

Spring Boot 的 `@MockBean` 和 `@SpyBean` 支持已在此版本中弃用，以支持 `@MockitoBean` 和 `@MockitoSpyBean` 支持。您可以通过在测试中添加 `@SuppressWarnings("removal")` 来暂时解决此弃用，或者如果代码库中有很多出现，则使用自定义注解。

虽然解决弃用很有用，但这只是一种临时情况，因为 `@MockBean` 和 `@SpyBean` 支持将来会被移除。如果您的测试在测试类中使用 `@MockBean` 和 `@SpyBean` 作为字段，您可以考虑直接替换：

```java
@SpringBootTest
class ApplicationTests {

    @MockitoBean
    private GreetingService greetingService;

    @Test
    void check() {
        // ...
    }
}
```

行为在 Spring Framework 参考文档的名为 `@MockitoBean` 和 `@MockitoSpyBean` 的部分中有更详细的解释。

`@MockBean`/`@SpyBean` 和 `@MockitoBean`/`@MockitoSpyBean` 之间有一个关键区别。新注解允许在测试类中用作字段，但不允许在 `@Configuration` 类中使用。在 `@Configuration` 中使用这些是一种声明一组模拟 beans 而无需在不同测试类中重复这些字段的方法：

```java
@SpringBootTest
@Import(TestConfig.class)
class ApplicationTests {

    @Test
    void check() {
        // ...
    }
}

@TestConfiguration
public class TestConfig {
    @MockBean
    private UserService userService;

    @MockBean
    private OrderService orderService;

    @MockBean
    private PrintingService ps1;
}
```

相反，这些"共享模拟 beans"可以直接在测试类上声明，如下所示：

```java
@SpringBootTest
@MockitoBean(types = {OrderService.class, UserService.class})
@MockitoBean(name = "ps1", types = PrintingService.class)
class ApplicationTests {

    @Test
    void check() {
        // ...
    }
}
```

如果在测试类本身（或其层次结构中的任何超类）上声明这些不切实际，您还可以考虑自定义注解：

```java
@SpringBootTest
@SharedMocks
class ApplicationTests {

    @Test
    void check() {
        // ...
    }
}

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@MockitoBean(types = {OrderService.class, UserService.class})
@MockitoBean(name = "ps1", types = PrintingService.class)
public @interface SharedMocks {
}
```

---

## 总结

本文档涵盖了 Spring Boot 4.0 的所有主要变更。在升级之前，请确保：

1. ✅ 升级到最新的 Spring Boot 3.5.x 版本
2. ✅ 检查所有依赖项的兼容性
3. ✅ 确认系统要求（Java 17+）
4. ✅ 审查已弃用的 API 和功能
5. ✅ 规划迁移策略（直接迁移或使用经典 starters）
6. ✅ 检查相关 Spring 项目的发行说明

祝您升级顺利！
