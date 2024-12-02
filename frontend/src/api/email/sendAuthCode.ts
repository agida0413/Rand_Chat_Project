import { useMutation } from '@tanstack/react-query'
import { api } from '@/api'

type BaseResponse = {
  status: number
  code: string
  timestamp: string
}
type SuccessResponse = BaseResponse & { data: string }
type ErrorResponse = BaseResponse & { message: string }

type ResponseValue = SuccessResponse | ErrorResponse

export const useSendAuthCode = () => {
  return useMutation<ResponseValue, Error, string>({
    mutationFn: async (email: string) => {
      const formData = new URLSearchParams()
      formData.append('email', email)

      const res = await api.post('/api/v1/member/email', {
        body: formData
      })

      const data = await res.json()

      if (!res.ok) {
        throw data
      }

      return data
    }
  })
}
