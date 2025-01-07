import SignupComponent from '@/components/signup'
import Brand from '@/components/brand'
import styles from './signup.module.scss'

export default function Signup() {
  return (
    <div className={styles.signupContainer}>
      <Brand />
      <SignupComponent />
    </div>
  )
}
