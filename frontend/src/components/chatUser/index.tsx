import styles from './chatUser.module.scss'
import ProfileImage from '../profileImage'
export default function ChatUser() {
  return (
    <div className={styles.peopleContent}>
      <div className={styles.profileImage}>
        <ProfileImage src="" />
      </div>
      <div className={styles.profileDetail}>
        <div className={styles.left}>
          <h3>Anil</h3>
          <h5>Aprill fool's day</h5>
        </div>
        <div className={styles.right}>
          <p>Today, 9.52pm</p>
          <p>v</p>
        </div>
      </div>
    </div>
  )
}
