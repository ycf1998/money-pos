# money-common-cache - 缓存模块

## 概述

缓存模块提供本地缓存和 Redis 缓存两种实现方式，支持灵活切换。

**设计优势**：
- **双缓存支持**：本地缓存（Hutool）+ Redis 分布式缓存
- **灵活切换**：通过配置切换缓存策略
- **统一接口**：`LocalCache` 接口提供统一的缓存操作 API
- **Spring Cache 集成**：Redis 缓存支持 `@Cacheable` 等注解

## 依赖

```xml
<!-- 缓存模块 -->
<dependency>
    <groupId>com.money</groupId>
    <artifactId>money-common-cache</artifactId>
</dependency>

<!-- Redis 缓存需额外引入 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

## 功能特性

### 1. 本地缓存

基于 Hutool Cache，支持多种缓存策略：

| 策略 | 说明 | 适用场景 |
|------|------|----------|
| LRU | 最近最少使用 | 一般缓存（默认） |
| LFU | 最不常用 | 热点数据 |
| FIFO | 先进先出 | 队列场景 |
| TIMED | 定时过期 | 临时数据 |
| WEAK | 弱引用 | 内存敏感场景 |

### 2. Redis 缓存

集成 Spring Cache，支持 `@Cacheable`、`@CachePut`、`@CacheEvict` 等注解。

## 使用方式

### 本地缓存

注入 `LocalCache` 接口使用：

```java
@Autowired
private LocalCache localCache;

localCache.put("key", value);
Object value = localCache.get("key");
localCache.remove("key");
```

### Redis 缓存

启用 Redis 后，使用 Spring Cache 注解：

```java
@Cacheable(value = "user", key = "#id")
public User getUser(Long id) {
    return userRepository.findById(id);
}

@CacheEvict(value = "user", key = "#id")
public void deleteUser(Long id) {
    userRepository.deleteById(id);
}
```

## 配置说明

### 本地缓存配置

```yaml
money:
  cache:
    local:
      # 提供者：hutool
      provider: hutool
      hutool:
        # 失效策略：LRU、LFU、FIFO、TIMED、WEAK
        strategy: LRU
        # 容量
        capacity: 102400
        # 过期时间（ms），0 代表永不过期
        ttl: 0
```

### Redis 缓存配置

```yaml
money:
  cache:
    redis:
      enabled: false  # 开关，默认关闭
      ttl: 86400000   # 过期时间（ms）

spring:
  redis:
    host: localhost
    port: 6379
    password: 
    database: 0
```

## 核心类说明

| 类名 | 说明 |
|------|------|
| `LocalCache` | 本地缓存接口，定义基本缓存操作 |
| `HutoolCache` | 基于 Hutool Cache 的实现 |
| `LocalCacheConfiguration` | 本地缓存自动配置类 |

## 相关链接

- [Hutool Cache 文档](https://www.hutool.cn/docs/#/cache/%E6%A6%82%E8%BF%B0)
- [Spring Cache 文档](https://docs.spring.io/spring-framework/docs/current/reference/html/integration.html#cache)
