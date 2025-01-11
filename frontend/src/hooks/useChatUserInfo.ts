import { ChatUserInfoProps, getChatUserInfo } from '@/api/chats'
import { useState, useEffect } from 'react'

export function useChatUserInfo(chatRoomId: string | undefined) {
  const initInfo: ChatUserInfoProps = {
    nickName: '',
    sex: '',
    manAge: '',
    profileImg: '',
    itsMeFlag: false,
    betweenDistance: '0.0'
  }
  const [userInfoData, setUserInfoData] = useState<ChatUserInfoProps>(initInfo)
  const [userInfoLoading, setUserInfoLoading] = useState<boolean>(false)
  const [userInfoError, setUserInfoError] = useState<Error | null>(null)

  useEffect(() => {
    if (!chatRoomId) return

    const fetchUserInfo = async () => {
      setUserInfoLoading(true)
      try {
        const res = await getChatUserInfo(chatRoomId)
        const other = res.filter(userInfo => userInfo.itsMeFlag === false)
        setUserInfoData(other[0])
      } catch (err) {
        setUserInfoError(err as Error)
      } finally {
        setUserInfoLoading(false)
      }
    }

    fetchUserInfo()
  }, [chatRoomId])

  return { userInfoData, userInfoLoading, userInfoError }
}
