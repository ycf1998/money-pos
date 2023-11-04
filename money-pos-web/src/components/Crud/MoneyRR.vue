<template>
    <div class="flex flex-col md:flex-row" :class="{'hidden': !moneyCrud.optShow.moneyRR}">
        <div class="flex flex-wrap gap-3">
            <slot />
            <div>
                <slot name="opt">
                    <el-button plain type="success" @click="moneyCrud.doQuery">搜索</el-button>
                    <el-button plain type="warning" @click="moneyCrud.reset">重置</el-button>
                </slot>
            </div>
        </div>
    </div>
</template>

<script setup>
import MoneyCrud from "@/components/crud/MoneyCrud.js";

defineProps({
    moneyCrud: {
        required: true,
        type: MoneyCrud
    }
})
</script>