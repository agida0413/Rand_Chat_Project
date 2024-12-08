export type ApiResponse<T = null> = {
  status: number
  code: string
  timestamp: string
  data: T
}

export type ApiError = {
  status: number
  message: string
  code: string
  timestamp: string
}

const API_BASE_URL = import.meta.env.VITE_API_URL

export function isApiError(response: unknown): response is ApiError {
  return (
    typeof response === 'object' &&
    response !== null &&
    'message' in response &&
    'status' in response &&
    'code' in response
  )
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
): Promise<T> => {
  try {
    const response = await fetch(`${API_BASE_URL}${url}`, {
      ...options,
      headers: {
        Accept: 'application/json',
        ...options.headers
        // Authorization: `Bearer ${getAccessToken()}`
      }
      // credentials: 'include'
    })

    const data = await handleResponse(response)

    if (isApiError(data)) {
      throw data
    }

    return data as T
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
