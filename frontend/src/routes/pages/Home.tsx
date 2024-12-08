import { useState } from 'react'
import { useSendAuthCode } from '@/api/email'

export default function Home() {
  const [email, setEmail] = useState('')

  const { mutate, isPending } = useSendAuthCode()
  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    if (!email) return

    mutate(email, {
      onSuccess: data => {
        console.log('인증 코드 전송 성공:', data)
      },
      onError: error => {
        console.error('인증 코드 전송 실패:', error)
      }
    })
  }

  return (
    <>
      <h1>Home</h1>
      <input
        type="text"
        value={email}
        onChange={e => setEmail(e.target.value)}
      />
      <button
        onClick={handleSubmit}
        disabled={isPending}>
        {isPending ? '대기' : '보내기'}
      </button>
    </>
  )
}
