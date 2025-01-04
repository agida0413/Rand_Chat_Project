import { Outlet, ScrollRestoration, useNavigation } from 'react-router-dom'
import Header from '@/components/header'
import Loading from './Loading'
import styles from './Defalut.module.scss'
import { useLocationPolling } from '@/hooks/useLocationPolling'

export default function DefaultLayout() {
  const { location, isLoading, error } = useLocationPolling()
  const navigation = useNavigation()
  if (navigation.state === 'loading') {
    return <Loading />
  }

  return (
    <main className={styles.mainContainer}>
      <Header />
      <Outlet />
      <ScrollRestoration />
    </main>
  )
}
