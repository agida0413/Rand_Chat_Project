import ChatUser from '@/components/chatUser'
import styles from './chatList.module.scss'
import { IoSearchOutline } from 'react-icons/io5'

export default function ChatList() {
  const chatUsers = Array.from({ length: 30 }, (_, index) => index + 1)

  return (
    <section className={styles.chatListContainer}>
      <div className={styles.inputContainer}>
        <IoSearchOutline />
        <input placeholder="검색" />
      </div>
      <div className={styles.peopleList}>
        <p className={styles.peopleName}>친구 목록</p>
        {chatUsers.map((user, index) => (
          <ChatUser key={index} />
        ))}
      </div>
    </section>
  )
}
