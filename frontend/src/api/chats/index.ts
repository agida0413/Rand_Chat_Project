import { getAccessToken } from '@/utils/auth'
import { api } from '..'

export interface ChatRoomProps {
  chatRoomId: string
  opsProfileImg: string
  opsNickName: string
  curMsg: string
  curMsgCrDate: string
  curChatType: 'TEXT' | 'IMG' | 'VIDEO' | 'LINK'
  unreadCount: number
  roomState: 'ACTIVE' | 'INACTIVE'
  abNormalFlag: boolean
}

export const getChatRoom = async () => {
  const VITE_SSE_API_URL = import.meta.env.VITE_SSE_API_URL
  const url = `${VITE_SSE_API_URL}/chat/api/v1/room`
  const res = await api.get<ChatRoomProps[]>(url, {
    headers: {
      Accept: 'application/json',
      access: `${getAccessToken()}`
    },
    credentials: 'include'
  })

  return res.data.data
}

export interface ChatUserInfoProps {
  nickName: string
  sex: '남자' | '여자' | ''
  manAge: string
  profileImg: string
  itsMeFlag: boolean
  betweenDistance: string
}

export const getChatUserInfo = async (chatRoomId: string) => {
  const VITE_SSE_API_URL = import.meta.env.VITE_SSE_API_URL
  const url = `${VITE_SSE_API_URL}/chat/api/v1/room/${chatRoomId}`

  const res = await api.get<ChatUserInfoProps[]>(url, {
    headers: {
      Accept: 'application/json',
      access: `${getAccessToken()}`
    },
    credentials: 'include'
  })

  return res.data.data
}

export interface ChatRoomFirstMsgInfoProps {
  chatRoomId: string
  message: string
  msgCrDateMs: string
  msgCrDate: string
  read: boolean
  curChatType: 'TEXT' | 'IMG' | 'VIDEO' | 'LINK'
  nickName: string
}

export const getChatRoomFirstMsgInfo = async (chatRoomId: string) => {
  const VITE_SSE_API_URL = import.meta.env.VITE_SSE_API_URL
  const url = `${VITE_SSE_API_URL}/chat/api/v1/msg/${chatRoomId}`

  const res = await fetch(url, {
    method: 'GET',
    headers: {
      Accept: 'application/json',
      access: `${getAccessToken()}`
    },
    credentials: 'include'
  })

  if (!res.ok) {
    if (res.status === 400) {
      return []
    }
    throw new Error('서버에서 데이터를 가져오는데 실패했습니다.')
  }

  const data = await res.json()
  return data.data as ChatRoomFirstMsgInfoProps[]
}

export const deleteExitChat = async (chatRoomId: string | undefined) => {
  const VITE_SSE_API_URL = import.meta.env.VITE_SSE_API_URL
  const url = `${VITE_SSE_API_URL}/chat/api/v1/room/${chatRoomId}`

  const res = await api.delete<ChatUserInfoProps[]>(url, {
    headers: {
      Accept: 'application/json',
      access: `${getAccessToken()}`
    },
    credentials: 'include'
  })

  return res
}
