import router from '.'
import store from '../store'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import { Message } from 'element-ui'
import TokenManage from '@/utils/tokenManage'
import getPageTitle from '@/utils/get-page-title'

NProgress.configure({ showSpinner: false })

const whiteList = ['/login'] // 白名单

router.beforeEach(async (to, from, next) => {
  NProgress.start()
  document.title = getPageTitle(to.meta.title)

  const hasToken = TokenManage.getToken()
  if (hasToken) {
    if (to.path === '/login') {
      next({ path: '/' })
      NProgress.done()
    } else {
      // TODO
      const hasRoles = store.getters.roles && store.getters.roles.length > 0
      // 已经拉取过信息，加载过路由直接放行
      if (hasRoles) {
        next()
      } else {
        try {
          // 获取信息，加载动态路由
          await store.dispatch('user/getInfo')
          const accessRoutes = await store.dispatch('permission/generateRoutes')
          accessRoutes.forEach(route => router.addRoute(route))
          next({ ...to, replace: true })
        } catch (error) {
          Message.error(error || 'Has Error')
          TokenManage.removeToken()
          next(`/login?redirect=${to.path}`)
          NProgress.done()
        }
      }
    }
  } else {
    if (whiteList.indexOf(to.path) !== -1) {
      next()
    } else {
      next(`/login?redirect=${to.path}`)
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  NProgress.done()
})
