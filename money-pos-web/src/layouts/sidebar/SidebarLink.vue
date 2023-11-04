<script setup>
import {computed} from "vue";
import {sidebarState} from '@/composables'

const titleShow = computed(() => sidebarState.isOpen || sidebarState.isHovered)

defineProps({
    summary: {
        type: Boolean,
        required: false,
    },
    href: {
        type: String,
        required: false,
    },
    to: {
        type: [String, Object],
        required: false,
    },
    active: {
        type: Boolean,
        default: false,
    },
    title: {
        type: String,
        required: true,
    },
    icon: {
        type: String,
        default: '',
    },
})
</script>

<template>
    <summary v-if="summary" :class="['relative flex rounded-lg px-2 py-2.5 text-sm font-medium cursor-pointer ' +
     'text-gray-500 hover:text-black dark:hover:text-gray-300',
        {'text-blue-700 bg-blue-50 dark:bg-gray-800 dark:text-blue-400': active && !sidebarState.isOpen && !sidebarState.isHovered}]">
        <svg-icon dir="open" :name="icon" />
        <span class="ml-2 text-base font-medium" v-show="titleShow">
            {{ title }}
        </span>
        <span v-show="titleShow" class="shrink-0 transition duration-300 group-open:-rotate-180 absolute right-1">
            <svg-icon name="down" />
        </span>
    </summary>

    <a v-else-if="href" :href="href" :class="['relative flex rounded-lg px-2 py-2.5 text-sm font-medium',
        {'text-blue-700 bg-blue-50 dark:bg-gray-800 dark:text-blue-400': active},
        {'text-gray-500 hover:text-blue-500 dark:hover:text-gray-300': !active}]">
        <svg-icon dir="open" :name="icon" />
        <span class="ml-2 text-base font-medium" v-show="titleShow">{{ title }}</span>
    </a>

    <router-link v-else-if="to" :to="to" :class="['relative flex rounded-lg px-2 py-2.5 text-sm font-medium',
        {'text-blue-700 bg-blue-50 dark:bg-gray-800 dark:text-blue-400': active},
        {'text-gray-500 hover:text-blue-500 dark:hover:text-gray-300': !active}]">
        <svg-icon dir="open" :name="icon" />
        <span class="ml-2 text-base" v-show="titleShow">
            {{ title }}
        </span>
    </router-link>
</template>
