import { login, logout, getInfo, getPublicKey} from '@/api/login'
import {getToken, setToken, removeToken, setUserId, removeUserId} from '@/utils/auth'
import {encrypt} from "../../utils/jsencrypt";

const user = {
  state: {
    token: getToken(),
    id: '',
    name: '',
    avatar: '',
    roles: [],
    permissions: []
  },

  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_ID: (state, id) => {
      state.id = id
    },
    SET_NICKNAME: (state, nickName) => {
      state.nickName = nickName
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
    },
    SET_PERMISSIONS: (state, permissions) => {
      state.permissions = permissions
    }
  },

  actions: {
    getPublicKey() {
      return new Promise((resolve, reject) => {
        getPublicKey()
          .then(res => {
            resolve(res)
          })
          .catch(error => {
            reject(error)
          })
      })
    },
    // 登录
    Login({ commit,dispatch }, userInfo) {
      return new Promise((resolve, reject) =>{
        dispatch('getPublicKey').then(publicKey => {
          const username = userInfo.username.trim()
          const password = encrypt(userInfo.password, publicKey)
          const code = userInfo.code
          const uuid = userInfo.uuid
          login(username, password, code, uuid).then(res => {
            setToken(res.token)
            commit('SET_TOKEN', res.token)
            resolve()
          }).catch(error => {
            reject(error)
          })
        })
      })
    },

    // 获取用户信息
    GetInfo({ commit, state }) {
      return new Promise((resolve, reject) => {
        getInfo().then(res => {
          console.log(res)
          const user = res.user
          const avatar = (user.avatar == "" || user.avatar == null) ? require("@/assets/images/profile.jpg") : process.env.VUE_APP_BASE_API + user.avatar;
          if (res.roles && res.roles.length > 0) { // 验证返回的roles是否是一个非空数组
            commit('SET_ROLES', res.roles)
            commit('SET_PERMISSIONS', res.permissions)
          } else {
            commit('SET_ROLES', ['ROLE_DEFAULT'])
          }
          commit('SET_ID', user.userId)
          commit('SET_NICKNAME', user.nickName)
          commit('SET_AVATAR', avatar)
          // 为了兼容老项目，需要存一下

          setUserId(user.userId)
          resolve(res)
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 退出系统
    LogOut({ commit, state }) {
      return new Promise((resolve, reject) => {
        logout(state.token).then(() => {
          commit('SET_TOKEN', '')
          commit('SET_ROLES', [])
          commit('SET_PERMISSIONS', [])
          removeToken()
          removeUserId()
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 前端 登出
    FedLogOut({ commit }) {
      return new Promise(resolve => {
        commit('SET_TOKEN', '')
        removeToken()
        removeUserId()
        resolve()
      })
    }
  }
}

export default user
