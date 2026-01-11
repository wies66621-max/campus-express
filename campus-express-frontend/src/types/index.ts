export interface ApiResponse<T = any> {
  code: number
  message: string
  data: T
}

export interface PageResult<T = any> {
  records: T[]
  total: number
  size: number
  current: number
  pages: number
}

export interface User {
  id: number
  username: string
  password?: string
  realName: string
  phone: string
  role: string
  status: number
}

export interface Express {
  id: number
  trackingNumber: string
  company: string
  receiverName: string
  receiverPhone: string
  pickupCode: string
  status: number
  stationId: number
  stationName?: string
  remark: string
  createTime: string
  updateTime: string
}

export interface Pickup {
  id: number
  expressId: number
  operatorId: number
  pickupTime: string
  status: number
  remark: string
  createTime: string
}

export interface Station {
  id: number
  stationName: string
  location: string
  contactPhone: string
  status: number
  createTime: string
  updateTime: string
}

export interface LoginForm {
  username: string
  password: string
}

export interface LoginResponse {
  token: string
  user: User
}
