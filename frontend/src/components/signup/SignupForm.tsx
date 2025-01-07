import { useSignupStore } from '@/store/signupStore'
import styles from './SignupForm.module.scss'
import { GENDER_DATA, SIGN_INPUT_LIST, SignInputListProps } from '@/constants'

export default function SignupForm() {
  const user = useSignupStore(state => state.user)
  const { setUser } = useSignupStore()

  return (
    <>
      <div className={styles.flexForm}>
        <div className={styles.genderWrapper}>
          성별
          {GENDER_DATA.map(({ label, value }) => (
            <label key={value}>
              <input
                type="radio"
                name="gender"
                value={value}
                data-label={label}
                checked={value === user.gender}
                className={styles.radioGroup}
                onChange={() => setUser({ gender: value })}
              />
            </label>
          ))}
        </div>
        <div className={styles.birthDayWrapper}>
          생년월일
          <input
            type="date"
            value={user.birthDay}
            onChange={e => setUser({ birthDay: e.target.value })}
          />
        </div>
      </div>

      {SIGN_INPUT_LIST.map(({ key, placeholder, type }: SignInputListProps) => (
        <input
          key={key}
          className={styles.inputForm}
          value={user[key as keyof typeof user]}
          type={type}
          placeholder={placeholder}
          onChange={e => setUser({ [key]: e.target.value })}
        />
      ))}
    </>
  )
}
