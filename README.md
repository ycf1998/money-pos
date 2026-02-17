<h1 align="center">💰 麦尼收银系统</h1>

<p align="center">
  <strong>Money - 基于 Spring Boot 2.7 & VueJS 3 的多店铺收银系统</strong>
</p>

<p align="center">
  <a href="https://spring.io/projects/spring-boot" target="_blank">
    <img src="https://img.shields.io/badge/Spring%20Boot-2.7.18-6DB33F?style=flat-square&logo=spring-boot" alt="Spring Boot">
  </a>
  <a href="https://vuejs.org/" target="_blank">
    <img src="https://img.shields.io/badge/Vue-3.x-4FC08D?style=flat-square&logo=vue.js" alt="Vue">
  </a>
  <a href="https://element-plus.org/" target="_blank">
    <img src="https://img.shields.io/badge/Element%20Plus-latest-409EFF?style=flat-square" alt="Element Plus">
  </a>
  <a href="https://baomidou.com/" target="_blank">
    <img src="https://img.shields.io/badge/MyBatis--Plus-3.5.7-0078D4?style=flat-square" alt="MyBatis-Plus">
  </a>
  <a href="https://www.oracle.com/java/" target="_blank">
    <img src="https://img.shields.io/badge/JDK-1.8+-E76F02?style=flat-square&logo=openjdk" alt="JDK">
  </a>
  <a href="https://opensource.org/licenses/MIT" target="_blank">
    <img src="https://img.shields.io/badge/License-MIT-yellow?style=flat-square" alt="License">
  </a>
</p>

<p align="center">
  <a href="http://114.132.70.84/money-pos-demo?tenant=M">👉 在线体验</a>
  ·
  <a href="#快速开始">🚀 快速开始</a>
  ·
  <a href="#技术栈">🛠️ 技术栈</a>
</p>

---

## 📖 项目简介

麦尼收银系统是一款基于自制快速后台开发框架 [QK-MONEY](https://github.com/ycf1998/qk-money) 开发的多店铺收银系统。系统支持**会员管理**、**商品管理**、**订单管理**等功能，并集成了**小票打印**等实用特性，适用于零售、餐饮等多种场景。

> 💡 **提示**：原 Vue2 版本请切换到 `main-vue2` 分支。

---

## ✨ 核心功能

| 模块 | 功能描述 |
|:---:|---|
| 📊 **首页报表** | 简单销售数据统计 |
| 🛒 **收银台** | 快速收银、小票打印、多种支付方式 |
| 👥 **会员管理** | 会员信息、积分管理、会员等级 |
| 🛍️ **商品管理** | 商品分类、品牌管理、库存管理 |
| 📦 **订单管理** | 订单查询、订单详情、订单统计 |
| ⚙️ **系统管理** | 用户管理、角色管理、权限管理、字典管理、租户管理 |

---

<a id="技术栈"></a>
## 🛠️ 技术栈

### 后端技术

| 技术 | 版本 | 说明 |
|:---:|:---:|---|
| **Spring Boot** | 2.7.18 | 核心框架 |
| **MyBatis-Plus** | 3.5.7 | ORM 持久层框架 |
| **Hutool** | 5.8.34 | Java 工具类库 |
| **JJWT** | 0.11.5 | JWT 令牌认证 |
| **Jackson** | 2.13.5 | JSON 序列化 |
| **Spring Doc** | 1.8.0 | API 文档 |
| **Qiniu** | 7.13.1 | 七牛云存储 |
| **XXL-JOB** | 2.3.1 | 分布式任务调度 |

### 前端技术

| 技术 | 说明 |
|:---:|---|
| **Vue 3** | 渐进式 JavaScript 框架 |
| **Vue Router** | 官方路由管理器 |
| **Pinia** | 轻量级状态管理库 |
| **Element Plus** | 基于 Vue 3 的 UI 组件库 |
| **TailwindCSS** | 实用优先的 CSS 框架 |
| **Vite** | 下一代前端构建工具 |

---

## 📋 环境要求

| 环境 | 版本要求 |
|:---:|:---:|
| **JDK** | 1.8+ |
| **MySQL** | 8+ |
| **Maven** | 3.8+ |
| **Node.js** | 16+ |

---

<a id="快速开始"></a>
## 🚀 快速开始

### 1️⃣ 克隆项目

```bash
git clone https://github.com/ycf1998/money-pos.git
cd money-pos
```

### 2️⃣ 初始化数据库

执行 `money_pos.sql` 脚本创建数据库：

```bash
mysql -u root -p < money_pos.sql
```

> ⚠️ **注意**：如果使用 MySQL 8 以下版本，请将脚本中的 `utf8mb4` 替换为 `utf8`，`utf8mb4_general_ci` 替换为 `utf8_general_ci`。

### 3️⃣ 配置数据库连接

编辑 [`application-dev.yml`](money-pos/qk-money-app/money-app-biz/src/main/resources/application-dev.yml)：

```yaml
spring:
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/money_pos?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2b8&allowPublicKeyRetrieval=true&useSSL=false
    username: root
    password: your_password
```

### 4️⃣ 启动后端服务

运行 [`QkMoneyApplication`](money-pos/qk-money-app/money-app-biz/src/main/java/com/money/QkMoneyApplication.java) 启动后端服务。

### 5️⃣ 启动前端服务

```bash
cd money-pos-web
npm install
npm run dev
```

---

## 🖼️ 系统截图

<div align="center">
  <h3>📊 首页仪表盘</h3>
  <img src="README.assets/image-20231022213735611.png" width="80%" alt="首页" style="border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.1);">
  
  <h3>🛒 收银台</h3>
  <img src="README.assets/image-20231022213834132.png" width="80%" alt="收银台" style="border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.1);">
  
  <h3>💳 收银结算</h3>
  <img src="README.assets/image-20231022213930361.png" width="80%" alt="收银" style="border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.1);">
  
  <h3>🧾 小票打印</h3>
  <img src="README.assets/image-20231022214227802.png" width="80%" alt="小票打印" style="border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.1);">
  
  <h3>👥 会员管理</h3>
  <img src="README.assets/image-20231022214014149.png" width="80%" alt="会员管理" style="border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.1);">
  
  <h3>🛍️ 商品管理</h3>
  <img src="README.assets/image-20231022214026761.png" width="80%" alt="商品管理" style="border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.1);">
  
  <h3>📦 订单管理</h3>
  <img src="README.assets/image-20231022214054581.png" width="80%" alt="订单管理" style="border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.1);">
  
  <h3>📋 订单详情</h3>
  <img src="README.assets/image-20231022214119628.png" width="80%" alt="订单详情" style="border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.1);">
</div>

---

<p align="center">
  <strong>🎉 感谢使用麦尼收银系统！</strong>
</p>
