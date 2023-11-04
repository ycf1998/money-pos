<!-- SVG 图标 -->
<template>
    <svg :class="iconClass" :fill="fill" aria-hidden="true">
        <use :xlink:href="iconName" />
    </svg>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
    dir: {
        type: String,
        default: ''
    },
    name: {
        type: String,
        required: true
    },
    class: {
        type: String,
        default: ''
    },
    fill: {
        type: String,
        default: 'currentColor'
    }
})

const iconName = computed(() => {
    return `#icon-${props.dir? props.dir + '-' : ''}${props.name}`
})
const iconClass = computed(() => {
    return props.class ? props.class : `w-6 h-6`
})
</script>