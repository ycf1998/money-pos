# SysPermissionController 测试用例覆盖

> 权限管理接口测试用例文档

---

## 基本信息

| 项目 | 内容 |
|------|------|
| Controller | `com.money.controller.SysPermissionController` |
| 测试类 | `com.money.system.controller.SysPermissionControllerTest` |
| 请求路径 | `/permissions` |
| 模块 | `money-app-system` |

---

## 操作的数据表

| 表名 | 操作类型 | 说明 |
|------|----------|------|
| `sys_permission` | CRUD | 资源权限表（主表） |

---

## 接口测试覆盖

| 接口 | 方法 | 权限 | 测试状态 | 测试用例 |
|------|------|------|------|----------|
| `/permissions` | GET | `permission:list` | ✅ | `testListPermissions`, `testListPermissionsByName`, `testListPermissionsByType` |
| `/permissions` | POST | `permission:add` | ✅ | `testAddPermissionSuccess`, `testAddDirPermission` |
| `/permissions` | PUT | `permission:edit` | ✅ | `testUpdatePermissionSuccess` |
| `/permissions` | DELETE | `permission:del` | ✅ | `testDeletePermissionSuccess`, `testDeletePermissionsBatch` |

---

## 测试用例详细列表

### 查询权限

| 用例 ID | 用例名称 | 测试方法 | 状态 | 备注 |
|---------|----------|----------|------|------|
| PERM-LIST-01 | 成功查询权限列表 | `testListPermissions` | ✅ | 验证返回树形结构 |
| PERM-LIST-02 | 成功按权限名称搜索 | `testListPermissionsByName` | ✅ | 验证模糊查询 |
| PERM-LIST-03 | 成功按权限类型筛选 | `testListPermissionsByType` | ✅ | 验证条件筛选 |

### 添加权限

| 用例 ID | 用例名称 | 测试方法 | 状态 | 备注 |
|---------|----------|----------|------|------|
| PERM-ADD-01 | 成功添加权限 | `testAddPermissionSuccess` | ✅ | 验证新增 BUTTON 类型权限 |
| PERM-ADD-02 | 成功添加目录权限 | `testAddDirPermission` | ✅ | 验证新增 DIR 类型权限 |

### 修改权限

| 用例 ID | 用例名称 | 测试方法 | 状态 | 备注 |
|---------|----------|----------|------|------|
| PERM-UPDATE-01 | 成功修改权限 | `testUpdatePermissionSuccess` | ✅ | 验证修改功能 |

### 删除权限

| 用例 ID | 用例名称 | 测试方法 | 状态 | 备注 |
|---------|----------|----------|------|------|
| PERM-DELETE-01 | 成功删除权限 | `testDeletePermissionSuccess` | ✅ | 验证单个删除 |
| PERM-DELETE-02 | 成功批量删除权限 | `testDeletePermissionsBatch` | ✅ | 验证批量删除 |

---

## 测试覆盖率统计

| 统计项 | 数量 | 百分比 |
|--------|------|--------|
| 接口总数 | 4 | 100% |
| 已覆盖接口 | 4 | 100% |
| 测试用例总数 | 8 | - |
| 通过用例数 | 8 | 100% |

---

> 更新时间：2026-03-30
