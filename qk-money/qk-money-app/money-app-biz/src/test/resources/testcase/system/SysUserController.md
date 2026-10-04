# SysUserController 测试用例覆盖

> 用户管理接口测试用例文档

---

## 基本信息

| 项目 | 内容 |
|------|------|
| Controller | `com.money.controller.SysUserController` |
| 测试类 | `com.money.system.controller.SysUserControllerTest` |
| 请求路径 | `/users` |
| 模块 | `money-app-system` |

---

## 操作的数据表

| 表名 | 操作类型 | 说明 |
|------|----------|------|
| `sys_user` | CRUD | 用户表（主表） |
| `sys_user_role_relation` | CRUD | 用户 - 角色关联表 |

---

## 接口测试覆盖

| 接口 | 方法 | 权限 | 测试状态 | 测试用例 |
|------|------|------|------|----------|
| `/users` | GET | `user:list` | ✅ | `testListUsers`, `testListUsersByName`, `testListUsersByEnabled`, `testListUsersOrderByCreateTime` |
| `/users` | POST | `user:add` | ✅ | `testAddUserSuccess` |
| `/users` | PUT | `user:edit` | ✅ | `testUpdateUserSuccess` |
| `/users` | DELETE | `user:del` | ✅ | `testDeleteUserSuccess`, `testDeleteUsersBatch` |
| `/users/updateProfile` | POST | - | ✅ | `testUpdateProfileSuccess` |
| `/users/changePassword` | POST | - | ✅ | `testChangePasswordSuccess` |
| `/users/uploadAvatar` | POST | - | ✅ | `testUploadAvatarSuccess` |

---

## 测试用例详细列表

### 分页查询用户

| 用例 ID | 用例名称 | 测试方法 | 状态 | 备注 |
|---------|----------|----------|------|------|
| USER-LIST-01 | 成功分页查询用户列表 | `testListUsers` | ✅ | 验证分页功能 |
| USER-LIST-02 | 成功按用户名搜索用户 | `testListUsersByName` | ✅ | 验证模糊查询 |
| USER-LIST-03 | 成功按启用状态筛选用户 | `testListUsersByEnabled` | ✅ | 验证条件筛选 |
| USER-LIST-04 | 成功按创建时间排序 | `testListUsersOrderByCreateTime` | ✅ | 验证排序功能 |

### 添加用户

| 用例 ID | 用例名称 | 测试方法 | 状态 | 备注 |
|---------|----------|----------|------|------|
| USER-ADD-01 | 成功添加用户 | `testAddUserSuccess` | ✅ | 验证新增功能 |

### 修改用户

| 用例 ID | 用例名称 | 测试方法 | 状态 | 备注 |
|---------|----------|----------|------|------|
| USER-UPDATE-01 | 成功修改用户 | `testUpdateUserSuccess` | ✅ | 验证修改功能 |

### 删除用户

| 用例 ID | 用例名称 | 测试方法 | 状态 | 备注 |
|---------|----------|----------|------|------|
| USER-DELETE-01 | 成功删除用户 | `testDeleteUserSuccess` | ✅ | 验证单个删除 |
| USER-DELETE-02 | 成功批量删除用户 | `testDeleteUsersBatch` | ✅ | 验证批量删除 |

### 更新个人资料

| 用例 ID | 用例名称 | 测试方法 | 状态 | 备注 |
|---------|----------|----------|------|------|
| USER-PROFILE-01 | 成功更新个人资料 | `testUpdateProfileSuccess` | ✅ | 验证个人资料更新 |

### 修改密码

| 用例 ID | 用例名称 | 测试方法 | 状态 | 备注 |
|---------|----------|----------|------|------|
| USER-PASSWORD-01 | 成功修改密码 | `testChangePasswordSuccess` | ✅ | 验证密码修改，包含新密码登录验证 |

### 上传头像

| 用例 ID | 用例名称 | 测试方法 | 状态 | 备注 |
|---------|----------|----------|------|------|
| USER-AVATAR-01 | 成功上传头像 | `testUploadAvatarSuccess` | ✅ | 验证头像上传功能 |

---

## 测试覆盖率统计

| 统计项 | 数量 | 百分比 |
|--------|------|--------|
| 接口总数 | 7 | 100% |
| 已覆盖接口 | 7 | 100% |
| 测试用例总数 | 11 | - |
| 通过用例数 | 11 | 100% |
| 禁用用例数 | 0 | 0% |

---

> 更新时间：2026-03-30
