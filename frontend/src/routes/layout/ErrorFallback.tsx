import { FallbackProps } from 'react-error-boundary'
export default function ErrorFallback({
  error,
  resetErrorBoundary
}: FallbackProps) {
  return (
    <>
      <h1>ErrorFallback...</h1>
      <div>{error.message}</div>
      <button onClick={() => resetErrorBoundary()}>reset</button>
    </>
  )
}
