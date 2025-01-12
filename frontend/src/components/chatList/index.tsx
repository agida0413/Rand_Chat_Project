import ChatUser from '@/components/chatUser'
import { useEffect, useState } from 'react'
import styles from './ChatList.module.scss'
import { IoSearchOutline } from 'react-icons/io5'
import { NavLink, useLocation } from 'react-router-dom'
import { useChatRoomStore } from '@/store/chatRoomStore'
import { useMultiChatting } from '@/hooks/useMultiChatting'

export default function ChatList() {
  const [inputChatRoom, setInputChatRoom] = useState('')
  const location = useLocation()
  const { chatRoom, actions } = useChatRoomStore()

  useEffect(() => {
    actions.fetchChatData()
  }, [])

  const { connectedRooms, connectToRoom } = useMultiChatting(chatRoom)

  return (
    <section className={styles.chatListContainer}>
      <div className={styles.inputContainer}>
        <IoSearchOutline />
        <input
          value={inputChatRoom}
          placeholder="검색"
          onChange={e => setInputChatRoom(e.target.value)}
        />
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
              }
              onClick={() => connectToRoom(room.chatRoomId)}>
              <ChatUser room={room} />
              {connectedRooms.includes(room.chatRoomId) ? (
                <span>Connected</span>
              ) : (
                <span>Disconnected</span>
              )}
            </NavLink>
          ))
        )}
      </div>
    </section>
  )
}
