import type { ApiResponse, PageResult, Station } from '@/types'
import { request } from '@/utils/request'

export const stationApi = {
  getList: async (params?: any): Promise<ApiResponse<PageResult<Station>>> => {
    return request.get<PageResult<Station>>('/admin/station/list', params)
  },

  getById: async (id: number): Promise<ApiResponse<Station>> => {
    return request.get<Station>(`/admin/station/${id}`)
  },

  add: async (data: Station): Promise<ApiResponse<void>> => {
    return request.post<void>('/admin/station/add', data)
  },

  update: async (data: Station): Promise<ApiResponse<void>> => {
    return request.put<void>('/admin/station/update', data)
  },

  delete: async (id: number): Promise<ApiResponse<void>> => {
    return request.delete<void>(`/admin/station/delete/${id}`)
  }
}
