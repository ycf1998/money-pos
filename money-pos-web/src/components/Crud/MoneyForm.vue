<template>
    <el-dialog v-model="visible" :title="getTitle()" draggable :class="dialogClass" destroy-on-close>
        <el-form
            :="$attrs"
            ref="moneyForm"
            :model="moneyCrud.form"
            label-width="auto"
            status-icon
            scroll-into-view-options
        >
            <slot />
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="visible = false">取消</el-button>
            <el-button type="primary" :loading="loading" @click="confirm">确认</el-button>
          </span>
        </template>
    </el-dialog>
</template>
<script setup>
import MoneyCrud from "@/components/crud/MoneyCrud.js";
import {computed, ref} from 'vue'

const {moneyCrud} = defineProps({
    moneyCrud: {
        required: true,
        type: MoneyCrud
    },
    dialogClass: {
        default: '!w-11/12 md:!w-1/2 lg:!w-1/3'
    }
})

const moneyForm = ref()
const loading = ref(false)
const visible = computed({
    get() {
        return moneyCrud.state !== moneyCrud.STATE.NONE
    },
    set() {
        moneyCrud.state = moneyCrud.STATE.NONE
    }
})

function getTitle() {
    if (moneyCrud.state === moneyCrud.STATE.ADD) return '新增'
    if (moneyCrud.state === moneyCrud.STATE.EDIT) return '编辑'
}

async function confirm() {
    await moneyForm.value.validate(async (valid) => {
        if (!valid) return
        loading.value = true
        const exec = moneyCrud.state === moneyCrud.STATE.ADD ? moneyCrud.doAdd : moneyCrud.doEdit
        exec().then(() => {
            loading.value = false
            visible.value = false
        }).catch(() => loading.value = false)
    })
}
</script>