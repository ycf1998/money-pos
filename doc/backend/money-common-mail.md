# money-common-mail - 邮件模块

## 概述

邮件模块提供邮件发送功能，对 `JavaMailSender` 进行封装，提供解耦且易用的 API。

**设计优势**：
- **统一接口**：`MailService` 提供简洁的邮件发送 API
- **构建器模式**：`MailRequest` 链式构建邮件请求
- **拦截器支持**：`PostmanInterceptor` 拦截邮件发送生命周期（发送前、成功、失败）
- **灵活配置**：支持多种邮箱服务商配置

## 依赖

```xml
<!-- 邮件模块 -->
<dependency>
    <groupId>com.money</groupId>
    <artifactId>money-common-mail</artifactId>
</dependency>
```

## 使用方式

### 基本使用

注入 `MailService` 使用：

```java
@Autowired
private MailService mailService;

MailRequest mailRequest = MailRequest.builder()
    .to("recipient@example.com")
    .subject("邮件主题")
    .content("邮件内容")
    .html(false)
    .build();

boolean success = mailService.send(mailRequest);
```

### 发送 HTML 邮件

```java
MailRequest mailRequest = MailRequest.builder()
    .to("recipient@example.com")
    .subject("HTML 邮件")
    .content("<h1>欢迎</h1><p>这是一封 HTML 邮件</p>")
    .html(true)
    .build();

mailService.send(mailRequest);
```

### 带附件的邮件

```java
File file = new File("/path/to/file.pdf");
MailRequest mailRequest = MailRequest.builder()
    .to("recipient@example.com")
    .subject("带附件的邮件")
    .content("请查收附件")
    .attachment("文件说明", file)
    .build();

mailService.send(mailRequest);
```

### 使用拦截器

```java
PostmanInterceptor interceptor = new PostmanInterceptor() {
    @Override
    public void beforeSending(MailRequest request) {
        log.info("准备发送邮件：{}", request.getTo());
    }

    @Override
    public void sendSucceeded(MailRequest request) {
        log.info("邮件发送成功：{}", request.getTo());
    }

    @Override
    public void sendFailed(MailRequest request, Exception e) {
        log.error("邮件发送失败：{}", request.getTo(), e);
    }
};

MailRequest mailRequest = MailRequest.builder()
    .to("recipient@example.com")
    .subject("测试邮件")
    .content("测试内容")
    .interceptor(interceptor)
    .build();

mailService.send(mailRequest);
```

## 配置说明

```yaml
money:
  mail:
    # 邮箱服务器
    host: smtp.qq.com
    # 账号
    username: your-email@qq.com
    # 密码（QQ 邮箱为授权码）
    password: your-auth-code
    # 端口
    port: 465
    # 协议
    protocol: smtps
    # 默认编码
    default-encoding: utf-8
    # 发件人别名
    from-alias: 麦尼
    # 额外配置
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
            required: true
```

### 常见邮箱服务商

| 服务商 | host | 说明 |
|--------|------|------|
| QQ 邮箱 | smtp.qq.com | 需使用授权码 |
| 163 邮箱 | smtp.163.com | 需使用授权码 |
| Gmail | smtp.gmail.com | 需使用应用专用密码 |

## 核心类说明

| 类名/接口 | 说明 |
|-----------|------|
| `MailService` | 邮件服务接口 |
| `MailServiceImpl` | 邮件服务实现（封装 `JavaMailSender`） |
| `MailRequest` | 邮件请求构建器 |
| `PostmanInterceptor` | 邮件发送拦截器接口 |
| `EmailConfiguration` | 邮件自动配置类 |

## 注意事项

1. **授权码**：QQ 邮箱、163 邮箱等需使用授权码而非登录密码
2. **SSL 配置**：使用 465 端口时需配置 `starttls.enable=true`
3. **垃圾箱**：自建邮箱服务发送简单邮件可能被拉入垃圾箱
4. **频率限制**：注意邮箱服务商的发送频率限制

## 相关链接

- [JavaMail API 文档](https://javaee.github.io/javamail/)
- [QQ 邮箱 SMTP 服务](https://service.mail.qq.com/detail/0/75)
