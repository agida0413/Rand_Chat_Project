import ForgotPassword from '@/routes/pages/ForgotPassword'
import { useState } from 'react'
import { useAuth } from '@/hooks/useLogin'
import { Link } from 'react-router-dom'

export default function Login() {
  const {
    emailError,
    emailErrorMessage,
    passwordError,
    passwordErrorMessage,
    validateInputs,
    handleSubmit
  } = useAuth()

  const [open, setOpen] = useState(false)

  return (
    <>
      <ForgotPassword
        open={open}
        handleClose={() => setOpen(false)}
      />
      <button
        type="submit"
        onClick={validateInputs}>
        로그인
      </button>
      <button
        type="button"
        onClick={() => setOpen(true)}>
        비밀번호를 잊어버리셨나요?
      </button>
      회원이 아니신가요?
      <Link to="/signup">회원가입</Link>
    </>
  )
}
