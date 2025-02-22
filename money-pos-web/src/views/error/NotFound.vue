<template>
    <div class="bg-base-100 grid h-screen px-4 place-content-center">
        <div class="text-center">
            <!-- 404 图标 -->
            <svg-icon name="error-404" class="w-auto h-56 mx-auto sm:h-64" />

            <!-- 标题 -->
            <p class="text-2xl font-bold tracking-tight text-gray-900 sm:text-4xl dark:text-gray-100">
                Uh-oh! 404!
            </p>

            <!-- 描述 -->
            <p class="mt-4 text-gray-500 dark:text-gray-300">We can't find that page.</p>

            <!-- 返回首页按钮 -->
            <router-link
                to="/"
                class="inline-block px-5 py-3 mt-6 text-sm font-medium text-white bg-indigo-600 rounded hover:bg-indigo-700 focus:outline-none focus:ring"
            >
                Go Back Home
            </router-link>
        </div>
    </div>
</template>