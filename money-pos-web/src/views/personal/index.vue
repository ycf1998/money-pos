<template>
    <div class="flex flex-1 justify-center sm:m-2 my-2 p-6">
        <el-card class="w-full h-min md:w-11/12 lg:w-4/5">
            <el-tabs v-model="activeName">
                <!-- 基础信息 -->
                <el-tab-pane label="基础信息" name="base" class="flex flex-col">
                    <el-upload
                        :action="uploadUrl"
                        :headers="uploadHeader"
                        :on-success="handleAvatarSuccess"
                        :show-file-list="false"
                        accept="image/*"
                    >
                        <el-avatar :src="infoForm.avatar" shape="square" class="!w-28 !h-28 mb-4 cursor-pointer" />
                    </el-upload>
                    <el-form
                        ref="infoFormRef"
                        :model="infoForm"
                        :rules="infoFormRules"
                        label-width="auto"
                        label-position="top"
                        class="w-full"
                    >
                        <el-form-item label="昵称" prop="nickname">
                            <el-input v-model.trim="infoForm.nickname" />
                        </el-form-item>
                        <el-form-item label="手机号" prop="phone">
                            <el-input v-model.number="infoForm.phone" />
                        </el-form-item>
                        <el-form-item label="邮箱" prop="email">
                            <el-input v-model.trim="infoForm.email" />
                        </el-form-item>
                        <el-form-item label="简介" prop="remark">
                            <el-input v-model.trim="infoForm.remark" type="textarea" :rows="6" show-word-limit
                                      maxlength="200" />
                        </el-form-item>
                        <el-form-item>
                            <el-button type="primary" :loading="loading" @click="updateInfo">提交</el-button>
                        </el-form-item>
                    </el-form>
                </el-tab-pane>

                <!-- 修改密码 -->
                <el-tab-pane label="修改密码" name="changePwd">
                    <el-form
                        ref="changePwdFormRef"
                        :model="changePwdForm"
                        :rules="changePwdFormRule"
                        label-width="auto"
                        label-position="top"
                    >
                        <el-form-item label="旧密码" prop="oldPassword">
                            <el-input v-model="changePwdForm.oldPassword" type="password" show-password />
                        </el-form-item>
                        <el-form-item label="新密码" prop="newPassword">
                            <el-input v-model="changePwdForm.newPassword" type="password" show-password />
                        </el-form-item>
                        <el-form-item label="确认密码" prop="confirmPassword">
                            <el-input v-model="changePwdForm.confirmPassword" type="password" show-password />
                        </el-form-item>
                        <el-form-item>
                            <el-button type="primary" :loading="loading" @click="changePassword">提交</el-button>
                        </el-form-item>
                    </el-form>
                </el-tab-pane>
            </el-tabs>
        </el-card>
    </div>
</template>

<script setup>
import { useGlobalProp } from '@/composables/globalProp.js';
import { getToken } from '@/composables/token.js';
import { useUserStore } from '@/store';
import { ref } from 'vue';

const globalProp = useGlobalProp();
const userStore = useUserStore();
const activeName = ref('base');
const loading = ref(false);

// 上传头像配置
const uploadUrl = `${import.meta.env.VITE_BASE_URL}/users/uploadAvatar`;
const uploadHeader = {
    [globalProp.$money.tokenHeader]: getToken(),
};

// 基础信息表单
const infoFormRef = ref();
const infoForm = ref({
    avatar: globalProp.$money.getOssUrl(userStore.info.avatar),
    nickname: userStore.info.nickname,
    phone: userStore.info.phone,
    email: userStore.info.email,
    remark: userStore.info.remark,
});

const infoFormRules = {
    nickname: [{ required: true, message: '请输入昵称', trigger: 'blur' }],
    phone: [
        {
            pattern: /^1([38][0-9]|4[014-9]|[59][0-35-9]|6[2567]|7[0-8])\d{8}$/,
            message: '请输入有效的手机号',
            trigger: 'blur'
        },
    ],
    email: [{ type: 'email', message: '请输入有效的邮箱', trigger: 'blur' }],
};

// 修改密码表单
const changePwdFormRef = ref();
const changePwdForm = ref({
    oldPassword: '',
    newPassword: '',
    confirmPassword: '',
});

const validateConfirmPassword = (rule, value, callback) => {
    if (value !== changePwdForm.value.newPassword) {
        callback(new Error('两次输入的密码不一致'));
    } else {
        callback();
    }
};

const changePwdFormRule = {
    oldPassword: [{ required: true, message: '请输入旧密码', trigger: 'blur' }],
    newPassword: [{ required: true, message: '请输入新密码', trigger: 'blur' }],
    confirmPassword: [
        { required: true, message: '请确认新密码', trigger: 'blur' },
        { validator: validateConfirmPassword, trigger: 'blur' },
    ],
};

// 头像上传成功回调
function handleAvatarSuccess(response, file) {
    infoForm.value.avatar = URL.createObjectURL(file.raw);
    globalProp.$message.success('头像更换成功');
}

// 更新基础信息
async function updateInfo() {
    // 校验表单
    const valid = await infoFormRef.value.validate();
    if (!valid) return;

    loading.value = true;
    try {
        await userStore.updateInfo(infoForm.value);
        globalProp.$message.success('信息修改成功');
    } catch (error) {
        console.error(error)
    } finally {
        loading.value = false;
    }
}

// 修改密码
async function changePassword() {
    // 校验表单
    const valid = await changePwdFormRef.value.validate();
    if (!valid) return;

    loading.value = true;
    try {
        await userStore.changePassword(changePwdForm.value);
        globalProp.$message.success('密码修改成功');
    } catch (error) {
        console.error(error)
    } finally {
        loading.value = false;
    }
}
</script>