import {
  createBrowserRouter,
  RouterProvider,
  useRouteError,
  useNavigate
} from 'react-router-dom'
import DefaultLayout from './layout/Default'
import Home from './pages/home'
import Chat from './pages/chat'
import NotFound from './pages/NotFound'
import ErrorFallback from './layout/ErrorFallback'
import { Suspense } from 'react'
import Loading from './layout/Loading'

import Login from './pages/login'
import Signup from './pages/signup'
import { requiresAuth } from './loaders'
import UpdatePassword from './pages/updatePassword'
import UpdateProfile from './pages/updateProfile'
import SettingDefaultLayout from './layout/SettingDefault'
import ChatTest from './pages/chat/chatTest'
import ChatDefaultLayout from './layout/ChatDefault'

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
    loader: requiresAuth
  },
  {
    path: '/login',
    element: <Login />,
    loader: requiresAuth
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
        element: <ChatDefaultLayout />,
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
        element: <SettingDefaultLayout />,
        children: [
          {
            path: 'update-profile',
            element: (
              <Suspense fallback={<Loading />}>
                <UpdateProfile />
              </Suspense>
            )
          },
          {
            path: 'update-password',
            element: (
              <Suspense fallback={<Loading />}>
                <UpdatePassword />
              </Suspense>
            )
          }
        ]
      }
    ]
  },
  {
    path: '/chattest',
    element: <ChatTest />
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
