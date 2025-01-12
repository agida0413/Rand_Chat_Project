import { ApiError, isApiError, setAccessToken } from '@/utils/auth'
import { getReissueToken } from './login'

export type ApiResponse<T = null> = {
  status: number
  code: string
  timestamp: string
  data: T
}

const handleResponse = async (response: Response) => {
  if (!response.ok) {
    const errorData: ApiError | null = await response.json().catch(() => null)

    if (errorData && errorData.status && errorData.message) {
      throw errorData
    }

    throw {
      status: response.status,
      message: response.statusText || '알 수 없는 에러 입니다.',
      code: 'UNKNOWN_ERROR',
      timestamp: new Date().toISOString()
    }
  }

  const contentType = response.headers.get('content-type')
  if (contentType && contentType.includes('application/json')) {
    return response.json()
  }

  return response.text()
}
const fetchWrapper = async <T>(
  url: string,
  options: RequestInit = {}
): Promise<{ data: T; headers: Headers }> => {
  try {
    const response = await fetch(`${url}`, {
      ...options,
      headers: {
        ...options.headers
      }
    })

    if (response.status === 410 || response.status === 401) {
      try {
        const reissueResponse = await getReissueToken()
        if (
          reissueResponse.status === 500 ||
          reissueResponse.status === 401 ||
          reissueResponse.status === 410
        ) {
          return {
            data: reissueResponse as T,
            headers: reissueResponse.headers
          }
        }
        const newAccessToken = reissueResponse.headers?.get('access')

        if (newAccessToken) {
          setAccessToken(newAccessToken)
          const retryResponse = await fetch(`${url}`, {
            ...options,
            headers: {
              ...options.headers,
              access: newAccessToken
            }
          })
          const data = await handleResponse(retryResponse)
          if (isApiError(data)) throw data
          return { data: data as T, headers: retryResponse.headers }
        }
      } catch (error) {
        console.error('API Error:', error)
        throw error
      }
    }

    const data = await handleResponse(response)

    if (isApiError(data)) throw data

    return { data: data as T, headers: response.headers }
  } catch (error) {
    console.error('API Error:', error)
    throw error
  }
}

export const api = {
  get: <T>(url: string, options?: RequestInit) =>
    fetchWrapper<ApiResponse<T>>(url, { ...options, method: 'GET' }),

  post: <T>(url: string, options?: RequestInit) =>
    fetchWrapper<ApiResponse<T>>(url, { ...options, method: 'POST' }),

  put: <T>(url: string, options?: RequestInit) =>
    fetchWrapper<ApiResponse<T>>(url, { ...options, method: 'PUT' }),

  delete: <T>(url: string, options?: RequestInit) =>
    fetchWrapper<ApiResponse<T>>(url, { ...options, method: 'DELETE' }),

  patch: <T>(url: string, options?: RequestInit) =>
    fetchWrapper<ApiResponse<T>>(url, { ...options, method: 'PATCH' })
}
