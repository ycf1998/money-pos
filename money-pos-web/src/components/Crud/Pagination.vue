<!--分页-->
<template>
  <el-pagination v-show="page.total>0" :page-size.sync="page.size" :total="page.total" :current-page.sync="page.page" style="margin-top: 8px;" layout="total, prev, pager, next, sizes" @size-change="crud.sizeChangeHandler($event)" @current-change="crud.pageChangeHandler" />
</template>
<script>
import { pagination } from './crud'
export default {
  mixins: [pagination()]
}
</script>
<style>
.el-pagination {
  display: flex;
  flex-wrap: wrap;
}
</style>
