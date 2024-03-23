<script setup>
import Logo from '@/layouts/Logo.vue'
import { sidebarState } from '@/composables'

const tenantTitle = window.tenant?.tenantName || import.meta.env.VITE_TITLE
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
    </div>
</template>
