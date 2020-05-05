import Cookies from 'js-cookie'

const TokenKey = 'my_coding_token'

export function getToken() {
  return Cookies.get(TokenKey)
}

export function setToken(token) {
  // Create a cookie that expires 7 days from now, valid across the entire site.
  // Cookies.set(TokenKey, token, { expires: 7 })
  const millisecond = new Date().getTime()
  const expiresTime = new Date(millisecond + 30 * 60 * 1000)
  return Cookies.set(TokenKey, token, { expires: expiresTime })
}

export function removeToken() {
  return Cookies.remove(TokenKey)
}
