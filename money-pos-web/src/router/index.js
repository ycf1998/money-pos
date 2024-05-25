import {createRouter, createWebHashHistory} from 'vue-router'
import intercept from "./interceptor.js"

import Layout from "@/layouts/DashboardLayout.vue"
import NotFound from "@/views/error/NotFound.vue";

// 存放固定的路由
const defaultRouterList = [
    {
        path: '/',
        redirect: () => "/dashboard"
    },
    {
        path: '/dashboard',
        component: Layout,
        children: [
            {
                path: '',
                name: 'Dashboard',
                component: () => import('@/views/dashboard/index.vue'),
            }
        ]
    },
    // 登录页
    {
        path: '/login',
        name: 'Login',
        component: () => import('@/views/Login.vue'),
    },
    // 个人中心
    {
        path: '/personal',
        component: Layout,
        children: [
            {
                path: '',
                name: 'Personal',
                component: () => import('@/views/personal/index.vue'),
            }
        ]
    },
    // 404
    {path: '/:pathMatch(.*)*', name: 'NotFound', component: NotFound}
]

export const routes = [...defaultRouterList]

const router = createRouter({
    history: createWebHashHistory(import.meta.env.BASE_URL),
    routes,
})

export default intercept(router)