import { useEffect, useRef, useState } from 'react'
import { Client } from '@stomp/stompjs'

interface ChatMessage {
  type: 'TALK'
  sender: string
  message: string
}

export default function Chat() {
  const client = useRef<Client | null>(null)
  const [socketAddress, setSocketAddress] = useState('')
  const [sendAddress, setSendAddress] = useState('')
  const [subscribeAddress, setSubscribeAddress] = useState('')
  const [input, setInput] = useState('')
  const [connected, setConnected] = useState(false)
  const [token, setToken] = useState('')
  
  // 메시지 전송 핸들러
  const sendHandler = () => {
    if (client.current?.connected) {
      // const message: ChatMessage = {
      //   type: 'TALK',
      //   sender: '테스트유저',
      //   message: input
      // }

      // 'client.current' is the STOMP client, use 'client.current.send()' correctly
      client.current.publish({
        destination: sendAddress,  // The address to send the message to
        body: input, // The message content
      })
    }
  }

  // 연결 핸들러
  const connectHandler = () => {
    if (client.current?.connected) return

    // WebSocket URL에 토큰을 쿼리 파라미터로 포함
    const socketUrl = `${socketAddress}?access_token=${token}`

    client.current = new Client({
      brokerURL: socketUrl, // URL에 쿼리 파라미터로 토큰 포함
      onConnect: () => {
        setConnected(true)
        // 구독 시작
        client.current?.subscribe(subscribeAddress, (message) => {
          const receivedMessage = JSON.parse(message.body)
          console.log('수신된 메시지:', receivedMessage) // 메시지 수신 시 콘솔에 출력
        })
      },
      onStompError: (frame) => {
        console.error('STOMP 오류:', frame)
        setConnected(false)
      },
      onWebSocketClose: () => {
        console.log('WebSocket 연결 종료')
        setConnected(false)
      },
      onWebSocketError: (error) => {
        console.error('WebSocket 오류:', error)
        setConnected(false)
      },
    })

    client.current.activate() // 연결 활성화
  }

  useEffect(() => {
    return () => {
      if (client.current?.connected) {
        client.current.deactivate() // 연결 종료
      }
    }
  }, [])

  return (
    <>
      <div>
        <p>토큰</p>
        <input
          onChange={(e) => setToken(e.target.value)}
          value={token}
        />
      </div>
      <br />
      <div>
        <p>구독 주소</p>
        <input
          onChange={(e) => setSubscribeAddress(e.target.value)}
          value={subscribeAddress}
        />
      </div>
      <br />
      <div>
        <p>웹소켓 주소 (ex ws://주소)</p>
        <input
          onChange={(e) => setSocketAddress(e.target.value)}
          value={socketAddress}
        />
      </div>
      <br />
      <div>
        <p>send 주소</p>
        <input
          onChange={(e) => setSendAddress(e.target.value)}
          value={sendAddress}
        />
      </div>
      <br />
      <button onClick={connectHandler} disabled={connected}>
        {connected ? '연결됨' : '연결하기'}
      </button>
      <br />
      <br />
      <div>
        <p>메시지 보내기</p>
        <input
          onChange={(e) => setInput(e.target.value)}
          value={input}
        />
        <button onClick={sendHandler} disabled={!connected}>
          메시지 보내기
        </button>
      </div>
    </>
  )
}
