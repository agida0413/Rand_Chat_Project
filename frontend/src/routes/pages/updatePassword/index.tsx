import { useState } from 'react'
import styles from './updatePassword.module.scss'
import { putMemberPwd } from '@/api/login'
import { notify } from '@/utils/toast'
import { useNavigate } from 'react-router-dom'

export default function UpdatePassword() {
  const navigate = useNavigate()

  const [password, setPassword] = useState('')
  const [newPassword, setNewPassword] = useState('')
  const [chkPassword, setChkPassword] = useState('')

  const validatePassword = (password: string) => {
    if (/\s/.test(password)) {
      return '비밀번호에 공백을 포함할 수 없습니다.'
    }

    if (password.length < 8 || password.length > 16) {
      return '비밀번호는 8자 이상, 16자 이하로 입력해야 합니다.'
    }

    if (
      !/[a-zA-Z]/.test(password) ||
      !/[0-9]/.test(password) ||
      !/[!@#$%^&*(),.?":{}|<>]/.test(password)
    ) {
      return '비밀번호는 하나 이상의 문자, 숫자, 특수문자를 포함해야 합니다.'
    }

    return ''
  }

  const handleMemberPwd = async () => {
    if (password.length === 0) {
      notify('error', '현재 비밀번호를 입력하여주세요.')
    } else if (newPassword.length === 0) {
      notify('error', '새 비밀번호를 입력하여주세요.')
    } else {
      const passwordValidationError = validatePassword(newPassword)
      if (passwordValidationError) {
        notify('error', passwordValidationError)
        return
      }

      if (newPassword !== chkPassword) {
        notify('error', '새 비밀번호가 동일하지 않습니다.')
      } else {
        const res = await putMemberPwd(password, newPassword)
        if (res.status === 400) {
          notify('error', res.message)
        } else if (res.status === 200) {
          navigate('/')
          notify('success', '비밀번호가 변경 되었습니다.')
        }
      }
    }
  }

  return (
    <div className={styles.settingPassword}>
      <div className={styles.PasswordContainer}>
        <h1>비밀번호 변경</h1>
        <div className={styles.PasswordForm}>
          <div>
            <div>
              <h2>현재 비밀번호</h2>
              <input
                type="password"
                value={password}
                onChange={e => setPassword(e.target.value)}
              />
            </div>
            <div>
              <h2>새 비밀번호</h2>
              <input
                type="password"
                value={newPassword}
                onChange={e => setNewPassword(e.target.value)}
              />
            </div>
            <div>
              <h2>새 비밀번호 확인</h2>
              <input
                type="password"
                value={chkPassword}
                onChange={e => setChkPassword(e.target.value)}
              />
            </div>
          </div>
          <button onClick={handleMemberPwd}>변경하기</button>
        </div>
      </div>
    </div>
  )
}
