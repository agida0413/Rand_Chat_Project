import { Outlet, ScrollRestoration, useNavigation } from 'react-router-dom'
import Header from '@/components/header'
import Loading from './Loading'
import styles from './Defalut.module.scss'
import { useMatchStore } from '@/store/matchStore'
import MatchProfile from '@/components/matchProfile'
import { useEffect } from 'react'
import { useChatStore } from '@/store/chatStore'
import { useMultiChatting } from '@/hooks/useMultiChatting'
import { useLocationPolling } from '@/hooks/useLocationPolling'

export default function DefaultLayout() {
  const { isOpenModal } = useMatchStore()
  const { actions } = useChatStore()
  const { connectToRoom } = useMultiChatting()
  const { getCurrentPosition } = useLocationPolling()

  const navigation = useNavigation()
  if (navigation.state === 'loading') {
    return <Loading />
  }

  useEffect(() => {
    const intervalId = setInterval(getCurrentPosition, 300000)
    getCurrentPosition()
    actions.initChatRoom().then(data => {
      data.forEach(room => connectToRoom(room.chatRoomId))
    })
    return () => {
      clearInterval(intervalId)
    }
  }, [])

  return (
    <main className={styles.mainContainer}>
      {isOpenModal ? <MatchProfile /> : ''}
      <Header />
      <Outlet />
      <ScrollRestoration />
    </main>
  )
}
