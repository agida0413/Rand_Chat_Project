import { getAccessToken } from '@/utils/auth'

export const putMemberProfile = async (uptProfileImg: File) => {
  const formData = new FormData()
  formData.append('uptProfileImg', uptProfileImg)

  const API_BASE_URL = import.meta.env.VITE_API_URL
  const url = `${API_BASE_URL}/api/v1/member/profile`
  const res = await fetch(url, {
    method: 'PUT',
    headers: {
      // 'Content-Type': 'multipart/form-data',
      Accept: 'application/json',
      access: `${getAccessToken()}`
    },
    body: formData,
    credentials: 'include'
  })

  const data = await res.json()
  return data
}
