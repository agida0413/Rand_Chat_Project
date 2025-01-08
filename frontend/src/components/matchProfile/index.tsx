import { useMatchStore } from '@/store/matchStore'
import styles from './MatchProfile.module.scss'
import defaultImg from '@/assets/images/default-profile.webp'

export default function MatchProfile() {
  const { setIsOpenModal, matchingData } = useMatchStore()

  const handleClose = () => {
    setIsOpenModal(false)
  }

  return (
    <section className={styles.matchProfileForm}>
      <div className={styles.background}></div>
      <div className={styles.contents}>
        <span className={styles.matchDetail}>
          <h1>프로필</h1>
          <img
            src={
              matchingData.profileImg === null
                ? defaultImg
                : matchingData.profileImg
            }
          />
          <span className={styles.matchText}>
            <h2>{matchingData.nickname}</h2>
            <p>{matchingData.sex}</p>
            <p>거리 : {matchingData.distance}</p>
          </span>
        </span>
        <span className={styles.matchButton}>
          <button>수락</button>
          <button onClick={handleClose}>거절</button>
        </span>
      </div>
    </section>
  )
}
