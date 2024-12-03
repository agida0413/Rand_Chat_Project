import {
  createBrowserRouter,
  RouterProvider,
  useRouteError
} from 'react-router-dom'
import DefaultLayout from './layout/Default'
import Home from './pages/Home'
import Chat from './pages/Chat'
import About from './pages/About'
import NotFound from './pages/NotFound'
import ErrorFallback from './layout/ErrorFallback'
import { Suspense } from 'react'
import Loading from './layout/Loading'

function ErrorBoundary() {
  const error: Error = useRouteError() as Error
  return (
    <ErrorFallback
      error={error}
      resetErrorBoundary={() => location.reload()}
    />
  )
}

const router = createBrowserRouter([
  {
    path: ':chatId',
    element: <Chat />
  },
  {
    path: '/',
    element: <DefaultLayout />,
    children: [
      {
        path: '/',
        element: <Home />,
        errorElement: <ErrorBoundary />
      },
      {
        path: 'about',
        element: <About />,
        errorElement: <ErrorBoundary />
      },
      {
        path: '*',
        element: <NotFound />,
        errorElement: <ErrorBoundary />
      }
    ]
  }
])

export default function Router() {
  return (
    <Suspense fallback={<Loading />}>
      <RouterProvider router={router} />
    </Suspense>
  )
}
