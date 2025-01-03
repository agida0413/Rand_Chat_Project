import { NavLink, useLocation } from 'react-router-dom'
import styles from './setting.module.scss'
import { IoArrowForwardOutline } from 'react-icons/io5'

interface SettingProps {
  onDeactivateModal: () => void
  onDeleteModal: () => void
}

export default function Setting({
  onDeactivateModal,
  onDeleteModal
}: SettingProps) {
  const location = useLocation()

  const settings = [
    { name: '프로필 수정', path: '/setting/update-profile' },
    { name: '비밀번호 변경', path: '/setting/update-password' }
  ]

  return (
    <div className={styles.settingWrapper}>
      <h1 className={styles.settingTitle}>세팅 목록</h1>
      <span>
        {settings.map(({ name, path }) => (
          <NavLink
            key={name}
            to={location.pathname === path ? '/setting' : path}
            className={styles.settingItem}>
            <p>{name}</p>
            <IoArrowForwardOutline />
          </NavLink>
        ))}
        <div
          className={styles.settingItem}
          onClick={onDeactivateModal}>
          <p>계정 비활성화</p>
          <IoArrowForwardOutline />
        </div>
        <div
          className={styles.settingItem}
          onClick={onDeleteModal}>
          <p>회원 탈퇴</p>
          <IoArrowForwardOutline />
        </div>
      </span>
    </div>
  )
}
