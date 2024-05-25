<template>
    <PageWrapper>
        <!-- 搜索栏 -->
        <MoneyRR :money-crud="moneyCrud">
            <el-input v-model="moneyCrud.query.nameOrDesc" placeholder="字典名称/描述" class="md:!w-48"
                      @keyup.enter.native="moneyCrud.doQuery" />
        </MoneyRR>
        <!-- 数据表格 -->
        <div class="inline-grid md:grid-cols-2 md:gap-2">
            <el-card header="字典" shadow="never" class="mb-4 md:mb-0">
                <!-- 操作行 -->
                <MoneyCUD :money-crud="moneyCrud" class="pb-4" />
                <MoneyCrudTable :money-crud="moneyCrud" class="pb-4" highlight-current-row>
                    <template #opt="{scope}">
                        <MoneyUD :money-crud="moneyCrud" :scope="scope" />
                    </template>
                </MoneyCrudTable>
            </el-card>
            <el-card shadow="never">
                <template #header>
                    <span>字典详情</span>
                    <el-button plain class="float-right" :disabled="!moneyCrud2.optShow.add" @click="moneyCrud2.toAdd">
                        添加详情
                    </el-button>
                </template>
                <MoneyCrudTable :money-crud="moneyCrud2">
                    <template #hidden="{scope}">
                        <el-tag v-if="scope.row.hidden === true" type="info">是</el-tag>
                        <el-tag v-else type="success">否</el-tag>
                    </template>
                    <template #opt="{scope}">
                        <MoneyUD :money-crud="moneyCrud2" :scope="scope" />
                    </template>
                </MoneyCrudTable>
            </el-card>
        </div>
        <!-- 表单 -->
        <MoneyForm :money-crud="moneyCrud" :rules="rules">
            <el-form-item label="字典名称" prop="dictName">
                <el-input v-model.trim="moneyCrud.form.dictName" />
            </el-form-item>
            <el-form-item label="字典描述">
                <el-input v-model.trim="moneyCrud.form.dictDesc" type="textarea" maxlength="250" show-word-limit />
            </el-form-item>
        </MoneyForm>
        <!-- 表单 -->
        <MoneyForm :money-crud="moneyCrud2" :rules="rules2">
            <el-form-item label="字典名" prop="dict">
                <el-input v-model.trim="moneyCrud2.form.dict" placeholder="点击表格选中字典" disabled />
            </el-form-item>
            <el-form-item label="字典值" prop="value">
                <el-input v-model.trim="moneyCrud2.form.value" />
            </el-form-item>
            <el-form-item label="中文描述" prop="cnDesc">
                <el-input v-model.trim="moneyCrud2.form.cnDesc" />
            </el-form-item>
            <!-- TODO 多语言 -->
            <!--            <el-form-item label="英文描述" prop="enDesc">-->
            <!--                <el-input v-model.trim="moneyCrud2.form.enDesc" />-->
            <!--            </el-form-item>-->
            <el-form-item label="隐藏" prop="hidden">
                <el-radio-group v-model="moneyCrud2.form.hidden">
                    <el-radio v-for="(item, index) in [true, false]" :key="index" :value="item">
                        {{ item ? '是' : '否' }}
                    </el-radio>
                </el-radio-group>
            </el-form-item>
            <el-form-item label="排序" prop="sort">
                <el-input-number v-model.number="moneyCrud2.form.sort" :min="0" :max="999" controls-position="right" />
            </el-form-item>
        </MoneyForm>
    </PageWrapper>
</template>
<script setup>
import PageWrapper from "@/components/PageWrapper.vue";
import MoneyCrud from '@/components/crud/MoneyCrud.js';
import MoneyCrudTable from "@/components/crud/MoneyCrudTable.vue";
import MoneyRR from "@/components/crud/MoneyRR.vue";
import MoneyUD from "@/components/crud/MoneyUD.vue";
import MoneyCUD from "@/components/crud/MoneyCUD.vue";
import MoneyForm from "@/components/crud/MoneyForm.vue";

import {ref} from "vue";
import {useUserStore} from "@/store/index.js";
import dictApi from "@/api/system/dict.js";

const userStore = useUserStore()
const columns = [
    {prop: 'dictName', label: '字典名称'},
    {prop: 'dictDesc', label: '字典描述'},
    {
        prop: 'opt',
        label: '操作',
        width: 120,
        align: 'center',
        fixed: 'right',
        showOverflowTooltip: false,
        isMoneyUD: true
    }
]
const rules = {
    dictName: [{required: true, message: '请输入字典名称'}]
}
const moneyCrud = ref(new MoneyCrud({
    columns,
    crudMethod: dictApi,
    // 权限控制
    optShow: {
        checkbox: userStore.hasPermission(['dict:edit', 'dict:del']),
        add: userStore.hasPermission('dict:add'),
        edit: userStore.hasPermission('dict:edit'),
        del: userStore.hasPermission('dict:del')
    },
}))
moneyCrud.value.init(moneyCrud)

const columns2 = [
    {prop: 'value', label: '字典值'},
    {prop: 'cnDesc', label: '中文描述'},
    // TODO 多语言
    {prop: 'enDesc', label: '英文描述', show: false},
    {prop: 'hidden', label: '隐藏'},
    {prop: 'sort', label: '排序', sortable: true},
    {
        prop: 'opt',
        label: '操作',
        width: 120,
        align: 'center',
        fixed: 'right',
        showOverflowTooltip: false,
        isMoneyUD: true
    }
]
const rules2 = {
    dict: [{required: true, message: '请选择字典'}],
    value: [{required: true, message: '请输入字典值'}],
    cnDesc: [{required: true, message: '请输入中文描述'}],
}
const moneyCrud2 = ref(new MoneyCrud({
    columns: columns2,
    isPage: false,
    defaultForm: {
        sort: 999,
        hidden: false
    },
    crudMethod: {
        list: false,
        add: dictApi.addDetail,
        edit: dictApi.editDetail,
        del: dictApi.delDetail
    },
    // 权限控制
    optShow: {
        checkbox: false,
        add: userStore.hasPermission('dict:add'),
        edit: userStore.hasPermission('dict:edit'),
        del: userStore.hasPermission('dict:del')
    },
}))
moneyCrud2.value.init(moneyCrud2)

let currentDict = null
// 字典行点击查询字典详情
moneyCrud.value.currentChange = async (currentRow) => {
    if (!currentRow) return
    currentDict = currentRow
    const dict = currentRow.dictName
    const {data} = await dictApi.getDetail(dict)
    moneyCrud2.value.data = data
    moneyCrud2.value.defaultForm.dict = dict
}
moneyCrud.value.Hook.afterDoQuery = () => {
    moneyCrud2.value.data = []
    moneyCrud2.value.defaultForm.dict = null
}
moneyCrud2.value.Hook.afterDoQuery = () => {
    if (currentDict) moneyCrud.value.currentChange(currentDict)
}
</script>
<style>
.el-button-group.ml-4 {
    margin-left: 0.25rem;
}
</style>