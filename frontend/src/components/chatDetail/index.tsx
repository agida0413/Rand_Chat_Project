import ProfileImage from '../profileImage'
import styles from './chatDetail.module.scss'
export default function ChatDetail() {
  return (
    <section className={styles.chatDetailContainer}>
      <div className={styles.contactContainer}>
        <div className={styles.profileImage}>
          <ProfileImage src="" />
        </div>
        <div className={styles.profileDetail}>
          <h3>Anil</h3>
          <p>Online - Last seen, 2.02px</p>
        </div>
      </div>
      <div className={styles.chatContentContainer}>
        <div className={styles.in}>왼쪽</div>
        <div className={styles.out}>오른쪽</div>
      </div>
      <div className={styles.chatInputContainer}>
        <input
          type="text"
          // value={password}
          // onChange={e => setPassword(e.target.value)}
        />
      </div>
    </section>
  )
}
