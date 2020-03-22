import Vue from 'vue'
import App from './App.vue'
import router from './router'

// 通过反向代理设置前端请求发送地址
var axios = require('axios')
axios.defaults.baseURL = 'http://localhost:9081/mc'
// 全局注册使用
Vue.prototype.$axios = axios
Vue.config.productionTip = false

Vue.config.productionTip = false

new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})
