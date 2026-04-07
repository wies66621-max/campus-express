import type { ApiResponse, PageResult, Express } from '@/types'
import { request } from '@/utils/request'

export const expressApi = {
  inbound: async (data: { trackingNumber: string; receiverPhone: string; company?: string }): Promise<ApiResponse<void>> => {
    return request.post<void>('/admin/express/inbound', data)
  },

  getList: async (params?: any): Promise<ApiResponse<PageResult<Express>>> => {
    return request.get<PageResult<Express>>('/admin/express/list', params)
  },

  getById: async (id: string): Promise<ApiResponse<Express>> => {
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

  delete: async (id: string): Promise<ApiResponse<void>> => {
    return request.delete<void>(`/admin/express/delete/${id}`)
  },

  getMyExpress: async (pageNum: number, pageSize: number): Promise<ApiResponse<PageResult<Express>>> => {
    return request.get<PageResult<Express>>('/user/express/my', { pageNum, pageSize })
  },

  pickupByCode: async (pickupCode: string): Promise<ApiResponse<any>> => {
    return request.post<any>('/user/express/pickup', { pickupCode })
  },

  searchByExpressNo: async (expressNo: string, pageNum: number, pageSize: number): Promise<ApiResponse<PageResult<Express>>> => {
    return request.get<PageResult<Express>>('/user/express/search/no', { expressNo, pageNum, pageSize })
  },

  searchByPhone: async (phone: string, pageNum: number, pageSize: number): Promise<ApiResponse<PageResult<Express>>> => {
    return request.get<PageResult<Express>>('/user/express/search/phone', { phone, pageNum, pageSize })
  },

  quickSearch: async (params: {
    trackingNumber?: string
    pickupCode?: string
    receiverPhone?: string
    status?: number
    pageNum?: number
    pageSize?: number
  }): Promise<ApiResponse<PageResult<Express>>> => {
    return request.get<PageResult<Express>>('/admin/express/search', params)
  }
}
