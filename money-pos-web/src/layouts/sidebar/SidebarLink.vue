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
      'relative flex items-center rounded-lg px-3 py-2.5 text-sm font-medium cursor-pointer transition-all duration-300',
      {
        'text-blue-700 bg-blue-50 dark:bg-gray-800 dark:text-blue-400': active && !summary,
        'text-gray-500 hover:text-blue-500 dark:hover:text-gray-300 hover:bg-gray-50 dark:hover:bg-gray-800': !active
      },
    ]"
    >
        <div v-if="active && !summary" class="absolute left-0 top-1/2 transform -translate-y-1/2 w-1 h-6 bg-blue-500 rounded-r-full"></div>
        <svg-icon dir="open" :name="icon" class="w-5 h-5" />
        <span v-show="titleShow" class="ml-3 text-sm font-medium transition-opacity duration-300">{{ title }}</span>
        <span
            v-if="summary && titleShow"
            class="shrink-0 transition-transform duration-300 group-open:-rotate-180 absolute right-3"
        >
      <svg-icon name="down" class="w-4 h-4" />
    </span>
    </component>
</template>