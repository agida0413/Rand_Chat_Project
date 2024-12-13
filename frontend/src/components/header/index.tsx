import { NavLink } from 'react-router-dom'

const navigations = [
  { to: '/', label: 'Home' },
  { to: '/about', label: 'About' }
]

export default function Header() {
  return (
    <header>
      {navigations.map(nav => (
        <NavLink
          key={nav.to}
          to={nav.to}>
          {nav.label}
        </NavLink>
      ))}
    </header>
  )
}
