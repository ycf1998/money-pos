<!--搜索与重置-->
<template>
  <span>
    <el-button v-waves class="filter-item" type="success" icon="el-icon-search" @click="crud.toQuery">搜索</el-button>
    <el-button v-if="crud.optShow.reset" class="filter-item" type="warning" icon="el-icon-refresh-left" @click="crud.resetQuery()">重置</el-button>
  </span>
</template>
<script>
import { crud } from './crud'
import waves from '@/directive/waves' // waves directive

export default {
  directives: { waves },
  mixins: [crud()],
  props: {
    itemClass: {
      type: String,
      required: false,
      default: ''
    }
  }
}
</script>
