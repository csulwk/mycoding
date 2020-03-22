import Vue from 'vue'
import Router from 'vue-router'

// 导入组件
import Home from "@/components/Home";
import Login from "@/components/Login";
import Register from "@/components/Register";

Vue.use(Router)

export default new Router({
  routes: [
    {
      path:"/",    // 默认显示页面
      component:Home,
      name:"Home"
    },
    {
      path: '/login',
      name: 'Login',
      component: Login
    },
    {
      path: '/register',
      name: 'Register',
      component: Register
    }
  ]
  //model: history    // 去掉地址栏上的"#"号
})
