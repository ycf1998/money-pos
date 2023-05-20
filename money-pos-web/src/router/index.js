import Vue from 'vue'
import Router from 'vue-router'
import Layout from '@/layout'

Vue.use(Router)

export const constantRoutes = [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path(.*)',
        component: () => import('@/views/redirect/index')
      }
    ]
  },
  {
    path: '/login',
    component: () => import('@/views/user/login/index'),
    hidden: true
  },
  {
    path: '/403',
    component: () => import('@/views/error-page/403'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/error-page/404'),
    hidden: true
  },

  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [{
      path: 'dashboard',
      name: 'Dashboard',
      component: () => import('@/views/dashboard/index'),
      meta: { title: '首页', icon: 'dashboard', affix: true }
    }]
  },
  {
    path: '/profile',
    component: Layout,
    redirect: '/profile/index',
    children: [
      {
        path: 'index',
        name: 'Profile',
        component: () => import('@/views/user/profile/index'),
        meta: { title: '个人资料', icon: 'dashboard' },
        hidden: true
      }
    ]
  },
  {
    path: '/changePassword',
    component: Layout,
    redirect: '/changePassword/index',
    children: [
      {
        path: 'index',
        name: 'ChangePassword',
        component: () => import('@/views/user/changePassword/index'),
        meta: { title: '修改密码', icon: 'dashboard' },
        hidden: true
      }
    ]
  }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()
export function resetRouter () {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher
}

// 解决路由版本问题：Uncaught (in promise) Error: Redirected when going from “/login?redirect=%2Fdashboard“ to “/“ via
const originalPush = Router.prototype.push
Router.prototype.push = function push (location, onResolve, onReject) {
  if (onResolve || onReject) return originalPush.call(this, location, onResolve, onReject)
  return originalPush.call(this, location).catch(err => err)
}

export default router;