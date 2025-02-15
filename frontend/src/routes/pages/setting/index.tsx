import { NavLink, useLocation, useNavigate } from 'react-router-dom'
import styles from './setting.module.scss'
import { IoArrowForwardOutline } from 'react-icons/io5'
import { postLogout } from '@/api/login'
import { removeAccessToken, removeMatchToken } from '@/utils/auth'
import { useMatchStore } from '@/store/matchStore'

interface SettingProps {
  onDeactivateModal: () => void
  onDeleteModal: () => void
}

export default function Setting({
  onDeactivateModal,
  onDeleteModal
}: SettingProps) {
  const location = useLocation()
  const navigate = useNavigate()
  const { resetMatchState } = useMatchStore()

  const handleLogout = () => {
    resetMatchState()
    postLogout()
    removeAccessToken()
    removeMatchToken()
    navigate('/login')
  }
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
        </div>
        <div
          className={styles.settingItem}
          onClick={onDeleteModal}>
          <p>회원 탈퇴</p>
        </div>
        <div
          className={styles.settingItem}
          onClick={handleLogout}>
          <p>로그아웃</p>
        </div>
      </span>
    </div>
  )
}
