import {
  CreateCheckAuthCode,
  CreateCheckAuthCodeRequest,
  CreateSendAuthCode,
  CreateSendAuthCodeRequest
} from '@/api/email'
import { useMutation } from '@tanstack/react-query'
import { notify } from '@/utils/toast'

export const useSendAuthCode = () => {
  return useMutation({
    mutationFn: ({ email }: CreateSendAuthCodeRequest) =>
      CreateSendAuthCode({ email }),
    onSuccess: () => {
      notify('success', '인증코드가 전송되었습니다')
    },
    onError: (error: Error) => {
      notify('error', error.message)
    }
  })
}

export const useCheckAuthCode = () => {
  return useMutation({
    mutationFn: ({ email, authCode }: CreateCheckAuthCodeRequest) =>
      CreateCheckAuthCode({ email, authCode }),
    onSuccess: () => {
      notify('success', '인증이 완료되었습니다')
    },
    onError: (error: Error) => {
      notify('error', error.message)
    }
  })
}
