<template>
    <div class="hidden">
        <div id="printTarget" class="relative text-center p-1" style="width: 58mm;left: -2mm">
            <template v-if="order.info">
                <h1 class="my-3 text-base font-bold">麦尼收银</h1>
                <h2 class="text-left">单号：{{ order.info.orderNo }}</h2>
                <h2 class="text-left">时间：{{ order.info.createTime }}</h2>
                <div class="flex flex-col gap-3 mt-2">
                    <div class="grid grid-cols-4  text-center text-base font-bold">
                        <span>原价</span>
                        <span>现价</span>
                        <span>优惠</span>
                        <span>小计</span>
                    </div>
                    <div v-for="(item, index) in order.detail" :key="index" class="grid grid-cols-4 text-center gap-1">
                        <span class="col-span-3">{{ item.goodsName }}</span>
                        <span class="col-span-1">X {{ item.quantity }}</span>
                        <span class="col-span-1">{{ item.salePrice }}</span>
                        <span class="col-span-1">{{ item.goodsPrice }}</span>
                        <span class="col-span-1">{{ order.info.vip ? item.coupon : 0 }}</span>
                        <span class="col-span-1">{{ NP.times(item.goodsPrice, item.quantity) }}</span>
                    </div>

                    <div class="grid grid-cols-2 text-center text-base">
                        <span class="col-span-2">-----------------------------</span>
                        <span>总计：{{ order.info.totalAmount }}</span>
                        <span>优惠：{{ NP.minus(order.info.totalAmount, order.info.payAmount) }}</span>
                        <span>应付：{{ order.info.payAmount }}</span>
                        <span>用券：{{ order.info.couponAmount }}</span>
                        <span v-if="order.info.vip && order.member.type !== 'INNER'">会员：{{ order.member.name
                            }}</span>
                        <span v-if="order.info.vip && order.member.type !== 'INNER'">余券：{{ order.member.coupon
                            }}</span>
                    </div>
                    <h2>联系电话：{{ contract }}</h2>
                </div>
            </template>
        </div>
    </div>
</template>
<script setup>
import {ref, nextTick} from "vue";
import {useUserStore} from "@/store/index.js";
import NP from "number-precision";
import print from 'print-js'
import 'print-js/dist/print.css';

const order = ref({})
const contract = useUserStore().info.phone

defineExpose({
    print: async (o) => {
        order.value = o
        await nextTick()
        print({
            printable: 'printTarget',
            type: 'html',
            targetStyles: ['*'],
        })
        order.value = {}
    }
})
</script>