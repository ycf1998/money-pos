<template>
  <div class="app-container">
    <!-- 搜索 -->
    <div v-if="crud.props.searchToggle" class="filter-container">
      <!-- 【搜索条件】prop前缀都要加query. -->
      <el-input v-model="query.name" placeholder="名称" style="width: 200px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
      <rr-operation />
    </div>
    <!-- CRUD操作 -->
    <crud-operation :permission="permission" :hidden-columns="[]" />
    <!-- 列表 -->
    <el-table ref="table" v-loading="crud.loading" :data="crud.data" style="width: 100%;" @selection-change="crud.selectionChangeHandler" @sort-change="crud.sortChangeHandler">
      <el-table-column type="selection" width="55" />
      <!-- 【列控件】增加sortable支持排序 -->
      <el-table-column :show-overflow-tooltip="true" prop="query.name" label="名称" />
      <el-table-column label="操作" width="115" align="center" fixed="right">
        <template slot-scope="scope">
          <ud-operation :data="scope.row" :permission="permission" />
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <pagination />
    <!--表单-->
    <el-dialog append-to-body :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="600px">
      <el-form ref="form" :inline="true" :model="form" :rules="rules" size="small" label-width="80px">
        <!-- 【表单控件】绑定的字段前缀都要加form. -->
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" />
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

// 【引入对应的api文件】基础crud, 方法名必须为 add,edit,del
import crudTemplate from '@/api/template'

export default {
  name: 'Template',
  components: { Pagination, rrOperation, udOperation, crudOperation },
  cruds() {
    // 【url是查询接口】
    return CRUD({ title: '模板', url: '/template', crudMethod: { ...crudTemplate } })
  },
  mixins: [presenter(), header(), form({
    // 【表单初始值】
    id: null,
    name: null
  }), crud()],
  data() {
    return {
      // 【操作权限定义】
      permission: {
        add: ['template:add'],
        edit: ['template:edit'],
        del: ['template:del']
      },
      // 表单验证规则
      rules: {
        name: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    // 修改成功提示信息（不修改就是下面这三个）
    this.crud.msg.add = '新增成功'
    this.crud.msg.edit = '修改成功'
    this.crud.msg.del = '删除成功'
  },
  methods: {
    // 新增与编辑前做的操作，还有很多HOOK
    [CRUD.HOOK.afterToCU](crud, form) {

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

