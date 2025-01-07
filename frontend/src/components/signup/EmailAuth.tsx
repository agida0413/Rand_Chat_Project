import { Timer } from '@/components/timer'
import SignupForm from './SignupForm'
import styles from './EmailAuth.module.scss'
import { useSignupStore } from '@/store/signupStore'
import { useCheckAuthCodeMutation } from '@/hooks/useCheckAuthCodeMutation'
import { useSendAuthCodeMutation } from '@/hooks/useSendAuthCodeMutation'

export default function EmailAuth() {
  const { email } = useSignupStore(state => state.user)
  const { authCode, isAuthCodeVerified, isRunning, authTime } = useSignupStore(
    state => state.auth
  )
  const { setUser, setAuth } = useSignupStore()
  const { handleSendAuthCode, isPendingSendAuthCode } =
    useSendAuthCodeMutation()
  const { handleCheckAuthCode, isPendingCheckAuthCode } =
    useCheckAuthCodeMutation()

  return (
    <div className={styles.emailAuthForm}>
      <span className={styles.parentForm}>
        <div className={styles.authCheck}>
          <input
            type="email"
            value={email}
            placeholder="이메일"
            onChange={e => setUser({ email: e.target.value })}
          />
          <button onClick={() => handleSendAuthCode()}>
            {isPendingSendAuthCode ? '전송중' : '인증받기'}
          </button>
        </div>
        <div className={styles.authCheck}>
          <input
            type="text"
            value={authCode}
            placeholder="인증번호"
            maxLength={6}
            onChange={e => setAuth({ authCode: e.target.value })}
          />
          {isRunning && <Timer currentTime={authTime} />}
          <button onClick={() => handleCheckAuthCode()}>
            {isPendingCheckAuthCode ? '인증중' : '인증'}
          </button>
        </div>
      </span>
      <SignupForm />
    </div>
  )
}
