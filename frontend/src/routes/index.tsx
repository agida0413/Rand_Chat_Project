import {
  createBrowserRouter,
  RouterProvider,
  useRouteError,
  useNavigate
} from 'react-router-dom'
import DefaultLayout from './layout/Default'
import Home from './pages/Home'
import Chat from './pages/chat'
import About from './pages/About'
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
    path: ':chatId',
    element: (
      <Suspense fallback={<Loading />}>
        <Chat />
      </Suspense>
    ),
    loader: requiresAuth
  },
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
        path: 'about',
        element: (
          <Suspense fallback={<Loading />}>
            <About />
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
