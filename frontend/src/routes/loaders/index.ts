import { getAccessToken, getUser } from '@/utils/auth'
import { redirect } from 'react-router-dom'

export async function requiresAuth() {
  const token = getAccessToken()
  if (token) {
    try {
      const userData = await getUser()
      return userData
    } catch (error) {
      console.log(error)
      //   const url = new URL(request.url)
      // const redirectTo = url.pathname + url.search
      // return redirect(`/login?redirectTo=${encodeURIComponent(redirectTo)}`)
    }
  }
  // try {
  //   const result = await refreshAccessToken()
  //   console.log(result)
  //   // if (result && 'redirect' in result) {
  //   //   return redirect(result.redirect)
  //   // }
  // } catch (error) {
  //   const url = new URL(request.url)
  //   const redirectTo = url.pathname + url.search
  //   return redirect(`/login?redirectTo=${encodeURIComponent(redirectTo)}`)
  // }
  // return null
}

export const requiresLogin = async () => {
  const token = getAccessToken()
  if (token) {
    return redirect('/')
  }
  // try {
  //   await refreshAccessToken()
  // } catch (error) {
  //   console.log(error)
  //   const url = new URL(request.url)
  //   const redirectTo = url.pathname + url.search
  //   return redirect(`/login?redirectTo=${encodeURIComponent(redirectTo)}`)
  // }
  // return null
}
