# OmsOrderController 测试用例覆盖

> 订单接口测试用例文档

---

## 基本信息

| 项目 | 内容 |
|------|------|
| Controller | `com.money.controller.OmsOrderController` |
| 测试类 | `com.money.controller.OmsOrderControllerTest` |
| 请求路径 | `/oms/order` |
| 模块 | `money-app-biz` |

---

## 操作的数据表

| 表名 | 操作类型 | 说明 |
|------|----------|------|
| `oms_order` | R/Update | 订单表（主表） |

---

## 接口测试覆盖

| 接口 | 方法 | 权限 | 测试状态 | 测试用例 |
|------|------|------|------|----------|
| `/oms/order` | GET | `omsOrder:list` | ✅ | `testListOrders`, `testListOrdersByStatus` |
| `/oms/order/count` | GET | `omsOrder:list` | ✅ | `testOrderCount` |
| `/oms/order/detail` | GET | `omsOrder:list` | ✅ | `testOrderDetail` |
| `/oms/order/returnOrder` | DELETE | `omsOrder:edit` | ✅ | `testReturnOrderSuccess` |
| `/oms/order/returnGoods` | DELETE | `omsOrder:edit` | ✅ | `testReturnGoodsSuccess` |

---

## 测试用例详细列表

### 分页查询订单

| 用例 ID | 用例名称 | 测试方法 | 状态 | 备注 |
|---------|----------|----------|------|------|
| ORDER-LIST-01 | 成功分页查询订单列表 | `testListOrders` | ✅ | 验证分页功能 |
| ORDER-LIST-02 | 成功按订单状态搜索 | `testListOrdersByStatus` | ✅ | 验证状态过滤 |

### 订单统计

| 用例 ID | 用例名称 | 测试方法 | 状态 | 备注 |
|---------|----------|----------|------|------|
| ORDER-COUNT-01 | 成功获取订单统计信息 | `testOrderCount` | ✅ | 验证统计接口 |

### 订单详情

| 用例 ID | 用例名称 | 测试方法 | 状态 | 备注 |
|---------|----------|----------|------|------|
| ORDER-DETAIL-01 | 成功获取订单详情 | `testOrderDetail` | ✅ | 验证详情查询 |

### 退单

| 用例 ID | 用例名称 | 测试方法 | 状态 | 备注 |
|---------|----------|----------|------|------|
| ORDER-RETURN-01 | 成功退单 | `testReturnOrderSuccess` | ✅ | 验证退单功能 |

### 退货

| 用例 ID | 用例名称 | 测试方法 | 状态 | 备注 |
|---------|----------|----------|------|------|
| ORDER-GOODS-RETURN-01 | 成功退货 | `testReturnGoodsSuccess` | ✅ | 验证退货功能 |

---

## 测试覆盖率统计

| 统计项 | 数量 | 百分比 |
|--------|------|--------|
| 接口总数 | 5 | 100% |
| 已覆盖接口 | 5 | 100% |
| 测试用例总数 | 6 | - |
| 通过用例数 | 6 | 100% |
| 禁用用例数 | 0 | 0% |

---

> 更新时间：2026-04-06
> 创建人：AI Assistant
