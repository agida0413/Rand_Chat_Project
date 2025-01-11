import styles from './ErrorFallback.module.scss'

type ErrorPageProps = {
  error: Error
  resetErrorBoundary: () => void
}

export default function ErrorFallback({
  error,
  resetErrorBoundary
}: ErrorPageProps) {
  console.error(error?.message || 'An unexpected error occurred.')

  return (
    <div className={styles.errorContainer}>
      <h2>뭔가 엄청난 일이 일어나고 있어요.</h2>
      <p>
        <a href="mailto:alstn7616@naver.com">여기로</a> 문의 주시면 빠르게
        해결하겠습니다.
      </p>
      <button onClick={resetErrorBoundary}>홈으로</button>
    </div>
  )
}
