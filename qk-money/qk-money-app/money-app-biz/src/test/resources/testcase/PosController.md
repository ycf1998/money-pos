# PosController 测试用例覆盖

> 收银接口测试用例文档

---

## 基本信息

| 项目 | 内容 |
|------|------|
| Controller | `com.money.controller.PosController` |
| 测试类 | `com.money.controller.PosControllerTest` |
| 请求路径 | `/pos` |
| 模块 | `money-app-biz` |

---

## 操作的数据表

| 表名 | 操作类型 | 说明 |
|------|----------|------|
| `gms_goods` | R | 商品表（查询） |
| `ums_member` | R | 会员表（查询） |
| `oms_order` | C | 订单表（创建） |
| `oms_order_detail` | C | 订单明细表（创建） |
| `oms_order_log` | C | 订单日志表（创建） |

---

## 接口测试覆盖

| 接口 | 方法 | 权限 | 测试状态 | 测试用例 |
|------|------|------|------|----------|
| `/pos/goods` | GET | `pos:cashier` | ✅ | `testListGoodsByBarcode`, `testListGoodsAll` |
| `/pos/members` | GET | `pos:cashier` | ✅ | `testListMemberByName`, `testListMemberAll` |
| `/pos/settleAccounts` | POST | `pos:cashier` | ✅ | `testSettleAccountsSuccess`, `testSettleAccountsWithVip` |

---

## 测试用例详细列表

### 商品列表

| 用例 ID | 用例名称 | 测试方法 | 状态 | 备注 |
|---------|----------|----------|------|------|
| POS-GOODS-01 | 成功按条码查询商品 | `testListGoodsByBarcode` | ✅ | 验证条码搜索功能 |
| POS-GOODS-02 | 成功获取所有商品列表 | `testListGoodsAll` | ✅ | 验证无参数查询 |

### 会员列表

| 用例 ID | 用例名称 | 测试方法 | 状态 | 备注 |
|---------|----------|----------|------|------|
| POS-MEMBER-01 | 成功按会员名称搜索会员 | `testListMemberByName` | ✅ | 验证会员搜索功能 |
| POS-MEMBER-02 | 成功获取所有会员列表 | `testListMemberAll` | ✅ | 验证无参数查询 |

### 收款结算

| 用例 ID | 用例名称 | 测试方法 | 状态 | 备注 |
|---------|----------|----------|------|------|
| POS-SETTLE-01 | 成功收款（普通订单） | `testSettleAccountsSuccess` | ✅ | 验证普通订单结算 |
| POS-SETTLE-02 | 成功收款（VIP订单） | `testSettleAccountsWithVip` | ✅ | 验证VIP会员订单结算 |

---

## 测试覆盖率统计

| 统计项 | 数量 | 百分比 |
|--------|------|--------|
| 接口总数 | 3 | 100% |
| 已覆盖接口 | 3 | 100% |
| 测试用例总数 | 6 | - |
| 通过用例数 | 6 | 100% |
| 禁用用例数 | 0 | 0% |

---

## 测试数据准备

### 商品数据
- 使用 `GoodsDataService` 创建测试商品
- 需要有效的 barcode、价格、库存等信息

### 会员数据
- 使用 `MemberDataService` 创建测试会员
- 需要有效的 code、coupon 等信息

### 结算数据
- 结算接口需要商品 ID 和数量
- 需要验证订单创建、库存扣减、会员优惠券使用等业务流程

---

> 更新时间：2026-04-06
> 创建人：AI Assistant
