import { SyntheticEvent } from 'react'
import defaultImg from '@/assets/images/default-profile.webp'
import styles from './profileImage.module.scss'

interface ProfileImageProps {
  src?: string
  alt?: string
}

export default function ProfileImage({
  src,
  alt = 'profile'
}: ProfileImageProps) {
  const onErrorImg = (e: SyntheticEvent<HTMLImageElement>) => {
    e.currentTarget.onerror = null
    e.currentTarget.src = defaultImg
  }

  const imageSrc = src ?? defaultImg

  return (
    <img
      className={styles.imageContainer}
      src={imageSrc}
      alt={alt}
      onError={onErrorImg}
    />
  )
}
