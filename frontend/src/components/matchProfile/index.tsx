import { useMatchStore } from '@/store/matchStore'
import styles from './SearchPassword.module.scss'

export default function MatchProfile() {
  const { setIsOpenModal } = useMatchStore()

  const handleClose = () => {
    setIsOpenModal(false)
  }

  return (
    <section className={styles.searchPasswordForm}>
      <div className={styles.background}></div>
      <div className={styles.contents}>
        <span>
          <h1>비밀번호 초기화</h1>
          <p>
            계정의 이메일 주소를 입력하면 비밀번호를 재설정 할 수 있는 링크를
            보내드립니다.
          </p>
        </span>
        <span>
          <input
            type="email"
            placeholder="이메일을 입력하세요"
          />
          <button>수락</button>
          <button onClick={handleClose}>거절</button>
        </span>
      </div>
    </section>
  )
}
