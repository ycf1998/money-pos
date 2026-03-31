# GmsGoodsCategoryController 测试用例覆盖

> 商品分类接口测试用例文档

---

## 基本信息

| 项目 | 内容 |
|------|------|
| Controller | `com.money.controller.GmsGoodsCategoryController` |
| 测试类 | `com.money.controller.GmsGoodsCategoryControllerTest` |
| 请求路径 | `/gms/goodsCategory` |
| 模块 | `money-app-biz` |

---

## 操作的数据表

| 表名 | 操作类型 | 说明 |
|------|----------|------|
| `gms_goods_category` | CRUD | 商品分类表（树形结构） |

---

## 接口测试覆盖

| 接口 | 方法 | 权限 | 测试状态 | 测试用例 |
|------|------|------|------|----------|
| `/gms/goodsCategory/select` | GET | 无 | ✅ | `testCategorySelect` |
| `/gms/goodsCategory/tree` | GET | `gmsGoods:list` | ✅ | `testCategoryTree` |
| `/gms/goodsCategory` | POST | `gmsGoods:add` | ✅ | `testAddRootCategoryWithoutIcon`, `testAddChildCategoryWithIcon` |
| `/gms/goodsCategory` | PUT | `gmsGoods:edit` | ✅ | `testUpdateCategoryName`, `testUpdateCategoryWithIcon` |
| `/gms/goodsCategory` | DELETE | `gmsGoods:del` | ✅ | `testDeleteCategorySuccess`, `testDeleteCategoriesBatch` |

---

## 测试用例详细列表

### 分类下拉选择

| 用例 ID | 用例名称 | 测试方法 | 状态 | 备注 |
|---------|----------|----------|------|------|
| CAT-SELECT-01 | 成功获取分类下拉列表 | `testCategorySelect` | ✅ | 验证下拉选择接口 |

### 分类树形结构

| 用例 ID | 用例名称 | 测试方法 | 状态 | 备注 |
|---------|----------|----------|------|------|
| CAT-TREE-01 | 成功获取分类树 | `testCategoryTree` | ✅ | 验证树形结构（含父子关系） |

### 添加分类

| 用例 ID | 用例名称 | 测试方法 | 状态 | 备注 |
|---------|----------|----------|------|------|
| CAT-ADD-01 | 成功添加根分类（无 icon） | `testAddRootCategoryWithoutIcon` | ✅ | pid=0，无文件上传 |
| CAT-ADD-02 | 成功添加子分类（带 icon） | `testAddChildCategoryWithIcon` | ✅ | pid=父分类ID，带文件上传 |

### 修改分类

| 用例 ID | 用例名称 | 测试方法 | 状态 | 备注 |
|---------|----------|----------|------|------|
| CAT-UPDATE-01 | 成功修改分类名称 | `testUpdateCategoryName` | ✅ | 验证修改功能 |
| CAT-UPDATE-02 | 成功修改分类并更新 icon | `testUpdateCategoryWithIcon` | ✅ | 验证带文件上传的修改 |

### 删除分类

| 用例 ID | 用例名称 | 测试方法 | 状态 | 备注 |
|---------|----------|----------|------|------|
| CAT-DELETE-01 | 成功删除分类 | `testDeleteCategorySuccess` | ✅ | 验证单个删除 |
| CAT-DELETE-02 | 成功批量删除分类 | `testDeleteCategoriesBatch` | ✅ | 验证批量删除 |

---

## 测试覆盖率统计

| 统计项 | 数量 | 百分比 |
|--------|------|--------|
| 接口总数 | 5 | 100% |
| 已覆盖接口 | 5 | 100% |
| 测试用例总数 | 8 | - |
| 通过用例数 | 8 | 100% |
| 禁用用例数 | 0 | 0% |

---

## 特殊说明

- 分类为树形结构，测试需考虑父子关系（pid=0 为根分类）
- POST/PUT 使用 `@RequestPart`，需使用 multipart 请求
- 删除操作未测试存在子分类时的行为（业务可能有限制）

---

> 更新时间：2026-04-04
> 创建人：AI Assistant