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

export interface ExtendedChatRoomProps extends ChatRoomProps {
  msgInfo?: ChatRoomFirstMsgInfoProps[]
  chatUserInfo?: ChatUserInfoProps[]
}

interface ChatActions {
  initChatRoom: () => Promise<ChatRoomProps[]>
  fetchChatInfo: (chatRoomId: string) => ChatUserInfoProps[]
  fetchChatData: (chatRoomId: string) => Promise<void>
  addMessage: (msg: ChatRoomFirstMsgInfoProps) => void
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
              const msgInfoWithFlag = reverseData.map(msg => ({
                ...msg
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
      },

      // 특정 방에 메시지 추가
      addMessage: (msg: ChatRoomFirstMsgInfoProps) =>
        set(state => {
          const index = state.chatRoom.findIndex(
            room => String(room.chatRoomId) === String(msg.chatRoomId)
          )
          if (index === -1) {
            console.log('index 찾기 실패')
            return state
          }

          const updatedMsgInfo = [...(state.chatRoom[index].msgInfo || []), msg]

          const updatedChatRoom = {
            ...state.chatRoom[index],
            curMsg: msg.message,
            curChatType: msg.curChatType,
            curMsgCrDate: msg.msgCrDate,
            msgInfo: updatedMsgInfo
          }

          const updatedChatRoomList = [
            updatedChatRoom,
            ...state.chatRoom.filter((_, i) => i !== index)
          ]

          return { chatRoom: updatedChatRoomList }
        })
    }
  }))
)
