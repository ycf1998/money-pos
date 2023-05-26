<template>
  <div>
    <div v-if="query.dict === ''">
      <div class="my-code">点击字典查看详情</div>
    </div>
    <div v-else>
      <!--字典详情管理-->
      <el-table ref="table" v-loading="crud.loading" :data="crud.data" highlight-current-row style="width: 100%;" @selection-change="crud.selectionChangeHandler">
        <el-table-column label="所属字典">
          {{ query.dict }}
        </el-table-column>
        <el-table-column prop="label" label="字典标签" />
        <el-table-column prop="value" label="字典值" />
        <el-table-column prop="sort" label="排序" />
        <el-table-column label="操作" width="130px" align="center" fixed="right">
          <template slot-scope="scope">
            <udOperation :data="scope.row" :permission="permission" />
          </template>
        </el-table-column>
      </el-table>
      <!--字典详情表单渲染-->
      <el-dialog append-to-body :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="360px">
        <el-form ref="form" :inline="true" :model="form" :rules="rules" label-width="80px">
          <el-form-item label="字典名" prop="dict">
            <el-input v-model="form.dict" disabled style="width: 220px;" @keydown.native="keydown($event)" />
          </el-form-item>
          <el-form-item label="字典标签" prop="label">
            <el-input v-model="form.label" style="width: 220px;" @keydown.native="keydown($event)" />
          </el-form-item>
          <el-form-item label="字典值" prop="value">
            <el-input v-model="form.value" style="width: 220px;" @keydown.native="keydown($event)" />
          </el-form-item>
          <el-form-item label="排序" prop="sort">
            <el-input-number v-model.number="form.sort" :min="0" :max="999" controls-position="right" style="width: 220px;" />
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="text" @click="crud.cancelCU">取消</el-button>
          <el-button :loading="crud.status.cu === 2" type="primary" @click="crud.submitCU">确认</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import dictDetailApi from '@/api/system/dictDetail'
import udOperation from '@/components/Crud/UD.operation.vue'
import CRUD, { presenter, header, form, crud } from '@/components/Crud/crud'

export default {
  name: 'DictDetail',
  components: { udOperation },
  cruds() {
    return CRUD({ title: '字典详情', url: '/dict/detail', crudMethod: { ...dictDetailApi }, queryOnPresenterCreated: false, isPage: false })
  },
  mixins: [
    presenter(),
    header(),
    form({
      // 表单初始值
      id: null,
      dict: null,
      label: null,
      value: null,
      sort: 999
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
      // 表单验证规则
      rules: {
        dict: [{ required: true, message: '请输入字典名', trigger: 'blur' }],
        label: [{ required: true, message: '请输入字典标签', trigger: 'blur' }],
        value: [{ required: true, message: '请输入字典值', trigger: 'blur' }],
        sort: [{ required: true, message: '请输入排序', trigger: 'blur' }]
      }
    }
  },
  methods: {
    // 新增修改前做的操作
    [CRUD.HOOK.beforeToCU](crud, form) {
      this.form.dict = this.query.dict
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
