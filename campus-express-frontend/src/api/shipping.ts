import { request } from '@/utils/request'

export interface ShippingAppointment {
  id?: string
  userId?: string
  userName?: string
  userPhone?: string
  senderName: string
  senderPhone: string
  senderAddress?: string
  receiverName: string
  receiverPhone: string
  receiverAddress?: string
  company?: string
  packageType?: number
  weight?: number
  remark?: string
  status?: number
  stationId?: string
  stationName?: string
  auditTime?: string
  auditorId?: string
  auditorName?: string
  auditRemark?: string
  createTime?: string
  updateTime?: string
}

export const shippingAppointmentApi = {
  getMyPage: (params: { current?: number; size?: number; status?: number }) => {
    return request.get<any>('/shipping-appointment/my-page', { params })
  },

  getPage: (params: { current?: number; size?: number; userId?: string; status?: number; stationId?: string }) => {
    return request.get<any>('/shipping-appointment/page', { params })
  },

  create: (data: ShippingAppointment) => {
    return request.post<ShippingAppointment>('/shipping-appointment', data)
  },

  getDetail: (id: string) => {
    return request.get<ShippingAppointment>(`/shipping-appointment/${id}`)
  },

  audit: (id: string, status: number, auditRemark?: string) => {
    return request.put<boolean>(`/shipping-appointment/audit/${id}`, null, { status, auditRemark })
  },

  cancel: (id: string) => {
    return request.put<boolean>(`/shipping-appointment/cancel/${id}`)
  }
}
