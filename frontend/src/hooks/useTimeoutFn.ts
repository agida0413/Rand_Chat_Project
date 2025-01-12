import { useCallback, useEffect, useRef } from 'react'

export default function useTimeoutFn(fn: () => void, ms: number): [() => void, () => void] {
  const timeoutId = useRef<ReturnType<typeof setTimeout> | null>(null)
  const callback = useRef(fn)

  useEffect(() => {
    callback.current = fn
  }, [fn])

  const run = useCallback(() => {
    if (timeoutId.current) {
      clearTimeout(timeoutId.current)
    }
    timeoutId.current = setTimeout(() => {
      callback.current()
    }, ms)
  }, [ms])

  const clear = useCallback(() => {
    if (timeoutId.current) {
      clearTimeout(timeoutId.current)
      timeoutId.current = null
    }
  }, [])

  useEffect(() => clear, [clear])

  return [run, clear]
}
