module.exports = {
    devServer: {
        port: 9080,
        proxy: {
            '/api': {
                target: 'http://localhost:9081/mc',
                changeOrigin: true,
                ws: true,
                pathRewrite: {
                    '^/api': '/'
                }
            }
        }
  }
}