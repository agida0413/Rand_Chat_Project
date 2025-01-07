import { getAccessToken } from '@/utils/auth'
import { api } from '..'

export type GetMatchRequest = {
  distance: number
}

export const getMatch = async ({ distance }: GetMatchRequest) => {
  const VITE_SSE_API_URL = import.meta.env.VITE_SSE_API_URL
  const url = `${VITE_SSE_API_URL}/match?distance=${distance}`
  const res = await api.get(url, {
    headers: {
      Accept: 'application/json',
      access: `${getAccessToken()}`
    },
    credentials: 'include'
  })

  return res
}
