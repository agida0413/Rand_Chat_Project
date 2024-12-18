import TermsAgreement from './TermsAgreement'
import { useNavigate } from 'react-router-dom'
import EmailAuth from './EmailAuth'
import { useSignupStore } from '@/store/signupStore'
import { useSignupMutation } from '@/hooks/useSignupMutation'
import styles from './Signup.module.scss'
import { notify } from '@/utils/toast'
export default function SignupPage() {
  const navigate = useNavigate()
  const { currentPage } = useSignupStore(state => state.auth)
  const { submitSignup, isPendingSignup } = useSignupMutation()
  const { setAuth, resetState } = useSignupStore()
  const { isAllChecked } = useSignupStore(state => state.auth)

  const handleNavigation = (direction: 'next' | 'prev') => {
    if (direction === 'next' && currentPage === 1 && !isAllChecked) {
      notify('error', '이용약관 전체동의를 체크 해주세요')
      return false
    } else if (direction === 'prev' && currentPage === 1) {
      resetState()
      navigate(-1)
    } else if (direction === 'next' && currentPage === 2) {
      submitSignup()
    } else {
      setAuth({ currentPage: currentPage + (direction === 'next' ? 1 : -1) })
    }
  }

  return (
    <section className={styles.signupContainer}>
      <header>
        <h1>회원가입</h1>
      </header>

      {currentPage === 1 && <TermsAgreement />}
      {currentPage === 2 && <EmailAuth />}

      <div className={styles.buttons}>
        <button onClick={() => handleNavigation('prev')}>
          {currentPage === 1 ? '로그인' : '이전'}
        </button>

        <button onClick={() => handleNavigation('next')}>
          {currentPage === 2 && isPendingSignup
            ? '가입중'
            : currentPage === 2
              ? '가입'
              : '다음'}
        </button>
      </div>
    </section>
  )
}
