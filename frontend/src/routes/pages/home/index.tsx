import { useMatchConnection } from '@/hooks/useMatchMutation'
import styles from './home.module.scss'
import { useState } from 'react'
import Slider from '@/components/slider'
import { getMatch } from '@/api/match'

export default function Home() {
  const [sliderValue, setSliderValue] = useState(0.1)

  const handleSliderChange = (value: number) => {
    setSliderValue(value)
  }

  const { startMatchConnection } = useMatchConnection()
  const handleMatch = async () => {
    startMatchConnection()
    await getMatch({ distance: sliderValue })
  }

  return (
    <div className={styles.homeContainer}>
      <div className={styles.checkContainer}>
        <div className={styles.caption}>
          <h1>확인해주세요!</h1>
          <div>
            <li>안전하고 즐거운 대화를 위해 개인 정보는 공유하지 마세요!</li>
            <li>
              상대방을 존중하며 대화하세요. 불편한 내용은 신고 버튼을 눌러
              주세요.
            </li>
            <li>
              랜덤채팅은 일회성 대화입니다. 중요한 정보를 공유하지 않도록
              주의하세요.
            </li>
            <li>
              본 플랫폼은 익명성을 보장하지만, 법적으로 문제가 되는 행위는
              책임을 물을 수 있습니다.
            </li>
          </div>
        </div>
        <div className={styles.sliderContainer}>
          <p>매칭 거리</p>
          <Slider
            min={0.1}
            max={100.1}
            step={1}
            initialValue={sliderValue}
            onChange={handleSliderChange}
          />
        </div>
        <div>
          <button onClick={handleMatch}>랜덤매칭</button>
        </div>
      </div>
    </div>
  )
}
