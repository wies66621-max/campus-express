import type { ApiResponse, PageResult, Pickup } from '@/types'
import { request } from '@/utils/request'

export const pickupApi = {
  getList: async (params?: any): Promise<ApiResponse<PageResult<Pickup>>> => {
    return request.get<PageResult<Pickup>>('/admin/pickup-record/list', params)
  },

  getById: async (id: string): Promise<ApiResponse<Pickup>> => {
    return request.get<Pickup>(`/admin/pickup-record/${id}`)
  },

  add: async (data: Pickup): Promise<ApiResponse<void>> => {
    return request.post<void>('/admin/pickup-record/add', data)
  },

  update: async (data: Pickup): Promise<ApiResponse<void>> => {
    return request.put<void>('/admin/pickup-record/update', data)
  },

  delete: async (id: string): Promise<ApiResponse<void>> => {
    return request.delete<void>(`/admin/pickup-record/delete/${id}`)
  }
}
