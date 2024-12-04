const { defineConfig } = require('@vue/cli-service')
module.exports = {
  devServer: {
    client: {
      overlay: false,
    },
    proxy: {
      '/api': {
        target: 'http://localhost:8080/api', // 后端接口地址
        changeOrigin: true,
        // secure: false,
        // pathRewrite: {
        //   '^/api': ''  // 如果API路径前缀不同，可以进行路径重写
        // }
      }
    }
  }
}

