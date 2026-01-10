export const validatePhone = (phone: string): boolean => {
  const reg = /^1[3-9]\d{9}$/
  return reg.test(phone)
}

export const validateUsername = (username: string): boolean => {
  const reg = /^[a-zA-Z0-9_]{4,20}$/
  return reg.test(username)
}

export const validatePassword = (password: string): boolean => {
  return password.length >= 6
}
