import { Outlet, useNavigation } from 'react-router-dom'
import Loading from './Loading'
import styles from './ChatDefault.module.scss'
import ChatList from '@/components/chatList'

export default function ChatDefaultLayout() {
  const navigation = useNavigation()
  if (navigation.state === 'loading') {
    return <Loading />
  }

  return (
    <div className={styles.chatContainer}>
      <ChatList />
      <Outlet />
    </div>
  )
}
