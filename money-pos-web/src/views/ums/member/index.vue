<template>
    <PageWrapper>
        <!-- 搜索栏 -->
        <MoneyRR :money-crud="moneyCrud">
            <el-input v-model="moneyCrud.query.code" placeholder="会员号" class="md:!w-48"
                      @keyup.enter.native="moneyCrud.doQuery" />
            <el-input v-model="moneyCrud.query.name" placeholder="会员名称" class="md:!w-48"
                      @keyup.enter.native="moneyCrud.doQuery" />
            <el-input v-model="moneyCrud.query.phone" placeholder="手机号码" class="md:!w-48"
                      @keyup.enter.native="moneyCrud.doQuery" />
            <el-select v-model="moneyCrud.query.type" clearable placeholder="会员类型" class="md:!w-48"
                       @change="moneyCrud.doQuery">
                <el-option v-for="item in dict.memberType" :key="item.value" :label="item.desc" :value="item.value" />
            </el-select>
        </MoneyRR>
        <!-- 操作行 -->
        <MoneyCUD :money-crud="moneyCrud" />
        <!-- 数据表格 -->
        <MoneyCrudTable :money-crud="moneyCrud">
            <template #address="{scope}">
                {{ scope.row.province + scope.row.city + scope.row.district + scope.row.address }}
            </template>
            <template #type="{scope}">
                {{ dict.memberTypeKv[scope.row.type] }}
            </template>
            <template #opt="{scope}">
                <MoneyUD :money-crud="moneyCrud" :scope="scope" />
            </template>
        </MoneyCrudTable>
        <!-- 表单 -->
        <MoneyForm :money-crud="moneyCrud" :rules="rules">
            <el-form-item label="会员名称" prop="name">
                <el-input v-model.trim="moneyCrud.form.name" />
            </el-form-item>
            <el-form-item label="会员类型" prop="type">
                <el-select v-model="moneyCrud.form.type" placeholder="请选择会员类型" class="w-full">
                    <el-option v-for="item in dict.memberType" :key="item.value" :label="item.desc"
                               :value="item.value" />
                </el-select>
            </el-form-item>
            <el-form-item label="手机号" prop="phone">
                <el-input v-model="moneyCrud.form.phone" />
            </el-form-item>
            <el-form-item label="抵用券" prop="coupon">
                <ComputeInput v-model="moneyCrud.form.coupon" />
            </el-form-item>
            <el-form-item label="地址">
                <PcdLinkage :province="moneyCrud.form.province" :city="moneyCrud.form.city"
                            :district="moneyCrud.form.district" @change="pcdChange" class="w-full" />
            </el-form-item>
            <el-form-item label="详细地址" prop="address">
                <el-input v-model.trim="moneyCrud.form.address" type="textarea" maxlength="250" show-word-limit />
            </el-form-item>
            <el-form-item label="备注">
                <el-input v-model.trim="moneyCrud.form.remark" type="textarea" maxlength="250" show-word-limit />
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
import MoneyUD from "@/components/crud/MoneyUD.vue";
import MoneyForm from "@/components/crud/MoneyForm.vue";
import PcdLinkage from "@/components/PcdLinkage.vue";
import ComputeInput from "@/components/ComputeInput.vue";

import {ref} from "vue";
import {useUserStore} from "@/store/index.js";
import memberApi from "@/api/ums/member.js";
import dictApi from "@/api/system/dict.js";

const userStore = useUserStore()
const columns = [
    {prop: 'code', label: '会员号', show: false},
    {prop: 'name', label: '会员名称'},
    {prop: 'type', label: '会员类型'},
    {prop: 'phone', label: '手机号码'},
    {prop: 'coupon', label: '抵用券'},
    {prop: 'consumeAmount', label: '总消费金额', sortable: 'custom'},
    {prop: 'consumeCoupon', label: '消费抵用券'},
    {prop: 'consumeTimes', label: '消费次数', sortable: 'custom'},
    {prop: 'cancelTimes', label: '取消订单次数', show: false},
    {prop: 'address', label: '地址'},
    {prop: 'remark', label: '备注', show: false},
    {
        prop: 'opt',
        label: '操作',
        width: 120,
        align: 'center',
        fixed: 'right',
        showOverflowTooltip: false,
        isMoneyUD: true
    },
]
const rules = {
    name: [
        {required: true, message: '请输入会员名称'}
    ],
    type: [
        {required: true, message: '请选择会员类型'}
    ],
    phone: [
        {required: true, message: '请输入手机号'},
        {pattern: /^1([38][0-9]|4[014-9]|[59][0-35-9]|6[2567]|7[0-8])\d{8}$/, message: '格式错误'}
    ],
    coupon: [
        {required: true, message: '请输入抵用券', trigger: 'change'},
        {pattern: /^(0|[1-9]\d*)(\.\d+)?$/, message: '仅支持0和正数'}
    ]
}
const moneyCrud = ref(new MoneyCrud({
    columns,
    crudMethod: memberApi,
    // 权限控制
    optShow: {
        checkbox: userStore.hasPermission(['umsMember:edit', 'umsMember:del']),
        add: userStore.hasPermission('umsMember:add'),
        edit: userStore.hasPermission('umsMember:edit'),
        del: userStore.hasPermission('umsMember:del')
    },
    defaultForm: {
        type: 'MEMBER',
        coupon: 0,
        province: '福建省',
        city: '泉州市',
        district: '南安'
    }
}))
moneyCrud.value.init(moneyCrud)

const dict = ref([])
const init = async () => {
    dict.value = await dictApi.loadDict(["memberType"])
}
init()

function pcdChange(province, city, district) {
    moneyCrud.value.form.province = province
    moneyCrud.value.form.city = city
    moneyCrud.value.form.district = district
}
</script>
