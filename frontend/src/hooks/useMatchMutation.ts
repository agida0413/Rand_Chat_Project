import { notify } from '@/utils/toast'
import { getAccessToken } from '@/utils/auth'
import { EventSourcePolyfill } from 'event-source-polyfill'
import { useMatchStore } from '@/store/matchStore'
import { getMatch } from '@/api/match'
import { useLocationPolling } from './useLocationPolling'

export function useMatchConnection() {
  const VITE_SSE_API_URL = import.meta.env.VITE_SSE_API_URL
  const {
    isLocationGranted,
    isConnecting,
    isConnected,
    setIsConnecting,
    setIsConnected,
    setIsOpenModal,
    setMatchingData
  } = useMatchStore()
  const { getCurrentPosition } = useLocationPolling()

  const startMatchConnection = async (distance: number) => {
    if (!isLocationGranted) {
      notify('error', '위치 접근 권한을 허용해주세요.')
      return
    }
    const accessToken = getAccessToken()
    getCurrentPosition()

    try {
      getMatch({ distance })
      setIsConnecting(true)
      notify('success', '매칭이 시작되었습니다.')

      const eventSource = new EventSourcePolyfill(
        `${VITE_SSE_API_URL}/match/sse`,
        {
          headers: {
            access: accessToken
          },
          withCredentials: true
        }
      )

      eventSource.addEventListener('MATCHING_CHANNEL', event => {
        try {
          const messageEvent = event as MessageEvent
          const eventData = JSON.parse(messageEvent.data)

          const matchingData = {
            status: eventData.status,
            code: eventData.code,
            nickname: eventData.data.nickname,
            sex: eventData.data.sex,
            profileImg:
              eventData.data.profileImg !== 'null'
                ? eventData.data.profileImg
                : null,
            distance: parseFloat(eventData.data.distance),
            matchAcptToken: eventData.data.matchAcptToken,
            timestamp: eventData.timestamp
          }

          setMatchingData(matchingData)
        } catch (error) {
          console.error('Error parsing MATCHING_CHANNEL event data:', error)
        }
      })

      eventSource.onopen = e => {
        setIsConnecting(false)
        setIsConnected(true)
        notify('success', '매칭이 성공하였습니다.')
        setIsOpenModal(true)
        console.log(e)
      }

      eventSource.onerror = () => {
        setIsConnecting(false)
        setIsConnected(false)
        // notify('error', '매칭에 실패하였습니다.')
        eventSource.close()
      }

      return eventSource
    } catch (error) {
      console.log(error, '====')
      setIsConnecting(false)
      setIsConnected(false)
      return null
    }
  }

  return {
    startMatchConnection,
    isConnecting,
    isConnected
  }
}
