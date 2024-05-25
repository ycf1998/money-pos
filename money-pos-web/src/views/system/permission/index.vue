<template>
    <PageWrapper>
        <!-- 搜索栏 -->
        <MoneyRR :money-crud="moneyCrud">
            <el-input v-model.number="moneyCrud.query.condition" placeholder="名称/编码" class="md:!w-48"
                      @keyup.enter.native="moneyCrud.doQuery" />
            <el-select v-model="moneyCrud.query.permissionType" clearable placeholder="资源类型" class="md:!w-48">
                <el-option v-for="item in dict.permissionType" :key="item.value" :label="item.desc"
                           :value="item.value" @keyup.enter.native="moneyCrud.doQuery" />
            </el-select>
        </MoneyRR>
        <!-- 操作行 -->
        <MoneyCUD :money-crud="moneyCrud" />
        <!-- 数据表格 -->
        <MoneyCrudTable :money-crud="moneyCrud" row-key="id">
            <template #permissionType="{scope}">
                {{ dict.permissionTypeKv[scope.row.permissionType] }}
            </template>
            <template #icon="{scope}">
                <svg-icon v-if="scope.row.icon" :name="scope.row.icon" dir="open" />
            </template>
            <template #hidden="{scope}">
                <el-tag v-if="scope.row.hidden" type="info">是</el-tag>
                <el-tag v-else type="success">否</el-tag>
            </template>
            <template #opt="{scope}">
                <MoneyUD :money-crud="moneyCrud" :scope="scope" del-confirm-msg="确定删除该节点及其子节点?" />
            </template>
        </MoneyCrudTable>
        <!-- 表单 -->
        <MoneyForm :money-crud="moneyCrud" :inline="true" :rules="rulesMap[type]" :dialog-class="'!w-11/12 md:!w-5/12'">
            <el-form-item label="资源类型" prop="permissionType" class="w-full">
                <el-radio-group v-model="moneyCrud.form.permissionType" @change="changePermissionType">
                    <el-radio-button v-for="(item, index) in dict.permissionType" :key="index" :label="item.value">
                        {{ item.desc }}
                    </el-radio-button>
                </el-radio-group>
            </el-form-item>
            <el-form-item v-if="type !== 'BUTTON'" label="图标" prop="icon" class="w-full">
                <IconSelect class="!w-full" @selected="icon => moneyCrud.form.icon = icon"
                            :default-icon="moneyCrud.form.icon" />
            </el-form-item>
            <el-form-item :label="type === 'BUTTON' ? '权限名称' : '标题'" prop="permissionName" class="w-full">
                <el-input v-model="moneyCrud.form.permissionName" />
            </el-form-item>
            <el-form-item v-if="type === 'BUTTON'" label="权限标识" prop="permission" class="w-full">
                <el-input v-model="moneyCrud.form.permission" placeholder="如：user:list" />
            </el-form-item>
            <el-form-item v-if="type !== 'BUTTON'" label="路由地址" prop="routerPath" class="w-full">
                <el-input v-model="moneyCrud.form.routerPath" placeholder="开头不带 /">
                    <template #suffix>
                        <el-tooltip placement="top" content="等于上级路由地址 + 填写的地址">
                            <el-icon class="el-input__icon">
                                <Help />
                            </el-icon>
                        </el-tooltip>
                    </template>
                </el-input>
            </el-form-item>
            <div v-if="type === 'MENU'" class="flex justify-between flex-wrap md:flex-nowrap">
                <el-form-item label="组件名称" prop="componentName" class="w-full !mr-0 md:w-1/2 md:!mr-2.5">
                    <el-input v-model="moneyCrud.form.componentName" placeholder="如：User">
                        <template #suffix>
                            <el-tooltip placement="top" content="首字母大写，如：User">
                                <el-icon class="el-input__icon">
                                    <Help />
                                </el-icon>
                            </el-tooltip>
                        </template>
                    </el-input>
                </el-form-item>
                <el-form-item label="组件路径" prop="componentPath" class="w-full !mr-0 md:w-1/2">
                    <el-input v-model="moneyCrud.form.componentPath" placeholder="如：system/user/index">
                        <template #suffix>
                            <el-tooltip placement="top" content="等于 @/views/ + 填写的路径 + .vue">
                                <el-icon class="el-input__icon">
                                    <Help />
                                </el-icon>
                            </el-tooltip>
                        </template>
                    </el-input>
                </el-form-item>
            </div>
            <el-form-item label="上级类目" prop="parentId" class="w-full">
                <el-tree-select v-model="moneyCrud.form.parentId" :data="permissionsTree" class="!w-full"
                                value-key="id" :props="{ label: 'permissionName' }" check-strictly
                                :render-after-expand="false" :expand-on-click-node="false" />
            </el-form-item>
            <el-form-item label="排序" prop="sort">
                <el-input-number v-model.number="moneyCrud.form.sort" :min="0" :max="999" controls-position="right" />
            </el-form-item>
            <el-form-item v-if="type !== 'BUTTON'" label="隐藏" prop="hidden">
                <el-radio-group v-model="moneyCrud.form.hidden">
                    <el-radio-button v-for="(item, index) in [true, false]" :key="index" :label="item">
                        {{ item ? '是' : '否' }}
                    </el-radio-button>
                </el-radio-group>
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
import permissionApi from "@/api/system/permission.js";
import dictApi from "@/api/system/dict.js";
import {useUserStore} from "@/store/index.js";
import IconSelect from "@/components/IconSelect.vue";

