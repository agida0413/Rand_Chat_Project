import {
  ChatRoomFirstMsgInfoProps,
  ChatRoomProps,
  ChatUserInfoProps,
  getChatRoom,
  getChatRoomFirstMsgInfo,
  getChatUserInfo
} from '@/api/chats'
import { create } from 'zustand'
import { devtools } from 'zustand/middleware'

export interface ExtendedChatMsgInfo extends ChatRoomFirstMsgInfoProps {
  itsMeFlag?: boolean
}

export interface ExtendedChatRoomProps extends ChatRoomProps {
  msgInfo?: ExtendedChatMsgInfo[]
  chatUserInfo?: ChatUserInfoProps[]
}

interface ChatActions {
  initChatRoom: () => Promise<ChatRoomProps[]>
  fetchChatInfo: (chatRoomId: string) => ChatUserInfoProps[]
  fetchChatData: (chatRoomId: string) => Promise<void>
  addMessage: (msg: ExtendedChatMsgInfo, chatRoomId: string) => void
}

const initialState: ExtendedChatRoomProps[] = []

export const useChatStore = create<
  { chatRoom: ExtendedChatRoomProps[] } & { actions: ChatActions }
>()(
  devtools(set => ({
    chatRoom: initialState,
    actions: {
      initChatRoom: async () => {
        const roomData = await getChatRoom()

        set(() => ({
          chatRoom: roomData
        }))

        return roomData
      },

      fetchChatInfo: async (chatRoomId: string) => {
        const currentChatRoom = useChatStore
          .getState()
          .chatRoom.find(room => room.chatRoomId === chatRoomId)
        if (currentChatRoom?.chatUserInfo) return

        const userInfo: ChatUserInfoProps[] = await getChatUserInfo(chatRoomId)

        set(state => {
          const updatedChatRoom = state.chatRoom.map(room => {
            if (room.chatRoomId === chatRoomId) {
              return {
                ...room,
                chatUserInfo: userInfo
              }
            }
            return room
          })
          return { chatRoom: updatedChatRoom }
        })
      },

      // 특정 방에 메시지 추가
      addMessage: (msg: ExtendedChatMsgInfo, chatRoomId: string) =>
        set(state => {
          const updatedChatRoom = state.chatRoom.map(room => {
            if (room.chatRoomId === chatRoomId) {
              return {
                ...room,
                msgInfo: room.msgInfo
                  ? [...room.msgInfo, { ...msg, itsMeFlag: true }]
                  : [{ ...msg, itsMeFlag: true }]
              }
            }
            return room
          })
          return { chatRoom: updatedChatRoom }
        }),

      // 특정 방의 메시지 데이터를 가져옴
      fetchChatData: async (chatRoomId: string) => {
        const currentChatRoom = useChatStore
          .getState()
          .chatRoom.find(room => room.chatRoomId === chatRoomId)
        if (currentChatRoom?.msgInfo) return

        const data: ChatRoomFirstMsgInfoProps[] =
          await getChatRoomFirstMsgInfo(chatRoomId)

        const reverseData = data.reverse()

        set(state => {
          const updatedChatRoom = state.chatRoom.map(room => {
            if (room.chatRoomId === chatRoomId) {
              // chatUserInfo에서 나의 사용자 정보를 가져옵니다
              const myNickName = room.chatUserInfo?.[0]?.nickName

              // 메시지에 itsMeFlag를 설정합니다
              const msgInfoWithFlag = reverseData.map(msg => ({
                ...msg,
                itsMeFlag: msg.nickName === myNickName
              }))

              return {
                ...room,
                msgInfo: msgInfoWithFlag
              }
            }
            return room
          })
          return { chatRoom: updatedChatRoom }
        })
      }
    }
  }))
)
