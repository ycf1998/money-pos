import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import 'normalize.css/normalize.css'
import '@/styles/index.scss'

import Cookies from 'js-cookie'

import dict from './components/Dict'
import permission from '@/directive/permission'
import '@/icons'
import '@/router/permission'

Vue.use(dict)
Vue.use(permission)
Vue.use(ElementUI, {
  size: Cookies.get('size') || 'small',
})

Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: h => h(App),
}).$mount('#app')
