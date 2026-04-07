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
  id: string
  username: string
  password?: string
  realName: string
  phone: string
  role: string
  status: number
}

export interface Express {
  id: string
  trackingNumber: string
  company: string
  receiverName: string
  receiverPhone: string
  pickupCode: string
  status: number
  stationId: string
  stationName?: string
  remark: string
  createTime: string
  updateTime: string
}

export interface Pickup {
  id: string
  expressId: string
  operatorId: string
  pickupTime: string
  status: number
  remark: string
  createTime: string
}

export interface Station {
  id: string
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
