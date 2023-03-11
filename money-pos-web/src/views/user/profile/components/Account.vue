<template>
  <el-form ref="profileForm" :rules="rules" :model="user">
    <el-form-item label="昵称" prop="nickname">
      <el-input v-model.trim="user.nickname" />
    </el-form-item>
    <el-form-item label="手机号码">
      <el-input v-model.trim="user.phone" />
    </el-form-item>
    <el-form-item label="用户邮箱">
      <el-input v-model.trim="user.email" />
    </el-form-item>
    <el-form-item label="描述">
      <el-input v-model.trim="user.remark" type="textarea" maxlength="250" show-word-limit />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="submit">更新</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import { updateProfile } from '@/api/system/user'
export default {
  props: {
    user: {
      type: Object,
      default: () => {
        return {
          nickname: '',
          phone: '',
          email: '',
          remark: ''
        }
      }
    }
  },
  data() {
    return {
      rules: {
        nickname: [{ required: true, trigger: 'blur', message: '请输入昵称' }]
      }
    }
  },
  methods: {
    submit() {
      this.$refs.profileForm.validate(valid => {
        if (valid) {
          updateProfile(this.user).then(response => {
            this.$message({
              message: '个人资料已更新',
              type: 'success',
              duration: 5 * 1000
            })
          })
        }
      })
    }
  }
}
</script>
