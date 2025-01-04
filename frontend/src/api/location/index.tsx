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
  const url = '/member/location'

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

  console.log(res)

  return res
}
