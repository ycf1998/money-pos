# UmsMemberController 测试用例覆盖

> 会员管理接口测试用例文档

---

## 基本信息

| 项目 | 内容 |
|------|------|
| Controller | `com.money.controller.UmsMemberController` |
| 测试类 | `com.money.controller.UmsMemberControllerTest` |
| 请求路径 | `/ums/member` |
| 模块 | `money-app-biz` |

---

## 操作的数据表

| 表名 | 操作类型 | 说明 |
|------|----------|------|
| `ums_member` | CRUD | 会员表（主表） |

---

## 接口测试覆盖

| 接口 | 方法 | 权限 | 测试状态 | 测试用例 |
|------|------|------|------|----------|
| `/ums/member` | GET | `umsMember:list` | ✅ | `testListMembers`, `testListMembersByName`, `testListMembersByType`, `testListMembersOrderByConsumeAmount` |
| `/ums/member` | POST | `umsMember:add` | ✅ | `testAddMemberSuccess` |
| `/ums/member` | PUT | `umsMember:edit` | ✅ | `testUpdateMemberSuccess` |
| `/ums/member` | DELETE | `umsMember:del` | ✅ | `testDeleteMemberSuccess`, `testDeleteMembersBatch` |

---

## 测试用例详细列表

### 分页查询会员

| 用例 ID | 用例名称 | 测试方法 | 状态 | 备注 |
|---------|----------|----------|------|------|
| MEMBER-LIST-01 | 成功分页查询会员列表 | `testListMembers` | ✅ | 验证分页功能 |
| MEMBER-LIST-02 | 成功按会员名称搜索 | `testListMembersByName` | ✅ | 验证模糊查询 |
| MEMBER-LIST-03 | 成功按会员类型筛选 | `testListMembersByType` | ✅ | 验证条件筛选 |
| MEMBER-LIST-04 | 成功按消费金额排序 | `testListMembersOrderByConsumeAmount` | ✅ | 验证排序功能 |

### 添加会员

| 用例 ID | 用例名称 | 测试方法 | 状态 | 备注 |
|---------|----------|----------|------|------|
| MEMBER-ADD-01 | 成功添加会员 | `testAddMemberSuccess` | ✅ | 验证新增功能 |

### 修改会员

| 用例 ID | 用例名称 | 测试方法 | 状态 | 备注 |
|---------|----------|----------|------|------|
| MEMBER-UPDATE-01 | 成功修改会员 | `testUpdateMemberSuccess` | ✅ | 验证修改功能 |

### 删除会员

| 用例 ID | 用例名称 | 测试方法 | 状态 | 备注 |
|---------|----------|----------|------|------|
| MEMBER-DELETE-01 | 成功删除会员 | `testDeleteMemberSuccess` | ✅ | 验证单个删除 |
| MEMBER-DELETE-02 | 成功批量删除会员 | `testDeleteMembersBatch` | ✅ | 验证批量删除 |

---

## 测试覆盖率统计

| 统计项 | 数量 | 百分比 |
|--------|------|--------|
| 接口总数 | 4 | 100% |
| 已覆盖接口 | 4 | 100% |
| 测试用例总数 | 9 | - |
| 通过用例数 | 9 | 100% |
| 禁用用例数 | 0 | 0% |

---

> 更新时间：2026-04-03
> 创建人：AI Assistant
