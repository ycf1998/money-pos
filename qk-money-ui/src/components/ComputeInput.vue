<!--
====================================================================
=                计算用的输入框，支持金额计算，比如 100*2
====================================================================
-->
<template>
    <el-input v-model="inputValue" @input="handleInput" @keydown="handleKeydown" @blur="handleBlur"
              :placeholder="placeholder" :disabled="disabled" :clearable="clearable" v-bind="$attrs">
        <template #prefix>
            <slot name="prefix"/>
        </template>
    </el-input>
</template>

<script setup>
import { ref, watch } from 'vue';
import NP from 'number-precision';

const props = defineProps({
    modelValue: { type: [String, Number], default: '' },
    placeholder: { type: String },
    disabled: { type: Boolean, default: false },
    clearable: { type: Boolean, default: true },
    precision: { type: Number, default: 2 },
})

const emit = defineEmits(['update:modelValue'])

const inputValue = ref(props.modelValue)

watch(() => props.modelValue, (val) => {
    inputValue.value = val
})

function handleInput(val) {
    const lastChar = val.slice(-1)
    if (['+', '-', '*', '/'].includes(lastChar)) {
        return
    }
    emit('update:modelValue', val)
}

function handleKeydown(event) {
    const key = event.key
    if (['+', '-', '*', '/'].includes(key)) {
        event.preventDefault()
        inputValue.value += key
    }
}

function handleBlur() {
    try {
        const result = NP.round(eval(inputValue.value), props.precision)
        inputValue.value = result
        emit('update:modelValue', result)
    } catch (e) {
        // 如果计算失败，保留原值
    }
}
</script>