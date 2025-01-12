import styles from './chatUser.module.scss'
import ProfileImage from '../profileImage'
import { ChatRoomProps } from '@/api/chats'

interface ChatUserProps {
  room: ChatRoomProps
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
  return (
    <div className={styles.peopleContent}>
      <div className={styles.profileImage}>
        <ProfileImage src={room.opsProfileImg} />
      </div>
      <div className={styles.profileDetail}>
        <div className={styles.left}>
          <h3>{room.opsNickName}</h3>
          <h5>{curMsg}</h5>
          <h5>{room.curChatType}</h5>
        </div>
        <div className={styles.right}>
          <p>{room.curMsgCrDate}</p>
          <p>v</p>
        </div>
      </div>
    </div>
  )
}
