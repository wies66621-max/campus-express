import type { ApiResponse, PageResult, Station } from '@/types'
import { request } from '@/utils/request'

export const stationApi = {
  getList: async (params?: any): Promise<ApiResponse<PageResult<Station>>> => {
    return request.get<PageResult<Station>>('/admin/station/list', params)
  },

  search: async (stationName?: string, location?: string, status?: number, pageNum?: number, pageSize?: number): Promise<ApiResponse<PageResult<Station>>> => {
    return request.get<PageResult<Station>>('/admin/station/search', { stationName, location, status, pageNum, pageSize })
  },

  getById: async (id: string): Promise<ApiResponse<Station>> => {
    return request.get<Station>(`/admin/station/${id}`)
  },

  add: async (data: Station): Promise<ApiResponse<void>> => {
    return request.post<void>('/admin/station/add', data)
  },

  update: async (data: Station): Promise<ApiResponse<void>> => {
    return request.put<void>('/admin/station/update', data)
  },

  delete: async (id: string): Promise<ApiResponse<void>> => {
    return request.delete<void>(`/admin/station/delete/${id}`)
  }
}
