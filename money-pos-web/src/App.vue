<template>
    <el-config-provider :locale="locale">
        <router-view />
    </el-config-provider>
</template>
<script setup>
// Element-Plus 国际化
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
const locale = zhCn
</script>
