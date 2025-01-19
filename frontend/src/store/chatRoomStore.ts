import { ChatRoomProps, getChatRoom } from '@/api/chats'
import { create } from 'zustand'
import { devtools } from 'zustand/middleware'

interface ChatRoomActions {
  resetState: () => void
  fetchChatData: () => Promise<ChatRoomProps[]>
}

const initialState: ChatRoomProps[] = []

export const useChatRoomStore = create<
  { chatRoom: ChatRoomProps[] } & { actions: ChatRoomActions }
>()(
  devtools(set => ({
    chatRoom: initialState,
    actions: {
      resetState: () =>
        set(() => ({
          chatRoom: []
        })),
      fetchChatData: async () => {
        const roomData = await getChatRoom()

        set(() => ({
          chatRoom: roomData
        }))

        return roomData
      }
    }
  }))
)
