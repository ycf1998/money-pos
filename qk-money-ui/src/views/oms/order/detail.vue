<template>
    <el-card v-if="order.id" class="flex-1 rounded-md bg-base-100 sm:m-2 my-2">
        <template #header>
            <div class="flex justify-between items-center">
                <span>订单状态：
                    <el-tag :type="statusColor[order.status] || ''">{{ dict.orderStatusKv[order.status] }}</el-tag>
                </span>
                <el-button type="primary" text @click="print">打印单据</el-button>
            </div>
        </template>
        <el-descriptions title="基本信息" :column="mobile ? 2: 4" border>
            <el-descriptions-item>
                <template #label>
                    <el-icon>
                        <Promotion />
                    </el-icon>
                    订单号
                </template>
                {{ order.orderNo }}
            </el-descriptions-item>
            <el-descriptions-item>
                <template #label>
                    <el-icon>
                        <User />
                    </el-icon>
                    会员名
                </template>
                {{ order.member }}
            </el-descriptions-item>
            <el-descriptions-item>
                <template #label>
                    <el-icon>
                        <Phone />
                    </el-icon>
                    联系方式
                </template>
                {{ member.phone }}
            </el-descriptions-item>
            <el-descriptions-item>
                <template #label>
                    <el-icon>
                        <UserFilled />
                    </el-icon>
                    会员类型
                </template>
                {{ dict.memberTypeKv[member.type] }}
            </el-descriptions-item>
            <el-descriptions-item>
                <template #label>
                    <el-icon>
                        <OfficeBuilding />
                    </el-icon>
                    地址
                </template>
                {{ order.province + order.city + order.district + order.address }}
            </el-descriptions-item>
            <el-descriptions-item>
                <template #label>
                    <el-icon>
                        <Tickets />
                    </el-icon>
                    备注
                </template>
                {{ order.remark }}
            </el-descriptions-item>
        </el-descriptions>

        <h4 class="mt-4 font-bold">商品信息</h4>
        <el-table class="mt-4" :data="detail" border :summary-method="getSummaries" show-summary>
            <el-table-column prop="goodsBarcode" label="条码" align="center" />
            <el-table-column prop="goodsName" label="名称" align="center" />
            <el-table-column prop="quantity" label="数量" align="center" />
            <el-table-column prop="salePrice" label="售价" align="center" />
            <el-table-column prop="vipPrice" label="会员价" align="center" />
            <el-table-column prop="coupon" label="抵用券" align="center" />
            <el-table-column prop="goodsPrice" label="应收" align="center" />
            <el-table-column prop="subTotal" label="小计" align="center">
                <template #default="{ row }">
                    {{ NP.times(row.quantity, row.goodsPrice) }}
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center" width="150">
                <template #default="{ row }">
                    <el-button type="danger" size="small" @click="handleReturnGoods(row)">退货</el-button>
                    <span v-if="row.returnQuantity > 0" class="text-gray-500"> ( 已退X{{ row.returnQuantity }} )</span>
                </template>
            </el-table-column>
        </el-table>

        <el-descriptions class="mt-4" title="费用信息" :column="4" border direction="vertical">
            <el-descriptions-item>
                <template #label>总计</template>
                {{ order.totalAmount }}
            </el-descriptions-item>
            <el-descriptions-item>
                <template #label>优惠</template>
                {{ NP.minus(order.totalAmount, order.couponAmount) }}
            </el-descriptions-item>
            <el-descriptions-item>
                <template #label>用券</template>
                <span>{{ order.couponAmount }}</span>
                <span v-if="returnCoupon > 0" class="text-gray-500"> ( -{{ returnCoupon }} )</span>
            </el-descriptions-item>
            <el-descriptions-item>
                <template #label>共支付</template>
                <span class="text-red-600 text-xl">￥{{ order.payAmount }}</span>
                <span v-if="returnPrice > 0" class="text-gray-500"> ( -{{ returnPrice }} )</span>
            </el-descriptions-item>
        </el-descriptions>

        <h4 class="mt-4 font-bold">操作记录</h4>
        <el-table class="mt-4" :data="log" border>
            <el-table-column prop="description" label="操作描述" align="center">
                <template #default="{ row }">
                    <span v-html="row.description" />
                </template>
            </el-table-column>
            <el-table-column prop="createBy" label="操作人" align="center" />
            <el-table-column prop="createTime" label="操作时间" align="center" />
        </el-table>
        <print-order ref="printOrder" />
    </el-card>
</template>
<script setup>
import PrintOrder from "@/views/pos/printOrder.vue";

import {onBeforeMount, ref} from 'vue'
import {useRoute} from 'vue-router'

import orderApi from '@/api/oms/order.js'
import dictApi from "@/api/system/dict.js";
import NP from "number-precision";
import {isMobile} from "@/utils/index.js";

const mobile = isMobile()
const printOrder = ref()
const id = useRoute().params.id
const statusColor = {
    'RETURN': 'info',
    'PAID': 'success',
    'DONE': 'success'
}
const dict = ref()
const order = ref({})
const detail = ref([])
const member = ref({})
const log = ref({})
const returnPrice = ref(0)
const returnCoupon = ref(0)

onBeforeMount(async () => {
    dict.value = await dictApi.loadDict(["orderStatus", "memberType"])
    await loadDetail()
})

async function loadDetail() {
    const res = await orderApi.getDetail(id).then(res => res.data)
    order.value = res.order
    detail.value = res.orderDetail
    member.value = res.member
    log.value = res.orderLog
}

function print() {
    printOrder.value.print({
        info: order.value,
        detail: detail.value,
        member: member.value
    })
}

function handleReturnGoods(row) {
    ElMessageBox.prompt('请输入退货数量', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /\d+/,
        inputErrorMessage: '请输入正数数',
    })
        .then(({value}) => {
            if (value > parseInt(row.quantity) - parseInt(row.returnQuantity)) {
                ElMessage.warning('退货数量不能超过剩余商品数量')
                return
            }
            const params = {
                id: row.id,
                quantity: value
            }
            orderApi.returnGoods(params).then(() => {
                ElMessage.success('退货成功')
                loadDetail()
            })
        })
}

function getSummaries(param) {
    const {columns, data} = param
    const sums = []
    columns.forEach((column, index) => {
        if (index === 0) {
            sums[index] = '总价'
            return
        }
        if (column.property === 'subTotal') {
            let values = data.map((item) =>
                Number(item['quantity'] * item['goodsPrice'])
            )
            sums[index] = values.reduce((prev, curr) => {
                const value = Number(curr)
                if (!isNaN(value)) {
                    return prev + curr
                } else {
                    return prev
                }
            }, 0)
            values = data.map((item) =>
                Number(item['returnQuantity'] * item['goodsPrice'])
            )
            returnPrice.value = values.reduce((prev, curr) => {
                const value = Number(curr)
                if (!isNaN(value)) {
                    return prev + curr
                } else {
                    return prev
                }
            }, 0)
            values = data.map((item) =>
                Number(item['returnQuantity'] * item['coupon'])
            )
            returnCoupon.value = values.reduce((prev, curr) => {
                const value = Number(curr)
                if (!isNaN(value)) {
                    return prev + curr
                } else {
                    return prev
                }
            }, 0)
        }
    })
    return sums
}
</script>