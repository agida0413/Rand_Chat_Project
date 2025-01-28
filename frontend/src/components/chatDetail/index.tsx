import { useParams, useNavigate } from 'react-router-dom'
import ProfileImage from '../profileImage'
import styles from './chatDetail.module.scss'
import { useEffect, useRef, useState } from 'react'
import { useChatStore } from '@/store/chatStore'
import { IoExit, IoImageSharp } from 'react-icons/io5'
import { deleteExitChat, getChatEnter } from '@/api/chats'
import { useMultiChatting } from '@/hooks/useMultiChatting'

export default function ChatDetail() {
  const { chatId } = useParams()
  const { sendHandler, sendImage } = useMultiChatting()
  const navigate = useNavigate()
  const { chatRoom, actions } = useChatStore()
  const { fetchChatData } = actions
  const [input, setInput] = useState('')
  const chatContentRef = useRef<HTMLDivElement>(null)
  const fileInputRef = useRef<HTMLInputElement>(null)

  const handleSendMsg = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Enter') {
      e.preventDefault()
      sendHandler(chatId, input, 'TEXT')
      setInput('')
    }
  }

  const handleSendImage = () => {
    fileInputRef.current?.click()
  }

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0]
    if (file && chatId) {
      sendImage(chatId, file)
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
    getChatEnter(chatId)
    fetchChatData(chatId)
  }, [chatId])

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
                className={styles.outContainer}
                key={index}>
                <div className={styles.outInfo}>
                  <p className={styles.outRead}>
                    {chat.read ? <>&nbsp;</> : '1'}
                  </p>
                  <p className={styles.outDate}>
                    {/* 시간 추후 수정 */}
                    {chat.msgCrDate.split(' ')[1]}
                  </p>
                </div>

                {chat.chatType === 'IMG' ? (
                  <div className={styles.outImageContainer}>
                    <img src={chat.message} />
                  </div>
                ) : (
                  <div className={styles.out}>{chat.message}</div>
                )}
              </div>
            ) : (
              <div
                className={styles.inContainer}
                key={index}>
                {chat.chatType === 'IMG' ? (
                  <div className={styles.inImageContainer}>
                    <img src={chat.message} />
                  </div>
                ) : (
                  <div className={styles.in}>{chat.message}</div>
                )}
                <div className={styles.inInfo}>
                  <p className={styles.inRead}>&nbsp;</p>
                  <p className={styles.inDate}>
                    {/* 시간 추후 수정 */}
                    {chat.msgCrDate.split(' ')[1]}
                  </p>
                </div>
              </div>
            )
          )}
      </div>
      <div className={styles.chatInputContainer}>
        <input
          type="file"
          ref={fileInputRef}
          style={{ display: 'none' }}
          onChange={handleFileChange}
        />
        <IoImageSharp onClick={handleSendImage} />
        <input
          type="text"
          className={styles.chatInput}
          value={input}
          onChange={e => setInput(e.target.value)}
          onKeyUp={handleSendMsg}
        />
      </div>
    </section>
  )
}
