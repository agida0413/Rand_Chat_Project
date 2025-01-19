import ChatUser from '@/components/chatUser'
import { useEffect, useState } from 'react'
import styles from './ChatList.module.scss'
import { IoSearchOutline } from 'react-icons/io5'
import { NavLink, useLocation } from 'react-router-dom'
import { useChatStore } from '@/store/chatStore'
import { getAccessToken } from '@/utils/auth'

export default function ChatList() {
  const [inputChatRoom, setInputChatRoom] = useState('')
  const location = useLocation()
  const { chatRoom, actions } = useChatStore()

  // useEffect(() => {
  //   actions.fetchChatData()
  // }, [])

  // const handleTest = async () => {
  //   // const VITE_SSE_API_URL = import.meta.env.VITE_SSE_API_URL
  //   const API_BASE_URL = import.meta.env.VITE_API_URL
  //   const url = `${API_BASE_URL}/api/v1/test/1`

  //   const res = await fetch(url, {
  //     method: 'GET',
  //     headers: {
  //       // Accept: 'application/json',
  //       access: `${getAccessToken()}`
  //     }
  //     // credentials: 'include'
  //   })
  // }

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
        {/* <button onClick={handleTest}>test</button> */}
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
