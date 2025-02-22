<!-- 计算输入框：支持在输入框进行加减乘除 -->
<script setup>
import NP from 'number-precision';
import { ref } from 'vue';

const props = defineProps(['modelValue']);
const emits = defineEmits(['update:modelValue']);
const inputRef = ref(null);

function compute() {
    const reg = /(.*)([+\-*/])(.*)/;
    const match = props.modelValue.match(reg);

    if (match) {
        const [_, x, operate, y] = match;

        // 校验输入值是否为有效数字
        if (isNaN(x) || isNaN(y)) {
            emits('update:modelValue', 'Invalid input');
            return;
        }

        let result;
        switch (operate) {
            case '+':
                result = NP.plus(Number(x), Number(y));
                break;
            case '-':
                result = NP.minus(Number(x), Number(y));
                break;
            case '*':
                result = NP.times(Number(x), Number(y));
                break;
            case '/':
                result = NP.divide(Number(x), Number(y));
                break;
            default:
                result = props.modelValue;
        }

        emits('update:modelValue', result.toString());
        inputRef.value?.focus(); // 计算完成后自动聚焦输入框
    }
}
</script>

<template>
    <el-input
        ref="inputRef"
        :model-value="modelValue"
        @input="(value) => $emit('update:modelValue', value)"
        @keydown.enter="compute"
    >
        <template #prefix>
            <svg-icon name="calculator" class="w-4 h-4" />
        </template>
    </el-input>
</template>