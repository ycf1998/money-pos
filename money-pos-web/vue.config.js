const { defineConfig } = require('@vue/cli-service')
const path = require('path')

function resolve (dir) {
  return path.join(__dirname, dir)
}

module.exports = defineConfig({
  publicPath: process.env.ENV === 'staging' ? '/money-pos-demo' : '/money-pos',
  outputDir: 'dist',
  assetsDir: 'static',
  transpileDependencies: true,
  lintOnSave: false,
  devServer: {
    port: 9528,
    open: true
  },
  configureWebpack: {
    resolve: {
      fallback: { path: require.resolve("path-browserify") },
    },
  },
  chainWebpack (config) {
    // svg 配置
    config.module
      .rule('svg')
      .exclude.add(resolve('src/icons'))
      .end()
    // icon 配置
    config.module
      .rule('icons')
      .test(/\.svg$/)
      .include.add(resolve('src/icons'))
      .end()
      .use('svg-sprite-loader')
      .loader('svg-sprite-loader')
      .options({
        symbolId: 'icon-[name]'
      })
      .end()
    config
      .when(process.env.NODE_ENV !== 'development',
        config => {
          config
            .optimization.splitChunks({
              chunks: 'all',
              cacheGroups: {
                libs: {
                  name: 'chunk-libs',
                  test: /[\\/]node_modules[\\/]/,
                  priority: 10,
                  chunks: 'initial' // only package third parties that are initially dependent
                },
                elementUI: {
                  name: 'chunk-elementUI', // split elementUI into a single package
                  priority: 20, // the weight needs to be larger than libs and app or it will be packaged into libs or app
                  test: /[\\/]node_modules[\\/]_?element-ui(.*)/ // in order to adapt to cnpm
                },
                commons: {
                  name: 'chunk-commons',
                  test: resolve('src/components'), // can customize your rules
                  minChunks: 3, //  minimum common number
                  priority: 5,
                  reuseExistingChunk: true
                }
              }
            })
          // https:// webpack.js.org/configuration/optimization/#optimizationruntimechunk
          config.optimization.runtimeChunk('single')
        }
      )
  }
})
