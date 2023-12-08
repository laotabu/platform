import Cookies from 'js-cookie'

const TokenKey = 'Admin-Token'

export function getToken() {
  return Cookies.get(TokenKey)
}

export function setToken(token) {
  return Cookies.set(TokenKey, token)
}

export function removeToken() {
  return Cookies.remove(TokenKey)
}

// 兼容老项目
const userIdToken = 'userId-Token'
export function setUserId(userId) {
  return Cookies.set(userIdToken, userId)
}
export function removeUserId() {
  return Cookies.remove(userIdToken)
}
