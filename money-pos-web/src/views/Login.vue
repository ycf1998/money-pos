<template>
    <div class="login-container" @keydown.enter="login">
        <div class="login-card animate-fade-in">
            <el-form ref="loginFormRef" :model="loginForm" :rules="rules"
                     class="mb-0 mt-2 rounded-lg p-4 sm:p-6 lg:p-8">
                <h1 class="text-center text-2xl font-bold text-indigo-600 sm:text-3xl mb-10">
                    {{ title }}
                </h1>

                <!-- 用户名输入 -->
                <div class="form-group">
                    <label for="username" class="sr-only">Username</label>
                    <el-form-item prop="username" class="form-item">
                        <el-input 
                            v-model="loginForm.username" 
                            size="large" 
                            placeholder="请输入账号"
                            prefix-icon="User"
                            class="form-input"
                        />
                    </el-form-item>
                </div>

                <!-- 密码输入 -->
                <div class="form-group">
                    <label for="password" class="sr-only">Password</label>
                    <el-form-item prop="password" class="form-item">
                        <el-input 
                            v-model="loginForm.password" 
                            size="large" 
                            type="password" 
                            show-password
                            placeholder="请输入密码"
                            prefix-icon="Lock"
                            class="form-input"
                        />
                    </el-form-item>
                </div>

                <!-- 登录按钮 -->
                <el-button
                    :loading="loading"
                    @click="login"
                    size="large"
                    class="login-button"
                >
                    登录
                </el-button>
            </el-form>

            <!-- 开发环境快捷登录 -->
            <div v-if="noProd" class="quick-login-container">
                <el-tag
                    class="quick-login-tag"
                    type="danger"
                    @click="() => { loginForm.username = 'money'; loginForm.password = '123' }"
                >
                    超级管理员
                </el-tag>
                <el-tag
                    class="quick-login-tag"
                    type="success"
                    @click="() => { loginForm.username = 'admin'; loginForm.password = '123456' }"
                >
                    管理员
                </el-tag>
                <el-tag
                    class="quick-login-tag"
                    @click="() => { loginForm.username = 'guest'; loginForm.password = '123456' }"
                >
                    游客
                </el-tag>
            </div>
        </div>
    </div>
</template>


<script setup>
import { useUserStore } from '@/store';
import { ref } from 'vue';
import { useRouter } from 'vue-router';

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

async function login() {
    // 校验表单
    const valid = await loginFormRef.value.validate();
    if (!valid) return;

    loading.value = true;
    try {
        // 调用登录接口
        await userStore.login(loginForm.value);

        // 登录成功后跳转到首页
        await router.push({ path: '/' });
    } catch (error) {
        console.error("登录失败：", error)
    } finally {
        loading.value = false;
    }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
  position: relative;
  overflow: hidden;
}

.login-container::before {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(circle, rgba(255,255,255,0.1) 1px, transparent 1px);
  background-size: 30px 30px;
  animation: moveBackground 20s linear infinite;
}

@keyframes moveBackground {
  0% {
    transform: translate(0, 0);
  }
  100% {
    transform: translate(30px, 30px);
  }
}

.login-card {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.18);
  padding: 32px;
  width: 100%;
  max-width: 420px;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.login-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 25px 50px rgba(0, 0, 0, 0.15);
}

/* 动画效果 */
.animate-fade-in {
  animation: fadeInUp 0.6s ease-out forwards;
  opacity: 0;
  transform: translateY(20px);
}

