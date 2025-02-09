import { Outlet, useNavigate, useNavigation } from 'react-router-dom'
import Header from '@/components/header'
import Loading from './Loading'
import styles from './Defalut.module.scss'
import { useMatchStore } from '@/store/matchStore'
import MatchProfile from '@/components/matchProfile'
import { useEffect } from 'react'
import { useLocationPolling } from '@/hooks/useLocationPolling'
import { useUserStore } from '@/store/userStore'

export default function DefaultLayout() {
  const isMobile = window.innerWidth <= 1023
  const userStore = useUserStore()
  const { actions } = userStore
  const { setUser } = actions

  const { isOpenModal } = useMatchStore()
  const { getCurrentPosition } = useLocationPolling()

  const navigation = useNavigation()
  const navigate = useNavigate()
  if (navigation.state === 'loading') {
    return <Loading />
  }

  useEffect(() => {
    setUser()
    const intervalId = setInterval(getCurrentPosition, 300000)
    getCurrentPosition()
    return () => {
      clearInterval(intervalId)
    }
  }, [])

  useEffect(() => {
    if (performance.navigation.type === 1) navigate('/')
  }, [navigate])

  return (
    <main className={styles.mainContainer}>
      {isOpenModal ? <MatchProfile /> : ''}
      {!isMobile && (
        <>
          <Header />
          <Outlet />
        </>
      )}
      {isMobile && (
        <div className={styles.mainDivContainer}>
          <Outlet />
          <Header />
        </div>
      )}
    </main>
  )
}
