# money-common-mail(邮件模块)

​		该模块提供邮件发送功能。相对于短信来说，邮件不用钱。项目中使用过两种邮件服务，分别是`QQ邮箱`和[shinak](https://github.com/shinak)搭的`shahow邮箱`服务。

> `shahow邮箱服务`因为是自建，当邮件内容过于简单时会被`QQ邮箱`拉进垃圾箱，测试时可到垃圾箱看看...

## 依赖

~~~xml
<!-- 邮件模块-->
<dependency>
    <groupId>com.money</groupId>
    <artifactId>money-common-mail</artifactId>
</dependency>
~~~

## 使用

**🌰`MailDemoController`**

## 功能设计

​		该模块十分简单，就是通过`EmailConfiguration`向容器注入一个`MailService`的Bean。而`MailService`只是对`JavaMailSender`进行封装，达到解耦并提供更简捷易用的api，也额外提供了一些辅助功能，如

### 拦截器`PostmanInterceptor`

​		`PostmanInterceptor` 是一个接口，用于声明邮件发送生命周期（**如发送前、发送成功、发送失败后**）做的一些操作。使用方式：

- 通过`MailService`的方法入参`MailRequest`传入一个`PostmanInterceptor`数组

### 异步发送（TODO）

​		大概思路是新增一个`sendMailAsync`的方法，通过配置的线程池或者传入线程进行异步发送，对于异步发送的结果刚好可以由拦截器处理。

## 相关配置

`shahow邮箱:此账号可以直接使用`

~~~yaml
money:
  # 邮件服务
  mail:
    host: smtp.shahow.top # 邮箱服务器
    username: qk-money@money.shahow.top
    password: Vv123456#
    properties:
      mail:
        smtp:
          auth: true 
          starttls: # 使用SSL安全协议，必须配置如下
            enable: true
            required: true
    port: 465  # 端口
    protocol: smtps # 协议
    default-encoding: utf-8
    fromAlias: 麦尼 # 发件人别名
~~~

`QQ邮箱`

~~~yaml
money:
  # 邮件服务
  mail:
    host: smtp.qq.com # 邮箱服务器
    username: 374648769@qq.com # 账号
    password:  # 授权码（得去邮箱获取）
    properties:
      mail:
        smtp:
          auth: true # 使用
          starttls: # 使用SSL安全协议，必须配置如下
            enable: true
            required: true
    port: 465  # 端口
    protocol: smtps # 协议
    default-encoding: utf-8
    fromAlias: 麦尼 # 发件人别名
~~~
