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
  page: number
  pageChk: boolean
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
  chatType: 'TEXT' | 'IMG' | 'VIDEO' | 'LINK'
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

export const getChatRoomMsgInfo = async (chatRoomId: string, page: number) => {
  const VITE_SSE_API_URL = import.meta.env.VITE_SSE_API_URL
  const url = `${VITE_SSE_API_URL}/chat/api/v1/msg/${chatRoomId}/${page}`

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

export const postChatRoomImage = async (chatRoomId: string, file: File) => {
  // const VITE_SSE_API_URL = import.meta.env.VITE_SSE_API_URL
  // const url = `${VITE_SSE_API_URL}/chat/api/v1/msg/${chatRoomId}/img`

  // const formData = new FormData()
  // formData.append('img', file)

  // const response = await fetch(url, {
  //   method: 'POST',
  //   headers: {
  //     access: `${getAccessToken()}`
  //   },
  //   credentials: 'include',
  //   body: formData
  // })

  // if (!response.ok) {
  //   throw new Error('Failed to upload image')
  // }

  // const { data } = await response.json()

  return {
    imgUrl:
      'https://randchat.s3.ap-northeast-2.amazonaws.com/412d7c2e-5momo.png'
  }
  // return data
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

export interface ChatRoomReadProps {
  reader: string
  readFlag: string
  chatRoomId: string
  type: 'READ-EVENT'
}

export const getChatEnter = async (chatRoomId: string) => {
  console.log('call Enter')
  const VITE_SSE_API_URL = import.meta.env.VITE_SSE_API_URL
  const url = `${VITE_SSE_API_URL}/chat/ws/api/enter/${chatRoomId}`

  await api.get<ChatUserInfoProps[]>(url, {
    headers: {
      Accept: 'application/json',
      access: `${getAccessToken()}`
    },
    credentials: 'include'
  })
}
