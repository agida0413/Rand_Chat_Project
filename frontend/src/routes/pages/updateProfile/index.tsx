import { putMemberProfile } from '@/api/user'
import styles from './updateProfile.module.scss'
import defaultImg from '@/assets/images/default-profile.webp'
import { SyntheticEvent, useRef, useState } from 'react'
import { IoArrowBackSharp, IoCamera } from 'react-icons/io5'
import { useUserStore } from '@/store/userStore'
import { useNavigate } from 'react-router-dom'
import { notify } from '@/utils/toast'

export default function UpdateProfile() {
  const { user, actions } = useUserStore()
  const [imageSrc, setImageSrc] = useState(user.profile_img ?? defaultImg)
  const { updateProfileImg } = actions
  const fileInputRef = useRef<HTMLInputElement>(null)
  const navigate = useNavigate()
  const isMobile = window.innerWidth <= 1023

  const onErrorImg = (e: SyntheticEvent<HTMLImageElement>) => {
    e.currentTarget.onerror = null
    setImageSrc(defaultImg)
  }

  const handleSendImage = () => {
    fileInputRef.current?.click()
  }

  const handleChangeProfile = async (
    e: React.ChangeEvent<HTMLInputElement>
  ) => {
    const file = e.target.files?.[0]
    if (file) {
      const reader = new FileReader()

      reader.onloadend = () => {
        if (reader.result) {
          updateProfileImg(reader.result as string)
          putMemberProfile(file)
          navigate('/')
          notify('success', '프로필 변경이 완료 되었습니다.')
        }
      }

      reader.readAsDataURL(file)
    }
  }

  return (
    <div className={styles.settingDetail}>
      {isMobile && <IoArrowBackSharp onClick={() => navigate('/setting')} />}
      <h1>프로필 수정</h1>
      <div className={styles.imageWrapper}>
        <div className={styles.imageContainer}>
          <img
            className={styles.image}
            src={imageSrc}
            alt="profile"
            onError={onErrorImg}
          />
          <button className={styles.changeButton}>
            <input
              type="file"
              ref={fileInputRef}
              style={{ display: 'none' }}
              onChange={handleChangeProfile}
            />
            <IoCamera onClick={handleSendImage} />
          </button>
        </div>
      </div>
    </div>
  )
}
