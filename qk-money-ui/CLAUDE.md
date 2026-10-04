# 前端

Vue 3 + Vite + Element Plus + TailwindCSS + Pinia。下列命令均在本目录执行。

## 命令

| 命令 | 说明 |
|---|---|
| `npm install` | 安装依赖 |
| `npm run dev` | 开发服务，`localhost:1520` |
| `npm run build` | 打包 |

Tailwind 由 `@tailwindcss/vite` 插件处理，随 `dev` / `build` 一并编译。

## 目录

```
src/
├── api/            请求层，按业务域分目录；mock.js 为纯前端数据
├── assets/         静态资源（图标等）
├── components/     通用组件；crud/ 为 MoneyCrud 系列
├── composables/    组合式函数（token、全局属性）
├── layouts/        布局
├── plugins/        全局注册（SVG 图标、$money 等）
├── router/         路由与拦截器
├── store/          Pinia
├── style/          main.css、tailwind.css
├── views/          页面
└── money.config.js 全局约定：请求头名、OSS 路径、语言时区
```

**业务页面写入 `views/`，接口写入 `api/`，与框架页面并存。**

## 约定

- 列表页使用 `components/crud/` 下的 MoneyCrud 系列，不手写表格；完整 API 见 `components/crud/README.md`
- 资源地址（头像、logo）经 `useGlobalProp().$money.getOssUrl(...)` 获取，不拼接裸路径
- 请求头（token、租户、语言、时区）统一配置于 `money.config.js`，由 `api/axios.js` 自动附加；接口只处理业务字段
- 后端地址、站点标题、纯前端模式由 `.env.development` / `.env.production` 控制：`VITE_BASE_URL` 指向后端，`VITE_ONLY_UI` 置为 `alert` 或 `log` 即进入纯前端模式（请求不真实发出，走 `api/mock.js`）
- 自动导入仅覆盖 Element Plus；Vue API 与项目自身组件均需显式 `import`

## 文档

| 内容 | 位置 |
|---|---|
| MoneyCrud 用法 | `../doc/frontend/MoneyCrud.md` |
| 请求封装 | `../doc/frontend/Axios封装.md` |
| 权限 | `../doc/frontend/权限封装.md` |
| 全局 SVG 图标 | `../doc/frontend/全局svg.md` |
| Tailwind | `../doc/frontend/TailwindCSS.md` |

前端页面的写法规范与可复用组件清单见 `../.claude/skills/qk-dev/references/`。
