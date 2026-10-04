<script setup>
import { sidebarState } from "@/composables/index.js";
import SidebarLink from "@/layouts/sidebar/SidebarLink.vue";
import { useRouter } from "vue-router";

const { menu } = defineProps(['menu']);
const router = useRouter();

const isCurrentRoute = (routeName) => router.currentRoute.value.name === routeName;
const isCurrentPath = (path) => router.currentRoute.value.path.startsWith(path);
</script>

<template>
    <template v-if="menu.children && menu.children.length === 1">
        <SidebarMenu :menu="menu.children[0]" />
    </template>
    <li v-else-if="menu.children">
        <details :open="isCurrentPath(menu.path)" class="group [&_summary::-webkit-details-marker]:hidden">
            <SidebarLink summary :title="menu.meta.title" :icon="menu.meta.icon" :active="isCurrentPath(menu.path)" />
            <ul v-show="sidebarState.isOpen || sidebarState.isHovered" class="mt-2 space-y-1 px-3">
                <SidebarMenu v-for="(subMenu, index) in menu.children" :key="index" :menu="subMenu" />
            </ul>
        </details>
    </li>
    <li v-else>
        <SidebarLink :title="menu.meta.title" :icon="menu.meta.icon" :to="{ name: menu.name }"
                     :active="isCurrentRoute(menu.name)" />
    </li>
</template>