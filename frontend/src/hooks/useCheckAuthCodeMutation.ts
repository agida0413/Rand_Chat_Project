import { useSignupStore } from '@/store/signupStore'
import { useMutation } from '@tanstack/react-query'
import { postCheckAuthCode } from '@/api/signup'
import { notify } from '@/utils/toast'
import { validateAuthCode } from '@/utils/validator'
export function useCheckAuthCodeMutation() {
  const { email } = useSignupStore(state => state.user)
  const { authCode } = useSignupStore(state => state.auth)
  const { setAuth } = useSignupStore()
  const {
    mutate,
    isPending: isPendingCheckAuthCode,
    isError: isErrorCheckAuthCode
  } = useMutation({
    mutationFn: postCheckAuthCode,
    onSuccess: () => {
      const targetTime = new Date()
      targetTime.setMinutes(targetTime.getMinutes() + 10)
      setAuth({
        isAuthCodeVerified: true,
        isRunning: true,
        authTime: targetTime.getTime()
      })
      notify('success', '인증이 완료되었습니다')
    },
    onError: (error: Error) => {
      notify('error', error.message)
    }
  })

  const handleCheckAuthCode = () => {
    const validation = validateAuthCode(authCode)
    if (!validation.isValid) {
      notify('error', validation.message!)
      return false
    }
    mutate({ email, authCode })
  }

  return { handleCheckAuthCode, isPendingCheckAuthCode, isErrorCheckAuthCode }
}
