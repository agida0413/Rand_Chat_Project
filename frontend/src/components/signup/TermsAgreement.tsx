import styles from './TermsAgreement.module.scss'
import { TERM_DATA, TermDataProps } from '@/constants'
import { useSignupStore } from '@/store/signupStore'

export default function TermsAgreement() {
  const { isAllChecked, checkedItems } = useSignupStore(state => state.auth)
  const { handleAllCheck, handleSingleCheck } = useSignupStore()

  return (
    <div className={styles.termsAgreementForm}>
      <span>
        <h2>이용약관 전체동의</h2>
        <input
          type="checkbox"
          checked={isAllChecked}
          onChange={e => handleAllCheck(e.target.checked)}
        />
      </span>

      <div>
        {TERM_DATA.map(
          ({ title, required, content }: TermDataProps, index: number) => (
            <details key={index}>
              <summary>
                <p>
                  {title} {required && '(필수)'}
                </p>
                <input
                  type="checkbox"
                  checked={checkedItems[index]}
                  onChange={e => handleSingleCheck(index, e.target.checked)}
                />
              </summary>
              <p className={styles.contents}>{content}</p>
            </details>
          )
        )}
      </div>
    </div>
  )
}