const userStore = useUserStore()
const columns = [
    {prop: 'permissionName', label: '权限名称', width: 150},
    {prop: 'icon', label: '图标', width: 80},
    {prop: 'permission', label: '权限标识'},
    {prop: 'permissionType', label: '资源类型',},
    {prop: 'componentPath', label: '组件路径', align: "center"},
    {prop: 'hidden', label: '隐藏', align: "center"},
    {prop: 'createTime', label: '创建时间', show: false},
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
const rulesMap = {
    DIR: {
        icon: [{required: true, message: '请选择图标', trigger: 'change'}],
        permissionName: [
            {required: true, message: '请输入标题'},
            {min: 2, max: 20, message: '长度在 2 到 20 个字符'}
        ],
        routerPath: [{required: true, message: '请填写路由地址'}],
        parentId: [{required: true, message: '请选择上级目录'}]
    },
    MENU: {
        icon: [{required: true, message: '请选择图标', trigger: 'change'}],
        permissionName: [
            {required: true, message: '请输入标题'},
            {min: 2, max: 20, message: '长度在 2 到 20 个字符'}
        ],
        permission: [{required: true, message: '请填写权限标识'}],
        routerPath: [{required: true, message: '请填写路由地址'}],
        componentName: [{required: true, message: '请填写组件名称'}],
        componentPath: [{required: true, message: '请填写组件路径'}],
        parentId: [{required: true, message: '请选择上级目录'}]
    },
    BUTTON: {
        permissionName: [
            {required: true, message: '请输入权限名称'},
            {min: 2, max: 20, message: '长度在 2 到 20 个字符'}
        ],
        permission: [{required: true, message: '请填写权限标识'}],
        parentId: [{required: true, message: '请选择所属菜单'}]
    }
}
const moneyCrud = ref(new MoneyCrud({
    columns,
    isPage: false,
    crudMethod: permissionApi,
    defaultForm: {
        icon: 'app',
        permissionType: 'DIR',
        parentId: null,
        sort: 999,
        hidden: false,
    },
    optShow: {
        checkbox: userStore.hasPermission(['permission:edit', 'permission:del']),
        add: userStore.hasPermission('permission:add'),
        edit: userStore.hasPermission('permission:edit'),
        del: userStore.hasPermission('permission:del')
    },
}))
const dict = ref([])
const permissionsTree = ref([])
const type = ref('DIR')
moneyCrud.value.init(moneyCrud, async () => {
    dict.value = await dictApi.loadDict(["permissionType"])
})
moneyCrud.value.Hook.beforeToAdd = () => {
    changePermissionType('DIR')
}
moneyCrud.value.Hook.beforeToEdit = (form) => {
    changePermissionType(form.permissionType)
}

/**
 * 切换资源类型
 * @param value
 */
function changePermissionType(value) {
    type.value = value
    // 目录和菜单上级只能是目录，按钮上级只能是菜单
    const data = moneyCrud.value.data
    const disableType = ['DIR', 'MENU'].includes(value) ? ['MENU', 'BUTTON'] : ['DIR', 'BUTTON']
    permissionsTree.value = flagDisabled([{
        id: 0,
        permissionName: '顶级类目',
        permissionType: 'DIR',
        children: data
    }], disableType)
}

function flagDisabled(data, disableType) {
    if (!data || data.length < 1) return
    data.forEach(e => {
        e.disabled = disableType.includes(e.permissionType)
        flagDisabled(e.children, disableType)
    })
    return data
}
</script>