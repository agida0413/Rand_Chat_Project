import { createRoot } from 'react-dom/client'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { ReactQueryDevtools } from '@tanstack/react-query-devtools'
import { CookiesProvider } from 'react-cookie'

import './main.module.scss'
import { ToastContainer } from 'react-toastify'
import 'react-toastify/dist/ReactToastify.css'
import styles from './main.module.scss'

import Router from './routes'
import { ErrorBoundary, FallbackProps } from 'react-error-boundary'
import ErrorFallback from './routes/layout/ErrorFallback'

const queryClient = new QueryClient()

function fallbackRender({ error, resetErrorBoundary }: FallbackProps) {
  return (
    <ErrorFallback
      error={error}
      resetErrorBoundary={resetErrorBoundary}
    />
  )
}

createRoot(document.getElementById('root')!).render(
  <ErrorBoundary FallbackComponent={fallbackRender}>
    <QueryClientProvider client={queryClient}>
      <ToastContainer className={styles.toastContainer} />
      <CookiesProvider>
        <Router />
      </CookiesProvider>
      {import.meta.env.NODE_ENV === 'development' && <ReactQueryDevtools />}
    </QueryClientProvider>
  </ErrorBoundary>
)
