<template>
  <div class="app-container">
    <!-- 搜索 -->
    <div v-if="crud.props.searchToggle" class="filter-container">
      <el-input v-model="query.name" placeholder="名称" style="width: 200px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
      <rr-operation />
    </div>
    <!-- CRUD操作 -->
    <crud-operation :permission="permission" />
    <!-- 品牌列表 -->
    <el-table
      ref="table"
      v-loading="crud.loading"
      :data="crud.data"
      style="width: 100%;"
      @selection-change="crud.selectionChangeHandler"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column :show-overflow-tooltip="true" prop="name" label="名称" />
      <el-table-column :show-overflow-tooltip="true" prop="logo" label="logo">
        <template slot-scope="scope">
          <el-image
            style="width: 25px; height: 25px"
            :src="loadLogo(scope.row.logo)"
            :preview-src-list="[loadLogo(scope.row.logo)]"
          />
        </template>
      </el-table-column>
      <el-table-column :show-overflow-tooltip="true" prop="description" label="描述" />
      <el-table-column :show-overflow-tooltip="true" prop="goodsCount" label="商品数量" />
      <el-table-column
        label="操作"
        width="115"
        align="center"
        fixed="right"
      >
        <template slot-scope="scope">
          <ud-operation :data="scope.row" :permission="permission" />
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <pagination />
    <!--表单渲染-->
    <el-dialog append-to-body :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="400px">
      <el-form ref="form" :inline="true" :model="form" :rules="rules" size="small" label-width="80px">
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" @keydown.native="keydown($event)" />
        </el-form-item>
        <el-form-item label="logo" prop="logo">
          <el-upload
            class="avatar-uploader"
            action=""
            :auto-upload="false"
            :on-change="handleLogoSuccess"
            :show-file-list="false"
            accept="image/*"
          >
            <img v-if="form.logo" :src="loadLogo(form.logo)" style="width: 50px; height: 50px">
            <i v-else class="el-icon-plus" />
          </el-upload>
        </el-form-item>
        <el-form-item label="描述">
          <el-input
            v-model.trim="form.description"
            style="width: 250px"
            type="textarea"
            maxlength="250"
            show-word-limit
          />
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
import oss from '@/utils/oss'
import crudBrand from '@/api/gms/brand'
import rrOperation from '@/components/Crud/RR.operation.vue'
import udOperation from '@/components/Crud/UD.operation.vue'
import crudOperation from '@/components/Crud/CRUD.operation.vue'
import Pagination from '@/components/Crud/Pagination.vue'
import CRUD, { presenter, header, form, crud } from '@/components/Crud/crud'

export default {
  name: 'Brand',
  components: { Pagination, rrOperation, udOperation, crudOperation },
  cruds() {
    return CRUD({ title: '品牌', url: '/brands', crudMethod: { ...crudBrand }})
  },
  mixins: [presenter(), header(), form({
    // 表单初始值
    id: null,
    name: null,
    logo: null,
    description: null,
    logoFile: null
  }), crud()],
  data() {
    return {
      // 操作权限定义
      permission: {
        add: ['brand:add'],
        edit: ['brand:edit'],
        del: ['brand:del']
      },
      logoUrl: '',
      // 表单验证规则
      rules: {
        name: [
          { required: true, message: '请输入品牌名称', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    // 加载logo
    loadLogo(url) {
      return oss.loadImage(url, oss.TYPE.LOCAL)
    },
    // 选择logo预览
    handleLogoSuccess(file) {
      this.form.logo = URL.createObjectURL(file.raw)
      this.form.logoFile = file.raw
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
