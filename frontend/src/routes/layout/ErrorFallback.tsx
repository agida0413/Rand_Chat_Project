type ErrorPageProps = {
  error: Error
  resetErrorBoundary: () => void
}

export default function ErrorFallback({
  error,
  resetErrorBoundary
}: ErrorPageProps) {
  return (
    <div>
      <h1>Error occurred</h1>
      <p>{error?.message || 'An unexpected error occurred.'}</p>
      <button onClick={resetErrorBoundary}>Retry</button>
    </div>
  )
}
