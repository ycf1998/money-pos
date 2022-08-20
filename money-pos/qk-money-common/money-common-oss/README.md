# money-common-oss(对象存储OSS模块)

​		该模块提供OSS（对象存储）的能力，通俗的讲就是文件上传下载的能力。目前已提供本地和七牛云这两个服务，可自由选择或一起使用。使用方式简单，注入`OSSDelegate<T>`Bean，根据泛型调用不同服务。

## 依赖

~~~xml
<!-- 对象存储OSS模块-->
<dependency>
    <groupId>com.money</groupId>
    <artifactId>money-common-oss</artifactId>
</dependency>
<!-- 七牛云（若使用七牛云需引入） -->
<dependency>
	<groupId>com.qiniu</groupId>
	<artifactId>qiniu-java-sdk</artifactId>
</dependency>
~~~

## 使用

**🌰`OssDemoController`**

## 相关配置

配置故意使用了如下的引入方式，所以相关配置写在对应的`oss.properties`文件，不喜欢就提出来和`application-money.yml`写一起就好了

```yaml
spring:
  config:
    # 引入对象存储的配置
    import: oss.properties
```

~~~properties
# ================================= 本地
# 目标空间
local.bucket = F:/qk-money/
# 资源处理器
local.resource-handler = /assets/**
# ================================= 七牛云
# 访问密钥
qiniu.access-key =
# 秘密密钥
qiniu.secret-key =
# 是否使用https
qiniu.use-https = false
# 访问域名
qiniu.domain = r3xou9o36.hn-bkt.clouddn.com
# 目标空间
qiniu.bucket = qk-money
# 区域
qiniu.region = huanan
# 令牌过期时间
qiniu.token-expire = 3600
# 上传策略 https://developer.qiniu.com/kodo/1206/put-policy
qiniu.policy.returnBody = {\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"fname\":\"$(x:fname)\",\"age\":\"$(x:age)\"}
~~~

## 参考

[Spring mvc传参](https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-multipart-forms)
