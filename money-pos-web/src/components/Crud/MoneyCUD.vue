<template>
    <div class="flex justify-between">
        <div>
            <el-button plain type="primary" v-if="moneyCrud.optShow.add" @click="moneyCrud.toAdd">新增</el-button>
            <el-button plain type="warning" :disabled="moneyCrud.selections.length !== 1"
                       v-if="moneyCrud.optShow.edit" @click="moneyCrud.toEdit(moneyCrud.selections[0])">修改
            </el-button>
            <el-button plain type="danger" :disabled="moneyCrud.selections.length < 1"
                       v-if="moneyCrud.optShow.del" @click="confirm">删除
            </el-button>
            <slot />
        </div>
        <el-button-group class="ml-4">
            <el-button plain class="p-3" @click="toggleMoneyRR">
                <el-icon>
                    <Search />
                </el-icon>
            </el-button>
            <el-button plain class="p-3" @click="refresh">
                <el-icon>
                    <Refresh />
                </el-icon>
            </el-button>
            <el-popover trigger="click" placement="bottom-start">
                <el-checkbox label="全选" v-model="checkAll" :indeterminate="isIndeterminate" @change="handleCheckAllChange" />
                <el-checkbox-group v-model="checked" @change="handleCheckedChange">
                    <el-checkbox class="w-full" v-for="(col, index) in moneyCrud.columns" :key="index"
                                 :label="col.label" :value="col.prop" />
                </el-checkbox-group>
                <template #reference>
                    <el-button plain class="p-3">
                        <el-icon>
                            <Operation />
                        </el-icon>
                    </el-button>
                </template>
            </el-popover>
        </el-button-group>
    </div>
</template>

<script setup>
import MoneyCrud from "@/components/crud/MoneyCrud.js";
import {computed, ref} from 'vue'

const {moneyCrud} = defineProps({
    moneyCrud: {
        required: true,
        type: MoneyCrud
    }
})

const confirm = () => {
    moneyCrud.$confirm(
        `确认删除选中的 ${moneyCrud.selections.length} 数据?`,
        '提示',
        {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
        }
    ).then(() => {
        moneyCrud.doDel(moneyCrud.selections)
    })
}

const checkAll = ref(false)
const isIndeterminate = ref(false)
const checkColumn = ref(getChecked())
const checked = computed({
    get() {
        return checkColumn.value
    },
    set(newValue) {
        checkColumn.value = newValue
    }
})
handleCheckedChange(checkColumn.value)

function toggleMoneyRR() {
    moneyCrud.optShow.moneyRR = !moneyCrud.optShow.moneyRR
}

function refresh() {
    moneyCrud.doQuery()
}

function getChecked() {
    return moneyCrud.columns.filter(e => e.show !== false).map(e => e.prop)
}

function handleCheckAllChange(val) {
    moneyCrud.columns.forEach(e => e.show = val)
    checkColumn.value = getChecked()

    isIndeterminate.value = false
}

function handleCheckedChange(value) {
    moneyCrud.columns.forEach(e => e.show = value.includes(e.prop))
    checkColumn.value = getChecked()

    const checkedCount = value.length
    checkAll.value = checkedCount === moneyCrud.columns.length
    isIndeterminate.value = checkedCount > 0 && checkedCount < moneyCrud.columns.length
}
</script>