<script setup>
import { computed } from 'vue';
import { sidebarState } from '@/composables';

const titleShow = computed(() => sidebarState.isOpen || sidebarState.isHovered);

const prop = defineProps({
    summary: Boolean,
    href: String,
    to: [String, Object],
    active: { type: Boolean, default: false },
    title: { type: String, required: true },
    icon: { type: String, default: '' },
});
</script>

<template>
    <component
        :is="summary ? 'summary' : href ? 'a' : 'router-link'"
        :href="href"
        :to="to"
        :class="[
      'relative flex rounded-lg px-2 py-2.5 text-sm font-medium cursor-pointer',
      {
        'text-blue-700 bg-blue-50 dark:bg-gray-800 dark:text-blue-400': active && !summary,
        'text-gray-500 hover:text-blue-500 dark:hover:text-gray-300': !active
      },
    ]"
    >
        <svg-icon dir="open" :name="icon" />
        <span v-show="titleShow" class="ml-2 text-base font-medium">{{ title }}</span>
        <span
            v-if="summary && titleShow"
            class="shrink-0 transition duration-300 group-open:-rotate-180 absolute right-1"
        >
      <svg-icon name="down" />
    </span>
    </component>
</template>