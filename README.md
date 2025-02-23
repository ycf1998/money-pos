<h1 align="center">💰 麦尼收银系统 </h1>

<p align="center">Money - Spring Boot 2.7 & VueJS 3 Cashier System</p>

> 原 Vue2 版本请切换到 `main-vue2` 分支。

## 🚀 项目介绍

麦尼收银系统是基于自制快速后台开发框架 [QK-MONEY](https://github.com/ycf1998/qk-money) 开发的一款收银系统，适用于多店铺管理。系统支持会员管理、商品管理、订单管理等功能，并集成了小票打印等实用特性。

[👉 前往体验](http://114.132.70.84/money-pos-demo?tenant=M)

## ✨ 功能特性

- **📊 首页报表**：提供简单的销售数据统计展示。
- **⚙️ 系统管理**：
  - 用户管理、角色管理、权限管理
  - 字典管理、租户管理（支持多店铺）
- **💵 收银台**：支持小票打印。
- **👤 会员管理**：会员信息管理。
- **🛍️ 商品管理**：商品分类、品牌管理。
- **📦 订单管理**：订单查询、订单详情。

## 🛠️ 技术栈

### 后端技术栈

| 依赖         | 版本   |
| ------------ | ------ |
| Spring Boot  | 2.7.18 |
| MyBatis-Plus | 3.5.7  |
| Hutool       | 5.8.34 |
| JJWT         | 0.11.5 |
| Jackson      | 2.13.5 |
| Spring Doc   | 1.8.0  |
| Qiniu        | 7.13.1 |
| XXL-JOB      | 2.3.1  |

### 前端技术栈

- [Vue 3](https://github.com/vuejs/vue-next) - 渐进式 JavaScript 框架。
- [Vue Router](https://router.vuejs.org/zh/) - 官方路由管理器。
- [Pinia](https://pinia.vuejs.org/zh/) - 轻量级状态管理库。
- [Element Plus](https://element-plus.org/zh-CN) - 基于 Vue 3 的 UI 组件库。
- [TailwindCSS](https://tailwindcss.com/) - 实用优先的 CSS 框架。
- [Vite](https://vitejs.dev/) - 下一代前端构建工具。

## 📋 环境要求

- **JDK**：1.8+
- **MySQL**：8+
- **Maven**：3.8+
- **Node.js**：16+

## 🚀 快速开始

### 1. 克隆项目

```bash
git clone https://github.com/ycf1998/money-pos.git
```

### 2. 后端配置与启动

1. **初始化数据库**：
   - 执行 `money_pos.sql` 脚本创建数据库。
   - 如果使用 MySQL 8 以下版本，请将脚本中的 `utf8mb4` 替换为 `utf8`，`utf8mb4_general_ci` 替换为 `utf8_general_ci`。

2. **修改数据库配置**：
   - 打开 [`application-dev.yml`](https://github.com/ycf1998/money-pos/blob/main/money-pos/qk-money-app/money-app-biz/src/main/resources/application-dev.yml)，修改数据库连接信息：

   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://127.0.0.1:3306/money_pos?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2b8&allowPublicKeyRetrieval=true&useSSL=false
       username: root
       password: root
   ```

3. **启动应用**：
   - 运行 [`QkMoneyApplication`](https://github.com/ycf1998/money-pos/blob/main/money-pos/qk-money-app/money-app-biz/src/main/java/com/money/QkMoneyApplication.java) 启动后端服务。

### 3. 前端配置与启动

1. **进入前端目录**：

   ```bash
   cd money-pos-web
   ```

2. **安装依赖**：

   ```bash
   npm install
   ```

3. **启动前端服务**：

   ```bash
   npm run dev
   ```

##  🖼️ 系统截图

![首页](README.assets/image-20231022213735611.png)
![收银台](README.assets/image-20231022213834132.png)
![收银](README.assets/image-20231022213930361.png)
![小票打印](README.assets/image-20231022214227802.png)
![会员管理](README.assets/image-20231022214014149.png)
![商品管理](README.assets/image-20231022214026761.png)
![订单管理](README.assets/image-20231022214054581.png)
![订单详情](README.assets/image-20231022214119628.png)
