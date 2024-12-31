import {
  createBrowserRouter,
  RouterProvider,
  useRouteError,
  useNavigate
} from 'react-router-dom'
import DefaultLayout from './layout/Default'
import Home from './pages/home'
import Chat from './pages/chat'
import Setting from './pages/setting'
import NotFound from './pages/NotFound'
import ErrorFallback from './layout/ErrorFallback'
import { Suspense } from 'react'
import Loading from './layout/Loading'

import Login from './pages/login'
import Signup from './pages/signup'
import { requiresAuth, requiresLogin } from './loaders'

function ErrorBoundary() {
  const error: Error = useRouteError() as Error
  const navigate = useNavigate()

  return (
    <ErrorFallback
      error={error}
      resetErrorBoundary={() => navigate('/')}
    />
  )
}

const publicRoutes = [
  {
    path: '/signup',
    element: <Signup />,
    loader: requiresLogin
  },
  {
    path: '/login',
    element: <Login />,
    loader: requiresLogin
  }
]

const protectedRoutes = [
  {
    path: '/',
    element: <DefaultLayout />,
    loader: requiresAuth,
    children: [
      {
        path: '/',
        element: (
          <Suspense fallback={<Loading />}>
            <Home />
          </Suspense>
        )
      },
      {
        path: 'chat',
        element: (
          <Suspense fallback={<Loading />}>
            <Chat />
          </Suspense>
        ),
        children: [
          {
            path: ':chatId',
            element: (
              <Suspense fallback={<Loading />}>
                <Chat />
              </Suspense>
            )
          }
        ]
      },
      {
        path: 'setting',
        element: (
          <Suspense fallback={<Loading />}>
            <Setting />
          </Suspense>
        )
      }
    ]
  }
]

const router = createBrowserRouter([
  {
    errorElement: <ErrorBoundary />,
    children: [
      ...publicRoutes,
      ...protectedRoutes,
      {
        path: '*',
        element: <NotFound />
      }
    ]
  }
])

export default function Router() {
  return <RouterProvider router={router} />
}
