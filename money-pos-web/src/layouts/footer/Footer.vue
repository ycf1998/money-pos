<template>
    <footer class="flex flex-col flex-shrink-0 w-full gap-3 px-6 py-4 md:flex-row md:justify-between md:items-center border-t border-gray-200 dark:border-gray-700">
        <p class="text-sm text-gray-500 dark:text-gray-400 transition-colors">
            &#169; <span>{{ new Date().getFullYear() }}</span> QK-Money UI, All rights reserved
        </p>

        <p class="hidden md:flex items-center gap-1.5 text-sm text-gray-500 dark:text-gray-400 transition-colors">
            <span>UI template from</span>
            <span class="w-5 h-5 text-red-500">❤️</span>
            <span>by</span>
            <a href="https://github.com/ycf1998" target="_blank" class="text-blue-600 hover:underline transition-colors">Money</a>
        </p>
    </footer>
</template>