import type { ApiResponse, PageResult, User } from '@/types'
import { request } from '@/utils/request'

export const userApi = {
  getList: async (params?: any): Promise<ApiResponse<PageResult<User>>> => {
    return request.get<PageResult<User>>('/admin/user/list', params)
  },

  getById: async (id: string): Promise<ApiResponse<User>> => {
    return request.get<User>(`/admin/user/${id}`)
  },

  add: async (data: User): Promise<ApiResponse<void>> => {
    return request.post<void>('/admin/user/add', data)
  },

  update: async (data: User): Promise<ApiResponse<void>> => {
    return request.put<void>('/admin/user/update', data)
  },

  delete: async (id: string, currentUserId: string): Promise<ApiResponse<void>> => {
    return request.delete<void>(`/admin/user/delete/${id}`, { currentUserId })
  },

  updateStatus: async (id: string, status: number): Promise<ApiResponse<void>> => {
    return request.put<void>(`/admin/user/status/${id}`, undefined, { status })
  },

  resetPassword: async (id: string): Promise<ApiResponse<void>> => {
    return request.put<void>(`/admin/user/reset-password/${id}`)
  },

  search: async (keyword: string, pageNum?: number, pageSize?: number): Promise<ApiResponse<PageResult<User>>> => {
    return request.get<PageResult<User>>('/admin/user/search', { keyword, pageNum, pageSize })
  }
}
