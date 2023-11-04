<!-- 图标选择器 -->
<script setup>
import {ref} from "vue";
import iconNames from 'virtual:svg-icons-names';
import SvgIcon from "@/components/SvgIcon.vue";

const {defaultIcon, dir} = defineProps({
    dir: {
        type: String,
        default: 'open'
    },
    defaultIcon: {
        type: String,
        default: 'app'
    }
})
defineEmits(['selected'])

const icon = ref(defaultIcon + '')
const prefix = `icon-${dir ? dir + '-' : ''}`
const icons = iconNames.filter(name => name.startsWith(prefix)).map(name => name.replace(prefix, ''))
</script>
<template>
    <el-select v-model="icon" filterable placeholder="请选择图标" @change="() => $emit('selected', icon)">
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