import {
  createBrowserRouter,
  RouterProvider,
  useRouteError,
  useNavigate
} from 'react-router-dom'
import DefaultLayout from './layout/Default'
import Home from './pages/Home'
import Chat from './pages/Chat'
import About from './pages/About'
import NotFound from './pages/NotFound'
import ErrorFallback from './layout/ErrorFallback'
import { Suspense } from 'react'
import Loading from './layout/Loading'
import Login from './pages/Login'
import Signup from './pages/Signup'
import { requiresAuth } from './loaders/requiresAuth'

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
    element: <Signup />
  },
  {
    path: '/login',
    element: <Login />
  }
]

const protectedRoutes = [
  {
    path: ':chatId',
    element: <Chat />,
    loader: requiresAuth
  },
  {
    path: '/',
    element: <DefaultLayout />,
    loader: requiresAuth,
    children: [
      {
        path: '/',
        element: <Home />
      },
      {
        path: 'about',
        element: <About />
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
  return (
    <Suspense fallback={<Loading />}>
      <RouterProvider router={router} />
    </Suspense>
  )
}
