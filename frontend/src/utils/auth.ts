import Cookies, { CookieSetOptions } from 'universal-cookie'
import { queryClient } from '@/lib/reactQuery'
import { getUserInfo } from '@/api/login'
export const AUTH_TOKEN_KEY = 'accessToken'
export const MATCH_TOKEN_KEY = 'matchToken'
export const REFRESH_TOKEN_KEY = 'refreshToken'

export type ApiError = {
  status: number
  message: string
  code: string
  timestamp: string
}

const cookies = new Cookies()

export const getAccessToken = () => {
  return localStorage.getItem(AUTH_TOKEN_KEY) || ''
}

export const setAccessToken = (token: string) => {
  localStorage.setItem(AUTH_TOKEN_KEY, token)
}

export const removeAccessToken = () => {
  localStorage.removeItem(AUTH_TOKEN_KEY)
}

export const getMatchToken = () => {
  return localStorage.getItem(MATCH_TOKEN_KEY) || ''
}

export const setMatchToken = (token: string) => {
  localStorage.setItem(MATCH_TOKEN_KEY, token)
}

export const removeMatchToken = () => {
  localStorage.removeItem(MATCH_TOKEN_KEY)
}

export const setCookie = (
  name: string,
  value: string,
  options?: CookieSetOptions
): void => {
  cookies.set(name, value, { ...options })
}

export const getCookie = (name: string): string | undefined => {
  return cookies.get(name)
}

export const removeCookie = (
  name: string,
  options?: CookieSetOptions
): void => {
  cookies.remove(name, { path: '/', ...options })
}

export const getUser = async () => {
  try {
    const queryData = await queryClient.ensureQueryData({
      queryKey: ['user'],
      queryFn: () => getUserInfo().then(res => res)
    })
    return queryData
  } catch (error) {
    throw error
  }
}

export function isApiError(response: unknown): response is ApiError {
  return (
    typeof response === 'object' &&
    response !== null &&
    'message' in response &&
    'status' in response &&
    'code' in response
  )
}
