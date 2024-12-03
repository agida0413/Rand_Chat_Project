import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { ThemeProvider } from 'styled-components'
import {
  MutationCache,
  QueryCache,
  QueryClient,
  QueryClientProvider
} from '@tanstack/react-query'
import { ReactQueryDevtools } from '@tanstack/react-query-devtools'

import { ToastContainer } from 'react-toastify'
import 'react-toastify/dist/ReactToastify.css'
import { notify } from '@/utils/toast'

import { GlobalStyle } from '@/styles/global'
import theme from '@/styles/theme'
import Router from './routes'

const queryClient = new QueryClient({
  queryCache: new QueryCache({
    onError: error => {
      notify('error', error.message)
    }
  }),
  mutationCache: new MutationCache({
    onError: error => {
      notify('error', error.message)
    }
  })
})

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <QueryClientProvider client={queryClient}>
      <ThemeProvider theme={theme}>
        <ToastContainer />
        <GlobalStyle />
        <Router />
      </ThemeProvider>
      <ReactQueryDevtools />
    </QueryClientProvider>
  </StrictMode>
)
