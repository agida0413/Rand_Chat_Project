import { useState, useRef } from 'react'
import { Client } from '@stomp/stompjs'
import { getAccessToken } from '@/utils/auth'
import { useChatStore } from '@/store/chatStore'

export function useMultiChatting() {
  const { actions } = useChatStore()
  const [connectedRooms, setConnectedRooms] = useState<string[]>([])
  const clients = useRef<Map<string, Client>>(new Map())
  const access = getAccessToken()
  const socketAddress = `${import.meta.env.VITE_WS_API_URL}/chat/ws`

  const connectToRoom = (chatRoomId: string) => {
    if (clients.current.has(chatRoomId)) {
      console.log(`Already connected to room ${chatRoomId}`)
      return
    }

    const socketUrl = `${socketAddress}?access=${access}`
    const client = new Client({
      brokerURL: socketUrl,
      connectHeaders: { access },
      onConnect: () => {
        console.log(`Connected to room ${chatRoomId}`)
        setConnectedRooms(prev => [...prev, chatRoomId])

        client.subscribe(
          `/sub/chat/room/${chatRoomId}`,
          message => {
            const receivedMessage = JSON.parse(message.body)
            actions.addMessage(receivedMessage)
          },
          { access }
        )
      },
      onStompError: frame => {
        console.error(`STOMP Error for room ${chatRoomId}:`, frame)
      },
      onWebSocketClose: () => {
        console.log(`WebSocket closed for room ${chatRoomId}`)
        setConnectedRooms(prev => prev.filter(id => id !== chatRoomId))
        // clients.current.delete(chatRoomId)
      },
      onWebSocketError: error => {
        console.error(`WebSocket error for room ${chatRoomId}:`, error)
      }
    })

    // if (!clients.current.has(chatRoomId)) {
    clients.current.set(chatRoomId, client)
    client.activate()
    // }
  }

  const disconnectFromRoom = (chatRoomId: string) => {
    const client = clients.current.get(chatRoomId)
    if (client) {
      client.deactivate()
      clients.current.delete(chatRoomId)
      setConnectedRooms(prev => prev.filter(id => id !== chatRoomId))
      console.log(`Disconnected from room ${chatRoomId}`)
    }
  }

  const sendHandler = (
    chatRoomId: string | undefined,
    message: string,
    chatType: 'TEXT' | 'IMG' | 'VIDEO' | 'LINK'
  ) => {
    console.log(1)
    if (chatRoomId) {
      const client = clients.current.get(chatRoomId)
      console.log(clients.current)
      if (!client?.connected) {
        console.error(`Client not connected to room ${chatRoomId}`)
        return
      }

      const messagePayload = {
        chatType,
        message
      }

      const sendAddress = `/pub/chat/room/${chatRoomId}`

      client.publish({
        destination: sendAddress,
        body: JSON.stringify(messagePayload),
        headers: { access }
      })
    }
  }

  return { connectedRooms, connectToRoom, disconnectFromRoom, sendHandler }
}
