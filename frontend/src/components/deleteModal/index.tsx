import { putMemberDel } from '@/api/user'
import styles from './DeleteModal.module.scss'
import { useState } from 'react'

interface DeleteModalProps {
  handleClose: () => void
}

export default function DeleteModal({ handleClose }: DeleteModalProps) {
  const [password, setPassword] = useState('')

  const handleDelete = () => {
    putMemberDel(password)
  }
  return (
    <section className={styles.deleteModalForm}>
      <div
        className={styles.background}
        onClick={handleClose}></div>
      <div className={styles.contents}>
        <span>
          <h1>회원탈퇴</h1>
          <p>회원을 정말로 탈퇴 하시겠습니까?</p>
        </span>
        <span>
          <input
            value={password}
            onChange={e => setPassword(e.target.value)}
            type="password"
            placeholder="비밀번호를 입력하세요"
          />
          <button onClick={handleDelete}>회원탈퇴</button>
        </span>
      </div>
    </section>
  )
}
