import { useEffect } from 'react'
import useTimeoutFn from './useTimeoutFn'

export default function useDebounce(
  fn: () => void,
  ms: number,
  deps: React.DependencyList
): () => void {
  const [run, clear] = useTimeoutFn(fn, ms)

  //eslint-disable-next-line
  useEffect(run, deps)

  return clear
}
