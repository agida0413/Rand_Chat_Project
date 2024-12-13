import LoginForm from '@/components/login'
import Brand from '@/components/brand'
import styles from './login.module.scss'

export default function Login() {
  return (
    <div className={styles.loginContainer}>
      <Brand />
      <LoginForm />
    </div>
  )
}
