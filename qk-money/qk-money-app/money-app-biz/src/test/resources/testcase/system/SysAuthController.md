# SysAuthController 测试用例覆盖

> 认证管理接口测试用例文档

---

## 基本信息

| 项目 | 内容 |
|------|------|
| Controller | `com.money.controller.SysAuthController` |
| 测试类 | `com.money.system.controller.SysAuthControllerTest` |
| 请求路径 | `/auth` |
| 模块 | `money-app-system` |

---

## 操作的数据表

| 表名 | 操作类型 | 说明 |
|------|----------|------|
| `sys_user` | SELECT | 用户表（验证登录） |
| `sys_user_role_relation` | SELECT | 用户 - 角色关联表（获取角色） |
| `sys_role` | SELECT | 角色表（获取角色信息） |
| `sys_role_permission_relation` | SELECT | 角色 - 权限关联表（获取权限） |
| `sys_permission` | SELECT | 权限表（获取权限信息） |

---

## 接口测试覆盖

| 接口 | 方法 | 权限 | 测试状态 | 测试用例 |
|------|------|------|------|----------|
| `/auth/login` | POST | - | ✅ | `testLogin` |
| `/auth/logout` | POST | - | ✅ | `testLogout` |
| `/auth/refreshToken` | GET | - | ✅ | `testRefreshToken` |
| `/auth/router` | GET | - | ✅ | `testGetVueRouter` |
| `/auth/own` | GET | - | ✅ | `testGetOwnInfo` |

---

## 测试用例详细列表

### 登录

| 用例 ID | 用例名称 | 测试方法 | 状态 | 备注 |
|---------|----------|----------|------|------|
| AUTH-LOGIN-01 | 成功登录 | `testLogin` | ✅ | 验证登录功能，返回 token |

### 登出

| 用例 ID | 用例名称 | 测试方法 | 状态 | 备注 |
|---------|----------|----------|------|------|
| AUTH-LOGOUT-01 | 成功登出 | `testLogout` | ✅ | 验证登出功能 |

### 刷新 Token

| 用例 ID | 用例名称 | 测试方法 | 状态 | 备注 |
|---------|----------|----------|------|------|
| AUTH-REFRESH-01 | 成功刷新 Token | `testRefreshToken` | ✅ | 验证刷新 token 功能 |

### 获取路由菜单

| 用例 ID | 用例名称 | 测试方法 | 状态 | 备注 |
|---------|----------|----------|------|------|
| AUTH-ROUTER-01 | 成功获取 Vue 路由菜单 | `testGetVueRouter` | ✅ | 验证获取路由菜单 |

### 获取个人信息

| 用例 ID | 用例名称 | 测试方法 | 状态 | 备注 |
|---------|----------|----------|------|------|
| AUTH-INFO-01 | 成功获取个人信息 | `testGetOwnInfo` | ✅ | 验证获取个人信息 |

---

## 测试覆盖率统计

| 统计项 | 数量 | 百分比 |
|--------|------|--------|
| 接口总数 | 5 | 100% |
| 已覆盖接口 | 5 | 100% |
| 测试用例总数 | 5 | - |
| 通过用例数 | 5 | 100% |

---

> 更新时间：2026-03-30
