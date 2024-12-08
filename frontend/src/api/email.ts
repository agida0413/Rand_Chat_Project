import { api } from '@/api'

export type CreateCheckAuthCodeRequest = {
  email: string
  authCode: string
}

export type CreateSendAuthCodeRequest = {
  email: string
}

export const CreateCheckAuthCode = async ({
  email,
  authCode
}: CreateCheckAuthCodeRequest) => {
  const url = '/member/email/check'

  const formData = new URLSearchParams()
  formData.append('email', email)
  formData.append('authCode', authCode)

  const res = await api.post(url, {
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    },
    body: formData
  })

  return res
}

export const CreateSendAuthCode = async ({
  email
}: CreateSendAuthCodeRequest) => {
  const url = '/member/email'

  const formData = new URLSearchParams()
  formData.append('email', email)

  const res = await api.post(url, {
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    },
    body: formData
  })

  return res
}
