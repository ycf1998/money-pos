<script setup>
import Logo from '@/layouts/Logo.vue'
import { sidebarState } from '@/composables'

const tenantTitle = window.tenant?.tenantName
</script>

<template>
    <div :class="[
        'flex items-center flex-shrink-0 px-3',
        {
            'justify-between':
                sidebarState.isOpen || sidebarState.isHovered,
            'justify-center':
                !sidebarState.isOpen && !sidebarState.isHovered,
        },
    ]">
        <router-link to="/" class="inline-flex items-center gap-2">
            <span class="sr-only">Money</span>
            <Logo class="w-10 h-auto" />
            <h2 v-show="sidebarState.isOpen || sidebarState.isHovered" class="ml-1 pt-3 font-serif">{{ tenantTitle }}</h2>
        </router-link>

        <a href="javascript:void(0)" v-show="sidebarState.isOpen || sidebarState.isHovered"
            @click="sidebarState.isOpen = !sidebarState.isOpen">
            <svg-icon name="menu-open" class="w-6 h-6 hidden lg:block" v-show="sidebarState.isOpen" />
            <svg-icon name="menu-close" class="w-6 h-6 hidden lg:block" v-show="!sidebarState.isOpen" />
            <svg-icon name="window-close" class="w-6 h-6 lg:hidden" />
        </a>
    </div>
</template>
