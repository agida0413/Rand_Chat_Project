import styles from './DeactivateModal.module.scss'

interface DeactivateModalProps {
  handleClose: () => void
}

const handleDeactivate = () => {}

export default function DeactivateModal({ handleClose }: DeactivateModalProps) {
  return (
    <section className={styles.deactivateModalForm}>
      <div
        className={styles.background}
        onClick={handleClose}></div>
      <div className={styles.contents}>
        <span>
          <h1>계정 비활성화</h1>
          <p>계정을 정말로 비활성화 하시겠습니까?</p>
        </span>
        <span>
          <button onClick={handleDeactivate}>비활성화</button>
        </span>
      </div>
    </section>
  )
}