@keyframes fadeInUp {
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 表单元素动画 */
.form-group {
  margin-bottom: 20px;
  animation: fadeInUp 0.6s ease-out forwards;
  opacity: 0;
  transform: translateY(10px);
}

.form-group:nth-child(1) {
  animation-delay: 0.2s;
}

.form-group:nth-child(2) {
  animation-delay: 0.4s;
}

.login-button {
  animation: fadeInUp 0.6s ease-out forwards;
  opacity: 0;
  transform: translateY(10px);
  animation-delay: 0.6s;
}

/* 微交互效果 */
.quick-login-container {
  display: flex;
  justify-content: center;
  gap: 12px;
  margin-top: 24px;
  padding-top: 20px;
  border-top: 1px solid rgba(0, 0, 0, 0.05);
  animation: fadeInUp 0.6s ease-out forwards;
  opacity: 0;
  transform: translateY(10px);
  animation-delay: 0.8s;
}

/* 加载动画优化 */
.el-button--loading .el-icon-loading {
  animation: rotate 1s linear infinite;
}

@keyframes rotate {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

/* 输入框聚焦时的标签动画 */
.el-input__wrapper {
  transition: all 0.3s ease;
}

/* 平滑滚动效果 */
html {
  scroll-behavior: smooth;
}

.quick-login-tag {
  cursor: pointer;
  padding: 10px 20px;
  border-radius: 8px;
  transition: all 0.3s ease;
  font-size: 14px;
  font-weight: 600;
  position: relative;
  overflow: hidden;
  border: 2px solid transparent;
  background-clip: padding-box, border-box;
  background-origin: padding-box, border-box;
}

.quick-login-tag:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
}

.quick-login-tag:active {
  transform: translateY(0);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

/* 为不同类型的标签添加特定样式 */
.quick-login-tag[type="danger"] {
  background: linear-gradient(135deg, #fff 0%, #fff 100%);
  background-image: linear-gradient(#fff, #fff), linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  color: #f5576c;
}

.quick-login-tag[type="danger"]:hover {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  color: white;
}

.quick-login-tag[type="success"] {
  background: linear-gradient(135deg, #fff 0%, #fff 100%);
  background-image: linear-gradient(#fff, #fff), linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  color: #4facfe;
}

.quick-login-tag[type="success"]:hover {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  color: white;
}

.quick-login-tag:not([type]) {
  background: linear-gradient(135deg, #fff 0%, #fff 100%);
  background-image: linear-gradient(#fff, #fff), linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
  color: #764ba2;
}

.quick-login-tag:not([type]):hover {
  background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
  color: #333;
}

/* 响应式设计 */
.form-group {
  margin-bottom: 20px;
}

.form-item {
  margin-bottom: 0;
}

.form-input {
  border-radius: 12px;
  border: 2px solid rgba(102, 126, 234, 0.1);
  transition: all 0.3s ease;
  height: 56px;
  font-size: 16px;
}

.form-input:hover {
  border-color: rgba(102, 126, 234, 0.3);
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.form-input:focus {
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.2);
}

/* 登录按钮样式 */
.login-button {
  width: 100%;
  height: 56px;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  color: white;
  margin-top: 24px;
  position: relative;
  overflow: hidden;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
}

.login-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.4);
  background: linear-gradient(135deg, #764ba2 0%, #667eea 100%);
}

.login-button:active {
  transform: translateY(0);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
}

.login-button::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255,255,255,0.2), transparent);
  transition: all 0.6s ease;
}

.login-button:hover::before {
  left: 100%;
}

/* 标题样式优化 */
.text-center {
  font-weight: 700;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  margin-bottom: 32px;
  position: relative;
}

.text-center::after {
  content: '';
  position: absolute;
  bottom: -10px;
  left: 50%;
  transform: translateX(-50%);
  width: 60px;
  height: 4px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 2px;
}

@media (max-width: 768px) {
  .login-card {
    padding: 24px;
    margin: 0 16px;
    max-width: 100%;
  }
  
  .quick-login-container {
    flex-wrap: wrap;
  }
  
  .quick-login-tag {
    font-size: 13px;
    padding: 8px 16px;
    margin-bottom: 8px;
  }
  
  .form-input {
    height: 50px;
    font-size: 15px;
  }
  
  .login-button {
    height: 50px;
    font-size: 15px;
    margin-top: 20px;
  }
  
  .text-center {
    font-size: 24px;
    margin-bottom: 24px;
  }
  
  .form-group {
    margin-bottom: 16px;
  }
}

@media (max-width: 480px) {
  .login-card {
    padding: 20px;
    margin: 0 12px;
  }
  
  .text-center {
    font-size: 20px;
    margin-bottom: 20px;
  }
  
  .form-input {
    height: 48px;
    font-size: 14px;
  }
  
  .login-button {
    height: 48px;
    font-size: 14px;
  }
  
  .quick-login-tag {
    font-size: 12px;
    padding: 6px 14px;
  }
}

/* 平板设备优化 */
@media (min-width: 769px) and (max-width: 1024px) {
  .login-card {
    max-width: 400px;
  }
}
</style>