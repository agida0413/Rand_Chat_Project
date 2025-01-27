import {
  Outlet,
  ScrollRestoration,
  useNavigate,
  useNavigation
} from 'react-router-dom'
import Header from '@/components/header'
import Loading from './Loading'
import styles from './Defalut.module.scss'
import { useMatchStore } from '@/store/matchStore'
import MatchProfile from '@/components/matchProfile'
import { useEffect } from 'react'
import { useLocationPolling } from '@/hooks/useLocationPolling'

export default function DefaultLayout() {
  const { isOpenModal } = useMatchStore()
  const { getCurrentPosition } = useLocationPolling()

  const navigation = useNavigation()
  const navigate = useNavigate()
  if (navigation.state === 'loading') {
    return <Loading />
  }

  useEffect(() => {
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
      <Header />
      <Outlet />
      <ScrollRestoration />
    </main>
  )
}
