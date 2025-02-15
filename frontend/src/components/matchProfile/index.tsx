import { useMatchStore } from '@/store/matchStore'
import styles from './MatchProfile.module.scss'
import defaultImg from '@/assets/images/default-profile.webp'
import { useMatchActionMutation } from '@/hooks/useMatchActionMutation'

export default function MatchProfile() {
  const { matchingData, setIsOpenModal } = useMatchStore()
  const { startMatchActionConnection } = useMatchActionMutation()

  const handleAccept = () => {
    startMatchActionConnection(true)
    setIsOpenModal(false)
  }
  const handleReject = () => {
    startMatchActionConnection(false)
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
            <p>거리 : {matchingData.distance} km</p>
          </span>
        </span>
        <span className={styles.matchButton}>
          <button onClick={handleAccept}>수락</button>
          <button onClick={handleReject}>거절</button>
        </span>
      </div>
    </section>
  )
}
