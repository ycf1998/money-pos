<template>
  <div>
    <el-input v-model="searchCategory" placeholder="输入关键字进行过滤" />
    <el-tree ref="tree" class="filter-tree" node-key="id" highlight-current icon-class="el-icon-collection-tag" :data="categoryTree" default-expand-all :filter-node-method="filterCategory" :expand-on-click-node="false" @node-click="selectCategory">
      <span slot-scope="{ node, data }" class="custom-tree-node">
        <span>{{ data.name }}</span>
        <span @click.stop>
          <template v-if="data.pid == -1">
            <el-popover v-model="data.visible" placement="top" width="260">
              <el-form ref="cateForm" :model="newChild" :rules="rules" label-width="80px">
                <!-- <el-form-item label="图标" prop="icon">
                  <el-upload class="avatar-uploader" action="" :auto-upload="false" :on-change="handleIconSuccess" :show-file-list="false" accept="image/*">
                    <img v-if="newChild.icon" :src="loadIcon(newChild.icon)" style="width: 50px;height: 50px">
                    <i v-else class="el-icon-plus" />
                  </el-upload>
                </el-form-item> -->
                <el-form-item label="分类名称" prop="name">
                  <el-input v-model="newChild.name" @keydown.native="keydown($event)" />
                </el-form-item>
                <div style="text-align: right; margin: 0">
                  <el-button size="mini" type="text" @click="data.visible = false">取消</el-button>
                  <el-button type="primary" size="mini" @click="saveCate(data)">确定</el-button>
                </div>
              </el-form>
              <el-button slot="reference" type="text" @click="appendNode(data)">
                <i class="el-icon-circle-plus-outline" />
              </el-button>
            </el-popover>
          </template>
          <template v-else>
            <el-popover v-model="data.visible" placement="top" width="260">
              <el-form ref="cateForm" :model="newChild" :rules="rules" label-width="80px">
                <!-- <el-form-item label="图标" prop="icon">
                  <el-upload class="avatar-uploader" action="" :auto-upload="false" :on-change="handleIconSuccess" :show-file-list="false" accept="image/*">
                    <img v-if="newChild.icon" :src="loadIcon(newChild.icon)" style="width: 50px;height: 50px">
                    <i v-else class="el-icon-plus" />
                  </el-upload>
                </el-form-item> -->
                <el-form-item label="分类名称" prop="name">
                  <el-input v-model="newChild.name" @keydown.native="keydown($event)" />
                </el-form-item>
                <div style="text-align: right; margin: 0">
                  <el-button size="mini" type="text" @click="data.visible = false">取消</el-button>
                  <el-button type="primary" size="mini" @click="updateCate(data)">确定</el-button>
                </div>
              </el-form>
              <el-button slot="reference" type="text" @click="updateNode(data)">
                <i style="color:green" class="el-icon-edit" />
              </el-button>
            </el-popover>
            <el-button type="text" @click="() => removeNode(node, data)">
              <i style="color:red" class="el-icon-remove-outline" />
            </el-button>
          </template>
        </span>
      </span>
    </el-tree>
  </div>
</template>

<script>
import crudGoodsCategory from '@/api/gms/goodsCategory'
import oss from '@/utils/oss'

export default {
  name: 'CategoryTree',
  data() {
    return {
      searchCategory: '',
      categoryTree: [],
      newChild: {
        pid: null,
        name: null,
        icon: null,
        iconFile: null
      },
      visible: false,
      rules: {
        name: [
          { required: true, message: '名称不能为空', trigger: 'change' }
        ]
      }
    }
  },
  watch: {
    searchCategory(val) {
      this.$refs.tree.filter(val)
    }
  },
  created() {
    this.loadCategoryTree()
  },
  methods: {
    // 加载分类树
    loadCategoryTree() {
      this.categoryTree = []
      crudGoodsCategory.getTree().then(res => {
        this.categoryTree.push(res.data)
      })
    },
    // 分类树搜索
    filterCategory(value, data) {
      if (!value) return true
      return data.name.indexOf(value) !== -1
    },
    // 选中分类
    selectCategory(data) {
      this.$emit('node-click', data.id)
    },
    appendNode(data) {
      this.closeForm(this.categoryTree, data)
      this.newChild = {
        pid: data.id,
        name: '',
        icon: null,
        iconFile: null
      }
    },
    updateNode(data) {
      this.closeForm(this.categoryTree, data)
      this.newChild = {
        id: data.id,
        pid: data.pid,
        name: data.name,
        icon: data.icon,
        iconFile: null
      }
    },
    closeForm(tree, data) {
      tree.forEach(t => {
        if (t.id !== data.id) {
          t.visible = false
        }
        if (t.children && t.children.length) {
          this.closeForm(t.children, data)
        }
      })
    },
    // 保存分类
    saveCate(data) {
      this.$refs.cateForm.validate((valid) => {
        if (valid) {
          crudGoodsCategory.add(this.newChild).then(res => {
            this.loadCategoryTree()
            data.visible = false
            this.$emit('node-update')
          })
        }
      })
    },
    // 修改分类
    updateCate(data) {
      this.$refs.cateForm.validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.newChild)
          crudGoodsCategory.edit(tempData).then(res => {
            this.loadCategoryTree()
            data.visible = false
            this.$emit('node-update')
          })
        }
      })
    },
    // 删除分类
    removeNode(node, data) {
      this.$confirm('是否删除该分类?', '提示', {
        cancelButtonText: '取消',
        confirmButtonText: '确定',
        type: 'warning'
      }).then(() => {
        crudGoodsCategory.del([data.id]).then(() => {
          const parent = node.parent
          const children = parent.data.children || parent.data
          const index = children.findIndex(d => d.id === data.id)
          children.splice(index, 1)
          this.$emit('node-update')
        })
      })
    },
    // 加载icon
    loadIcon(url) {
      return oss.loadImage(url, oss.TYPE.LOCAL)
    },
    // 选择icon预览
    handleIconSuccess(file) {
      this.newChild.icon = URL.createObjectURL(file.raw)
      this.newChild.iconFile = file.raw
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

<style lang="scss" scoped>
.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}
</style>
