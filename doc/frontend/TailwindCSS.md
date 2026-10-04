# TailwindCSS

官网：https://tailwindcss.com/docs

仓库使用 **Tailwind CSS v4**，通过官方 Vite 插件 `@tailwindcss/vite` 接入。v4 不再需要 `postcss.config.js`、`tailwind.config.js` 与 `autoprefixer`，主题与变体直接在 CSS 中配置。

## 依赖

`package.json` 的 `devDependencies`：

```json
"@tailwindcss/vite": "^4.3.3",
"tailwindcss": "^4.3.3"
```

## Vite 插件

[qk-money-ui/vite.config.js](../../qk-money-ui/vite.config.js) 注册插件：

```js
import tailwindcss from '@tailwindcss/vite'

export default defineConfig(async ({ mode }) => ({
    plugins: [
        tailwindcss(),
        vue(),
        // ...
    ],
}))
```

## 入口样式

[qk-money-ui/src/style/tailwind.css](../../qk-money-ui/src/style/tailwind.css)：

```css
@import "tailwindcss";

/* 使用 class 策略的暗色模式（与 @vueuse/core useDark 配合） */
@custom-variant dark (&:is(.dark *));

/* 自定义主题 */
@theme {
  --font-sans: "Inter", "Noto Sans SC", -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "PingFang SC", "Microsoft YaHei", sans-serif;
}
```

- `@import "tailwindcss"` 取代 v3 的 `@tailwind base; @tailwind components; @tailwind utilities;`。
- 主题变量（字体、颜色等）写在 `@theme` 中，不再有 `tailwind.config.js` 的 `theme.extend`。
- `@custom-variant dark (&:is(.dark *))` 将 `dark:` 变体绑定到 `.dark` 类；`@vueuse/core` 的 `useDark()`（见 `src/composables/index.js`）负责在 `<html>` 上切换该类。内容扫描（v3 的 `content`）在 v4 中自动完成，无需配置。

## 接入主入口

[qk-money-ui/src/main.js](../../qk-money-ui/src/main.js)：

```js
import './style/main.css';
import './style/tailwind.css';
```

不提供单独的 Tailwind 构建脚本，样式随 `npm run dev` / `npm run build` 由 Vite 插件编译。

## 使用

在模板中直接写原子类，例如 `src/components/PageWrapper.vue`：

```html
<main class="flex-1 rounded-md bg-base-100 p-6 sm:min-h-[calc(100vh-8rem)]">
```

其中 `bg-base-100` / `bg-base-200` 是 `src/style/main.css` 中定义的自定义类，桥接 Element Plus 的 CSS 变量以实现明暗主题联动。

v4 的重要修饰符写成后缀 `!`（v3 为前缀），仓库内如 `md:w-48!`、`w-11/12!`、`class="w-11/12! md:w-1/2! lg:w-1/3!"`。
