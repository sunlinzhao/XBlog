const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    port: 17011,
    // proxy: { // 配置代理。解决跨域
    //   '/api': {
    //     target: 'http://localhost:17010',
    //     changeOrigin: true,
    //     pathRewrite: {
    //       '^/api': ''
    //     }
    //   }
    // }
  }
})
