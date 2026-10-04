# Axios 封装

请求层按职责分为三层：

| 文件 | 职责 |
|---|---|
| `src/money.config.js` | 全局约定：请求头名、token 类型、语言、时区、OSS 路径 |
| `src/api/axios.js` | axios 实例与请求/响应拦截器 |
| `src/api/index.js` | 对业务暴露的请求函数（`req` 等） |

业务接口按域组织在 `src/api/{domain}/{entity}.js`。

## 请求实例

`src/api/axios.js` 创建单例：

```js
const instance = axios.create({
    baseURL: import.meta.env.VITE_BASE_URL,
    timeout: 7000,
});
```

`baseURL` 取自环境变量（`.env.development` 为 `http://localhost:9000/qk-money`，`.env.production` 为 `/qk-money/api`），超时 7 秒。业务侧写 url 时不带 `/api` 前缀。

## 请求拦截器

按顺序附加以下请求头，头名全部来自 `money.config.js`，不写死：

| 请求头 | 配置键 | 值 |
|---|---|---|
| `Authorization` | `tokenHeader` | `Bearer <token>`，`getToken()` 有值时才带 |
| `Y-tenant` | `tenantHeader` | `window.tenant.id` |
| `X-qk-request` | `requestIdHeader` | 当前时间戳 |
| `X-qk-lang` | `i18nHeader` | `MoneyConfig.lang`（`zh-cn`） |
| `X-qk-timezone` | `timezoneHeader` | `MoneyConfig.timezone`（`GMT+08:00`） |

- token 通过 `src/composables/token.js` 读写，存于 cookie（键 `accessToken`）。
- 租户：从地址栏查询参数 `tenant` 取 `tenantCode`；与缓存的 `window.tenant` 不一致时，先请求 `${baseURL}/tenants/byCode?code=xxx` 获取并缓存；取不到则中断请求。

## 响应拦截器

成功分支取 `response.data`：

- 无 `code` 字段：直接返回原 body（如文件流）。
- `code === 200`：返回整个 body，业务侧取 `res.data`。
- `code !== 200`：`ElMessage.error(body.message)` 并 reject。

失败分支：

| 情况 | 处理 |
|---|---|
| `Network Error` | 提示网络错误 |
| HTTP 401 | 提示登录已过期，调用 `useUserStore().logout()` |
| HTTP 403 | 提示无权限 |
| 其他 | 提示 `error.message` |

## 对外请求函数

`src/api/index.js` 导出（既有命名导出，也有默认导出 `req`，两种导入写法均可）：

| 函数 | 说明 |
|---|---|
| `req(options)` | 通用请求。开启纯前端模式（`VITE_ONLY_UI`）时不发真实请求，按 `method` + `url` 从 `src/api/mock.js` 取数据；否则走 axios 实例 |
| `reqForm(options)` | `Content-Type: application/x-www-form-urlencoded` |
| `reqFormData(options)` | `Content-Type: multipart/form-data` |
| `reqMixed(options, { key, jsonData }, formData)` | `Content-Type: multipart/mixed`，用于 JSON + 文件混合提交 |

## 业务侧引用

```js
import req from '@/api/index.js'
// 等价写法：import { req } from '@/api/index.js'

export default {
    list: (params) => req({ method: 'GET', url: '/user', params }),
    add: (data) => req({ method: 'POST', url: '/user', data }),
    edit: (data) => req({ method: 'PUT', url: '/user', data }),
    del: (ids) => req({ method: 'DELETE', url: '/user', data: ids }),
}
```

- `list` 用 `params`，增改删用 `data`；`del` 直接传 id 数组。
- 响应已在拦截器解包，业务代码直接取 `res.data`。
- 仓库示例：`src/api/system/auth.js`（默认导入）、`src/api/system/tenant.js`（命名导入 `req`、`reqMixed`）。

纯前端开发模式见仓库根 `README.md` 的「前端独立开发（Mock 模式）」。
