import ChatDetail from '@/components/chatDetail'
import styles from './chat.module.scss'
import ChatList from '@/components/chatList'
export default function Chat() {
  return (
    <div className={styles.chatContainer}>
      <ChatList />
      <ChatDetail />
    </div>
  )
}
