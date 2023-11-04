<template>
    <el-empty v-if="!moneyCrud.isInit" :="$attrs" description="加载中..." />
    <el-table :="$attrs" v-if="moneyCrud.isInit"
              class="max-h-full" show-overflow-tooltip flexible
              ref="moneyTable" :data="moneyCrud.data" :default-sort="moneyCrud.defaultSort"
              @sort-change="moneyCrud.doSort"
              @current-change="moneyCrud.currentChange"
              @selection-change="moneyCrud.selectionChange">
        <template slot="empty">
            <p>{{ moneyCrud.isInit ? '暂无数据' : '' }}</p>
        </template>
        <el-table-column v-if="moneyCrud.optShow.checkbox" :selectable="row => !moneyCrud.rowOptDisabled.checkbox(row)"
                         type="selection" width="55" />
        <el-table-column v-for="(col, index) in showColumns" :key="index"
                         :sortable="col.sortable" :show-overflow-tooltip="col.showOverflowTooltip"
                         :prop="col.prop" :label="col.label" :width="col.width" :min-width="col.minWidth"
                         :fixed="col.fixed" :align="col.align">
            <template #default="scope">
                <slot :name="col.prop" :scope="scope">
                    {{ scope.row[col.prop] }}
                </slot>
            </template>
        </el-table-column>
    </el-table>
    <!-- 分页 -->
    <el-pagination
        v-if="moneyCrud.isPage && moneyCrud.isInit"
        class="flex-wrap justify-center gap-2 md:justify-normal md:gap-0"
        :current-page="moneyCrud.page.currentPage"
        :page-size="moneyCrud.page.pageSize"
        :total="moneyCrud.page.total"
        :page-sizes="[10, 20, 50, 100]"
        layout="prev, pager, next, sizes, ->, total"
        @current-change="moneyCrud.currentPageChange"
        @size-change="moneyCrud.pageSizeChange"
    />
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
const moneyTable = ref()
const showColumns = computed(() => moneyCrud.columns.filter(e => e.show !== false))
</script>
