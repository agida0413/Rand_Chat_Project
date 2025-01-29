import ChatUser from '@/components/chatUser'
import { useEffect, useState } from 'react'
import styles from './ChatList.module.scss'
import { IoSearchOutline } from 'react-icons/io5'
import { NavLink, useLocation } from 'react-router-dom'
import { useChatStore } from '@/store/chatStore'
import { useMultiChatting } from '@/hooks/useMultiChatting'
export default function ChatList() {
  const [inputChatRoom, setInputChatRoom] = useState('')
  const location = useLocation()
  const { chatRoom, actions } = useChatStore()
  const { connectToRoom } = useMultiChatting()
  const { fetchChatInfo } = actions

  useEffect(() => {
    actions.initChatRoom().then(data => {
      data.forEach(room => {
        connectToRoom(room.chatRoomId)
        fetchChatInfo(room.chatRoomId)
      })
    })
  }, [])

  const filteredChatRooms = chatRoom.filter(room =>
    room.opsNickName.toLowerCase().includes(inputChatRoom.toLowerCase())
  )

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
        {filteredChatRooms.length === 0 ? (
          <h4>채팅방이 없습니다</h4>
        ) : (
          filteredChatRooms.map((room, index) => (
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
