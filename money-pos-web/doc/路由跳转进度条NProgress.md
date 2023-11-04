# 路由跳转进度条 NProgress

### 安装 NProgress
```bash
npm i nprogress
```

### 配置 router
```js
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'

...

router.beforeEach((to, from, next) => {
    NProgress.start()
    next()
})

router.afterEach(() => {
    NProgress.done()
})
```