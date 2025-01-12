import { getAccessToken } from '@/utils/auth'
import { useState, useEffect, useRef } from 'react'
import { Client } from '@stomp/stompjs'
import { useChatStore } from '@/store/chatStore'

export function useChatting(chatId: string | undefined) {
  const { actions } = useChatStore()
  const [connected, setConnected] = useState(false)
  const client = useRef<Client | null>(null)
  const access = getAccessToken()
  const socketAddress = `${import.meta.env.VITE_WS_API_URL}/chat/ws`
  const sendAddress = `/pub/chat/room/${chatId}`
  const subscribeAddresses = [
    `/queue/${access}/error`,
    `/sub/chat/room/${chatId}`
  ]

  const sendHandler = (
    input: string,
    chatType: 'TEXT' | 'IMG' | 'VIDEO' | 'LINK'
  ) => {
    console.log('client.current : ', client.current)
    console.log('sendAddress : ', sendAddress)
    console.log('access : ', access)
    // if (client.current?.connected) {
    const message = {
      chatType,
      message: input
    }

    client.current.publish({
      destination: sendAddress,
      body: JSON.stringify(message),
      headers: {
        access: access
      }
    })
    // } else {
    //   console.log('sendHandler Else')
    // }
  }

  const connectHandler = () => {
    const socketUrl = `${socketAddress}?access=${access}`

    client.current = new Client({
      brokerURL: socketUrl,
      connectHeaders: {
        access: access
      },
      onConnect: () => {
        console.log('onConnect')

        setConnected(true)
        subscribeAddresses.forEach(address => {
          client.current?.subscribe(
            address,
            message => {
              console.log('message')
              const receivedMessage = JSON.parse(message.body)
              console.log(receivedMessage)
              actions.addMessage(receivedMessage)
            },
            {
              access: `${access}`
            }
          )
        })
      },
      onStompError: frame => {
        console.error('STOMP 오류:', frame)
        setConnected(false)
      },
      onWebSocketClose: () => {
        console.log('onWebSocketClose')
        setConnected(false)
      },
      onWebSocketError: error => {
        console.error('WebSocket 오류:', error)
        if (error instanceof DOMException) {
          console.log('WebSocket 오류 코드:', error.name)
          console.log('WebSocket 오류 메시지:', error.message)
        } else {
          console.log('알 수 없는 WebSocket 오류')
        }
        setConnected(false)
      }
    })

    client.current.activate()
  }

  useEffect(() => {
    console.log(111)
    console.log(client.current?.connected)
    console.log(222)
    return () => {
      if (client.current?.connected) {
        client.current.deactivate()
      }
      connectHandler()
    }
  }, [])

  return { connected, sendHandler }
}
