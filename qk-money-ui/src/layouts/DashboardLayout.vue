<script setup>
import { sidebarState } from '@/composables';
import Sidebar from './sidebar/Sidebar.vue';
import Navbar from './navbar/Navbar.vue';
import Footer from './footer/Footer.vue';
</script>

<template>
    <div class="min-h-screen bg-base-200 flex transition-colors duration-300">
        <Sidebar />

        <div
            style="transition-property: margin-left; transition-duration: 150ms"
            :class="[
                'flex-1 flex flex-col',
                {
                    'ml-0 md:ml-16': !sidebarState.isOpen && !sidebarState.isHovered,
                    'ml-0 md:ml-60': sidebarState.isOpen || sidebarState.isHovered,
                }
            ]"
        >
            <Navbar />
            <div class="flex-1 p-2">
                <router-view />
            </div>
            <Footer />
        </div>
    </div>
</template>