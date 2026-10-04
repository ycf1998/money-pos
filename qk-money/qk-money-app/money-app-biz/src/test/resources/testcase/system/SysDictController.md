# SysDictController 测试用例覆盖

> 字典管理接口测试用例文档

---

## 基本信息

| 项目 | 内容 |
|------|------|
| Controller | `com.money.controller.SysDictController` |
| 测试类 | `com.money.system.controller.SysDictControllerTest` |
| 请求路径 | `/dict` |
| 模块 | `money-app-system` |

---

## 操作的数据表

| 表名 | 操作类型 | 说明 |
|------|----------|------|
| `sys_dict` | CRUD | 字典表（主表） |
| `sys_dict_detail` | CRUD | 字典详情表 |

---

## 接口测试覆盖

| 接口 | 方法 | 权限 | 测试状态 | 测试用例 |
|------|------|------|------|----------|
| `/dict` | GET | `dict:list` | ✅ | `testListDicts`, `testListDictsByName` |
| `/dict` | POST | `dict:add` | ✅ | `testAddDictSuccess` |
| `/dict` | PUT | `dict:edit` | ✅ | `testUpdateDictSuccess` |
| `/dict` | DELETE | `dict:del` | ✅ | `testDeleteDictSuccess`, `testDeleteDictsBatch` |
| `/dict/detail` | GET | - | ✅ | `testGetDictDetail` |
| `/dict/detail` | POST | `dict:edit` | ✅ | `testAddDictDetailSuccess` |
| `/dict/detail` | PUT | `dict:edit` | ✅ | `testUpdateDictDetailSuccess` |
| `/dict/detail` | DELETE | `dict:edit` | ✅ | `testDeleteDictDetailSuccess`, `testDeleteDictDetailsBatch` |

---

## 测试用例详细列表

### 分页查询字典

| 用例 ID | 用例名称 | 测试方法 | 状态 | 备注 |
|---------|----------|----------|------|------|
| DICT-LIST-01 | 成功分页查询字典列表 | `testListDicts` | ✅ | 验证分页功能 |
| DICT-LIST-02 | 成功按字典名称/描述搜索 | `testListDictsByName` | ✅ | 验证模糊查询 |

### 添加字典

| 用例 ID | 用例名称 | 测试方法 | 状态 | 备注 |
|---------|----------|----------|------|------|
| DICT-ADD-01 | 成功添加字典 | `testAddDictSuccess` | ✅ | 验证新增功能 |

### 修改字典

| 用例 ID | 用例名称 | 测试方法 | 状态 | 备注 |
|---------|----------|----------|------|------|
| DICT-UPDATE-01 | 成功修改字典 | `testUpdateDictSuccess` | ✅ | 验证修改功能 |

### 删除字典

| 用例 ID | 用例名称 | 测试方法 | 状态 | 备注 |
|---------|----------|----------|------|------|
| DICT-DELETE-01 | 成功删除字典 | `testDeleteDictSuccess` | ✅ | 验证单个删除 |
| DICT-DELETE-02 | 成功批量删除字典 | `testDeleteDictsBatch` | ✅ | 验证批量删除 |

### 获取字典详情

| 用例 ID | 用例名称 | 测试方法 | 状态 | 备注 |
|---------|----------|----------|------|------|
| DICT-DETAIL-GET-01 | 成功获取字典详情 | `testGetDictDetail` | ✅ | 验证获取字典详情 |

### 添加字典详情

| 用例 ID | 用例名称 | 测试方法 | 状态 | 备注 |
|---------|----------|----------|------|------|
| DICT-DETAIL-ADD-01 | 成功添加字典详情 | `testAddDictDetailSuccess` | ✅ | 验证新增字典详情 |

### 修改字典详情

| 用例 ID | 用例名称 | 测试方法 | 状态 | 备注 |
|---------|----------|----------|------|------|
| DICT-DETAIL-UPDATE-01 | 成功修改字典详情 | `testUpdateDictDetailSuccess` | ✅ | 验证修改字典详情 |

### 删除字典详情

| 用例 ID | 用例名称 | 测试方法 | 状态 | 备注 |
|---------|----------|----------|------|------|
| DICT-DETAIL-DELETE-01 | 成功删除字典详情 | `testDeleteDictDetailSuccess` | ✅ | 验证单个删除 |
| DICT-DETAIL-DELETE-02 | 成功批量删除字典详情 | `testDeleteDictDetailsBatch` | ✅ | 验证批量删除 |

---

## 测试覆盖率统计

| 统计项 | 数量 | 百分比 |
|--------|------|--------|
| 接口总数 | 8 | 100% |
| 已覆盖接口 | 8 | 100% |
| 测试用例总数 | 11 | - |
| 通过用例数 | 11 | 100% |

---

> 更新时间：2026-03-30
