<template>
  <div class="app-container">
    <!-- 搜索栏 -->
    <div v-if="crud.props.searchToggle" class="filter-container">
      <!-- 【搜索条件】prop前缀都要加query. -->
      <el-input v-model="query.name" placeholder="名称" class="filter-item-200" @keyup.enter.native="crud.toQuery" />
      <el-select v-model="query.demoStatus" clearable placeholder="状态" class="filter-item-200" @change="crud.toQuery">
        <el-option v-for="item in dict.demoStatus" :key="item.value" :label="item.label" :value="item.value" />
      </el-select>
      <rr-operation />
    </div>
    <!-- 操作栏 -->
    <crud-operation :permission="permission" :hidden-columns="[]" />
    <!-- 数据表格 -->
    <el-table ref="table" v-loading="crud.loading" :data="crud.data" style="width: 100%;" @selection-change="crud.selectionChangeHandler" @sort-change="crud.sortChangeHandler">
      <!-- 复选框-->
      <el-table-column type="selection" width="55" />

      <el-table-column prop="name" label="名称" />
      <el-table-column prop="status" label="状态">
        <template slot-scope="scope">
          {{ dict.label.demoStatus[scope.row.status] }}
        </template>
      </el-table-column>
      <el-table-column sortable prop="createTime" label="创建时间" />
      <el-table-column sortable prop="updateTime" label="修改时间" />

      <el-table-column label="操作" width="115" align="center" fixed="right">
        <template slot-scope="scope">
          <ud-operation :data="scope.row" :permission="permission" />
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页栏 -->
    <pagination />
    <!-- 新增/修改表单 -->
    <el-dialog append-to-body :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="350px">
      <el-form ref="form" :inline="true" :model="form" :rules="rules" size="small" label-width="80px">

        <!-- 【表单控件】绑定的字段前缀都要加form. -->
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" style="width: 220px;" @keydown.native="keydown($event)" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" style="width: 220px;">
            <el-option v-for="item in dict.demoStatus" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="text" @click="crud.cancelCU">取消</el-button>
        <el-button :loading="crud.status.cu === 2" type="primary" @click="crud.submitCU">确认</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import rrOperation from '@/components/Crud/RR.operation.vue'
import udOperation from '@/components/Crud/UD.operation.vue'
import crudOperation from '@/components/Crud/CRUD.operation.vue'
import Pagination from '@/components/Crud/Pagination.vue'
import CRUD, { presenter, header, form, crud } from '@/components/Crud/crud'

import demoApi from '@/api/demo'

export default {
  name: 'Demo',
  components: { Pagination, rrOperation, udOperation, crudOperation },
  cruds() {
    // 【url是查询接口】
    return CRUD({ title: 'Demo', url: '/demo', crudMethod: { ...demoApi } })
  },
  mixins: [presenter(), header(), form({
    // 【表单初始值】
    id: null,
    name: null,
    status: null
  }), crud()],
  // 字典
  dicts: ['demoStatus'],
  data() {
    return {
      // 【操作权限定义】与按钮权限标识相同
      permission: {
        add: ['demo:add'],
        edit: ['demo:edit'],
        del: ['demo:del']
      },
      rules: {
      }
    }
  },
  methods: {
    // 禁止输入空格
    keydown(e) {
      if (e.keyCode === 32) {
        e.returnValue = false
      }
    }
  }
}
</script>
