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
  const url = '/member/login'

  const formData = new URLSearchParams()
  formData.append('username', username)
  formData.append('password', password)

  const res = await api.post(url, {
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded',
      Accept: 'application/json'
    },
    body: formData
  })

  return res
}

const API_BASE_URL = import.meta.env.VITE_API_URL
export const postReissueToken = async () => {
  const url = '/reissue'
  const res = await fetch(API_BASE_URL + url, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded',
      Accept: 'application/json',
      access: `${getAccessToken()}`
    },
    credentials: 'include'
  })

  return res
}

export const getUserInfo = async () => {
  const url = '/member/info'
  const res = await api.get<getUserInfoResponse>(url, {
    headers: {
      Accept: 'application/json',
      access: `${getAccessToken()}`
    },
    credentials: 'include'
  })

  return res
}
