interface ForgotPasswordProps {
  open: boolean
  handleClose: () => void
}

export default function ForgotPassword({
  open,
  handleClose
}: ForgotPasswordProps) {
  return (
    <>
      <h1>비밀번호 초기화</h1>
      <div>
        계정의 이메일 주소를 입력하면 비밀번호를 재설정할 수 있는 링크를
        보내드립니다.
      </div>
    </>
  )
}
