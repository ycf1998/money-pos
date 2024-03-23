<template>
    <el-input v-model="filterText" placeholder="输入关键字过滤" class="w-full mb-4" />

    <el-tree
        ref="treeRef"
        :data="[moneyCrud.data]"
        :props="{label: 'name'}"
        default-expand-all
        :expand-on-click-node="false"
        :filter-node-method="filterNode"
        @node-click="(data) => $emit('nodeClick', data)"
    >
        <template #default="{ node, data }">
            <div class="flex justify-between w-full">
                <el-text class="w-4/5" truncated>{{ node.label }}</el-text>
                <el-space class="ml-1" :size="0.5">
                    <el-icon v-if="data.pid === -1" @click="moneyCrud.toAdd">
                        <CirclePlus />
                    </el-icon>
                    <el-icon v-if="data.pid !== -1" @click="moneyCrud.toEdit(data)">
                        <EditPen />
                    </el-icon>
                    <el-popconfirm title="确定删除该分类？" width="200"
                                   @confirm="moneyCrud.doDel([data])">
                        <template #reference>
                            <el-icon class="ml-1">
                                <Remove v-if="data.pid !== -1" />
                            </el-icon>
                        </template>
                    </el-popconfirm>
                </el-space>
            </div>
        </template>
    </el-tree>

    <MoneyForm :money-crud="moneyCrud" :rules="rules">
        <el-form-item label="分类名称" prop="name">
            <el-input v-model.trim="moneyCrud.form.name" />
        </el-form-item>
    </MoneyForm>
</template>

<script setup>
import {ref, watch} from 'vue'
import MoneyForm from "@/components/crud/MoneyForm.vue";
import MoneyCrud from "@/components/crud/MoneyCrud.js";

import {useUserStore} from "@/store/index.js";
import goodsCategoryApi from "@/api/gms/goodsCategory.js";

defineEmits(['nodeClick'])

const userStore = useUserStore()
const filterText = ref('')
const treeRef = ref()
const rules = {
    name: [{required: true, message: '请输入分类名称'}]
}
const moneyCrud = ref(new MoneyCrud({
    isPage: false,
    crudMethod: {
        list: goodsCategoryApi.getTree,
        add: goodsCategoryApi.add,
        edit: goodsCategoryApi.edit,
        del: goodsCategoryApi.del
    },
    optShow: {
        add: userStore.hasPermission('gmsGoods:add'),
        edit: userStore.hasPermission('gmsGoods:edit'),
        del: userStore.hasPermission('gmsGoods:del')
    },
    defaultForm: {
        pid: 0
    }
}))
moneyCrud.value.init(moneyCrud)

watch(filterText, (val) => {
    treeRef.value.filter(val)
})

function filterNode(value, data) {
    if (!value) return true
    return data.name.includes(value)
}
</script>