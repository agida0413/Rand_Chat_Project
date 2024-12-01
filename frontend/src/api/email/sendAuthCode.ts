import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { api } from '@/api'

export const useSendAuthCode = () => {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: async (email: string) => {
      const res = await api.post('/api/v1/member/email', {
        body: JSON.stringify({ email })
      })

      if (!res.ok) {
        const errorData = await res.json()
        console.error('Error details:', errorData)
        throw new Error(errorData.message || '서버 에러 발생')
      }

      return res.json()
    },
    onMutate: async newUser => {},
    onSuccess: data => {
      console.log(data)
    },
    onError: error => {
      console.log(error)
    }
  })
}
