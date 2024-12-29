import { NavLink } from 'react-router-dom'
import styles from './Header.module.scss'
import { HiOutlineHome } from 'react-icons/hi2'
import { BsChatDots } from 'react-icons/bs'

const navigations = [
  { to: '/', label: 'Home', icon: <HiOutlineHome /> },
  { to: '/about', label: 'About', icon: <BsChatDots /> }
]

export default function Header() {
  return (
    <header className={styles.headerContainer}>
      {navigations.map(nav => (
        <NavLink
          key={nav.to}
          to={nav.to}
          className={styles.navLink}>
          <span className={styles.icon}>{nav.icon}</span>
          {nav.label}
        </NavLink>
      ))}
    </header>
  )
}
