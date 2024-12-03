import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { ThemeProvider } from 'styled-components'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { ReactQueryDevtools } from '@tanstack/react-query-devtools'

import { ToastContainer } from 'react-toastify'
import 'react-toastify/dist/ReactToastify.css'

import { GlobalStyle } from '@/styles/global'
import theme from '@/styles/theme'
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
  <StrictMode>
    <ErrorBoundary FallbackComponent={fallbackRender}>
      <ThemeProvider theme={theme}>
        <QueryClientProvider client={queryClient}>
          <ToastContainer />
          <GlobalStyle />
          <Router />
          {process.env.NODE_ENV === 'development' && <ReactQueryDevtools />}
        </QueryClientProvider>
      </ThemeProvider>
    </ErrorBoundary>
  </StrictMode>
)
