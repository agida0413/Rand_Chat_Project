import ChatUser from '@/components/chatUser'
import { useEffect, useState } from 'react'
import styles from './ChatList.module.scss'
import { IoSearchOutline } from 'react-icons/io5'
import { NavLink, useLocation } from 'node_modules/react-router-dom/dist'
import { ChatRoomProps, getChatRoom } from '@/api/chats'

export default function ChatList() {
  const [chatRoom, setChatRoom] = useState<ChatRoomProps[]>([])
  const location = useLocation()

  const fetchChatRooms = async () => {
    try {
      const roomData = await getChatRoom()
      setChatRoom(roomData)
    } catch (error) {
      console.error('채팅방 로딩 실패:', error)
    }
  }

  useEffect(() => {
    fetchChatRooms()
  }, [])

  return (
    <section className={styles.chatListContainer}>
      <div className={styles.inputContainer}>
        <IoSearchOutline />
        <input placeholder="검색" />
      </div>
      <div className={styles.peopleList}>
        <p className={styles.peopleName}>채팅 목록</p>
        {chatRoom.length === 0 ? (
          <h4>채팅방이 없습니다</h4>
        ) : (
          chatRoom.map((room, index) => (
            <NavLink
              className={({ isActive }) => (isActive ? styles.activeLink : '')}
              key={index}
              to={
                location.pathname === `/chat/${room.chatRoomId}`
                  ? '/chat'
                  : `/chat/${room.chatRoomId}`
              }>
              <ChatUser room={room} />
            </NavLink>
          ))
        )}
      </div>
    </section>
  )
}
