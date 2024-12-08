import { memo, useEffect, useState } from 'react'

interface TimerProps {
  currentTime: number
  onTimeEnd?: () => void
}

export const Timer = memo(({ currentTime, onTimeEnd }: TimerProps) => {
  // const MINUTES_IN_MS = currentTime * 60 * 1000
  const MINUTES_IN_MS = currentTime * 1000
  const INTERVAL = 1000
  const [timeLeft, setTimeLeft] = useState<number>(MINUTES_IN_MS)

  useEffect(() => {
    setTimeLeft(MINUTES_IN_MS)
  }, [currentTime])

  const minutes = String(Math.floor((timeLeft / (1000 * 60)) % 60)).padStart(
    2,
    '0'
  )
  const second = String(Math.floor((timeLeft / 1000) % 60)).padStart(2, '0')

  useEffect(() => {
    let timer: NodeJS.Timeout | null = null

    if (timeLeft > 0) {
      timer = setInterval(() => {
        setTimeLeft(prevTime => prevTime - INTERVAL)
      }, INTERVAL)
    } else if (timeLeft <= 0) {
      onTimeEnd?.()
    }

    return () => {
      if (timer) clearInterval(timer)
    }
  }, [timeLeft, onTimeEnd])

  return (
    <>
      {minutes} : {second}
    </>
  )
})
