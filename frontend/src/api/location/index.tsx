import { api } from '@/api'
import { getAccessToken } from '@/utils/auth'

export type UpdateLocationRequest = {
  localeLat: number
  localeLon: number
}

export const updateMyLocation = async ({
  localeLat,
  localeLon
}: UpdateLocationRequest) => {
  const API_BASE_URL = import.meta.env.VITE_API_URL
  const url = `${API_BASE_URL}/api/v1/member/location`

  const body = JSON.stringify({
    localeLat: localeLat,
    localeLon: localeLon
  })

  const res = await api.put(url, {
    headers: {
      'Content-Type': 'application/json',
      Accept: 'application/json',
      access: `${getAccessToken()}`
    },
    body,
    credentials: 'include'
  })

  return res
}
