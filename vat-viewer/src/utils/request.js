import axios from 'axios'
import { MessageBox, Message } from 'element-ui'
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
    // console.log('request.js --> res:' + JSON.stringify(res))
    // if the custom code is not 000000, it is judged as an error.
    if (res.retCode !== '000000') {
      Message({
        message: res.retMsg || 'Error',
        type: 'error',
        duration: 5 * 1000
      })

      // E00202: 用户未登录
      if (res.retCode === 'E00202') {
        // to re-login
        MessageBox.confirm('You have been logged out, you can cancel to stay on this page, or log in again', 'Confirm logout', {
          confirmButtonText: 'Re-Login',
          cancelButtonText: 'Cancel',
          type: 'warning'
        }).then(() => {
          store.dispatch('user/resetToken').then(() => {
            location.reload()
          })
        })
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
