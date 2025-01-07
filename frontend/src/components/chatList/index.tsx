import ChatUser from '@/components/chatUser'
import styles from './chatList.module.scss'
import { IoSearchOutline } from 'react-icons/io5'
import { NavLink, useLocation } from 'node_modules/react-router-dom/dist'

export default function ChatList() {
  const location = useLocation()
  const chatUsers = Array.from({ length: 30 }, (_, index) => index + 1)

  return (
    <section className={styles.chatListContainer}>
      <div className={styles.inputContainer}>
        <IoSearchOutline />
        <input placeholder="검색" />
      </div>
      <div className={styles.peopleList}>
        <p className={styles.peopleName}>채팅 목록</p>
        {chatUsers.length === 0 ? (
          <h4>채팅방이 없습니다</h4>
        ) : (
          chatUsers.map((_, index) => (
            <NavLink
              key={index}
              to={
                location.pathname === `/chat/${index}`
                  ? '/chat'
                  : `/chat/${index}`
              }>
              <ChatUser />
            </NavLink>
          ))
        )}
      </div>
    </section>
  )
}
