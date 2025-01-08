import { useState } from 'react'
import { notify } from '@/utils/toast'
import { getAccessToken } from '@/utils/auth'
import { EventSourcePolyfill } from 'event-source-polyfill'

export function useMatchConnection() {
  const [isConnecting, setIsConnecting] = useState(false)
  const [isConnected, setIsConnected] = useState(false)
  const [error, setError] = useState<Error | null>(null)
  const VITE_SSE_API_URL = import.meta.env.VITE_SSE_API_URL
  const startMatchConnection = () => {
    const accessToken = getAccessToken()

    try {
      const eventSource = new EventSourcePolyfill(
        `${VITE_SSE_API_URL}/match/sse`,
        {
          headers: {
            access: accessToken
          },
          withCredentials: true
        }
      )

      eventSource.onopen = () => {
        console.log('SSE 연결 성공!')
        setIsConnecting(false)
        setIsConnected(true)
        notify('success', '매칭이 성공하였습니다.')
      }

      // 에러 핸들링 개선
      eventSource.onerror = e => {
        console.log('SSE 에러 발생:', e)
        setError(new Error('SSE 연결 에러가 발생하였습니다.'))
        notify('error', '매칭에 실패하였습니다.')
        setIsConnected(false)
        setIsConnecting(false)
        eventSource.close()
      }

      return eventSource
    } catch (error) {
      console.error('EventSource 생성 중 에러:', error)
      setError(new Error('EventSource 생성 실패'))
      setIsConnecting(false)
      return null
    }
  }

  return {
    startMatchConnection,
    isConnecting,
    isConnected,
    error
  }
}
