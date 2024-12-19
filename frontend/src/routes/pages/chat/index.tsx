import SockJS from 'sockjs-client'
import { CompatClient, Stomp } from '@stomp/stompjs'
import { useEffect, useRef, useState } from 'react'
import { getAccessToken } from '@/utils/auth'

interface ChatMessage {
  type: 'TALK'
  roomId: string
  sender: string
  message: string
}

interface CustomError {
  message: string
  code?: number
}

export default function Chat() {
  const client = useRef<CompatClient>()
  const [connected, setConnected] = useState(false)
  const token = getAccessToken()

  const sendHandler = () => {
    if (!client.current?.connected) return

    const message: ChatMessage = {
      type: 'TALK',
      roomId: '1',
      sender: 'test',
      message: 'input'
    }

    client.current.send('/pub/chat/message', {}, JSON.stringify(message))
  }

  const connectHandler = () => {
    if (client.current?.connected) return

    client.current = Stomp.over(() => {
      const sock = new SockJS('http://localhost:8080/ws-stomp')
      return sock
    })

    client.current.connect(
      {
        Authorization: token
      },
      () => {
        setConnected(true)
        client.current!.subscribe(`/sub/chat/room/1`, message => {
          const receivedMessage = JSON.parse(message.body)
          console.log(receivedMessage)
        })
      },
      (error: CustomError) => {
        console.error('STOMP 연결 에러:', error)
        setConnected(false)
      }
    )
  }

  useEffect(() => {
    return () => {
      if (client.current?.connected) {
        client.current.disconnect()
      }
    }
  }, [])

  return (
    <>
      <div>chat</div>
      <button
        onClick={connectHandler}
        disabled={connected}>
        {connected ? '연결됨' : '연결하기'}
      </button>
      <button
        onClick={sendHandler}
        disabled={!connected}>
        메시지 보내기
      </button>
    </>
  )
}
