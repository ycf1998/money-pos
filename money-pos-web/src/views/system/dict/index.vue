<template>
  <div class="app-container">
    <!-- 搜索 -->
    <div v-if="crud.props.searchToggle" class="filter-container">
      <el-input v-model="query.nameOrDesc" placeholder="字典名或者描述" class="filter-item-200" @keyup.enter.native="crud.toQuery" />
      <rr-operation />
    </div>
    <el-row :gutter="20">
      <el-col :sm="24" :md="12">
        <el-card class="box-card" shadow="never">
          <div slot="header" class="clearfix">
            <span class="role-span">字典</span>
          </div>
          <!-- CRUD操作 -->
          <crud-operation :permission="permission" />
          <!-- 字典管理 -->
          <el-table ref="table" v-loading="crud.loading" :data="crud.data" style="width: 100%;" highlight-current-row @selection-change="crud.selectionChangeHandler" @current-change="handleCurrentChange">
            <el-table-column type="selection" width="55" />
            <el-table-column :show-overflow-tooltip="true" prop="name" label="字典名" />
            <el-table-column :show-overflow-tooltip="true" prop="description" label="字典描述" />
            <el-table-column label="操作" width="115" align="center" fixed="right">
              <template slot-scope="scope">
                <ud-operation :data="scope.row" :permission="permission" />
              </template>
            </el-table-column>
          </el-table>
          <!-- 分页 -->
          <pagination />
        </el-card>
      </el-col>
      <el-col :sm="24" :md="12">
        <!-- 字典详情管理 -->
        <el-card class="box-card" shadow="never">
          <div slot="header" class="clearfix">
            <el-tooltip class="item" effect="dark" content="点击字典显示字典详情" placement="top">
              <span class="role-span">字典详情</span>
            </el-tooltip>
            <el-button v-if="showButton" v-permission="['dict:edit']" icon="el-icon-plus" size="mini" style="float: right; padding: 6px 9px" type="primary" @click="$refs.dictDetail && $refs.dictDetail.crud.toAdd()">新增</el-button>
          </div>
          <dict-detail ref="dictDetail" />
        </el-card>
      </el-col>
    </el-row>
    <!--字典表单渲染-->
    <el-dialog append-to-body :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="360px">
      <el-form ref="form" :inline="true" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="字典名" prop="name">
          <el-input v-model="form.name" :disabled="crud.status.isEdit" style="width: 220px;" @keydown.native="keydown($event)" />
        </el-form-item>
        <el-form-item label="字典描述">
          <el-input v-model.trim="form.description" type="textarea" maxlength="250" show-word-limit style="width: 220px;" />
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
import dictDetail from './dictDetail'
import dictApi from '@/api/system/dict'
import rrOperation from '@/components/Crud/RR.operation.vue'
import udOperation from '@/components/Crud/UD.operation.vue'
import crudOperation from '@/components/Crud/CRUD.operation.vue'
import Pagination from '@/components/Crud/Pagination.vue'
import CRUD, { presenter, header, form, crud } from '@/components/Crud/crud'

export default {
  name: 'Dict',
  components: { Pagination, rrOperation, udOperation, crudOperation, dictDetail },
  cruds() {
    return CRUD({ title: '字典', url: '/dict', crudMethod: { ...dictApi } })
  },
  mixins: [
    presenter(),
    header(),
    form({
      // 表单初始值
      id: null,
      name: null,
      description: null
    }),
    crud()
  ],
  data() {
    return {
      // 操作权限定义
      permission: {
        add: ['dict:add'],
        edit: ['dict:edit'],
        del: ['dict:del']
      },
      // 显示字典详情新增按钮
      showButton: false,
      // 表单验证规则
      rules: {
        name: [{ required: true, message: '请输入字典名', trigger: 'blur' }]
      }
    }
  },
  methods: {
    // 新增与编辑前做的操作
    [CRUD.HOOK.afterDelete](crud, form) {
      this.showButton = false
      this.$refs.dictDetail.crud.data = []
    },
    // 选中字典
    handleCurrentChange(val) {
      if (val) {
        this.showButton = true
        this.$refs.dictDetail.query.dict = val.name
        this.$refs.dictDetail.crud.toQuery()
      }
    },
    // 禁止输入空格
    keydown(e) {
      if (e.keyCode === 32) {
        e.returnValue = false
      }
    }
  }
}
</script>

<style>
</style>
