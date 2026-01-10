import type { ApiResponse, LoginResponse, LoginForm, User } from '@/types'
import { request } from '@/utils/request'

export const authApi = {
  register: async (data: User): Promise<ApiResponse<void>> => {
    return request.post<void>('/user/register', data)
  },

  login: async (data: LoginForm): Promise<ApiResponse<LoginResponse>> => {
    return request.post<LoginResponse>('/user/login', data)
  },

  logout: async (): Promise<ApiResponse<void>> => {
    return request.post<void>('/user/logout')
  },

  getInfo: async (): Promise<ApiResponse<User>> => {
    return request.get<User>('/user/info')
  }
}
