<!-- 计算输入框：支持在输入框进行加减乘除 -->
<script setup>
import NP from 'number-precision'

const props = defineProps(['modelValue'])
const emits = defineEmits(['update:modelValue'])

function compute() {
    const reg = /(.*)([\\+\\-\\*/])(.*)/
    const group = props.modelValue.match(reg)
    if (group) {
        const x = group[1]
        const operate = group[2]
        const y = group[3]
        let result = props.modelValue
        switch (operate) {
            case '+':
                result = NP.plus(x, y)
                break
            case '-':
                result = NP.minus(x, y)
                break
            case '*':
                result = NP.times(x, y)
                break
            case '/':
                result = NP.divide(x, y)
                break
        }
        emits('update:modelValue', result)
    }
}
</script>
<template>
    <el-input :model-value="modelValue" @input="value => $emit('update:modelValue', value)"
              @keydown.enter="compute">
        <template #prefix>
            <svg-icon name="calculator" class="w-4 h-4" />
        </template>
    </el-input>
</template>