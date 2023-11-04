<template>
    <div class="mx-auto max-w-screen-xl px-4 py-24 sm:px-6 lg:px-8" @keydown.enter="login">
        <el-card class="mx-auto max-w-lg">
            <el-form ref="loginFormRef" :model="loginForm" :rules="rules"
                     class="mb-0 mt-2 rounded-lg p-4 sm:p-6 lg:p-8">
                <h1 class="text-center text-2xl font-bold text-indigo-600 sm:text-3xl mb-10">
                    {{title}}
                </h1>

                <div>
                    <label for="username" class="sr-only">username</label>
                    <el-form-item prop="username">
                        <el-input v-model="loginForm.username" size="large" placeholder="Enter username" />
                    </el-form-item>
                </div>

                <div>
                    <label for="password" class="sr-only">Password</label>
                    <el-form-item prop="password">
                        <el-input v-model="loginForm.password" size="large" type="password" show-password
                                  placeholder="Enter password" />
                    </el-form-item>
                </div>

                <el-button :loading="loading" @click="login" size="large"
                           class="w-full !bg-indigo-600 !text-white !rounded-lg mt-3">
                    Sign in
                </el-button>
            </el-form>
            <div class="flex justify-center gap-6" v-if="noProd">
                <el-tag class="cursor-pointer !p-2" type="danger" @click="() => {loginForm.username = 'money';loginForm.password= '123'}">超级管理员</el-tag>
                <el-tag class="cursor-pointer !p-2" type="success" @click="() => {loginForm.username = 'admin';loginForm.password= '123456'}">管理员</el-tag>
                <el-tag class="cursor-pointer !p-2" @click="() => {loginForm.username = 'guest';loginForm.password= '123456'}">游客</el-tag>
            </div>
        </el-card>
    </div>
</template>
<script setup>
const noProd = import.meta.env.MODE !== 'production'
const title = document.title
import {ref} from "vue"
import {useUserStore} from '@/store'
import {useRoute, useRouter} from 'vue-router'

const $router = useRouter();
const redirect = useRoute().query.redirect
const userStore = useUserStore()
const loginFormRef = ref()
const loginForm = ref({
    username: '',
    password: ''
})
const rules = {
    username: [{required: true, trigger: 'change'}],
    password: [{required: true, trigger: 'change'}]
}
const loading = ref(false)

async function login(evt) {
    evt.preventDefault()
    await loginFormRef.value.validate((valid) => {
        if (!valid) return
        loading.value = true
        userStore.login(loginForm.value)
            .then(() => $router.push({path: redirect || '/'}))
            .catch(() => loading.value = false)
    })
}
</script>