# 全局 SVG 图标

图标由 [vite-plugin-svg-icons](https://github.com/vbenjs/vite-plugin-svg-icons) 提供，把 `src/assets/icons/` 下的 SVG 编译成 SVG symbol，供全局复用。

## 插件配置

[qk-money-ui/vite.config.js](../../qk-money-ui/vite.config.js)：

```js
import { resolve } from 'path';
import { createSvgIconsPlugin } from 'vite-plugin-svg-icons'

createSvgIconsPlugin({
    // 图标存放路径
    iconDirs: [resolve(__dirname, 'src/assets/icons')],
    // 标识 id
    symbolId: 'icon-[dir]-[name]',
})
```

图标目录：`src/assets/icons/`（顶层图标）与 `src/assets/icons/open/`（菜单图标）。`symbolId` 的 `[dir]` 对应子目录名，顶层文件 dir 为空。

## 注册

[qk-money-ui/src/plugins/index.js](../../qk-money-ui/src/plugins/index.js) 负责注入注册脚本并全局注册组件（不在 `main.js`）：

```js
import 'virtual:svg-icons-register'
import SvgIcon from '@/components/SvgIcon.vue'

// install 内：
app.component('svg-icon', SvgIcon)
```

`SvgIcon` 已全局注册，模板中可直接使用，无需 import。

## SvgIcon 组件

源码：[qk-money-ui/src/components/SvgIcon.vue](../../qk-money-ui/src/components/SvgIcon.vue)

用法：

```html
<svg-icon name="sun" />
<svg-icon name="sys-user" dir="open" class="w-5 h-5" />
```

props：

| 属性 | 类型 | 默认值 | 说明 |
|---|---|---|---|
| `name` | String | —（必填） | 图标名，对应文件名 |
| `dir` | String | `''` | 子目录名，顶层图标留空 |
| `class` | String | `'w-6 h-6'` | CSS 类 |
| `fill` | String | `'currentColor'` | 填充色 |

完整的图标名 = `icon-{dir}-{name}`（dir 非空时）。

## 图标选择器 IconSelect

源码：[qk-money-ui/src/components/IconSelect.vue](../../qk-money-ui/src/components/IconSelect.vue)

`IconSelect` 未全局注册，需显式 import：

```vue
<IconSelect :default-icon="moneyCrud.form.icon" @selected="icon => moneyCrud.form.icon = icon" />
```

组件内部通过 `virtual:svg-icons-names` 读取全部 symbolId，按 dir 前缀过滤后生成下拉选项。

props：

- `dir`：图标目录，默认 `open`，即列出 `icons/open/` 下的图标。
- `defaultIcon`：默认选中的图标，默认 `app`。完整图标为 `icon-open-app`。

事件：

- `selected`：选中时触发，入参为图标名（不含 `icon-open-` 前缀）。
