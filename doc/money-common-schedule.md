# 定时任务模块

​	该模块提供定时任务功能，使用第三方组件`XXL-JOB`，并没有做封装，模块仅事先配置好了配置类而已，所以参考官网文档开发即可，以下仅作简单使用介绍。

## 依赖

~~~xml
<!-- 定时任务模块 -->
<dependency>
    <groupId>com.money</groupId>
    <artifactId>money-common-schedule</artifactId>
</dependency>
~~~

## 使用

### 1. 调度中心xxl-job-admin

#### 1.1. 初始化sql脚本

执行工程下 `xxl-job-admin/doc/db/tables_xxl_job.sql`脚本，会创建一个`xxl_job`的数据库

#### 1.2. 修改配置

该工程就是一个普通的Spring Boot工程，配置都一样，主要关注xxl相关的配置

`application.yml`

~~~yml
xxl:
    job:
    	# 访问令牌，执行器工程需填写相同令牌
        accessToken: MONEY
        # 国际化
        i18n: zh_CN
        # 日志保留天数
        logretentiondays: 30
        # 任务线程池
        triggerpool:
            fast:
                max: 200
            slow:
                max: 100
~~~

### 2. 执行器工程money-app-biz

#### 2.1. 引入依赖

~~~xml
<!-- 定时任务模块 -->
<dependency>
    <groupId>com.money</groupId>
    <artifactId>money-common-schedule</artifactId>
</dependency>
~~~

#### 2.2. 配置

~~~yml
# 定时任务XXL-JOB
xxl:
  job:
    access-token: MONEY # XXL-JOB调度中心设置的token
    admin:
      address: http://127.0.0.1:8000/xxl-job-admin # XXL-JOB调度中心地址
    executor:
      app-name: ${spring.application.name} # 执行器名称
      address:
      ip: # 指定IP注册，不填自动获取
      port: 0 # 指定端口注册，填0自动获取
      log-path: log/xxl-job/jobhandler # 日志存储路径
      log-retention-days: 30 # 日志存储天数
~~~

### 3. XXL-JOB管理面板配置执行器

首先启动调度中心工程，访问调度中心：http://localhost:8000/xxl-job-admin，默认登录账号 “admin/123456”

配置执行器，appName要和执行器工程配置的appName相同，才能被自动发现到

![image-20220722222543189](money-common-schedule.assets/image-20220722222543189.png)

### 4. 配置任务

具体参考官网：https://www.xuxueli.com/xxl-job 中**三、任务详解** 部分，不做赘述。

## 相关配置

~~~yml
# 定时任务XXL-JOB
xxl:
  job:
    access-token: MONEY # XXL-JOB调度中心设置的token
    admin:
      address: http://127.0.0.1:8000/xxl-job-admin # XXL-JOB调度中心地址
    executor:
      app-name: ${spring.application.name} # 执行器名称
      address:
      ip: # 指定IP注册，不填自动获取
      port: 0 # 指定端口注册，填0自动获取
      log-path: log/xxl-job/jobhandler # 日志存储路径
      log-retention-days: 30 # 日志存储天数
~~~
