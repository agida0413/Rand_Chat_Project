import { useMutation } from '@tanstack/react-query'
import { postSendAuthCode } from '@/api/signup'
import { useSignupStore } from '@/store/signupStore'
import { notify } from '@/utils/toast'
import { validateEmail } from '@/utils/validator'
export function useSendAuthCodeMutation() {
  const { setUser, setAuth } = useSignupStore()
  const { email } = useSignupStore(state => state.user)
  const {
    mutate,
    isPending: isPendingSendAuthCode,
    isError: isErrorSendAuthCode
  } = useMutation({
    mutationFn: postSendAuthCode,
    onSuccess: () => {
      const targetTime = new Date()
      targetTime.setMinutes(targetTime.getMinutes() + 3)
      setUser({ emailConfirm: email })
      setAuth({
        isRunning: true,
        authTime: targetTime.getTime()
      })
      notify('success', '인증코드가 전송되었습니다')
    },
    onError: (error: Error) => {
      notify('error', error.message)
    }
  })

  const handleSendAuthCode = () => {
    const validation = validateEmail(email)
    if (!validation.isValid) {
      notify('error', validation.message!)
      return false
    }
    mutate({ email })
  }

  return { handleSendAuthCode, isPendingSendAuthCode, isErrorSendAuthCode }
}
