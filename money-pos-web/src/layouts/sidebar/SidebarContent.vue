<script setup>
import SidebarMenu from "@/layouts/sidebar/SidebarMenu.vue";
import SidebarLink from "@/layouts/sidebar/SidebarLink.vue";
import { useRouter } from 'vue-router';
import { useAppStore } from "@/store/index.js";

const router = useRouter();
const menus = filterHidden(useAppStore().menus);

const isCurrentRoute = (routeName) => router.currentRoute.value.name === routeName;

function filterHidden(routes) {
    if (!routes) return routes;
    return routes
        .filter((e) => !e.hidden)
        .map((route) => ({
            ...route,
            children: filterHidden(route.children),
        }));
}
</script>

<template>
    <perfect-scrollbar tag="nav" aria-label="main" class="relative max-h-full">
        <ul class="px-3 space-y-1">
            <li>
                <SidebarLink title="仪表盘" icon="dashboard" :to="{ name: 'Dashboard' }"
                             :active="isCurrentRoute('Dashboard')" />
            </li>
            <!-- 动态菜单 -->
            <SidebarMenu v-for="(menu, index) in menus" :key="index" :menu="menu" />
        </ul>
    </perfect-scrollbar>
</template>