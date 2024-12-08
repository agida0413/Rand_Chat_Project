import { useState } from 'react'
import { checkEmail, checkAuth } from '@/utils/validator'
import { Timer } from '@/components/Timer'
import { useCheckAuthCode, useSendAuthCode } from '@/hooks/email'
import SignupForm from './SignupForm'
import ScrollContainer from '@/components/common/ScrollContainer'

export default function EmailAuth() {
  const [email, setEmail] = useState('')
  const [authCode, setAuthCode] = useState('')

  const [isRunning, setIsRunning] = useState(false)
  const [currentTime, setCurrentTime] = useState(0)
  const [isAuthCodeVerified, setIsAuthCodeVerified] = useState(true)

  const { mutate: sendAuthCode, isPending: sendAuthCodePending } =
    useSendAuthCode()

  const { mutate: checkAuthCode, isPending: checkAuthCodePending } =
    useCheckAuthCode()

  const handleSubmit = () => {
    if (!checkEmail(email)) return
    sendAuthCode(
      { email },
      {
        onSuccess: () => {
          setIsRunning(true)
          setCurrentTime(50)
        }
      }
    )
  }
  const handleCheck = () => {
    if (!checkAuth(authCode)) return
    checkAuthCode(
      { email, authCode },
      {
        onSuccess: () => {
          setIsAuthCodeVerified(true)
          setCurrentTime(12)
        }
      }
    )
  }

  return (
    <></>
    // <ScrollContainer>
    //   {isRunning && (
    //     <Timer
    //       currentTime={currentTime}
    //       onTimeEnd={() => setIsRunning(false)}
    //     />
    //   )}

    //   <Stack
    //     direction="row"
    //     spacing={2}
    //     alignItems="flex-end">
    //     <AuthInput
    //       label="이메일"
    //       // error={emailError}
    //       // errorMessage={emailErrorMessage}
    //       placeholder="이메일을 입력하세요"
    //       autoFocus
    //       value={email}
    //       onChange={e => setEmail(e.target.value)}
    //     />

    //     <Button
    //       type="submit"
    //       variant="contained"
    //       disabled={sendAuthCodePending}
    //       fullWidth
    //       onClick={handleSubmit}>
    //       인증코드 전송
    //     </Button>
    //   </Stack>
    //   <input
    //     type="text"
    //     value={authCode}
    //     onChange={e => setAuthCode(e.target.value)}
    //     placeholder="인증번호를 입력하세요"
    //   />
    //   <button
    //     type="button"
    //     onClick={handleCheck}
    //     disabled={checkAuthCodePending}>
    //     인증번호 확인
    //   </button>
    //   {isAuthCodeVerified && <SignupForm />}
    // </ScrollContainer>
  )
}
