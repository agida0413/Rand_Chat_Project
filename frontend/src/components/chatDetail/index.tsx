import { useParams, useNavigate } from 'react-router-dom'
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
  const navigate = useNavigate()
  const { chatRoom, actions } = useChatStore()
  const { fetchChatData, fetchChatInfo } = actions
  const [input, setInput] = useState('')
  const chatContentRef = useRef<HTMLDivElement>(null)

  const handleSendMsg = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Enter') {
      e.preventDefault()
      sendHandler(chatId, input, 'TEXT')
      setInput('')
    }
  }

  const handleExitChat = async () => {
    await deleteExitChat(chatId)
    navigate('/chat')
  }

  const currentChatRoom = chatRoom.find(room => room.chatRoomId === chatId)
  const messages = currentChatRoom?.msgInfo || []
  const myInfo =
    currentChatRoom?.chatUserInfo?.find(user => user.itsMeFlag) || null
  const userInfo =
    currentChatRoom?.chatUserInfo?.find(user => !user.itsMeFlag) || null
  useEffect(() => {
    if (chatContentRef.current) {
      chatContentRef.current.scrollTop = chatContentRef.current.scrollHeight
    }
  }, [chatId, chatRoom])

  useEffect(() => {
    if (!chatId) return
    fetchChatInfo(chatId)
    fetchChatData(chatId)
  }, [currentChatRoom, userInfo, navigate])

  if (!currentChatRoom || !userInfo) {
    return null
  }

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
