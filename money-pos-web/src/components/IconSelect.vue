<!-- 图标选择器 -->
<script setup>
import { ref, computed } from 'vue';
import iconNames from 'virtual:svg-icons-names';
import SvgIcon from '@/components/SvgIcon.vue';

const { defaultIcon, dir } = defineProps({
    dir: {
        type: String,
        default: 'open',
    },
    defaultIcon: {
        type: String,
        default: 'app',
    },
});

const emits = defineEmits(['selected']);
const icon = ref(defaultIcon);

// 动态生成图标列表
const prefix = computed(() => `icon-${dir ? dir + '-' : ''}`);
const icons = computed(() =>
    iconNames
        .filter((name) => name.startsWith(prefix.value))
        .map((name) => name.replace(prefix.value, ''))
);

// 确保默认值为有效图标
if (!icons.value.includes(icon.value)) {
    icon.value = icons.value[0] || '';
}
</script>

<template>
    <el-select
        v-model="icon"
        filterable
        placeholder="请选择图标"
        @change="(value) => $emit('selected', value)"
    >
        <template #prefix>
            <SvgIcon :dir="dir" :name="icon" class="w-4 h-4" />
        </template>
        <div class="grid grid-cols-3 px-1">
            <el-option
                v-for="item in icons"
                :key="item"
                :label="item"
                :value="item"
                class="flex items-center gap-1"
            >
                <span><SvgIcon :dir="dir" :name="item" class="w-4 h-4" /></span>
                <span>{{ item }}</span>
            </el-option>
        </div>
    </el-select>
</template>