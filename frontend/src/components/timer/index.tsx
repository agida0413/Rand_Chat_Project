import { memo, useEffect, useState } from 'react'

interface TimerProps {
  currentTime: number // milliseconds
}

export const Timer = memo(({ currentTime }: TimerProps) => {
  const [timeLeft, setTimeLeft] = useState<number>(0)

  useEffect(() => {
    const calculateTimeLeft = () => {
      const difference = currentTime - new Date().getTime()
      return difference > 0 ? difference : 0
    }

    setTimeLeft(calculateTimeLeft())

    const timer = setInterval(() => {
      const remaining = calculateTimeLeft()
      setTimeLeft(remaining)

      if (remaining <= 0) {
        clearInterval(timer)
      }
    }, 1000)

    return () => clearInterval(timer)
  }, [currentTime])

  const minutes = String(Math.floor((timeLeft / (1000 * 60)) % 60)).padStart(
    2,
    '0'
  )
  const second = String(Math.floor((timeLeft / 1000) % 60)).padStart(2, '0')

  return (
    <>
      {minutes} : {second}
    </>
  )
})
