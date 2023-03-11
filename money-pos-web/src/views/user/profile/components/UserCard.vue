<template>
  <el-card style="margin-bottom:20px;">
    <div slot="header" class="clearfix">
      <span>关于我</span>
    </div>

    <div class="user-profile">
      <div class="box-center">
        <pan-thumb :image="loadAvatar(user.avatar) + ''" :height="'100px'" :width="'100px'" :hoverable="false">
          <div>Hello</div>
          {{ user.nickname }}
        </pan-thumb>
      </div>
      <div class="box-center">
        <div class="user-name text-center">{{ user.nickname }}
          <el-upload style="display: inline" :action="uploadAvatar()" :headers="uploadHeader()" :on-success="handleLogoSuccess" :show-file-list="false" accept="image/*">
            <el-link type="primary" icon="el-icon-picture-outline" />
          </el-upload>
        </div>
        <div class="user-role text-center text-muted">{{ user.role }}</div>
      </div>
    </div>

    <div class="user-bio">
      <div class="user-skills user-bio-section">
        <div class="user-bio-section-header"><svg-icon icon-class="skill" /><span>个人信息</span></div>
        <div class="user-bio-section-body">
          <el-descriptions :column="1">
            <el-descriptions-item label="登录账号">{{ user.username }}</el-descriptions-item>
            <el-descriptions-item label="手机号码">{{ user.phone }}</el-descriptions-item>
            <el-descriptions-item label="用户邮箱">{{ user.email }}</el-descriptions-item>
            <el-descriptions-item label="创建时间">{{ user.createTime }}</el-descriptions-item>
          </el-descriptions>
        </div>
      </div>

      <div class="user-education user-bio-section">
        <div class="user-bio-section-header"><svg-icon icon-class="education" /><span>个人简介</span></div>
        <div class="user-bio-section-body">
          <div class="text-muted">
            {{ user.remark }}
          </div>
        </div>
      </div>
    </div>
  </el-card>
</template>

<script>
import PanThumb from '@/components/PanThumb'
import oss from '@/utils/oss'

export default {
  components: { PanThumb },
  props: {
    user: {
      type: Object,
      default: () => {
        return {
          name: '',
          email: '',
          avatar: '',
          role: '',
          remark: ''
        }
      }
    }
  },
  methods: {
    uploadHeader() {
      return oss.getHeaders()
    },
    loadAvatar(url) {
      return oss.loadImage(url, oss.TYPE.LOCAL)
    },
    uploadAvatar() {
      return process.env.VUE_APP_BASE_API + '/users/uploadAvatar'
    },
    // 选择logo预览
    handleLogoSuccess(response, file) {
      this.user.avatar = URL.createObjectURL(file.raw)
    }
  }
}
</script>

<style lang="scss" scoped>
.box-center {
  margin: 0 auto;
  display: table;
}

.text-muted {
  color: #777;
}

.user-profile {
  .user-name {
    font-weight: bold;
  }

  .box-center {
    padding-top: 10px;
  }

  .user-role {
    padding-top: 10px;
    font-weight: 400;
    font-size: 14px;
  }

  .box-social {
    padding-top: 30px;

    .el-table {
      border-top: 1px solid #dfe6ec;
    }
  }

  .user-follow {
    padding-top: 20px;
  }
}

.user-bio {
  margin-top: 20px;
  color: #606266;

  span {
    padding-left: 4px;
  }

  .user-bio-section {
    font-size: 14px;
    padding: 10px 0;

    .user-bio-section-header {
      border-bottom: 1px solid #dfe6ec;
      padding-bottom: 10px;
      margin-bottom: 10px;
      font-weight: bold;
    }
  }
}
</style>
