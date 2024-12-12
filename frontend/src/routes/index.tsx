import { createBrowserRouter, RouterProvider } from 'react-router-dom'
import DefaultLayout from './layout/Default'
import Home from './pages/Home'
import Chat from './pages/Chat'
import About from './pages/About'
import NotFound from './pages/NotFound'

const router = createBrowserRouter([
  {
    path: ':chatId',
    element: <Chat />
  },
  {
    element: <DefaultLayout />,
    children: [
      {
        path: '/',
        element: <Home />
      },
      {
        path: '/about',
        element: <About />
      },
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
