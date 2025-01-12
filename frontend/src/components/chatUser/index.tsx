import styles from './chatUser.module.scss'
import ProfileImage from '../profileImage'
import { ChatRoomProps } from '@/api/chats'

interface ChatUserProps {
  room: ChatRoomProps
}

function formatDate(dateString: string | undefined): string {
  if (!dateString) return ''

  const date = new Date(dateString)
  const now = new Date()

  const isSameDay = date.toDateString() === now.toDateString()
  const isSameYear = date.getFullYear() === now.getFullYear()

  if (isSameDay) {
    return date.toLocaleTimeString('en-US', {
      hour: '2-digit',
      minute: '2-digit',
      hour12: false
    })
  } else if (isSameYear) {
    return `${date.getMonth() + 1}/${date.getDate()}`
  } else {
    return `${date.getFullYear()}/${date.getMonth() + 1}/${date.getDate()}`
  }
}

export default function ChatUser({ room }: ChatUserProps) {
  const curMsg =
    room.curChatType === 'TEXT' || room.curChatType === 'LINK'
      ? room.curMsg || ''
      : room.curChatType === 'VIDEO'
        ? '비디오'
        : room.curChatType === 'IMG'
          ? '사진'
          : ''

  const formattedDate = formatDate(room.curMsgCrDate)
  return (
    <div className={styles.peopleContent}>
      <div className={styles.profileImage}>
        <ProfileImage src={room.opsProfileImg} />
      </div>
      <div className={styles.profileDetail}>
        <div className={styles.left}>
          <h3>{room.opsNickName}</h3>
          <h5>{curMsg}</h5>
        </div>
        <div className={styles.right}>
          <p>{formattedDate}</p>
          <p>{room.unreadCount}</p>
        </div>
      </div>
    </div>
  )
}
