<template>
    <el-config-provider :locale="zhCn">
        <router-view />
    </el-config-provider>
</template>
<script setup>
// Element-Plus 国际化
import zhCn from 'element-plus/es/locale/lang/zh-cn'
</script>
