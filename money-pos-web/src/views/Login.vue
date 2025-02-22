<template>
    <div class="mx-auto max-w-screen-xl px-4 py-24 sm:px-6 lg:px-8" @keydown.enter="login">
        <el-card class="mx-auto max-w-lg">
            <el-form ref="loginFormRef" :model="loginForm" :rules="rules"
                     class="mb-0 mt-2 rounded-lg p-4 sm:p-6 lg:p-8">
                <h1 class="text-center text-2xl font-bold text-indigo-600 sm:text-3xl mb-10">
                    {{ title }}
                </h1>

                <!-- 用户名输入 -->
                <div>
                    <label for="username" class="sr-only">Username</label>
                    <el-form-item prop="username">
                        <el-input v-model="loginForm.username" size="large" placeholder="请输入账号" />
                    </el-form-item>
                </div>

                <!-- 密码输入 -->
                <div>
                    <label for="password" class="sr-only">Password</label>
                    <el-form-item prop="password">
                        <el-input v-model="loginForm.password" size="large" type="password" show-password
                                  placeholder="请输入密码" />
                    </el-form-item>
                </div>

                <!-- 登录按钮 -->
                <el-button
                    :loading="loading"
                    @click="login"
                    size="large"
                    class="w-full !bg-indigo-600 !text-white !rounded-lg mt-3"
                >
                    登录
                </el-button>
            </el-form>

            <!-- 开发环境快捷登录 -->
            <div v-if="noProd" class="flex justify-center gap-6">
                <el-tag
                    class="cursor-pointer !p-2"
                    type="danger"
                    @click="() => { loginForm.username = 'money'; loginForm.password = '123' }"
                >
                    超级管理员
                </el-tag>
                <el-tag
                    class="cursor-pointer !p-2"
                    type="success"
                    @click="() => { loginForm.username = 'admin'; loginForm.password = '123456' }"
                >
                    管理员
                </el-tag>
                <el-tag
                    class="cursor-pointer !p-2"
                    @click="() => { loginForm.username = 'guest'; loginForm.password = '123456' }"
                >
                    游客
                </el-tag>
            </div>
        </el-card>
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