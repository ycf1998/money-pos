<template>
    <PageWrapper>
        <!-- 搜索栏 -->
        <MoneyRR :money-crud="moneyCrud">
            <el-date-picker v-model="datePicker" type="datetimerange" class="!w-full md:!w-auto"
                            :shortcuts="shortcuts" :default-time="defaultTime" :clearable="false"
                            value-format="YYYY-MM-DD HH:mm:ss"
                            range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期"
                            @change="handleDatePick" />
            <el-input v-model="moneyCrud.query.orderNo" placeholder="订单号" class="md:!w-48"
                      @keyup.enter.native="moneyCrud.doQuery" />
            <el-input v-model="moneyCrud.query.member" placeholder="会员" class="md:!w-48"
                      @keyup.enter.native="moneyCrud.doQuery" />
            <el-select v-model="moneyCrud.query.status" clearable placeholder="状态" class="md:!w-48"
                       @change="moneyCrud.doQuery">
                <el-option v-for="item in dict.orderStatus" :key="item.value" :label="item.desc" :value="item.value" />
            </el-select>
        </MoneyRR>
        <!-- 操作行 -->
        <MoneyCUD :money-crud="moneyCrud" class="!justify-center md:!justify-between flex-wrap items-end">
            <div class="flex gap-5 mb-2 md:gap-10">
                <el-statistic title="销售额" :precision="2" :value="orderCount.saleCount" />
                <el-statistic title="成本" :precision="2" :value="orderCount.costCount" />
                <el-statistic title="利润" :precision="2" :value="orderCount.profit" />
            </div>
        </MoneyCUD>
        <!-- 数据表格 -->
        <MoneyCrudTable :money-crud="moneyCrud">
            <template #address="{scope}">
                {{ scope.row.province + scope.row.city + scope.row.district + scope.row.address }}
            </template>
            <template #profit="{scope}">
                {{ NP.minus(scope.row.payAmount, scope.row.costAmount) }}
            </template>
            <template #status="{scope}">
                <el-tag :type="statusColor[scope.row.status] || 'primary'">
                    {{ dict.orderStatusKv[scope.row.status] }}
                </el-tag>
            </template>
            <template #opt="{scope}">
                <router-link :to="'/oms/order/detail/'+ scope.row.id" class="mr-2">
                    <el-button plain size="small">详情</el-button>
                </router-link>
                <el-popconfirm title="确定要退单吗？" @confirm="returnOrder(scope.row)"
                               v-if="scope.row.status !== 'RETURN'">
                    <template #reference>
                        <el-button plain type="danger" size="small">退单</el-button>
                    </template>
                </el-popconfirm>
            </template>
        </MoneyCrudTable>
        <!-- 表单 -->
        <MoneyForm :money-crud="moneyCrud">
            <!-- 补充表单项 -->
            <el-form-item label="表单项" prop="nickname">
                <el-input v-model="moneyCrud.form.name" />
            </el-form-item>
        </MoneyForm>
    </PageWrapper>
</template>

<script setup>
import PageWrapper from "@/components/PageWrapper.vue";
import MoneyCrud from '@/components/crud/MoneyCrud.js'
import MoneyCrudTable from "@/components/crud/MoneyCrudTable.vue";
import MoneyRR from "@/components/crud/MoneyRR.vue";
import MoneyCUD from "@/components/crud/MoneyCUD.vue";
import MoneyForm from "@/components/crud/MoneyForm.vue";

import {ref} from "vue";
import {useUserStore} from "@/store/index.js";
import NP from "number-precision";
import dayjs from "dayjs";
import orderApi from "@/api/oms/order.js";
import dictApi from "@/api/system/dict.js";

const userStore = useUserStore()
const columns = [
    {prop: 'orderNo', label: '订单号', width: 150},
    {prop: 'member', label: '会员'},
    {prop: 'address', label: '地址', show: false},
    {prop: 'costAmount', label: '成本'},
    {prop: 'totalAmount', label: '总价'},
    {prop: 'payAmount', label: '实付款'},
    {prop: 'couponAmount', label: '抵用券'},
    {prop: 'profit', label: '利润'},
    {prop: 'status', label: '状态', width: 100},
    {prop: 'createTime', label: '创建时间', width: 180},
    {prop: 'paymentTime', label: '支付时间', width: 180, show: false},
    {prop: 'completionTime', label: '完成时间', width: 180, show: false},
    {prop: 'remark', label: '备注', show: false},
    {
        prop: 'opt',
        label: '操作',
        width: 150,
        align: 'center',
        fixed: 'right',
        showOverflowTooltip: false,
    },
]
const moneyCrud = ref(new MoneyCrud({
    columns,
    crudMethod: orderApi,
    optShow: {checkbox: false, add: false, edit: false, del: false},
}))

const dict = ref({})
const statusColor = {
    'RETURN': 'info',
    'PAID': 'success',
    'DONE': 'success'
}
const datePicker = ref([dayjs().startOf('M').format('YYYY-MM-DD HH:mm:ss'), dayjs().endOf('M').format('YYYY-MM-DD HH:mm:ss')])
const defaultTime = [dayjs().startOf('d').toDate(), dayjs().endOf('d').toDate()]
const shortcuts = [
    {
        text: '今天',
        value() {
            return [dayjs().startOf('d').toDate(), dayjs().endOf('d').toDate()]
        }
    },
    {
        text: '本月',
        value() {
            return [dayjs().startOf('M').toDate(), dayjs().endOf('M').toDate()]
        }
    },
    {
        text: '最近7天',
        value() {
            return [dayjs().subtract(6, 'd').startOf('d').toDate(), dayjs().endOf('d').toDate()]
        }
    },
    {
        text: '最近30天',
        value() {
            return [dayjs().subtract(29, 'd').startOf('d').toDate(), dayjs().endOf('d').toDate()]
        }
    }
]
moneyCrud.value.init(moneyCrud, async () => {
    dict.value = await dictApi.loadDict(["orderStatus"])
    moneyCrud.value.query.startTime = datePicker.value[0]
    moneyCrud.value.query.endTime = datePicker.value[1]
})

const orderCount = ref({
    saleCount: 0,
    costCount: 0,
    profit: 0
})
moneyCrud.value.Hook.afterDoQuery = () => {
    orderApi.getCount(moneyCrud.value.query)
        .then(res => {
            const {data} = res
            orderCount.value.saleCount = data.saleCount
            orderCount.value.costCount = data.costCount
            orderCount.value.profit = data.profit
        })
}

function handleDatePick(value) {
    moneyCrud.value.query.startTime = value[0]
    moneyCrud.value.query.endTime = value[1]
    moneyCrud.value.doQuery()
}

function returnOrder(row) {
    orderApi.returnOrder([row.id])
        .then(() => {
            moneyCrud.value.messageOk()
            moneyCrud.value.doQuery()
        })
}
</script>
