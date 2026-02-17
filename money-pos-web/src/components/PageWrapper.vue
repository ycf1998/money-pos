<!-- 通用页面布局 -->
<template>
    <!-- Page Content -->
    <main :class="['flex-1 rounded-md bg-base-100 p-5 sm:min-h-[calc(100vh-8rem)] min-h-[calc(100vh-9rem)] overflow-hidden', customClass]">
        <div class="grid gap-6 h-full">
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