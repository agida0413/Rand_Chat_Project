import { Outlet, ScrollRestoration, useNavigation } from 'react-router-dom'
import Header from '@/components/header'
import Loading from './Loading'

export default function DefaultLayout() {
  const navigation = useNavigation()
  if (navigation.state === 'loading') {
    return <Loading />
  }

  return (
    <>
      <Header />
      <Outlet />
      <ScrollRestoration />
    </>
  )
}
