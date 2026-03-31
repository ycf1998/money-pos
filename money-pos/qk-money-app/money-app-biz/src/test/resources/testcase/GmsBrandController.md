# GmsBrandController 测试用例覆盖

> 商品品牌接口测试用例文档

---

## 基本信息

| 项目 | 内容 |
|------|------|
| Controller | `com.money.controller.GmsBrandController` |
| 测试类 | `com.money.controller.GmsBrandControllerTest` |
| 请求路径 | `/gms/brand` |
| 模块 | `money-app-biz` |

---

## 操作的数据表

| 表名 | 操作类型 | 说明 |
|------|----------|------|
| `gms_brand` | CRUD | 商品品牌表（主表） |

---

## 接口测试覆盖

| 接口 | 方法 | 权限 | 测试状态 | 测试用例 |
|------|------|------|------|----------|
| `/gms/brand/select` | GET | 无 | ✅ | `testBrandSelect` |
| `/gms/brand` | GET | `gmsBrand:list` | ✅ | `testListBrands`, `testListBrandsByName` |
| `/gms/brand` | POST | `gmsBrand:add` | ✅ | `testAddBrandWithoutLogo`, `testAddBrandWithLogo` |
| `/gms/brand` | PUT | `gmsBrand:edit` | ✅ | `testUpdateBrandSuccess`, `testUpdateBrandWithLogo` |
| `/gms/brand` | DELETE | `gmsBrand:del` | ✅ | `testDeleteBrandSuccess`, `testDeleteBrandsBatch` |

---

## 测试用例详细列表

### 品牌下拉选择

| 用例 ID | 用例名称 | 测试方法 | 状态 | 备注 |
|---------|----------|----------|------|------|
| BRAND-SELECT-01 | 成功获取品牌下拉列表 | `testBrandSelect` | ✅ | 验证下拉选择接口 |

### 分页查询品牌

| 用例 ID | 用例名称 | 测试方法 | 状态 | 备注 |
|---------|----------|----------|------|------|
| BRAND-LIST-01 | 成功分页查询品牌列表 | `testListBrands` | ✅ | 验证分页功能 |
| BRAND-LIST-02 | 成功按品牌名称搜索 | `testListBrandsByName` | ✅ | 验证模糊查询 |

### 添加品牌

| 用例 ID | 用例名称 | 测试方法 | 状态 | 备注 |
|---------|----------|----------|------|------|
| BRAND-ADD-01 | 成功添加品牌（无 logo） | `testAddBrandWithoutLogo` | ✅ | 验证无文件上传的新增 |
| BRAND-ADD-02 | 成功添加品牌（带 logo） | `testAddBrandWithLogo` | ✅ | 验证带文件上传的新增 |

### 修改品牌

| 用例 ID | 用例名称 | 测试方法 | 状态 | 备注 |
|---------|----------|----------|------|------|
| BRAND-UPDATE-01 | 成功修改品牌 | `testUpdateBrandSuccess` | ✅ | 验证修改功能 |
| BRAND-UPDATE-02 | 成功修改品牌并更新 logo | `testUpdateBrandWithLogo` | ✅ | 验证带文件上传的修改 |

### 删除品牌

| 用例 ID | 用例名称 | 测试方法 | 状态 | 备注 |
|---------|----------|----------|------|------|
| BRAND-DELETE-01 | 成功删除品牌 | `testDeleteBrandSuccess` | ✅ | 验证单个删除 |
| BRAND-DELETE-02 | 成功批量删除品牌 | `testDeleteBrandsBatch` | ✅ | 验证批量删除 |

---

## 测试覆盖率统计

| 统计项 | 数量 | 百分比 |
|--------|------|--------|
| 接口总数 | 5 | 100% |
| 已覆盖接口 | 5 | 100% |
| 测试用例总数 | 9 | - |
| 通过用例数 | 9 | 100% |
| 禁用用例数 | 0 | 0% |

---

> 更新时间：2026-04-04
> 创建人：AI Assistant