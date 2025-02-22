import { defineConfig } from "vite";
import { resolve } from 'path';
import vue from "@vitejs/plugin-vue";
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'
import { createSvgIconsPlugin } from 'vite-plugin-svg-icons'

export default defineConfig(async ({mode}) => ({
  base: mode  === 'demo' ? '/money-pos-demo' : '/money-pos',
  envPrefix: ["VITE_"],
  server: {
    port: 1520,
    strictPort: true,
  },

  plugins: [
    vue(),
    createSvgIconsPlugin({
      iconDirs: [resolve(__dirname, 'src/assets/icons')],
      symbolId: 'icon-[dir]-[name]'
    }),
    AutoImport({
      resolvers: [ElementPlusResolver()],
    }),
    Components({
      resolvers: [ElementPlusResolver()],
    }),
  ],

  resolve: {
    alias: {
      '@': resolve(__dirname, 'src'),
    },
  },

  build: {
    outDir: 'dist', // 输出目录
    assetsDir: 'assets', // 静态资源目录
    sourcemap: process.env.NODE_ENV === 'development', // 开发环境生成 sourcemap
    minify: 'esbuild', // 使用 esbuild 压缩代码
  },

}))
