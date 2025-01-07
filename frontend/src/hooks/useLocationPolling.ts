import { updateMyLocation } from '@/api/location'
import { useState, useEffect } from 'react'

export function useLocationPolling() {
  const [location, setLocation] = useState<{
    localeLat: number
    localeLon: number
  } | null>(null)
  const [isLoading, setIsLoading] = useState(false)
  const [error, setError] = useState<Error | null>(null)

  const updateLocation = async (latitude: number, longitude: number) => {
    setIsLoading(true)
    setError(null)

    try {
      await updateMyLocation({ localeLat: latitude, localeLon: longitude })
      setLocation({ localeLat: latitude, localeLon: longitude })
    } catch (err) {
      setError(new Error('위치 업데이트 실패'))
    } finally {
      setIsLoading(false)
    }
  }

  const getCurrentPosition = () => {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        position => {
          const { latitude, longitude } = position.coords
          updateLocation(latitude, longitude)
        },
        error => {
          console.error('위치 정보 가져오기 실패:', error)
          setError(new Error('위치 정보 가져오기 실패'))
        }
      )
    } else {
      setError(new Error('Geolocation이 지원되지 않는 브라우저입니다.'))
    }
  }

  useEffect(() => {
    getCurrentPosition()

    const intervalId = setInterval(getCurrentPosition, 300000)

    return () => {
      clearInterval(intervalId)
    }
  }, [])

  return { location, isLoading, error }
}
