import { constantRoutes, asyncRoutes } from '@/router'
import Layout from '@/layout/index'
import { buildRouter } from '@/api/system/permission'

const state = {
  routes: [],
  addRoutes: []
}

const mutations = {
  SET_ROUTES: (state, routes) => {
    state.addRoutes = routes
    state.routes = constantRoutes.concat(routes)
  }
}

// 【qk-money】 从后端获取动态路由
const actions = {
  generateRoutes({ commit }, roles) {
    return new Promise(resolve => {
      buildRouter().then(res => {
        let accessedRoutes = asyncRoutes
        // 【qk-money】前端调试时注释这两行，不使用后端传来的菜单，使用路由文件动态路由数组的菜单
        accessedRoutes = res.data
        collatingOfData(null, accessedRoutes)
        // 404 page must be placed at the end !!!
        accessedRoutes.push({ path: '*', redirect: '/404', hidden: true })
        commit('SET_ROUTES', accessedRoutes)
        resolve(accessedRoutes)
      })
    })
  }
}

const collatingOfData = (parentRoute, accessedRoutes) => {
  accessedRoutes.forEach(route => {
    // 外部链接
    if (!route.iframe) {
      // component
      if (route.component === 'Layout') {
        route.component = Layout
        // path补首/ （因为目录路由没有/将无法跳转）
        if (!route.path.startsWith('/')) {
          route.path = '/' + route.path
        }
      } else {
        route.component = loadView(route.component)
      }
      // 递归
      if (route.children && route.children.length) {
        collatingOfData(route, route.children)
      } else {
        delete route.children
      }
    }
  })
}

export const loadView = (view) => {
  return (resolve) => require([`@/views/${view}`], resolve)
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
