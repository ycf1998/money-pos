<template>
    <PageWrapper>
        <!-- 搜索栏 -->
        <MoneyRR :money-crud="moneyCrud">
            <el-input v-model="moneyCrud.query.name" placeholder="用户名/昵称" class="md:!w-48"
                      @keyup.enter.native="moneyCrud.doQuery" />
            <el-input v-model.number="moneyCrud.query.phone" placeholder="手机号" class="md:!w-48"
                      @keyup.enter.native="moneyCrud.doQuery" />
            <el-select v-model="moneyCrud.query.enabled" clearable placeholder="状态" class="md:!w-48">
                <el-option v-for="item in [true, false]" :key="item" :label="item ? '启用':'禁用'" :value="item" />
            </el-select>
        </MoneyRR>
        <!-- 操作行 -->
        <MoneyCUD :money-crud="moneyCrud" />
        <!-- 数据表格 -->
        <MoneyCrudTable :money-crud="moneyCrud">
            <template #roles="{scope}">
                <el-tag type="warning">{{ scope.row.roles[0].roleName }}</el-tag>
            </template>
            <template #enabled="{scope}">
                <el-switch v-model="scope.row.enabled" :disabled="moneyCrud.rowOptDisabled.checkbox(scope.row)" @change="changeEnabled(scope.row)" />
            </template>
            <template #opt="{scope}">
                <MoneyUD :money-crud="moneyCrud" :scope="scope" />
            </template>
        </MoneyCrudTable>
        <!-- 表单 -->
        <MoneyForm :money-crud="moneyCrud" :rules="rules">
            <el-form-item label="用户名" prop="username">
                <el-input v-model.trim="moneyCrud.form.username" :disabled="moneyCrud.state === moneyCrud.STATE.EDIT" />
            </el-form-item>
            <el-form-item label="昵称" prop="nickname">
                <el-input v-model.trim="moneyCrud.form.nickname" />
            </el-form-item>
            <el-form-item label="手机号" prop="phone">
                <el-input v-model.number="moneyCrud.form.phone" />
            </el-form-item>
            <el-form-item label="邮箱" prop="email">
                <el-input v-model.trim="moneyCrud.form.email" />
            </el-form-item>
            <el-form-item label="状态">
                <el-radio-group v-model="moneyCrud.form.enabled">
                    <el-radio v-for="(item, index) in [true, false]" :key="index" :label="item">
                        {{ item ? '启用' : '禁用' }}
                    </el-radio>
                </el-radio-group>
            </el-form-item>
            <el-form-item label="角色" prop="roles">
                <el-select v-model="moneyCrud.form.roles" class="w-full" multiple placeholder="请选择">
                    <el-option v-for="item in roles" :key="item" :label="item.roleName" :value="item.id" />
                </el-select>
            </el-form-item>
            <el-form-item label="描述">
                <el-input v-model.trim="moneyCrud.form.remark" type="textarea" maxlength="250" show-word-limit />
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
import {useGlobalProp} from "@/composables/globalProp.js";
import userApi from "@/api/system/user.js";
import roleApi from "@/api/system/role.js";

const globalProp = useGlobalProp()
const userStore = useUserStore()
const columns = [
    {prop: 'username', label: '用户名'},
    {prop: 'nickname', label: '昵称'},
    {prop: 'phone', label: '手机号'},
    {prop: 'email', label: '邮箱', width: 150},
    {prop: 'roles', label: '最高角色', align: 'center'},
    {prop: 'enabled', label: '状态', align: 'center'},
    {prop: 'createTime', label: '创建时间', width: 180, show: false},
    {prop: 'updateTime', label: '修改时间', width: 180, show: false},
    {prop: 'lastTime', label: '最近登录时间', width: 180, sortable: 'custom'},
    {prop: 'remark', label: '备注', show: false},
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
    username: [
        {required: true, message: '请输入用户名'},
        {min: 2, max: 20, message: '长度在 2 到 20 个字符'}
    ],
    nickname: [
        {required: true, message: '请输入用户昵称'},
        {min: 2, max: 20, message: '长度在 2 到 20 个字符'}
    ],
    email: [{type: 'email', message: '请输入正确的邮箱地址'}],
    phone: [
        {required: true, message: '请输入手机号'},
        {pattern: /^1([38][0-9]|4[014-9]|[59][0-35-9]|6[2567]|7[0-8])\d{8}$/, message: '格式错误'}
    ]
}
const moneyCrud = ref(new MoneyCrud({
    columns,
    crudMethod: userApi,
    defaultForm: {
        enabled: true
    },
    optShow: {
        checkbox: userStore.hasPermission(['user:edit', 'user:del']),
        add: userStore.hasPermission('user:add'),
        edit: userStore.hasPermission('user:edit'),
        del: userStore.hasPermission('user:del')
    },
    rowOptDisabled: {
        checkbox: (row) => row.roles[0].level <= userStore.level,
        edit: (row) => row.roles[0].level <= userStore.level,
        del: (row) => row.roles[0].level <= userStore.level
    }
}))
const roles = ref([])
moneyCrud.value.init(moneyCrud, async () => {
    roles.value = await roleApi.getAll().then(res => res.data)
})
moneyCrud.value.Hook.beforeToEdit = (form) => {
    form.roles = form.roles.map(e => e.id)
}

/**
 * 修改状态
 * @param row
 */
function changeEnabled(row) {
    const {id, nickname, enabled} = row
    globalProp.$confirm(
        `确认${enabled ? '启用' : '禁用'}用户【${nickname}】?`,
        '提示',
        {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
        }
    ).then(() => {
        userApi.edit({id, enabled})
            .then(() => moneyCrud.value.messageOk())
            .catch(() => row.enabled = !enabled)
    }).catch(() => row.enabled = !enabled)
}
</script>
