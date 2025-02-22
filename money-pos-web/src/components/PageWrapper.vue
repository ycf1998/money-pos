<!-- 通用页面布局 -->
<template>
    <!-- Page Content -->
    <main class="flex-1 rounded-md bg-base-100 sm:m-2 my-1 p-5">
        <div class="grid gap-6">
            <slot />
        </div>
    </main>
</template>

<script setup>
defineProps({
    class: {
        type: String,
        default: '',
    },
});
</script>