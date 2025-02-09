import { Outlet, useLocation, useNavigation } from 'react-router-dom'
import Loading from './Loading'
import styles from './ChatDefault.module.scss'
import ChatList from '@/components/chatList'

export default function ChatDefaultLayout() {
  const navigation = useNavigation()
  const location = useLocation()
  const isMobile = window.innerWidth <= 1023
  const isChatPage = location.pathname === '/chat'

  if (navigation.state === 'loading') return <Loading />

  return (
    <div className={styles.chatFormContainer}>
      {(!isMobile || isChatPage) && <ChatList />}
      {(isMobile && !isChatPage) || !isMobile ? <Outlet /> : null}
    </div>
  )
}
