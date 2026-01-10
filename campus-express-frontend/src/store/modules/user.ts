import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { User, LoginForm } from '@/types'
import { authApi } from '@/api/auth'

export const useUserStore = defineStore('user', () => {
  const token = ref<string>(localStorage.getItem('token') || '')
  const user = ref<User | null>(null)

  const login = async (loginForm: LoginForm): Promise<boolean> => {
    try {
      const response = await authApi.login(loginForm)
      if (response.code === 200) {
        token.value = response.data.token
        user.value = response.data.user
        localStorage.setItem('token', response.data.token)
        localStorage.setItem('userRole', response.data.user.role)
        localStorage.setItem('userInfo', JSON.stringify(response.data.user))
        return true
      }
      return false
    } catch (error) {
      console.error('登录失败:', error)
      return false
    }
  }

  const logout = async (): Promise<void> => {
    try {
      await authApi.logout()
    } catch (error) {
      console.error('登出失败:', error)
    } finally {
      token.value = ''
      user.value = null
      localStorage.removeItem('token')
      localStorage.removeItem('userRole')
      localStorage.removeItem('userInfo')
    }
  }

  const getUserInfo = async (): Promise<void> => {
    try {
      const response = await authApi.getInfo()
      if (response.code === 200) {
        user.value = response.data
        localStorage.setItem('userInfo', JSON.stringify(response.data))
      }
    } catch (error) {
      console.error('获取用户信息失败:', error)
    }
  }

  const initUserInfo = (): void => {
    const userInfo = localStorage.getItem('userInfo')
    if (userInfo) {
      user.value = JSON.parse(userInfo)
    }
  }

  return {
    token,
    user,
    login,
    logout,
    getUserInfo,
    initUserInfo
  }
})
