import { getAccessToken, getUser, removeAccessToken } from '@/utils/auth'
import { redirect } from 'react-router-dom'

export async function requiresAuth({ request }: { request: Request }) {
  const token = getAccessToken()
  const url = new URL(request.url)

  if (!token) {
    if (url.pathname === '/login' || url.pathname === '/signup') {
      return
    }
    return redirect(`/login`)
  }

  const userData = await getUser()

  if (userData.data.status === 500) {
    removeAccessToken()
    // const redirectTo = url.pathname + url.search
    // return redirect(`/login?redirectTo=${encodeURIComponent(redirectTo)}`)
    return redirect(`/login`)
  }

  if (url.pathname === '/login' || url.pathname === '/signup') {
    return redirect(`/`)
  }

  return userData
}
