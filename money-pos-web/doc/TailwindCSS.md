# TailwindCSS

官网：https://www.tailwindcss.cn/docs

### 安装 Tailwind CSS

```bash
npm install -D tailwindcss postcss autoprefixer
npx tailwindcss init
```

### 添加 PostCSS 配置
[postcss.config.js](../postcss.config.js)[]

```js
export default {
  plugins: {
    tailwindcss: {},
    autoprefixer: {},
  },
}
```

### 配置模板文件的路径
[tailwind.config.js](../tailwind.config.js)

```js
/** @type {import('tailwindcss').Config} */
export default {
  darkMode: 'class',
  content: ["./src/**/*.{html,vue,js}"],
  theme: {
    extend: {}
  },
  plugins: [],
}
```

### 将加载 Tailwind 的指令添加到你的 CSS 文件中
[tailwind.css](../src/style/tailwind.css)

```css
@tailwind base;
@tailwind components;
@tailwind utilities;
```