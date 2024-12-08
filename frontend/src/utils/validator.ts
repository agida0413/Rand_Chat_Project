import { notify } from './toast'

export const emailValidator = (email: string): boolean => {
  const emailRegex =
    /^[A-Za-z0-9_]+[A-Za-z0-9]*[@]{1}[A-Za-z0-9]+[A-Za-z0-9]*[.]{1}[A-Za-z]{1,3}$/
  return emailRegex.test(email)
}

export const checkEmail = (email: string) => {
  if (!email) {
    notify('error', '이메일을 입력해주세요')
    return false
  }
  if (email.includes(' ')) {
    notify('error', '이메일에 공백이 포함될 수 없습니다')
    return false
  }
  if (!emailValidator(email)) {
    notify('error', '올바른 이메일 형식이 아닙니다')
    return false
  }
  return true
}

export const checkAuth = (authCode: string) => {
  if (!authCode) {
    notify('error', '인증번호를 입력해주세요')
    return false
  }
  if (authCode.includes(' ')) {
    notify('error', '인증번호에 공백이 포함될 수 없습니다')
    return false
  }
  return true
}
