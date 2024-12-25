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
  const [subscribeAddresses, setSubscribeAddresses] = useState<string[]>([''])  // 여러 구독 주소를 배열로 관리
  const [input, setInput] = useState('')
  const [connected, setConnected] = useState(false)
  const [token, setToken] = useState('')

  // 메시지 전송 핸들러
  const sendHandler = () => {
    if (client.current?.connected) {
      const message = {
        message: input  // Assuming 'input' is the content you want to send
      };

      // 'client.current' is the STOMP client, use 'client.current.publish()' correctly
      client.current.publish({
        destination: sendAddress,  // The address to send the message to
        body: JSON.stringify(message), // The message content should be serialized to a JSON string
        headers: {
          'access': `${token}`
        }
      });
    }
  }

  // 연결 핸들러
  const connectHandler = () => {
    if (client.current?.connected) return

    // WebSocket URL에 토큰을 쿼리 파라미터로 포함
    const socketUrl = `${socketAddress}?access=${token}`

    client.current = new Client({
      brokerURL: socketUrl, // URL에 쿼리 파라미터로 토큰 포함
      connectHeaders: {
        access: token, // 연결 시 헤더에 토큰 추가
      },
      onConnect: () => {
        setConnected(true)
        // 구독 시작
        subscribeAddresses.forEach((address) => {
          client.current?.subscribe(address, (message) => {
            const receivedMessage = JSON.parse(message.body)
            console.log(`수신된 메시지 (${address}):`, receivedMessage) // 메시지 수신 시 콘솔에 출력
          }, {
            'access': `${token}`
          })
        })
      },
      onStompError: (frame) => {
        console.error('STOMP 오류:', frame);
        
        // Assuming `frame.body` contains the error response as a string
        const response = JSON.parse(frame.body);
        console.log(response); // Log only the message part
        
        setConnected(false);
      },
      onWebSocketClose: () => {
        console.log('WebSocket 연결 종료')
        setConnected(false)
      },
      onWebSocketError: (error) => {
        console.error('WebSocket 오류:', error)
        if (error instanceof DOMException) {
          console.log('WebSocket 오류 코드:', error.name); // 예: "NS_ERROR_CONNECTION_REFUSED"
          console.log('WebSocket 오류 메시지:', error.message);
        } else {
          console.log('알 수 없는 WebSocket 오류');
        }
        setConnected(false)
      },
    })

    client.current.activate() // 연결 활성화
  }

  // 구독 주소 추가 핸들러
  const addSubscribeAddress = () => {
    setSubscribeAddresses((prevAddresses) => [...prevAddresses, '']);
  }

  // 구독 주소 변경 핸들러
  const handleSubscribeAddressChange = (index: number, value: string) => {
    const updatedAddresses = [...subscribeAddresses];
    updatedAddresses[index] = value;
    setSubscribeAddresses(updatedAddresses);
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
      <br />
      <div>
        <p>구독 주소들</p>
        {subscribeAddresses.map((address, index) => (
          <div key={index}>
            <input
              type="text"
              value={address}
              onChange={(e) => handleSubscribeAddressChange(index, e.target.value)}
              placeholder={`구독 주소 ${index + 1}`}
            />
          </div>
        ))}
        <button onClick={addSubscribeAddress}>구독 주소 추가</button>
      </div>
    </>
  )
}
