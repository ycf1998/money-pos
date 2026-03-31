# SysRoleController 测试用例覆盖

> 角色管理接口测试用例文档

---

## 基本信息

| 项目 | 内容 |
|------|------|
| Controller | `com.money.controller.SysRoleController` |
| 测试类 | `com.money.system.controller.SysRoleControllerTest` |
| 请求路径 | `/roles` |
| 模块 | `money-app-system` |

---

## 操作的数据表

| 表名 | 操作类型 | 说明 |
|------|----------|------|
| `sys_role` | CRUD | 角色表（主表） |
| `sys_role_permission_relation` | CRUD | 角色 - 权限关联表 |
| `sys_user_role_relation` | DELETE | 用户 - 角色关联表（删除角色时级联清理） |

---

## 接口测试覆盖

| 接口 | 方法 | 权限 | 测试状态 | 测试用例 |
|------|------|------|----------|----------|
| `/roles/all` | GET | `role:list` | ✅ | `testGetAllRoles` |
| `/roles` | GET | `role:list` | ✅ | `testListRoles`, `testListRolesByRoleCode`, `testListRolesByEnabled` |
| `/roles` | POST | `role:add` | ✅ | `testAddRoleSuccess` |
| `/roles` | PUT | `role:edit` | ✅ | `testUpdateRoleSuccess` |
| `/roles` | DELETE | `role:del` | ✅ | `testDeleteRoleSuccess`, `testDeleteRolesBatch` |
| `/roles/{id}/permission` | POST | `role:edit` | ✅ | `testConfigurePermissionsSuccess`, `testConfigurePermissionsEmpty` |

---

## 测试用例详细列表

### 获取所有角色

| 用例 ID | 用例名称 | 测试方法 | 状态 | 备注 |
|---------|----------|----------|------|------|
| ROLE-ALL-01 | 成功获取所有角色列表 | `testGetAllRoles` | ✅ | 验证返回数据非空 |

### 分页查询角色

| 用例 ID | 用例名称 | 测试方法 | 状态 | 备注 |
|---------|----------|----------|------|------|
| ROLE-LIST-01 | 成功分页查询角色列表 | `testListRoles` | ✅ | 验证分页参数 |
| ROLE-LIST-02 | 成功按角色编码搜索角色 | `testListRolesByRoleCode` | ✅ | 验证模糊查询 |
| ROLE-LIST-03 | 成功按启用状态筛选角色 | `testListRolesByEnabled` | ✅ | 验证条件筛选 |

### 添加角色

| 用例 ID | 用例名称 | 测试方法 | 状态 | 备注 |
|---------|----------|----------|------|------|
| ROLE-ADD-01 | 成功添加角色 | `testAddRoleSuccess` | ✅ | 验证新增功能 |

### 修改角色

| 用例 ID | 用例名称 | 测试方法 | 状态 | 备注 |
|---------|----------|----------|------|------|
| ROLE-UPDATE-01 | 成功修改角色 | `testUpdateRoleSuccess` | ✅ | 验证修改功能 |

### 删除角色

| 用例 ID | 用例名称 | 测试方法 | 状态 | 备注 |
|---------|----------|----------|------|------|
| ROLE-DELETE-01 | 成功删除角色 | `testDeleteRoleSuccess` | ✅ | 验证单个删除 |
| ROLE-DELETE-02 | 成功批量删除角色 | `testDeleteRolesBatch` | ✅ | 验证批量删除 |

### 配置权限

| 用例 ID | 用例名称 | 测试方法 | 状态 | 备注 |
|---------|----------|----------|------|------|
| ROLE-PERMISSION-01 | 成功配置角色权限 | `testConfigurePermissionsSuccess` | ✅ | 验证权限配置 |
| ROLE-PERMISSION-02 | 成功清空角色权限 | `testConfigurePermissionsEmpty` | ✅ | 验证权限清空 |

---

## 测试覆盖率统计

| 统计项 | 数量 | 百分比 |
|--------|------|--------|
| 接口总数 | 6 | 100% |
| 已覆盖接口 | 6 | 100% |
| 测试用例总数 | 10 | - |
| 通过用例数 | 10 | 100% |

---

> 更新时间：2026-03-30
