<template>
    <el-dialog
        v-model="visible"
        title="结算清单"
        :width="dialogWidth"
        :close-on-click-modal="false"
        class="settle-dialog"
        @close="emit('update:modelValue', false)"
    >
        <div class="space-y-3">
            <div :class="showTwoColumns ? 'settle-grid' : ''">
                <template v-if="showTwoColumns">
                    <div class="settle-column" v-for="(column, colIndex) in settleColumns" :key="colIndex">
                        <div class="grid grid-cols-4 text-center text-sm font-medium el-text--secondary pb-2 border-b border-border">
                            <span>商品</span>
                            <span>原价</span>
                            <span>现价</span>
                            <span>优惠</span>
                        </div>
                        <div v-for="(item, index) in column" :key="index" class="py-2 border-b border-border last:border-0">
                            <div class="grid grid-cols-4 text-center text-sm">
                                <span class="font-medium">{{ item.goodsName }}</span>
                                <span class="el-text--secondary">{{ item.salePrice }}</span>
                                <span class="text-green-600 font-medium">{{ item.goodsPrice }}</span>
                                <span class="text-blue-500">{{ isVip ? item.coupon : 0 }}</span>
                            </div>
                            <div class="grid grid-cols-3 text-center text-xs el-text--secondary mt-1">
                                <span>× {{ item.quantity }}</span>
                                <span>小计 {{ NP.times(item.goodsPrice, item.quantity) }}</span>
                                <span>优惠 {{ isVip ? NP.times(item.coupon, item.quantity) : 0 }}</span>
                            </div>
                        </div>
                    </div>
                </template>
                <template v-else>
                    <div class="grid grid-cols-4 text-center text-sm font-medium el-text--secondary pb-2 border-b border-border">
                        <span>商品</span>
                        <span>原价</span>
                        <span>现价</span>
                        <span>优惠</span>
                    </div>
                    <div v-for="(item, index) in order" :key="index" class="py-2 border-b border-border last:border-0">
                        <div class="grid grid-cols-4 text-center text-sm">
                            <span class="font-medium">{{ item.goodsName }}</span>
                            <span class="el-text--secondary">{{ item.salePrice }}</span>
                            <span class="text-green-600 font-medium">{{ item.goodsPrice }}</span>
                            <span class="text-blue-500">{{ isVip ? item.coupon : 0 }}</span>
                        </div>
                        <div class="grid grid-cols-3 text-center text-xs el-text--secondary mt-1">
                            <span>× {{ item.quantity }}</span>
                            <span>小计 {{ NP.times(item.goodsPrice, item.quantity) }}</span>
                            <span>优惠 {{ isVip ? NP.times(item.coupon, item.quantity) : 0 }}</span>
                        </div>
                    </div>
                </template>
            </div>
            <div class="bg-fill-light rounded-lg p-3 space-y-2">
                <div class="flex justify-between text-sm">
                    <span class="el-text--secondary">总计</span>
                    <span class="font-medium">{{ totalAmount }}</span>
                </div>
                <div class="flex justify-between text-sm">
                    <span class="el-text--secondary">优惠</span>
                    <span class="font-medium text-red-500">{{ NP.minus(totalAmount, payAmount) }}</span>
                </div>
                <div class="flex justify-between text-base font-bold">
                    <span>应付</span>
                    <span class="text-green-600">{{ payAmount }}</span>
                </div>
                <div class="flex justify-between text-sm">
                    <span class="el-text--secondary">用券</span>
                    <span class="font-medium text-blue-500">{{ couponAmount }}</span>
                </div>
                <template v-if="isVip && member && member.type !== 'INNER'">
                    <el-divider class="my-2" />
                    <div class="flex justify-between text-sm">
                        <span class="el-text--secondary">会员</span>
                        <span class="font-medium">{{ member.name }}</span>
                    </div>
                    <div class="flex justify-between text-sm">
                        <span class="el-text--secondary">余券</span>
                        <span class="font-medium text-blue-500">{{ NP.minus(member.coupon, couponAmount) }}</span>
                    </div>
                </template>
            </div>
        </div>
        <template #footer>
            <div class="flex justify-end gap-2">
                <el-button @click="handleCancel">取消</el-button>
                <el-button type="primary" @click="handleConfirm">确认结算</el-button>
            </div>
        </template>
    </el-dialog>
</template>

<script setup>
import { computed, ref, watch } from 'vue'
import NP from 'number-precision'
import { isMobile } from '@/utils/index.js'

const props = defineProps({
    modelValue: {
        type: Boolean,
        default: false
    },
    order: {
        type: Array,
        default: () => []
    },
    isVip: {
        type: Boolean,
        default: false
    },
    member: {
        type: Object,
        default: null
    }
})

const emit = defineEmits(['update:modelValue', 'confirm', 'cancel'])

const visible = ref(props.modelValue)

watch(() => props.modelValue, (val) => {
    visible.value = val
})

const totalAmount = computed(() => props.order.reduce((prev, next) => NP.plus(prev, NP.times(next.quantity, next.salePrice)), 0))
const couponAmount = computed(() => props.order.reduce((prev, next) => NP.plus(prev, NP.times(next.quantity, props.isVip ? next.coupon : 0)), 0))
const payAmount = computed(() => props.order.reduce((prev, next) => NP.plus(prev, NP.times(next.quantity, next.goodsPrice)), 0))

const mobile = isMobile()
const showTwoColumns = computed(() => !mobile && props.order.length > 6)
const dialogWidth = computed(() => {
    if (mobile) return '90%'
    return showTwoColumns.value ? '720px' : '420px'
})

const settleColumns = computed(() => {
    if (props.order.length <= 6) return [props.order]
    const mid = Math.ceil(props.order.length / 2)
    return [props.order.slice(0, mid), props.order.slice(mid)]
})

function handleCancel() {
    visible.value = false
    emit('update:modelValue', false)
    emit('cancel')
}

function handleConfirm() {
    emit('confirm')
}
</script>

<style lang="less" scoped>
.settle-dialog {
    :deep(.bg-fill-light) {
        background-color: var(--el-fill-color-light);
    }
    
    :deep(.border-border) {
        border-color: var(--el-border-color);
    }
}

.settle-grid {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 1.5rem;
}

.settle-column {
    min-width: 0;
}
</style>
