import {
  ChatRoomFirstMsgInfoProps,
  ChatRoomProps,
  ChatRoomReadProps,
  ChatUserInfoProps,
  deleteChatEnter,
  getChatEnter,
  // getChatEnter,
  getChatRoom,
  getChatRoomFirstMsgInfo,
  getChatRoomMsgInfo,
  getChatUserInfo
} from '@/api/chats'
import { create } from 'zustand'
import { devtools } from 'zustand/middleware'

export interface ExtendedChatRoomProps extends ChatRoomProps {
  msgInfo?: ChatRoomFirstMsgInfoProps[]
  chatUserInfo?: ChatUserInfoProps[]
  nowChatId: string | null
}

interface ChatActions {
  initChatRoom: () => Promise<ChatRoomProps[]>
  fetchChatInfo: (chatRoomId: string) => ChatUserInfoProps[]
  fetchChatData: (chatRoomId: string) => Promise<void>
  fetchChatMoreData: (chatRoomId: string) => void
  addMessage: (msg: ChatRoomFirstMsgInfoProps) => void
  readMessage: (chatRooomId: string) => void
  setNowChatId: (id: string |null) => void
}

const initialState: ExtendedChatRoomProps[] = []

export const useChatStore = create<
  { chatRoom: ExtendedChatRoomProps[]; nowChatId: string | null } & { actions: ChatActions }
>()(
  devtools(set => ({
    chatRoom: initialState,
    nowChatId: null,
    actions: {
      initChatRoom: async () => {
        const roomData = await getChatRoom()
        const extendedRoomData: ExtendedChatRoomProps[] = roomData.map(room => ({
          ...room,
          nowChatId: null
        }))
        set(() => ({
          chatRoom: extendedRoomData
        }))
        return extendedRoomData
      },

      // 특정 방의 정보
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

      // 특정 방의 메시지 데이터
      fetchChatData: async (chatRoomId: string) => {
        console.log('chatRoomId : ', chatRoomId)
        // const currentChatRoom = useChatStore
        //   .getState()
        //   .chatRoom.find(room => room.chatRoomId === chatRoomId)
        // if (currentChatRoom?.msgInfo) return

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
                msgInfo: msgInfoWithFlag,
                pageChk: true
              }
            }
            return room
          })
          return { chatRoom: updatedChatRoom }
        })
      },

      // 특정 방의 추가 메시지 데이터
      fetchChatMoreData: async (chatRoomId: string) => {
        const currentChatRoom = useChatStore
          .getState()
          .chatRoom.find(room => room.chatRoomId === chatRoomId)

        if (!currentChatRoom) return

        const currentPage = currentChatRoom.page || 2
        const data = await getChatRoomMsgInfo(chatRoomId, currentPage)
        set(state => {
          if (data.length === 0) {
            const updatedChatRoom = state.chatRoom.map(room => {
              if (room.chatRoomId === chatRoomId) {
                return {
                  ...room,
                  pageChk: false
                }
              }
              return room
            })
            return { chatRoom: updatedChatRoom }
          }
          const updatedChatRoom = state.chatRoom.map(room => {
            if (room.chatRoomId === chatRoomId) {
              return {
                ...room,
                msgInfo: [...data, ...(room.msgInfo || [])],
                page: currentPage + 1,
                pageChk: true
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
          if (index === -1) return state

          const updatedMsgInfo = [...(state.chatRoom[index].msgInfo || []), msg]
          const updatedChatRoom = {
            ...state.chatRoom[index],
            curMsg: msg.message,
            curChatType: msg.chatType,
            curMsgCrDate: msg.msgCrDate,
            msgInfo: updatedMsgInfo
          }
          
          const myNickName = updatedChatRoom.chatUserInfo?.find(user => user.itsMeFlag)?.nickName

          if(myNickName !== updatedChatRoom.opsNickName){
            updatedChatRoom.unreadCount = updatedChatRoom.unreadCount + 1
          }
          const updatedChatRoomList = [
            updatedChatRoom,
            ...state.chatRoom.filter((_, i) => i !== index)
          ]


          return { chatRoom: updatedChatRoomList }
        }),

      // 특정 방 메시지 읽기
      readMessage: (receivedMessage: ChatRoomReadProps) =>
        set(state => {
          const index = state.chatRoom.findIndex(
            room =>
              String(room.chatRoomId) === String(receivedMessage.chatRoomId)
          )
          if (index === -1) return state

          const updatedChatRoom = {
            ...state.chatRoom[index]
          }

          console.log(state.chatRoom[index].opsNickName, receivedMessage.reader)
          if (state.chatRoom[index].opsNickName === receivedMessage.reader) {
            console.log('read')
            updatedChatRoom.unreadCount = 0
            updatedChatRoom.msgInfo = updatedChatRoom?.msgInfo?.map(msg => ({
              ...msg,
              read: true
            }));
          } 
          // else {
          //   console.log('unread')
          //   updatedChatRoom.unreadCount = state.chatRoom[index].unreadCount + 1
          // }

          const updatedChatRoomList = state.chatRoom.map((room, i) =>
            i === index ? updatedChatRoom : room
          )

          return { chatRoom: updatedChatRoomList }
        }),

        setNowChatId: (id: string | null) => 
          set(() => {
            if(id === null){
              deleteChatEnter()
            }else{
              getChatEnter(id)
            }
            return { nowChatId: id }
          })
    }
  }))
)

