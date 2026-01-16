import { request } from '@/utils/request'

export interface Notice {
  id?: number
  title?: string
  content?: string
  status?: number
  creatorId?: number
  creatorName?: string
  createTime?: string
  updateTime?: string
}

export const noticeApi = {
  getList: (params?: { limit?: number }) => {
    return request.get<Notice[]>('/notice/list', { params })
  },

  getDetail: (id: number) => {
    return request.get<Notice>(`/notice/${id}`)
  },

  add: (data: Notice) => {
    return request.post<Notice>('/admin/notice/add', data)
  },

  update: (data: Notice) => {
    return request.put<Notice>('/admin/notice/update', data)
  },

  delete: (id: number) => {
    return request.delete<string>(`/admin/notice/delete/${id}`)
  },

  getPage: (params: { current?: number; size?: number; status?: number }) => {
    return request.get<any>('/admin/notice/page', { params })
  }
}
