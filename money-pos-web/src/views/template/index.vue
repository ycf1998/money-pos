<template>
    <PageWrapper>
        <!-- 搜索栏 -->
        <MoneyRR :money-crud="moneyCrud">
            <!-- 补充搜索条件 -->
            <el-input v-model="moneyCrud.query.name" placeholder="搜索条件" class="md:!w-48"
                      @keyup.enter.native="moneyCrud.doQuery" />
        </MoneyRR>
        <!-- 操作行 -->
        <MoneyCUD :money-crud="moneyCrud" />
        <!-- 数据表格 -->
        <MoneyCrudTable :money-crud="moneyCrud">
            <template #opt="{scope}">
                <MoneyUD :money-crud="moneyCrud" :scope="scope" />
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
import MoneyCrud from '@/components/crud/MoneyCrud.js';
import MoneyCrudTable from "@/components/crud/MoneyCrudTable.vue";
import MoneyRR from "@/components/crud/MoneyRR.vue";
import MoneyCUD from "@/components/crud/MoneyCUD.vue";
import MoneyUD from "@/components/crud/MoneyUD.vue";
import MoneyForm from "@/components/crud/MoneyForm.vue";

import {ref} from "vue";
import {useUserStore} from "@/store/index.js";
import templateApi from "@/api/template.js";

const userStore = useUserStore()
const columns = [
    // 补充列
    {prop: 'name', label: '列'},
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
const moneyCrud = ref(new MoneyCrud({
    columns,
    crudMethod: templateApi,
    // 权限控制
    optShow: {
        checkbox: userStore.hasPermission(['template:edit', 'template:del']),
        add: userStore.hasPermission('template:add'),
        edit: userStore.hasPermission('template:edit'),
        del: userStore.hasPermission('template:del')
    },
}))
moneyCrud.value.init(moneyCrud)

</script>
