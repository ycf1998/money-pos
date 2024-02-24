<template>
    <PageWrapper>
        <!-- 搜索栏 -->
        <MoneyRR :money-crud="moneyCrud">
            <el-input v-model="moneyCrud.query.tenantCode" placeholder="编码" class="md:!w-48"
                      @keyup.enter.native="moneyCrud.doQuery" />
            <el-input v-model="moneyCrud.query.tenantName" placeholder="名称" class="md:!w-48"
                      @keyup.enter.native="moneyCrud.doQuery" />
        </MoneyRR>
        <!-- 操作行 -->
        <MoneyCUD :money-crud="moneyCrud" />
        <!-- 数据表格 -->
        <MoneyCrudTable :money-crud="moneyCrud">
            <template #logo="{scope}">
                <el-image
                    class="w-8 h-8"
                    preview-teleported
                    :src="$money.getOssUrl(scope.row.logo)"
                    :preview-src-list="[$money.getOssUrl(scope.row.logo)]"
                    fit="cover"
                />
            </template>
            <template #loginLink="{scope}">
                <el-link :href="'?tenant=' + scope.row.tenantCode" target="_blank" type="primary">点击前往
                </el-link>
            </template>
            <template #opt="{scope}">
                <MoneyUD :money-crud="moneyCrud" :scope="scope" />
            </template>
        </MoneyCrudTable>
        <!-- 表单 -->
        <MoneyForm :money-crud="moneyCrud" :rules="rules">
            <el-form-item label="Logo" prop="logo">
                <el-upload class="avatar-uploader" :auto-upload="false" :show-file-list="false" accept="image/*"
                           :on-change="handleLogoSuccess">
                    <img v-if="moneyCrud.form.logo" :src="$money.getOssUrl(moneyCrud.form.logo)" class="w-32" alt="">
                    <el-icon v-else class="avatar-uploader-icon !w-32 !h-32">
                        <Plus />
                    </el-icon>
                </el-upload>
            </el-form-item>
            <el-form-item label="编码" prop="tenantCode">
                <el-input v-model.trim="moneyCrud.form.tenantCode" />
            </el-form-item>
            <el-form-item label="名称" prop="tenantName">
                <el-input v-model.trim="moneyCrud.form.tenantName" />
            </el-form-item>
            <el-form-item label="描述">
                <el-input v-model.trim="moneyCrud.form.tenantDesc" type="textarea" maxlength="250" show-word-limit />
            </el-form-item>
        </MoneyForm>
    </PageWrapper>
</template>

<script setup>
import PageWrapper from "@/components/PageWrapper.vue";
import MoneyCrud from '@/components/crud/MoneyCrud.js';
import MoneyCrudTable from "@/components/crud/MoneyCrudTable.vue";
import MoneyRR from "@/components/crud/MoneyRR.vue";
import MoneyCUD from "@/components/crud/MoneyCUD.vue";
import MoneyUD from "@/components/crud/MoneyUD.vue";
import MoneyForm from "@/components/crud/MoneyForm.vue";

import {ref} from "vue";
import {useUserStore} from "@/store/index.js";
import tenantApi from "@/api/system/tenant.js";

const userStore = useUserStore()
const columns = [
    {prop: 'tenantCode', label: '编码'},
    {prop: 'logo', label: 'Logo'},
    {prop: 'tenantName', label: '名称'},
    {prop: 'tenantDesc', label: '描述'},
    {prop: 'loginLink', label: '登录链接'},
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
    tenantCode: [{required: true, message: '请输入租户编码'}],
    tenantName: [{required: true, message: '请输入租户名称'}]
}
const moneyCrud = ref(new MoneyCrud({
    columns,
    crudMethod: tenantApi,
    optShow: {
        checkbox: userStore.hasPermission(['tenant:edit', 'tenant:del']),
        add: userStore.hasPermission('tenant:add'),
        edit: userStore.hasPermission('tenant:edit'),
        del: userStore.hasPermission('tenant:del')
    },
    msg: {
        add: '新增成功！默认管理员账号：admin，密码：admin'
    }
}))
moneyCrud.value.init(moneyCrud)

function handleLogoSuccess(file) {
    moneyCrud.value.form.logo = URL.createObjectURL(file.raw)
    moneyCrud.value.form.logoFile = file.raw
}
</script>
