import { api } from '@/api'
import { getAccessToken } from '@/utils/auth'

export type PostLoginRequest = {
  username: string
  password: string
}

export interface getUserInfoResponse {
  email: string
  username: string
  nickname: string
  name: string
  profile_img: string
  sex: '남자' | '여자'
  birth: [number, number, number]
  manAge: number
}

export const postLogin = async ({ username, password }: PostLoginRequest) => {
  const API_BASE_URL = import.meta.env.VITE_API_URL
  const url = `${API_BASE_URL}/api/v1/member/login`

  const formData = new URLSearchParams()
  formData.append('username', username)
  formData.append('password', password)

  const res = await api.post(url, {
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded',
      Accept: 'application/json'
    },
    body: formData,
    credentials: 'include'
  })

  return res
}

export const postLogout = async () => {
  const API_BASE_URL = import.meta.env.VITE_API_URL
  const url = `${API_BASE_URL}/api/v1/member/logout`

  const res = await api.post(url, {
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded',
      Accept: 'application/json',
      access: `${getAccessToken()}`
    },
    credentials: 'include'
  })

  return res
}

export const getReissueToken = async () => {
  const API_BASE_URL = import.meta.env.VITE_API_URL
  const url = `${API_BASE_URL}/api/v1/reissue`
  const res = await fetch(url, {
    method: 'GET',
    headers: {
      'Content-Type': 'x-www-form-urlencoded',
      Accept: 'application/json',
      access: `${getAccessToken()}`
    },
    credentials: 'include'
  })

  return res
}

export const getUserInfo = async () => {
  const API_BASE_URL = import.meta.env.VITE_API_URL
  const url = `${API_BASE_URL}/api/v1/member/info`
  const res = await api.get<getUserInfoResponse>(url, {
    headers: {
      Accept: 'application/json',
      access: `${getAccessToken()}`
    },
    credentials: 'include'
  })

  return res
}

export const putMemberPwd = async (password: string, newPassword: string) => {
  const formData = new URLSearchParams()
  formData.append('password', password)
  formData.append('newPassword', newPassword)

  const API_BASE_URL = import.meta.env.VITE_API_URL
  const url = `${API_BASE_URL}/api/v1/member/pwd`
  const res = await fetch(url, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded',
      Accept: 'application/json',
      access: `${getAccessToken()}`
    },
    body: formData,
    credentials: 'include'
  })

  const data = await res.json()
  return data
}
