import { api } from '@/api'

export type PostCheckAuthCodeRequest = {
  email: string
  authCode: string
}

export type PostSendAuthCodeRequest = {
  email: string
}

export type PostSignupRequest = {
  email: string
  username: string
  password: string
  nickName: string
  sex: 'MAN' | 'FEMALE'
  birth: string
  name: string
}

export const postCheckAuthCode = async ({
  email,
  authCode
}: PostCheckAuthCodeRequest) => {
  const API_BASE_URL = import.meta.env.VITE_API_URL
  const url = `${API_BASE_URL}/api/v1/member/email/check`

  const formData = new URLSearchParams()
  formData.append('email', email)
  formData.append('authCode', authCode)

  const res = await api.post(url, {
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded',
      Accept: 'application/json'
    },
    body: formData
  })

  return res
}

export const postSendAuthCode = async ({ email }: PostSendAuthCodeRequest) => {
  const API_BASE_URL = import.meta.env.VITE_API_URL
  const url = `${API_BASE_URL}/api/v1/member/email`

  const formData = new URLSearchParams()
  formData.append('email', email)

  const res = await api.post(url, {
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded',
      Accept: 'application/json'
    },
    body: formData
  })

  return res
}

export const postSignup = async ({
  email,
  username,
  password,
  nickName,
  sex,
  birth,
  name
}: PostSignupRequest) => {
  const API_BASE_URL = import.meta.env.VITE_API_URL
  const url = `${API_BASE_URL}/api/v1/member`

  const formData = new URLSearchParams()
  formData.append('email', email)
  formData.append('username', username)
  formData.append('password', password)
  formData.append('nickName', nickName)
  formData.append('sex', sex)
  formData.append('birth', birth)
  formData.append('name', name)

  const res = await api.post(url, {
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded',
      Accept: 'application/json'
    },
    body: formData
  })

  return res
}
