'use strict';
const webpack = require('webpack')
const path = require('path')

const proxyServer = {
  target: 'http://localhost:8080',
  secure: false
}

module.exports = {
  outputDir: path.join(__dirname, '../../../build/resources/main/static'),
  css: {
    loaderOptions: {
      scss: {
        additionalData: '@import "~@/styles/main.scss";'
      }
    }
  },
  devServer: {
    proxy: {
      '/api/*': proxyServer,
      '/oauth/*': proxyServer
    },
    port: 4000
  },
  configureWebpack: {
    plugins: [
      new webpack.ProvidePlugin({
        '$': 'jquery',
        jQuery: 'jquery'
      })
    ]
  }
}
