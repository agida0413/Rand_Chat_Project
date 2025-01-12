import { ChatRoomFirstMsgInfoProps, getChatRoomFirstMsgInfo } from '@/api/chats'
import { create } from 'zustand'
import {
  // combine,
  devtools
  // persist
} from 'zustand/middleware'

export interface ChatFirstMsgInfoProps {
  chatRoomId: string
  message: string
  msgCrDateMs: string
  msgCrDate: string
  read: boolean
  curChatType: 'TEXT' | 'IMG' | 'VIDEO' | 'LINK'
  nickname: string // nickName, nickname 이름 이슈 있음음
}

interface ChatActions {
  resetState: () => void
  addMessage: (msg: ChatFirstMsgInfoProps) => void
  fetchChatData: (chatRoomId: string) => Promise<void>
}

const initialState: ChatRoomFirstMsgInfoProps[] = []

export const useChatStore = create<
  { chats: ChatRoomFirstMsgInfoProps[] } & { actions: ChatActions }
>()(
  devtools(set => ({
    chats: initialState,
    actions: {
      resetState: () =>
        set(() => ({
          chats: []
        })),
      addMessage: msg =>
        set(state => ({
          chats: [
            ...state.chats,
            {
              ...msg,
              nickName: msg.nickname
            }
          ]
        })),
      fetchChatData: async (chatRoomId: string) => {
        const data: ChatRoomFirstMsgInfoProps[] =
          await getChatRoomFirstMsgInfo(chatRoomId)

        console.log(data)
        const reverseData = data.reverse()
        set(() => ({
          chats: reverseData
        }))
      }
    }
  }))
)
