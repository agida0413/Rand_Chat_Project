import React, { useState } from 'react'
import styles from './slider.module.scss'

interface SliderProps {
  min: number
  max: number
  step: number
  initialValue: number
  onChange: (value: number) => void
}

const Slider: React.FC<SliderProps> = ({
  min,
  max,
  step,
  initialValue,
  onChange
}) => {
  const [value, setValue] = useState(initialValue)
  const displayedValue = value === 0.1 ? 0.1 : Math.floor(value)
  const displayedMax = Math.floor(max)

  const handleSliderChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const newValue = Number(event.target.value)
    setValue(newValue)
    onChange(newValue)
  }

  return (
    <div className={styles.slider}>
      <input
        type="range"
        min={min}
        max={max}
        step={step}
        value={value}
        onChange={handleSliderChange}
      />
      <div>
        <span>{min}</span>
        <span>{displayedValue}km</span>
        <span>{displayedMax}</span>
      </div>
    </div>
  )
}

export default Slider
