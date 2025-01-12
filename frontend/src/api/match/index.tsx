import { getAccessToken, getMatchToken } from '@/utils/auth'
import { api } from '..'

export const getMatch = async (distance: number) => {
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

export const postMatchAccept = async (approveChk: boolean) => {
  const VITE_SSE_API_URL = import.meta.env.VITE_SSE_API_URL
  const url = `${VITE_SSE_API_URL}/match`

  const body = JSON.stringify({
    approveChk: approveChk
  })

  const res = await api.post(url, {
    headers: {
      'Content-Type': 'application/json',
      access: `${getAccessToken()}`,
      matchToken: getMatchToken()
    },
    body,
    credentials: 'include'
  })

  return res
}
