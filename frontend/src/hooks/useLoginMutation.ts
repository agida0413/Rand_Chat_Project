import { postLogin } from '@/api/login'
import { notify } from '@/utils/toast'
import { validateLogin } from '@/utils/validator'
import { useMutation } from '@tanstack/react-query'
import { setAccessToken } from '@/utils/auth'
// import { useNavigate } from 'react-router-dom'

export function useLoginMutation() {
  // const navigate = useNavigate()
  const {
    mutate,
    isPending: isPendingLogin,
    isError: isErrorLogin
  } = useMutation({
    mutationFn: postLogin,
    onSuccess: res => {
      const accessToken = res.headers?.get('access')
      if (!accessToken) {
        notify('error', '로그인 중 에러가 발생하였습니다.')
        return false
      }

      setAccessToken(accessToken)
      notify('success', '로그인이 완료되었습니다')

      // const redirectTo = new URLSearchParams(location.search).get('redirectTo')
      // navigate(redirectTo || '/')
    },
    onError: (error: Error) => {
      notify('error', error.message)
    }
  })

  const loginMutation = (username: string, password: string) => {
    const validation = validateLogin(username, password)
    if (!validation.isValid) {
      notify('error', validation.message!)
      return false
    }

    mutate({ username, password })
  }

  return { loginMutation, isPendingLogin, isErrorLogin }
}
