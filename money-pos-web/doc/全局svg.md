# 全局 SVG

### 安装 [vite-plugin-svg-icons](https://github.com/vbenjs/vite-plugin-svg-icons)

```bash
npm i vite-plugin-svg-icons -D
```

### vite.config.js 中的配置插件

```js
import { resolve } from 'path';
import { createSvgIconsPlugin } from 'vite-plugin-svg-icons'

export default defineConfig(async () => ({
        plugins: [
            createSvgIconsPlugin({
                // 图标存放路径
                iconDirs: [resolve(__dirname, 'src/assets/icons')],
                // 标识 id
                symbolId: 'icon-[dir]-[name]'
            }),
        ]
    })
)
```

### main.js 注入脚本
```js
import 'virtual:svg-icons-register'
```

### 封装 SvgIcon 组件
[SvgIcon.vue](../src/components/SvgIcon.vue)

```vue
<!-- SVG 图标 -->
<template>
    <svg :class="iconClass" :fill="fill" aria-hidden="true">
        <use :xlink:href="iconName" />
    </svg>
</template>

<script setup>
    import { computed } from 'vue'

    const props = defineProps({
        dir: {
            type: String,
            default: ''
        },
        name: {
            type: String,
            required: true
        },
        class: {
            type: String,
            default: ''
        },
        fill: {
            type: String,
            default: 'currentColor'
        }
    })

    const iconName = computed(() => {
        return `#icon-${props.dir? props.dir + '-' : ''}${props.name}`
    })
    const iconClass = computed(() => {
        return props.class ? props.class : `w-6 h-6`
    })
</script>
```

### 全局注入 SvgIcon，方便使用

```js
app.component('svg-icon', SvgIcon)
```

### 使用

```vue
<svg-icon name="sun" />
```

### 获取所有 SymbolId（常用于图标选择器）
```js
import iconNmes from 'virtual:svg-icons-names'
// => ['icon-icon1','icon-icon2','icon-icon3']
```

## 图标选择器 IconSelect
[IconSelect.vue](../src/components/IconSelect.vue)

```vue
<IconSelect :default-icon="moneyCrud.form.icon" @selected="icon => moneyCrud.form.icon = icon" />
```

属性：
- dir：文件夹，默认为 open，代表可选图标为 icons/open 下的图标。
- default-icon：默认选中的图标，默认为 app。即完整 icon = icon-open-app。

方法：
- selected：选中图标触发事件，入参为图标名称，不包含 icon-open-。