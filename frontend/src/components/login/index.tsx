import { useState } from 'react'
import { useLoginMutation } from '@/hooks/useLoginMutation'
import { Link } from 'react-router-dom'
import styles from './LoginForm.module.scss'
import SearchPassword from '@/components/searchPassword'

export default function Login() {
  const { loginMutation, isPendingLogin } = useLoginMutation()

  const [open, setOpen] = useState(false)
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    loginMutation(username, password)
  }

  return (
    <>
      {open && <SearchPassword handleClose={() => setOpen(false)} />}
      <section className={styles.loginForm}>
        <header>
          <h1>로그인</h1>
        </header>

        <form>
          <div>
            <input
              placeholder="ID"
              value={username}
              onChange={e => setUsername(e.target.value)}
            />
          </div>
          <div>
            <input
              type="password"
              placeholder="Password"
              value={password}
              onChange={e => setPassword(e.target.value)}
            />
          </div>
          <button onClick={handleSubmit}>
            {isPendingLogin ? '로그인중' : '로그인'}
          </button>
        </form>

        <footer>
          <button onClick={() => setOpen(true)}>
            비밀번호를 잊어버리셨나요?
          </button>
          <p>
            회원이 아니신가요?{` `} <Link to="/signup">회원가입</Link>
          </p>
        </footer>
      </section>
    </>
  )
}
