import Vue from 'vue'
import Cookies from 'js-cookie'
import ElementUI from 'element-ui'
import 'normalize.css/normalize.css'
import 'element-ui/lib/theme-chalk/index.css'
import '@/styles/index.scss'

import App from './App'
import store from './store'
import router from './router'

import '@/icons' // icon图标
import '@/router/permission' // 路由权限控制
import permission from '@/directive/permission' // 按钮权限控制
import dict from './components/Dict' // 数据字典

Vue.use(dict)
Vue.use(permission)
Vue.use(ElementUI, {
  size: Cookies.get('size') || 'small'
})

Vue.config.productionTip = false

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
