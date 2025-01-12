import { useParams } from 'react-router-dom'
import ProfileImage from '../profileImage'
import styles from './chatDetail.module.scss'
import { useChatUserInfo } from '@/hooks/useChatUserInfo'
import { useEffect, useRef, useState } from 'react'
// import { useChatting } from '@/hooks/useMultiChatting'
import { useChatStore } from '@/store/chatStore'
import { IoExit } from 'node_modules/react-icons/io5'
import { deleteExitChat } from '@/api/chats'
import { useMultiChatting } from '@/hooks/useMultiChatting'
import { useChatRoomStore } from '@/store/chatRoomStore'

export default function ChatDetail() {
  const { chatId } = useParams()
  const { userInfoData } = useChatUserInfo(chatId)
  // const { sendHandler } = useChatting(chatId)
  const { chatRoom } = useChatRoomStore()
  const { sendHandler } = useMultiChatting(chatRoom)
  const { chats, actions } = useChatStore()

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
    actions.fetchChatData(chatId)
  }, [chatId])

  useEffect(() => {
    if (chatContentRef.current) {
      chatContentRef.current.scrollTop = chatContentRef.current.scrollHeight
    }
  }, [chats])

  return (
    <section className={styles.chatDetailContainer}>
      <div className={styles.contactContainer}>
        <div className={styles.contactLeft}>
          <div className={styles.profileImage}>
            <ProfileImage src={userInfoData.profileImg} />
          </div>
          <div className={styles.profileDetail}>
            <h3>{userInfoData.nickName}</h3>
            <p>나와의 거리 : {userInfoData.betweenDistance}</p>
          </div>
        </div>
        <IoExit onClick={handleExitChat} />
      </div>
      <div
        className={styles.chatContentContainer}
        ref={chatContentRef}>
        {chats.length > 0 &&
          chats.map((chat, index) =>
            chat.nickName === userInfoData.nickName ? (
              <div
                key={index}
                className={styles.in}>
                {chat.message}
              </div>
            ) : (
              <div
                key={index}
                className={styles.out}>
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
