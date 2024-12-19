import SockJS from 'sockjs-client'
import { CompatClient, Stomp } from '@stomp/stompjs'
import { useEffect, useRef, useState } from 'react'

interface ChatMessage {
  type: 'TALK'
  sender: string
  message: string
}

interface CustomError {
  message: string
  code?: number
}

export default function Chat() {
  const client = useRef<CompatClient>()
  const [socketAddress, setSocketAddress] = useState('')
  const [sendAddress, setSendAddress] = useState('')
  const [subscribeAddress, setSubscribeAddress] = useState('')
  const [input, setInput] = useState('')
  const [connected, setConnected] = useState(false)
  const [token, setToken] = useState('')

  const sendHandler = () => {
    if (!client.current?.connected) return

    const message: ChatMessage = {
      type: 'TALK',
      sender: '테스트유저',
      message: input
    }

    client.current.send(sendAddress, {}, JSON.stringify(message))
  }

  const connectHandler = () => {
    if (client.current?.connected) return

    client.current = Stomp.over(() => {
      const sock = new SockJS(socketAddress)
      return sock
    })

    client.current.connect(
      {
        access: token
      },
      () => {
        setConnected(true)
        client.current!.subscribe(subscribeAddress, message => {
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
      <div>
        <p>토큰</p>
        <input onChange={e => setToken(e.target.value)} />
      </div>
      <br />
      <div>
        <p>구독 주소</p>
        <input onChange={e => setSubscribeAddress(e.target.value)} />
      </div>
      <br />
      <div>
        <p>SockJS 주소 (ex ws: ~~)</p>
        <input onChange={e => setSocketAddress(e.target.value)} />
      </div>
      <br />
      <div>
        <p>send 주소</p>
        <input onChange={e => setSendAddress(e.target.value)} />
      </div>
      <br />
      <button
        onClick={connectHandler}
        disabled={connected}>
        {connected ? '연결됨' : '연결하기'}
      </button>
      <br />
      <br />
      <div>
        <p>메시지 보내기</p>
        <input onChange={e => setInput(e.target.value)} />
        <button
          onClick={sendHandler}
          disabled={!connected}>
          메시지 보내기
        </button>
      </div>
    </>
  )
}
