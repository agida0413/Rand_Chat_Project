import { useChatClientStore } from '@/store/chatClientStore'
import { useChatStore } from '@/store/chatStore'
import { getAccessToken } from '@/utils/auth'
import { Client } from '@stomp/stompjs'

export function useMultiChatting() {
  const {
    clients,
    connectedRooms,
    addClient,
    removeClient,
    addRoom,
    removeRoom
  } = useChatClientStore()

  const { actions } = useChatStore()

  const access = getAccessToken()
  const socketAddress = `${import.meta.env.VITE_WS_API_URL}/chat/ws`

  const connectToRoom = (chatRoomId: string) => {
    if (clients.has(chatRoomId)) {
      console.log(`Already connected to room ${chatRoomId}`)
      return
    }

    const socketUrl = `${socketAddress}?access=${access}`
    const client = new Client({
      brokerURL: socketUrl,
      connectHeaders: { access },
      onConnect: () => {
        console.log(`Connected to room ${chatRoomId}`)
        addClient(chatRoomId, client)
        addRoom(chatRoomId)

        client.subscribe(
          `/sub/chat/room/${chatRoomId}`,
          message => {
            const receivedMessage = JSON.parse(message.body)
            console.log('receivedMessage: ', receivedMessage)
            if (receivedMessage.type === 'READ-EVENT') {
              // actions.readMessage(receivedMessage)
            } else {
              const fixedMessage = {
                ...receivedMessage,
                nickName: receivedMessage.nickname,
                curChatType: receivedMessage.chatType,
                chatRoomId: receivedMessage.roomId
              }
              delete fixedMessage.nickname
              actions.addMessage(fixedMessage)
            }
          },
          { access }
        )
      },
      onStompError: frame => {
        console.error(`STOMP Error for room ${chatRoomId}:`, frame)
      },
      onWebSocketClose: () => {
        console.log(`WebSocket closed for room ${chatRoomId}`)
        disconnectFromRoom(chatRoomId)
      },
      onWebSocketError: error => {
        console.error(`WebSocket error for room ${chatRoomId}:`, error)
      }
    })

    client.activate()
  }

  const disconnectFromRoom = (chatRoomId: string) => {
    const client = clients.get(chatRoomId)
    if (client) {
      client.deactivate()
      removeClient(chatRoomId)
      removeRoom(chatRoomId)
      console.log(`Disconnected from room ${chatRoomId}`)
    } else {
      console.warn(`No client found for room ${chatRoomId}`)
    }
  }

  const sendHandler = (
    chatRoomId: string | undefined,
    message: string,
    chatType: 'TEXT' | 'IMG' | 'VIDEO' | 'LINK'
  ) => {
    if (!chatRoomId) {
      console.error('ChatRoom ID is undefined')
      return
    }

    const client = clients.get(chatRoomId)
    if (!client?.connected) {
      console.error(`Client not connected to room ${chatRoomId}`)
      return
    }

    const messagePayload = {
      chatType,
      message
    }

    const sendAddress = `/pub/chat/room/${chatRoomId}`
    console.log('messagePayload : ', messagePayload)

    client.publish({
      destination: sendAddress,
      body: JSON.stringify(messagePayload),
      headers: { access }
    })
  }

  return { connectedRooms, connectToRoom, disconnectFromRoom, sendHandler }
}
