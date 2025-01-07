import styles from './Brand.module.scss'

export default function Brand() {
  return (
    <article className={styles.brandSection}>
      <header>
        <h1>Rand Chat</h1>
        <p>
          내 주변의 특별한 인연을 만나는 새로운 방식, 우연이 필연이 되는 순간
        </p>
      </header>
    </article>
  )
}
