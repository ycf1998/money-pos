<script setup>
import { onMounted, onUnmounted, ref, watch } from 'vue';
import { useRoute } from 'vue-router';
import { useUserStore } from '@/store';
import { useFullscreen } from '@vueuse/core';
import { handleScroll, isDark, scrolling, sidebarState, toggleDarkMode } from '@/composables';
import { useGlobalProp } from '@/composables/globalProp.js';
import Logo from '@/layouts/Logo.vue';

const { isFullscreen, toggle: toggleFullScreen } = useFullscreen();
const avatar = useGlobalProp().$money.getOssUrl(useUserStore().info.avatar);
const route = useRoute();
const breadcrumb = ref([]);

onMounted(() => {
    document.addEventListener('scroll', handleScroll);
});

onUnmounted(() => {
    document.removeEventListener('scroll', handleScroll);
});

watch(
    () => route.fullPath,
    () => {
        breadcrumb.value = [{ path: '/', meta: { title: '首页' } }, ...route.matched.filter((e) => e.meta?.title)];
    },
    { immediate: true }
);

function logout() {
    useUserStore().logout();
}
</script>

<template>
    <!-- PC nav -->
    <nav
        aria-label="secondary"
        :class="[
      'sticky bg-base-100 top-0 z-10 px-6 py-3 flex items-center justify-between transition-transform duration-500',
      {
        '-translate-y-full': scrolling.down,
        'translate-y-0': scrolling.up,
      },
    ]"
    >
        <div class="flex items-center gap-2">
            <a href="javascript:void(0)" @click="sidebarState.isOpen = !sidebarState.isOpen" class="p-1 rounded-lg hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors hidden lg:block">
                <svg-icon :name="sidebarState.isOpen ? 'menu-open' : 'menu-close'" class="w-5 h-5 hidden lg:block" />
            </a>
            <el-breadcrumb separator="/">
                <el-breadcrumb-item v-for="item in breadcrumb" :to="{ path: item.path }" class="hover:text-blue-600 dark:hover:text-blue-400 transition-colors">
                    {{ item.meta.title }}
                </el-breadcrumb-item>
            </el-breadcrumb>
        </div>

        <div class="flex items-center gap-2">
            <a href="javascript:void(0)" class="p-1 rounded-lg hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors hidden md:inline-flex" @click="toggleDarkMode()">
                <svg-icon :name="isDark ? 'sun' : 'moon'" class="w-5 h-5" />
            </a>

            <a href="javascript:void(0)" class="p-1 rounded-lg hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors hidden md:inline-flex" @click="toggleFullScreen()">
                <svg-icon :name="isFullscreen ? 'arrow-collapse-all' : 'arrow-expand-all'" class="w-5 h-5" />
            </a>

            <el-dropdown>
                <div class="w-8 h-8 rounded overflow-hidden cursor-pointer">
                    <img :src="avatar" alt="avatar" class="w-full h-full object-cover" />
                </div>
                <template #dropdown>
                    <el-dropdown-menu>
                        <el-dropdown-item>
                            <router-link :to="{ name: 'Personal' }">个人中心</router-link>
                        </el-dropdown-item>
                        <el-dropdown-item @click="logout">登出</el-dropdown-item>
                    </el-dropdown-menu>
                </template>
            </el-dropdown>
        </div>
    </nav>

    <!-- Mobile bottom bar -->
    <nav
        :class="[
      'fixed bg-base-100 bottom-0 z-10 px-4 py-3 sm:px-6 inset-x-0 flex items-center justify-between transition-transform duration-500 md:hidden shadow-md',
      {
        'translate-y-full': scrolling.down,
        'translate-y-0': scrolling.up,
      },
    ]"
    >
        <div class="flex items-center gap-2">
            <a href="javascript:void(0)" class="p-2 rounded-full hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors md:hidden" @click="toggleDarkMode()">
                <svg-icon :name="isDark ? 'sun' : 'moon'" class="w-5 h-5" />
            </a>
        </div>

        <router-link to="" class="transition-transform hover:scale-105">
            <Logo class="w-10 h-10" />
            <span class="sr-only">Money</span>
        </router-link>

        <label @click="sidebarState.isOpen = !sidebarState.isOpen" class="p-2 rounded-full hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors cursor-pointer">
            <svg-icon :name="sidebarState.isOpen ? 'window-close' : 'menu'" class="w-6 h-6" />
        </label>
    </nav>
</template>