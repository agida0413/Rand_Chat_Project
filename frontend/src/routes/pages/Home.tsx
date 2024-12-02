import { useState } from 'react'
import { useSendAuthCode } from '@/api/email'

export default function Home() {
  const [email, setEmail] = useState('')
  const { mutate, isPending, error, data } = useSendAuthCode()

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    if (!email) return

    mutate(email)
  }

  return (
    <>
      <h1>Home</h1>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          value={email}
          onChange={e => setEmail(e.target.value)}
        />
        <button
          type="submit"
          disabled={isPending}>
          인증코드 전송
        </button>
      </form>
      {isPending && <p>전송 중...</p>}
      {error && <p>에러가 발생했습니다</p>}
      {data && <p>인증코드가 전송되었습니다</p>}
    </>
  )
}
