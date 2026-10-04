<template>
    <div class="bg-base-100 grid h-screen px-6 place-content-center">
        <div class="flex flex-col items-center text-center max-w-md mx-auto">
            <!-- 装饰圆 -->
            <div class="relative mb-8">
                <div class="w-40 h-40 rounded-full bg-[var(--el-color-primary-light-7)]
                            animate-pulse" />
                <div class="absolute inset-0 flex items-center justify-center">
                    <span class="text-7xl font-bold text-[var(--el-color-primary)] select-none">
                        404
                    </span>
                </div>
            </div>

            <!-- 标题 -->
            <h1 class="text-xl font-semibold text-gray-900 dark:text-gray-100">
                页面迷路了
            </h1>

            <!-- 描述 -->
            <p class="mt-2 text-sm text-gray-500 dark:text-gray-400 leading-relaxed">
                你访问的页面不存在，可能已被移除或地址输入有误
            </p>

            <!-- 快捷入口 -->
            <div class="mt-8 flex flex-wrap justify-center gap-3">
                <router-link
                    to="/"
                    class="inline-flex items-center gap-2 px-5 py-2.5 text-sm font-medium text-white rounded-lg
                           bg-[var(--el-color-primary)]
                           hover:bg-[var(--el-color-primary-dark-2)]
                           focus:outline-none focus:ring-2 focus:ring-[var(--el-color-primary)] focus:ring-offset-2
                           transition-all duration-200 shadow-sm hover:shadow-md"
                >
                    <svg-icon name="dashboard" dir="open" class="w-4 h-4" />
                    返回仪表盘
                </router-link>
                <router-link
                    to="/personal"
                    class="inline-flex items-center gap-2 px-5 py-2.5 text-sm font-medium rounded-lg
                           border border-gray-200 dark:border-gray-600
                           text-gray-600 dark:text-gray-300
                           hover:bg-gray-50 dark:hover:bg-gray-800
                           focus:outline-none focus:ring-2 focus:ring-gray-300 focus:ring-offset-2
                           transition-all duration-200"
                >
                    <svg-icon name="sys-user" dir="open" class="w-4 h-4" />
                    个人中心
                </router-link>
            </div>
        </div>
    </div>
</template>