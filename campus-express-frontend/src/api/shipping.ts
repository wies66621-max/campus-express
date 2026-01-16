import { request } from '@/utils/request'

export interface ShippingAppointment {
  id?: number
  userId?: number
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
  stationId?: number
  stationName?: string
  auditTime?: string
  auditorId?: number
  auditorName?: string
  auditRemark?: string
  createTime?: string
  updateTime?: string
}

export const shippingAppointmentApi = {
  getMyPage: (params: { current?: number; size?: number; status?: number }) => {
    return request.get<any>('/shipping-appointment/my-page', { params })
  },

  getPage: (params: { current?: number; size?: number; userId?: number; status?: number; stationId?: number }) => {
    return request.get<any>('/shipping-appointment/page', { params })
  },

  create: (data: ShippingAppointment) => {
    return request.post<ShippingAppointment>('/shipping-appointment', data)
  },

  getDetail: (id: number) => {
    return request.get<ShippingAppointment>(`/shipping-appointment/${id}`)
  },

  audit: (id: number, status: number, auditRemark?: string) => {
    return request.put<boolean>(`/shipping-appointment/audit/${id}`, null, { status, auditRemark })
  },

  cancel: (id: number) => {
    return request.put<boolean>(`/shipping-appointment/cancel/${id}`)
  }
}
