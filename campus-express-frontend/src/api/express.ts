import type { ApiResponse, PageResult, Express } from '@/types'
import { request } from '@/utils/request'

export const expressApi = {
  getList: async (params?: any): Promise<ApiResponse<PageResult<Express>>> => {
    return request.get<PageResult<Express>>('/admin/express/list', params)
  },

  getById: async (id: number): Promise<ApiResponse<Express>> => {
    return request.get<Express>(`/admin/express/${id}`)
  },

  getStatistics: async (): Promise<ApiResponse<any>> => {
    return request.get<any>('/admin/express/statistics')
  },

  add: async (data: Express): Promise<ApiResponse<void>> => {
    return request.post<void>('/admin/express/add', data)
  },

  update: async (data: Express): Promise<ApiResponse<void>> => {
    return request.put<void>('/admin/express/update', data)
  },

  delete: async (id: number): Promise<ApiResponse<void>> => {
    return request.delete<void>(`/admin/express/delete/${id}`)
  }
}
