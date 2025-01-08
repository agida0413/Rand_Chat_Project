import { Outlet, ScrollRestoration, useNavigation } from 'react-router-dom'
import Header from '@/components/header'
import Loading from './Loading'
import styles from './Defalut.module.scss'
import { useLocationPolling } from '@/hooks/useLocationPolling'
import { useMatchStore } from '@/store/matchStore'
import MatchProfile from '@/components/matchProfile'

export default function DefaultLayout() {
  useLocationPolling()
  const { isOpenModal } = useMatchStore()

  const navigation = useNavigation()
  if (navigation.state === 'loading') {
    return <Loading />
  }

  return (
    <main className={styles.mainContainer}>
      {isOpenModal ? <MatchProfile /> : ''}
      <Header />
      <Outlet />
      <ScrollRestoration />
    </main>
  )
}
