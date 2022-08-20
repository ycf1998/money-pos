<template>
  <div class="app-container">
    <el-form ref="changePwdForm" label-width="80px" :model="pwdGroup" status-icon :rules="rules">
      <el-form-item label="旧密码" prop="oldPassword">
        <el-input v-model="pwdGroup.oldPassword" type="password" autocomplete="off" />
      </el-form-item>
      <el-form-item label="新密码" prop="newPassword">
        <el-input v-model="pwdGroup.newPassword" type="password" autocomplete="off" />
      </el-form-item>
      <el-form-item label="确认密码" prop="confirmPwd">
        <el-input v-model="pwdGroup.confirmPwd" type="password" autocomplete="off" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm('changePwdForm')">提交</el-button>
        <el-button @click="resetForm('changePwdForm')">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
import { changePassword } from '@/api/system/user'

export default {
  name: 'ChangePassword',
  data() {
    var validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入新密码'))
      } else {
        if (this.pwdGroup.confirmPwd !== '') {
          this.$refs.changePwdForm.validateField('confirmPwd')
        }
        callback()
      }
    }
    var validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入新密码'))
      } else if (value !== this.pwdGroup.newPassword) {
        callback(new Error('两次输入新密码不一致!'))
      } else {
        callback()
      }
    }
    return {
      pwdGroup: {
        oldPassword: '',
        newPassword: '',
        confirmPwd: ''
      },
      rules: {
        oldPassword: [
          { required: true, trigger: 'blur', message: '请输入旧密码' }
        ],
        newPassword: [
          { required: true, trigger: 'blur', message: '请输入新密码' },
          { validator: validatePass, trigger: 'blur' }
        ],
        confirmPwd: [
          { required: true, trigger: 'blur', message: '请再次输入新密码' },
          { validator: validatePass2, trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          changePassword(this.pwdGroup).then(response => {
            this.$message({
              message: '修改密码成功',
              type: 'success',
              duration: 5 * 1000
            })
            this.$store.dispatch('user/logout').then(() => {
              this.$router.push({ path: '/login' })
            })
          })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    resetForm(formName) {
      this.$refs[formName].resetFields()
    }
  }
}
</script>

