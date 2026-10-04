# GmsGoodsController 测试用例覆盖

> 商品接口测试用例文档

---

## 基本信息

| 项目 | 内容 |
|------|------|
| Controller | `com.money.controller.GmsGoodsController` |
| 测试类 | `com.money.controller.GmsGoodsControllerTest` |
| 请求路径 | `/gms/goods` |
| 模块 | `money-app-biz` |

---

## 操作的数据表

| 表名 | 操作类型 | 说明 |
|------|----------|------|
| `gms_goods` | CRUD | 商品表（主表） |

---

## 接口测试覆盖

| 接口 | 方法 | 权限 | 测试状态 | 测试用例 |
|------|------|------|------|----------|
| `/gms/goods` | GET | `gmsGoods:list` | ✅ | `testListGoods`, `testListGoodsByName` |
| `/gms/goods` | POST | `gmsGoods:add` | ✅ | `testAddGoodsWithoutPic`, `testAddGoodsWithPic` |
| `/gms/goods` | PUT | `gmsGoods:edit` | ✅ | `testUpdateGoodsSuccess`, `testUpdateGoodsWithPic` |
| `/gms/goods` | DELETE | `gmsGoods:del` | ✅ | `testDeleteGoodsSuccess`, `testDeleteGoodsBatch` |

---

## 测试用例详细列表

### 分页查询商品

| 用例 ID | 用例名称 | 测试方法 | 状态 | 备注 |
|---------|----------|----------|------|------|
| GOODS-LIST-01 | 成功分页查询商品列表 | `testListGoods` | ✅ | 验证分页功能 |
| GOODS-LIST-02 | 成功按商品名称搜索 | `testListGoodsByName` | ✅ | 验证模糊查询 |

### 添加商品

| 用例 ID | 用例名称 | 测试方法 | 状态 | 备注 |
|---------|----------|----------|------|------|
| GOODS-ADD-01 | 成功添加商品（无图片） | `testAddGoodsWithoutPic` | ✅ | 验证无文件上传的新增 |
| GOODS-ADD-02 | 成功添加商品（带图片） | `testAddGoodsWithPic` | ✅ | 验证带文件上传的新增 |

### 修改商品

| 用例 ID | 用例名称 | 测试方法 | 状态 | 备注 |
|---------|----------|----------|------|------|
| GOODS-UPDATE-01 | 成功修改商品 | `testUpdateGoodsSuccess` | ✅ | 验证修改功能 |
| GOODS-UPDATE-02 | 成功修改商品并更新图片 | `testUpdateGoodsWithPic` | ✅ | 验证带文件上传的修改 |

### 删除商品

| 用例 ID | 用例名称 | 测试方法 | 状态 | 备注 |
|---------|----------|----------|------|------|
| GOODS-DELETE-01 | 成功删除商品 | `testDeleteGoodsSuccess` | ✅ | 验证单个删除 |
| GOODS-DELETE-02 | 成功批量删除商品 | `testDeleteGoodsBatch` | ✅ | 验证批量删除 |

---

## 测试覆盖率统计

| 统计项 | 数量 | 百分比 |
|--------|------|--------|
| 接口总数 | 4 | 100% |
| 已覆盖接口 | 4 | 100% |
| 测试用例总数 | 8 | - |
| 通过用例数 | 8 | 100% |
| 禁用用例数 | 0 | 0% |

---

> 更新时间：2026-04-06
> 创建人：AI Assistant
