import { useState } from 'react'
import { notify } from '@/utils/toast'
import { getAccessToken } from '@/utils/auth'

export function useMatchConnection() {
  const [isConnecting, setIsConnecting] = useState(false)
  const [isConnected, setIsConnected] = useState(false)
  const [error, setError] = useState<Error | null>(null)
  const API_BASE_URL = import.meta.env.VITE_SSE_API_URL
  const startMatchConnection = () => {
    const accessToken = getAccessToken()

    const eventSource = new EventSource(`${API_BASE_URL}/match/sse`, {
      headers: {
        access: `${accessToken}`,
        Cookie: ''
      },
      withCredentials: true
    })

    setIsConnecting(true)
    setError(null)

    eventSource.onopen = () => {
      console.log('SSE 연결 성공!')
      setIsConnecting(false)
      setIsConnected(true)
      notify('success', '매칭을 시작하였습니다.')
    }

    eventSource.onmessage = e => {
      console.log('수신한 SSE 데이터:', e.data)
      // 필요한 추가 데이터 처리
    }

    eventSource.onerror = e => {
      console.error('SSE 연결 에러 발생:', e)
      setError(new Error('SSE 연결 에러가 발생하였습니다.'))
      notify('error', 'SSE 연결 에러가 발생하였습니다.')
      setIsConnected(false)
      eventSource.close()
    }

    return () => {
      eventSource.close()
      setIsConnected(false)
      console.log('SSE 연결 종료')
    }
  }

  return {
    startMatchConnection,
    isConnecting,
    isConnected,
    error
  }
}
