import { NavLink } from 'react-router-dom'
import styled from 'styled-components'

const navigations = [
  { to: '/', label: 'Home' },
  { to: '/about', label: 'About' }
]

export default function TheHeader() {
  return (
    <header>
      <StyledNav>
        {navigations.map(nav => (
          <NavLink
            key={nav.to}
            to={nav.to}>
            {nav.label}
          </NavLink>
        ))}
      </StyledNav>
    </header>
  )
}

const StyledNav = styled.nav`
  .active {
    font-weight: bold;
  }
`
