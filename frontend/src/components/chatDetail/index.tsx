import { useParams, useNavigate } from 'react-router-dom'
import ProfileImage from '../profileImage'
import styles from './chatDetail.module.scss'
import { useEffect, useLayoutEffect, useRef, useState } from 'react'
import { useChatStore } from '@/store/chatStore'
import { IoExit, IoImageSharp, IoArrowBackSharp } from 'react-icons/io5'
import { deleteExitChat } from '@/api/chats'
import { useMultiChatting } from '@/hooks/useMultiChatting'

export default function ChatDetail() {
  const { chatId } = useParams()
  const { sendHandler, sendImage } = useMultiChatting()
  const navigate = useNavigate()
  const { chatRoom, actions } = useChatStore()
  const { fetchChatData, fetchChatMoreData, setNowChatId, deleteChatRoom } = actions
  const [input, setInput] = useState('')
  const chatContentRef = useRef<HTMLDivElement>(null)
  const fileInputRef = useRef<HTMLInputElement>(null)
  const topObserverRef = useRef<HTMLDivElement>(null)
  const [isFetching, setIsFetching] = useState(true)
  const isMobile = window.innerWidth <= 1023

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
    if(!chatId) return
    await deleteExitChat(chatId)
    deleteChatRoom(chatId)
    navigate('/chat')
  }

  const currentChatRoom = chatRoom.find(room => room.chatRoomId === chatId)
  const messages = currentChatRoom?.msgInfo || []
  const myInfo =
    currentChatRoom?.chatUserInfo?.find(user => user.itsMeFlag) || null
  const userInfo =
    currentChatRoom?.chatUserInfo?.find(user => !user.itsMeFlag) || null

  const [previousMessagesCount, setPreviousMessagesCount] = useState(messages.length)

  useEffect(() => {
    if (messages.length === previousMessagesCount + 1) {
      if (chatContentRef.current) {
        chatContentRef.current.scrollTop = chatContentRef.current.scrollHeight
      }
    }
  
    setPreviousMessagesCount(messages.length)
  }, [messages])

  useEffect(() => {
    if (chatId) setNowChatId(chatId?? null)
    return () => setNowChatId(null)
  }, [chatId, setNowChatId])
  
  useLayoutEffect(() => {
    if (!chatId) return
    fetchChatData(chatId).then(() => {
      setIsFetching(false)
      if (chatContentRef.current) {
        chatContentRef.current.scrollTop = chatContentRef.current.scrollHeight
      }
    })
  }, [chatId])

  useEffect(() => {
    if (!chatId || !topObserverRef.current || !chatContentRef.current) return

    const chatContainer = chatContentRef.current
    const previousScrollHeight = chatContainer.scrollHeight

    const observer = new IntersectionObserver(
      async entries => {
        if (
          entries[0].isIntersecting &&
          !isFetching &&
          currentChatRoom?.pageChk
        ) {
          setIsFetching(true)
          await fetchChatMoreData(chatId)

          requestAnimationFrame(() => {
            if (chatContainer) {
              chatContainer.scrollTop =
                chatContainer.scrollHeight - previousScrollHeight
            }
          })

          setIsFetching(false)
        }
      },
      { root: chatContainer, threshold: 0.1 }
    )

    observer.observe(topObserverRef.current)

    return () => observer.disconnect()
  }, [chatId, isFetching])

  if (!currentChatRoom || !userInfo) {
    return null
  }

  return (
    <section className={styles.chatDetailContainer}>
      <div className={styles.contactContainer}>
        <div className={styles.contactLeft}>
          {isMobile && <IoArrowBackSharp onClick={() => navigate('/chat')} />}
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
        <div
          ref={topObserverRef}
          className={styles.observer}></div>
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
          accept="image/*"
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
