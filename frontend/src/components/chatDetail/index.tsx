import { useParams } from 'react-router-dom'
import ProfileImage from '../profileImage'
import styles from './chatDetail.module.scss'
import { useEffect, useRef, useState } from 'react'
import { useChatStore } from '@/store/chatStore'
import { IoExit } from 'react-icons/io5'
import { deleteExitChat } from '@/api/chats'
import { useMultiChatting } from '@/hooks/useMultiChatting'

export default function ChatDetail() {
  const { chatId } = useParams()
  const { sendHandler } = useMultiChatting()
  const { chatRoom, actions } = useChatStore()

  const [input, setInput] = useState('')
  const chatContentRef = useRef<HTMLDivElement>(null)

  const handleSendMsg = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Enter') {
      e.preventDefault()
      sendHandler(chatId, input, 'TEXT')
      setInput('')
    }
  }

  const handleExitChat = () => {
    deleteExitChat(chatId)
  }

  useEffect(() => {
    if (!chatId) return
    actions.fetchChatData(chatId) // 메시지 데이터를 가져옵니다
    actions.fetchChatInfo(chatId) // 사용자 정보 가져오기
  }, [chatId])

  useEffect(() => {
    if (chatContentRef.current) {
      chatContentRef.current.scrollTop = chatContentRef.current.scrollHeight
    }
  }, [chatId])

  // 채팅방 정보를 찾고 메시지를 가져옵니다
  const currentChatRoom = chatRoom.find(room => room.chatRoomId === chatId)
  const messages = currentChatRoom?.msgInfo || []
  const myInfo =
    currentChatRoom?.chatUserInfo?.find(user => user.itsMeFlag) || null
  const userInfo =
    currentChatRoom?.chatUserInfo?.find(user => !user.itsMeFlag) || null

  if (!currentChatRoom || !userInfo) return <div>Loading...</div>

  return (
    <section className={styles.chatDetailContainer}>
      <div className={styles.contactContainer}>
        <div className={styles.contactLeft}>
          <div className={styles.profileImage}>
            <ProfileImage src={userInfo.profileImg} />
          </div>
          <div className={styles.profileDetail}>
            <h3>{userInfo.nickName}</h3>
            <p>나와의 거리 : {userInfo.betweenDistance}</p>
          </div>
        </div>
        <IoExit onClick={handleExitChat} />
      </div>
      <div
        className={styles.chatContentContainer}
        ref={chatContentRef}>
        {messages.length > 0 &&
          messages.map((chat, index) =>
            chat.nickName === myInfo?.nickName ? (
              <div
                key={index}
                className={styles.out}>
                {chat.message}
              </div>
            ) : (
              <div
                key={index}
                className={styles.in}>
                {chat.message}
              </div>
            )
          )}
      </div>
      <div className={styles.chatInputContainer}>
        <input
          type="text"
          value={input}
          onChange={e => setInput(e.target.value)}
          onKeyUp={handleSendMsg}
        />
      </div>
    </section>
  )
}
