export interface SignupForm {
  email: string
  username: string
  password: string
  nickName: string
  sex: 'MAN' | 'FEMALE'
  birth: string
  name: string
}

export interface ValidationResult {
  isValid: boolean
  message?: string
}

const PATTERNS = {
  이메일: /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/,
  비밀번호: /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,}$/,
  한글이름: /^[가-힣]{2,8}$/,
  생년월일: /^\d{4}-\d{2}-\d{2}$/,
  인증번호: /^\d{6}$/
} as const

export const validatePattern = (
  value: string,
  type: keyof typeof PATTERNS
): boolean => {
  return PATTERNS[type].test(value)
}

export const hasNoSpaces = (value: string): boolean => {
  return !value.includes(' ')
}

export const validateEmail = (email: string): ValidationResult => {
  if (!email) {
    return { isValid: false, message: '이메일을 입력해주세요' }
  }
  if (!hasNoSpaces(email)) {
    return { isValid: false, message: '이메일에 공백이 포함될 수 없습니다' }
  }
  if (!validatePattern(email, '이메일')) {
    return { isValid: false, message: '올바른 이메일 형식이 아닙니다' }
  }
  return { isValid: true }
}

export const validateAuthCode = (code: string): ValidationResult => {
  if (!code) {
    return { isValid: false, message: '인증번호를 입력해주세요' }
  }
  if (!hasNoSpaces(code)) {
    return { isValid: false, message: '인증번호에 공백이 포함될 수 없습니다' }
  }
  if (!validatePattern(code, '인증번호')) {
    return { isValid: false, message: '인증번호는 6자리 숫자여야 합니다' }
  }
  return { isValid: true }
}

export const validateSignupForm = (
  data: SignupForm,
  emailConfirm: string,
  passwordConfirm: string
): ValidationResult => {
  const emailResult = validateEmail(data.email)
  if (!emailResult.isValid) return emailResult

  if (data.email !== emailConfirm) {
    return { isValid: false, message: '인증받은 이메일로 진행해주세요' }
  }

  if (data.password !== passwordConfirm) {
    return { isValid: false, message: '비밀번호가 동일하지 않습니다' }
  }

  const validations = [
    {
      condition:
        !data.username || data.username.length < 8 || data.username.length > 16,
      message: '아이디는 8~16자 사이여야 합니다'
    },
    {
      condition: !validatePattern(data.password, '비밀번호'),
      message: '비밀번호는 영문, 숫자, 특수문자를 포함해야 합니다'
    },
    {
      condition: !/^[가-힣]+$/.test(data.nickName),
      message: '닉네임은 한글만 입력 가능합니다'
    },
    {
      condition: !validatePattern(data.birth, '생년월일'),
      message: '생년월일은 yyyy-MM-dd 형식이어야 합니다'
    },
    {
      condition: !validatePattern(data.name, '한글이름'),
      message: '이름은 2~8자의 한글이어야 합니다'
    }
  ]

  for (const { condition, message } of validations) {
    if (condition) {
      return { isValid: false, message }
    }
  }

  return { isValid: true }
}

export const validateLogin = (username: string, password: string) => {
  if (!username) {
    return { isValid: false, message: '아이디를 입력해주세요' }
  }
  if (!hasNoSpaces(username)) {
    return { isValid: false, message: '아이디에 공백이 포함될 수 없습니다' }
  }
  if (!password) {
    return { isValid: false, message: '비밀번호를 입력해주세요' }
  }
  return { isValid: true }
}
