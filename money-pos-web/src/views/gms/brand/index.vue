<template>
    <PageWrapper>
        <!-- 搜索栏 -->
        <MoneyRR :money-crud="moneyCrud">
            <el-input v-model="moneyCrud.query.name" placeholder="品牌名称" class="md:!w-48"
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
            <template #opt="{scope}">
                <MoneyUD :money-crud="moneyCrud" :scope="scope" />
            </template>
        </MoneyCrudTable>
        <!-- 表单 -->
        <MoneyForm :money-crud="moneyCrud" :rules="rules">
            <el-form-item label="Logo" prop="logo">
                <el-upload class="avatar-uploader" :auto-upload="false" :show-file-list="false" accept="image/*"
                           :on-change="handleLogoSuccess">
                    <img v-if="moneyCrud.form.logo" :src="$money.getOssUrl(moneyCrud.form.logo)" class="w-24" alt="logo">
                    <el-icon v-else class="avatar-uploader-icon !w-24 !h-24">
                        <Plus />
                    </el-icon>
                </el-upload>
            </el-form-item>
            <el-form-item label="名称" prop="name">
                <el-input v-model.trim="moneyCrud.form.name" />
            </el-form-item>
            <el-form-item label="描述">
                <el-input v-model.trim="moneyCrud.form.description" type="textarea" maxlength="250" show-word-limit />
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

import {ref} from "vue";
import {useUserStore} from "@/store/index.js";
import brandApi from "@/api/gms/brand.js"

const userStore = useUserStore()
const columns = [
    {prop: 'name', label: '品牌名称'},
    {prop: 'logo', label: 'Logo'},
    {prop: 'description', label: '描述'},
    {prop: 'goodsCount', label: '商品数量'},
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
        {required: true, message: '请输入品牌名称'}
    ]
}
const moneyCrud = ref(new MoneyCrud({
    columns,
    crudMethod: brandApi,
    optShow: {
        checkbox: userStore.hasPermission(['gmsBrand:edit', 'gmsBrand:del']),
        add: userStore.hasPermission('gmsBrand:add'),
        edit: userStore.hasPermission('gmsBrand:edit'),
        del: userStore.hasPermission('gmsBrand:del')
    }
}))
moneyCrud.value.init(moneyCrud)

function handleLogoSuccess(file) {
    moneyCrud.value.form.logo = URL.createObjectURL(file.raw)
    moneyCrud.value.form.logoFile = file.raw
}
</script>