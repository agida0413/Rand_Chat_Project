import { queryClient } from '@/lib/reactQuery'
import { postReissueToken, getUserInfo } from '@/api/login'
import { notify } from '@/utils/toast'
export const AUTH_TOKEN_KEY = 'accessToken'
export const REFRESH_TOKEN_KEY = 'refreshToken'

export type ApiError = {
  status: number
  message: string
  code: string
  timestamp: string
}

export const getAccessToken = () => {
  return localStorage.getItem(AUTH_TOKEN_KEY) || ''
}

export const setAccessToken = (token: string) => {
  localStorage.setItem(AUTH_TOKEN_KEY, token)
}

export const removeAccessToken = () => {
  localStorage.removeItem(AUTH_TOKEN_KEY)
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

// export const refreshAccessToken = async () => {
//   try {
//     const response = await postReissueToken()
//     if (!response.ok && response.status === 403) {
//       throw {
//         status: 403,
//         message: '다시 로그인하여 주세요.',
//         code: 'TOKEN_REFRESH_FAILED',
//         timestamp: new Date().toISOString()
//       }
//     }
//     if (!response.ok) {
//       throw {
//         status: response.status,
//         message: '토큰 갱신 실패',
//         code: 'TOKEN_REFRESH_FAILED',
//         timestamp: new Date().toISOString()
//       }
//     }

//     const accessToken = response.headers?.get('access')
//     if (!accessToken) {
//       throw {
//         status: 401,
//         message: '새로운 액세스 토큰이 없습니다',
//         code: 'TOKEN_NOT_FOUND',
//         timestamp: new Date().toISOString()
//       }
//     }
//     setAccessToken(accessToken)
//   } catch (error) {
//     if (isApiError(error)) {
//       // notify('error', error.message)
//       throw {
//         status: error.status,
//         message: error.message,
//         code: 'TOKEN_NOT_FOUND',
//         timestamp: new Date().toISOString()
//       }
//     } else {
//       // notify('error', '알 수 없는 오류가 발생했습니다')
//       throw {
//         status: 401,
//         message: '알 수 없는 오류가 발생했습니다.',
//         code: 'TOKEN_NOT_FOUND',
//         timestamp: new Date().toISOString()
//       }
//     }
//   }
// }

// export const refreshAccessToken = async () => {
//   try {
//     const response = await postReissueToken()
//     const accessToken = response.headers?.get('access')
//     return accessToken
//   } catch (error) {
//     if (isApiError(error)) {
//       throw {
//         status: error.status,
//         message: error.message,
//         code: 'TOKEN_NOT_FOUND',
//         timestamp: new Date().toISOString()
//       }
//     } else {
//       throw {
//         status: 401,
//         message: '알 수 없는 오류가 발생했습니다.',
//         code: 'TOKEN_NOT_FOUND',
//         timestamp: new Date().toISOString()
//       }
//     }
//   }
// }

export function isApiError(response: unknown): response is ApiError {
  return (
    typeof response === 'object' &&
    response !== null &&
    'message' in response &&
    'status' in response &&
    'code' in response
  )
}
