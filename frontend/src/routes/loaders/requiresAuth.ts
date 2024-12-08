import { redirect } from 'react-router-dom'

export interface User {
  name: string
  age: number
}

async function getUser() {
  const token = localStorage.getItem('access_token')
  // const res = await fetch('api', {
  //   headers: { 'Authorization': `Bearer ${token}` }
  // })
  // return res.json()

  // if (token) return { name: '' } satisfies User
  return null
}

export async function requiresAuth({ request }: { request: Request }) {
  const user = await getUser()
  if (!user) {
    const url = new URL(request.url)
    const redirectTo = url.pathname + url.search
    return redirect(`/login?redirectTo=${encodeURIComponent(redirectTo)}`)
  }
  return user
}
