import { notify } from '@/utils/toast'
import { getAccessToken, getMatchToken } from '@/utils/auth'
import { EventSourcePolyfill } from 'event-source-polyfill'
import { useMatchStore } from '@/store/matchStore'
import { postMatchAccept } from '@/api/match'

export function useMatchActionMutation() {
  const VITE_SSE_API_URL = import.meta.env.VITE_SSE_API_URL
  const { setIsOpenModal } = useMatchStore()

  const startMatchActionConnection = async (approveChk: boolean) => {
    const accessToken = getAccessToken()
    const matchToken = getMatchToken()

    try {
      postMatchAccept(approveChk)

      // TODO: postMatchAccept return 값중 "acceptState": "WAIT"일 경우에만 eventSource 타야 함.
      const eventSource = new EventSourcePolyfill(
        `${VITE_SSE_API_URL}/match/sse/accept`,
        {
          headers: {
            access: accessToken,
            matchToken: matchToken
          },
          withCredentials: true
        }
      )

      console.log(accessToken)
      console.log(matchToken)

      // eventSource.addEventListener('MATCHING_CHANNEL', event => {
      //   try {
      //     const messageEvent = event as MessageEvent
      //     const eventData = JSON.parse(messageEvent.data)

      //     const matchingData = {
      //       status: eventData.status,
      //       code: eventData.code,
      //       nickname: eventData.data.nickname,
      //       sex: eventData.data.sex,
      //       profileImg:
      //         eventData.data.profileImg !== 'null'
      //           ? eventData.data.profileImg
      //           : null,
      //       distance: parseFloat(eventData.data.distance),
      //       matchAcptToken: eventData.data.matchAcptToken,
      //       timestamp: eventData.timestamp
      //     }

      //     setMatchingData(matchingData)
      //   } catch (error) {
      //     console.error('Error parsing MATCHING_CHANNEL event data:', error)
      //   }
      // })

      eventSource.onopen = e => {
        notify('success', '매칭이 성공하였습니다.')
        setIsOpenModal(false)
        console.log(e)
      }

      eventSource.onerror = () => {
        setIsOpenModal(false)
        // notify('error', '매칭에 실패하였습니다.')
        eventSource.close()
      }

      return eventSource
    } catch (error) {
      setIsOpenModal(false)
      return null
    }
  }

  return {
    startMatchActionConnection
  }
}
