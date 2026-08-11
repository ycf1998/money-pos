<template>
  <div class="min-h-screen flex items-center justify-center bg-linear-to-br from-slate-50 via-white to-indigo-50 dark:from-gray-950 dark:via-gray-900 dark:to-indigo-950 p-5 relative overflow-hidden font-sans" @keydown.enter="login">
    <!-- 背景装饰：大号柔和光晕 -->
    <div class="absolute top-1/4 -left-32 w-96 h-96 rounded-full bg-indigo-200/30 dark:bg-indigo-800/20 blur-3xl pointer-events-none" />
    <div class="absolute bottom-1/4 -right-32 w-96 h-96 rounded-full bg-purple-200/20 dark:bg-purple-800/15 blur-3xl pointer-events-none" />

    <div class="w-full max-w-[400px] relative">
      <!-- 卡片 -->
      <div class="bg-white dark:bg-gray-900 rounded-2xl shadow-xl shadow-gray-200/60 dark:shadow-black/30 border border-gray-100 dark:border-gray-800 p-10">
        <!-- 顶部品牌标识 -->
        <div class="text-center mb-9">
          <el-icon class="text-[var(--el-color-primary)]" :size="40"><Coin /></el-icon>
          <h1 class="text-xl font-bold text-gray-900 dark:text-white">{{ title }}</h1>
          <p class="mt-1.5 text-sm text-gray-400 dark:text-gray-500">请登录您的账号</p>
        </div>

        <el-form ref="loginFormRef" :model="loginForm" :rules="rules" class="w-full" @submit.prevent>
          <!-- 用户名 -->
          <el-form-item prop="username" class="mb-5">
            <el-input
              v-model="loginForm.username"
              size="large"
              placeholder="请输入账号"
              class="login-input"
            >
              <template #prefix>
                <el-icon class="text-gray-400"><User /></el-icon>
              </template>
            </el-input>
          </el-form-item>

          <!-- 密码 -->
          <el-form-item prop="password" class="mb-6">
            <el-input
              v-model="loginForm.password"
              size="large"
              type="password"
              show-password
              placeholder="请输入密码"
              class="login-input"
            >
              <template #prefix>
                <el-icon class="text-gray-400"><Lock /></el-icon>
              </template>
            </el-input>
          </el-form-item>

          <!-- 登录按钮 -->
          <el-button
            :loading="loading"
            @click="login"
            size="large"
            class="login-submit-btn"
          >
            {{ loading ? '登录中...' : '登 录' }}
          </el-button>
        </el-form>

        <!-- 开发环境快捷登录 -->
        <div v-if="noProd" class="mt-8 pt-6 border-t border-gray-100 dark:border-gray-800">
          <p class="text-xs text-gray-400 mb-3 text-center">开发环境快速切换</p>
          <div class="flex flex-wrap justify-center gap-2">
            <el-button plain round size="small" type="danger" @click="fillLogin('money', '123')">
              超级管理员
            </el-button>
            <el-button plain round size="small" type="success" @click="fillLogin('admin', '123456')">
              管理员
            </el-button>
            <el-button plain round size="small" type="primary" @click="fillLogin('guest', '123456')">
              游客
            </el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useUserStore } from '@/store';
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { User, Lock } from '@element-plus/icons-vue'

const noProd = import.meta.env.MODE !== 'production';
const title = document.title;
const router = useRouter();
const userStore = useUserStore();

const loginFormRef = ref();
const loginForm = ref({
  username: '',
  password: '',
});

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'change' }],
  password: [{ required: true, message: '请输入密码', trigger: 'change' }],
};

const loading = ref(false);

function fillLogin(username, password) {
  loginForm.value.username = username;
  loginForm.value.password = password;
}

async function login() {
  const valid = await loginFormRef.value.validate();
  if (!valid) return;

  loading.value = true;
  try {
    await userStore.login(loginForm.value);
    await router.push({ path: '/' });
  } catch (error) {
    console.error('登录失败：', error);
  } finally {
    loading.value = false;
  }
}
</script>

<style scoped>
/* 输入框 */
.login-input {
  --input-radius: 10px;
}
.login-input :deep(.el-input__wrapper) {
  border-radius: var(--input-radius);
  border: 1.5px solid #e5e7eb;
  transition: all 0.25s ease;
  height: 46px;
  box-shadow: none;
  padding-left: 10px;
  background: #fafafa;
}
.login-input :deep(.el-input__wrapper:hover) {
  border-color: var(--el-color-primary-light-5);
  background: #fff;
}
.login-input :deep(.el-input__wrapper.is-focus) {
  border-color: var(--el-color-primary);
  box-shadow: 0 0 0 3px color-mix(in srgb, var(--el-color-primary) 12%, transparent);
  background: #fff;
}
.login-input :deep(.el-input__wrapper.is-error) {
  border-color: #f87171;
  box-shadow: 0 0 0 3px rgba(248, 113, 113, 0.1);
}
.login-input :deep(.el-input__inner) {
  font-size: 14px;
}
.login-input :deep(.el-input__prefix) {
  margin-right: 6px;
}

/* 登录按钮 */
.login-submit-btn {
  width: 100%;
  height: 46px;
  border-radius: 10px;
  font-size: 15px;
  font-weight: 600;
  letter-spacing: 0.06em;
  background: var(--el-color-primary);
  color: white;
  border: 0;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px color-mix(in srgb, var(--el-color-primary) 30%, transparent);
  cursor: pointer;
}
.login-submit-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 8px 20px color-mix(in srgb, var(--el-color-primary) 40%, transparent);
  background: var(--el-color-primary-dark-2);
}
.login-submit-btn:active {
  transform: translateY(0);
  box-shadow: 0 4px 12px color-mix(in srgb, var(--el-color-primary) 30%, transparent);
}
.login-submit-btn.is-loading {
  background: var(--el-color-primary-light-3);
}

/* 暗色模式 */
.dark .login-input :deep(.el-input__wrapper) {
  background: #1f2937;
  border-color: #374151;
}
.dark .login-input :deep(.el-input__wrapper:hover) {
  background: #1f2937;
  border-color: var(--el-color-primary-light-7);
}
.dark .login-input :deep(.el-input__wrapper.is-focus) {
  background: #1f2937;
  border-color: var(--el-color-primary-light-5);
  box-shadow: 0 0 0 3px color-mix(in srgb, var(--el-color-primary) 15%, transparent);
}
</style>