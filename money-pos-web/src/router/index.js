import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

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
      meta: { title: '首页', icon: 'dashboard', affix: true, noCache: true }
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
  },
  {
    path: '/order',
    component: Layout,
    children: [
      {
        path: 'detail/:id',
        component: () => import('@/views/oms/order/detail'),
        name: 'OrderDetail',
        meta: { title: '订单明细' },
        hidden: true
      }
    ]
  }
]

export const asyncRoutes = [
  {
    path: '/system',
    component: Layout,
    meta: { title: '系统管理', icon: 'el-icon-setting' },
    children: [
      {
        path: 'user',
        name: 'User',
        component: () => import('@/views/system/user/index'),
        meta: { title: '用户管理', icon: 'zujian' }
      },
      {
        path: 'role',
        name: 'Role',
        component: () => import('@/views/system/role/index'),
        meta: { title: '角色管理', icon: 'zujian' }
      },
      {
        path: 'permission',
        name: 'Permission',
        component: () => import('@/views/system/permission/index'),
        meta: { title: '权限管理', icon: 'zujian' }
      },
      {
        path: 'dict',
        name: 'Dict',
        component: () => import('@/views/system/dict/index'),
        meta: { title: '字典管理', icon: 'zujian' }
      },
      {
        path: 'tenant',
        name: 'Tenant',
        component: () => import('@/views/system/tenant/index'),
        meta: { title: '租户管理', icon: 'zujian' }
      }
    ]
  },
  {
    path: '/pos',
    component: Layout,
    meta: { title: '日常', icon: 'zujian' },
    children: [
      {
        path: 'pos',
        name: 'Pos',
        component: () => import('@/views/pos/index'),
        meta: { title: '收银台', icon: 'zujian' }
      }
    ]
  },
  {
    path: '/ums',
    component: Layout,
    meta: { title: '会员管理', icon: 'zujian' },
    children: [
      {
        path: 'member',
        name: 'Member',
        component: () => import('@/views/ums/member/index'),
        meta: { title: '会员管理', icon: 'zujian' }
      }
    ]
  },
  {
    path: '/gms',
    component: Layout,
    meta: { title: '商品管理', icon: 'zujian' },
    children: [
      {
        path: 'goods',
        name: 'Goods',
        component: () => import('@/views/gms/goods/index'),
        meta: { title: '商品', icon: 'zujian' }
      },
      {
        path: 'brand',
        name: 'Brand',
        component: () => import('@/views/gms/brand/index'),
        meta: { title: '品牌', icon: 'zujian' }
      }
    ]
  },
  {
    path: '/oms',
    component: Layout,
    meta: { title: '订单管理', icon: 'zujian' },
    children: [
      {
        path: 'order',
        name: 'Order',
        component: () => import('@/views/oms/order/index'),
        meta: { title: '订单', icon: 'zujian' }
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

export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher
}

export default router
