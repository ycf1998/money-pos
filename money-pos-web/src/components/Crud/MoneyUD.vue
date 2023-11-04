<template>
    <el-button plain type="warning" size="small" v-if="moneyCrud.optShow.edit"
               :disabled="scope.$index === -1 || moneyCrud.rowOptDisabled.edit(scope.row)"
               @click="moneyCrud.toEdit(scope.row)">
        <el-icon>
            <Edit />
        </el-icon>
    </el-button>
    <el-popconfirm :title="delConfirmMsg" width="200" @confirm="moneyCrud.doDel([scope.row])">
        <template #reference>
            <el-button plain type="danger" size="small" v-if="moneyCrud.optShow.del"
                       :disabled="scope.$index === -1 || moneyCrud.rowOptDisabled.del(scope.row)">
                <el-icon>
                    <Delete />
                </el-icon>
            </el-button>
        </template>
    </el-popconfirm>
</template>

<script setup>
import MoneyCrud from "@/components/crud/MoneyCrud.js";

defineProps({
    moneyCrud: {
        required: true,
        type: MoneyCrud
    },
    scope: {
        required: true
    },
    delConfirmMsg: {
        type: String,
        default: '确定删除本条数据吗？'
    }
})
</script>