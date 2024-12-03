const API_BASE_URL = import.meta.env.VITE_API_URL

export const getAccessToken = () => {
  const accessToken = document.cookie
    .split('; ')
    .find(row => row.startsWith('accessToken='))
    ?.split('=')[1]

  return accessToken || ''
}

const handleResponse = async (response: Response) => {
  if (!response.ok) {
    const errorData = await response.json().catch(() => null)

    const error = new Error(
      errorData?.message || `HTTP error! status: ${response.status}`
    )
    throw error
  }

  const contentType = response.headers.get('content-type')
  if (contentType && contentType.includes('application/json')) {
    return response.json()
  }

  return response.text()
}

const fetchWrapper = async (url: string, options: RequestInit = {}) => {
  try {
    const response = await fetch(`${API_BASE_URL}${url}`, {
      ...options,
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
        Accept: 'application/json',
        ...options.headers,
        Authorization: `Bearer ${getAccessToken()}`
      },
      credentials: 'include'
    })

    return handleResponse(response)
  } catch (error) {
    console.error('API Error:', error)
    throw error
  }
}

const get = (url: string, options: RequestInit = {}) => {
  return fetchWrapper(url, { ...options, method: 'GET' })
}

const post = (url: string, options: RequestInit = {}) => {
  return fetchWrapper(url, { ...options, method: 'POST' })
}

const put = (url: string, options: RequestInit = {}) => {
  return fetchWrapper(url, { ...options, method: 'PUT' })
}

const del = (url: string, options: RequestInit = {}) => {
  return fetchWrapper(url, { ...options, method: 'DELETE' })
}

const patch = (url: string, options: RequestInit = {}) => {
  return fetchWrapper(url, { ...options, method: 'PATCH' })
}

export const api = { get, post, put, delete: del, patch }
