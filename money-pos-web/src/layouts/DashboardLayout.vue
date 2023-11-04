<script setup>
import {sidebarState} from '@/composables'
import Sidebar from './sidebar/Sidebar.vue'
import Navbar from './navbar/Navbar.vue'
import Footer from './footer/Footer.vue'
</script>

<template>
    <div class="min-h-screen bg-base-200">
        <Sidebar />

        <div style="transition-property: margin; transition-duration: 150ms" :class="[
            'min-h-screen flex flex-col',
            {
                'lg:ml-60': sidebarState.isOpen,
                'md:ml-16': !sidebarState.isOpen,
            },
        ]">
            <Navbar />

            <router-view />

            <Footer />
        </div>
    </div>
</template>
