<template>
    <PageWrapper>
        <!-- 搜索栏 -->
        <MoneyRR :money-crud="moneyCrud">
            <el-input v-model="moneyCrud.query.roleCode" placeholder="角色编码" class="md:!w-48"
                      @keyup.enter.native="moneyCrud.doQuery" />
            <el-input v-model.number="moneyCrud.query.name" placeholder="角色名称/描述" class="md:!w-48"
                      @keyup.enter.native="moneyCrud.doQuery" />
            <el-select v-model="moneyCrud.query.enabled" clearable placeholder="状态" class="md:!w-48">
                <el-option v-for="item in [true, false]" :key="item" :label="item ? '启用':'禁用'" :value="item" />
            </el-select>
        </MoneyRR>
        <!-- 操作行 -->
        <MoneyCUD :money-crud="moneyCrud" />
        <!-- 数据表格 -->
        <MoneyCrudTable :money-crud="moneyCrud">
            <template #enabled="{scope}">
                <el-switch v-model="scope.row.enabled" :disabled="moneyCrud.rowOptDisabled.checkbox(scope.row)" @change="changeEnabled(scope.row)" />
            </template>
            <template #opt="{scope}">
                <el-button plain type="primary" size="small" @click="toConfigPermission(scope.row)"
                           :disabled="scope.row.level <= userStore.level">
                    <el-icon>
                        <Setting />
                    </el-icon>
                </el-button>
                <MoneyUD :money-crud="moneyCrud" :scope="scope" />
            </template>
        </MoneyCrudTable>
        <!-- 表单 -->
        <MoneyForm :money-crud="moneyCrud" :rules="rules">
            <el-form-item label="角色编码" prop="roleCode">
                <el-input v-model.trim="moneyCrud.form.roleCode" :disabled="moneyCrud.state === moneyCrud.STATE.EDIT" />
            </el-form-item>
            <el-form-item label="角色名称" prop="roleName">
                <el-input v-model.trim="moneyCrud.form.roleName" />
            </el-form-item>
            <el-form-item label="角色级别" prop="level">
                <el-input-number v-model.number="moneyCrud.form.level" :max="99" />
            </el-form-item>
            <el-form-item label="状态">
                <el-radio-group v-model="moneyCrud.form.enabled">
                    <el-radio v-for="(item, index) in [true, false]" :key="index" :label="item">
                        {{ item ? '启用' : '禁用' }}
                    </el-radio>
                </el-radio-group>
            </el-form-item>
            <el-form-item label="角色描述">
                <el-input v-model.trim="moneyCrud.form.description" type="textarea" maxlength="250"
                          show-word-limit />
            </el-form-item>
        </MoneyForm>
    </PageWrapper>
    <!-- 权限配置对话框 -->
    <el-dialog v-model="configDialogVisible" title="配置权限" draggable class="!w-11/12 md:!w-1/2 lg:!w-1/3"
               @open="openDialog" destroy-on-close>
        <el-tree ref="permissionTree" :props="{ label: 'permissionName' }" :data="permissions"
                 node-key="id" show-checkbox :render-after-expand="false" />
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="configDialogVisible = false">取消</el-button>
            <el-button type="primary" :loading="loading" @click="confirmConfig">确认</el-button>
          </span>
        </template>
    </el-dialog>
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
import roleApi from "@/api/system/role.js";
import permissionApi from "@/api/system/permission.js";
import {useGlobalProp} from "@/composables/globalProp.js";

const globalProp = useGlobalProp()
const userStore = useUserStore()
const columns = [
    {prop: 'roleCode', label: '角色编码'},
    {prop: 'roleName', label: '角色名称'},
    {prop: 'level', label: '角色级别'},
    {prop: 'description', label: '描述',},
    {prop: 'enabled', label: '状态', align: 'center'},
    {prop: 'count', label: '角色人数', align: "center"},
    {
        prop: 'opt',
        label: '操作',
        width: 180,
        align: 'center',
        fixed: 'right',
        showOverflowTooltip: false,
        isMoneyUD: true
    }
]
const rules = {
    roleCode: [
        {required: true, message: '请输入角色编码'},
        {min: 2, max: 20, message: '长度在 2 到 20 个字符'}
    ],
    roleName: [
        {required: true, message: '请输入角色名称'},
        {min: 2, max: 20, message: '长度在 2 到 20 个字符'}
    ]
}
const moneyCrud = ref(new MoneyCrud({
    columns,
    crudMethod: roleApi,
    defaultForm: {
        enabled: true,
        level: 1
    },
    optShow: {
        checkbox: userStore.hasPermission(['role:edit', 'role:del']),
        add: userStore.hasPermission('role:add'),
        edit: userStore.hasPermission('role:edit'),
        del: userStore.hasPermission('role:del')
    },
    rowOptDisabled: {
        checkbox: (row) => row.level <= userStore.level,
        edit: (row) => row.level <= userStore.level,
        del: (row) => row.level <= userStore.level
    }
}))
const configDialogVisible = ref(false)
const loading = ref(false)
const selectedRole = ref({})
const permissionTree = ref()
const permissions = ref([])
moneyCrud.value.init(moneyCrud, async () => {
    permissions.value = await permissionApi.list().then(res => res.data)
})

/**
 * 修改状态
 * @param row
 */
function changeEnabled(row) {
    const {id, roleName, enabled} = row
    globalProp.$confirm(
        `确认${enabled ? '启用' : '禁用'}角色【${roleName}】?`,
        '提示',
        {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
        }
    ).then(() => {
        roleApi.edit({id, enabled})
            .then(() => moneyCrud.value.messageOk())
            .catch(() => row.enabled = !enabled)
    }).catch(() => row.enabled = !enabled)
}

/**
 * 打开配置权限框
 * @param row
 */
function toConfigPermission(row) {
    selectedRole.value = row
    configDialogVisible.value = true
}

/**
 * 打开对话框前
 */
function openDialog() {
    selectedRole.value.permissions.forEach(e => permissionTree.value.setChecked(e.id, true, false))
}

/**
 * 提交权限配置
 */
function confirmConfig() {
    const checkedKeys = permissionTree.value.getCheckedKeys()
    loading.value = true
    roleApi.configurePermissions(selectedRole.value.id, checkedKeys)
        .then(() => {
            moneyCrud.value.messageOk()
            loading.value = false
            configDialogVisible.value = false
        })
        .catch(() => loading.value = false)
}
</script>
