import { useMutation } from '@tanstack/react-query'
import { api } from '@/api'
import { notify } from '@/utils/toast'

type BaseResponse = {
  status: number
  code: string
  timestamp: string
}
type SuccessResponse = BaseResponse & { data: string }
type ErrorResponse = BaseResponse & { message: string }

export const useSendAuthCode = () => {
  return useMutation<SuccessResponse, ErrorResponse, string>({
    mutationFn: async (email: string) => {
      const formData = new URLSearchParams()
      formData.append('email', email)

      const res = await api.post('/api/v1/member/email', {
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: formData
      })

      return res
    },
    onError: error => {
      notify('error', error.message)
    },
    onSuccess: () => {
      notify('success', '인증코드가 전송되었습니다.')
    }
  })
}
