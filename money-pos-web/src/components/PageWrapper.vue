<!-- 通用页面布局 -->
<template>
    <!-- Page Content -->
    <main :class="['flex-1 rounded-md bg-base-100 p-5 min-h-[calc(100vh-8rem)]', customClass]">
        <div class="grid gap-6">
            <slot />
        </div>
    </main>
</template>

<script setup>
defineProps({
    customClass: {
        type: String,
        default: '',
    },
});
</script>