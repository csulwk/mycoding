import axios from 'axios'
import { Message } from 'element-ui'
import store from '@/store'
import { getToken } from '@/utils/auth'

// create an axios instance
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API, // url = base url + request url
  timeout: 5000 // request timeout
})

// request interceptor
service.interceptors.request.use(
  config => {
    const token = getToken()
    console.log('request.js --> token：' + token)
    if (token) {
      config.headers['Authorization'] = token
    }
    return config
  },
  error => {
    console.log('request.js --> error:' + error)
    return Promise.reject(error)
  }
)

// response interceptor
service.interceptors.response.use(
  response => {
    const res = response.data
    console.log('request.js --> res:' + res)
    if (res.retCode && res.retCode !== '000000') {
      if (res.retCode === 'E00202') {
        // 未登录
        Message({
          message: 'request.js --> res: + res',
          type: 'error',
          duration: 3 * 1000
        })
        store.commit('SET_USER', {})
      }
      return Promise.reject(new Error(res.message || 'Error'))
    } else {
      return res
    }
  },
  error => {
    console.log('request.js --> err:' + error)
    Message({
      message: error.message,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

export default service
