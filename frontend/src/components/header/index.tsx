import { NavLink, useNavigate } from 'react-router-dom'
import styles from './Header.module.scss'
import {
  IoHomeOutline,
  IoHomeSharp,
  IoChatbubbleEllipsesSharp,
  IoChatbubbleEllipsesOutline,
  IoSettings,
  IoSettingsOutline,
  IoExit
} from 'react-icons/io5'
import ProfileImage from '../profileImage'
import { removeAccessToken, removeMatchToken } from '@/utils/auth'
import { postLogout } from '@/api/login'
import { useUserStore } from '@/store/userStore'

const navigations = [
  {
    to: '/',
    label: 'Home',
    activeIcon: <IoHomeSharp />,
    defaultIcon: <IoHomeOutline />
  },
  {
    to: '/chat',
    label: 'Chat',
    activeIcon: <IoChatbubbleEllipsesSharp />,
    defaultIcon: <IoChatbubbleEllipsesOutline />
  },
  {
    to: '/setting',
    label: 'Setting',
    activeIcon: <IoSettings />,
    defaultIcon: <IoSettingsOutline />
  }
]

export default function Header() {
  const navigate = useNavigate()
  const userStore = useUserStore()
  const { user } = userStore
  const isMobile = window.innerWidth <= 1023

  const handleLogout = () => {
    postLogout()
    removeAccessToken()
    removeMatchToken()
    navigate('/login')
  }

  return (
    <header className={styles.headerContainer}>
      {!isMobile && (
        <div className={styles.profileContainer}>
          <span>
            <ProfileImage src={user.profile_img} />
          </span>
        </div>
      )}

      {navigations.map(nav => (
        <NavLink
          key={nav.to}
          to={nav.to}
          className={({ isActive }) => `${isActive ? styles.active : ''}`}>
          {({ isActive }) => (isActive ? nav.activeIcon : nav.defaultIcon)}
        </NavLink>
      ))}

      {!isMobile && (
        <button
          onClick={handleLogout}
          className={styles.logoutButton}>
          <IoExit />
        </button>
      )}
    </header>
  )
}
