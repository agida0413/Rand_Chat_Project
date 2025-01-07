import { postSignup } from '@/api/signup'
import { useMutation } from '@tanstack/react-query'
import { notify } from '@/utils/toast'
import { useSignupStore } from '@/store/signupStore'
import { useNavigate } from 'react-router-dom'
import { SignupForm, validateSignupForm } from '@/utils/validator'

export function useSignupMutation() {
  const navigate = useNavigate()

  const {
    emailConfirm,
    email,
    id,
    passwordConfirm,
    password,
    nickName,
    gender,
    birthDay,
    name
  } = useSignupStore(state => state.user)
  const { resetState } = useSignupStore()

  const {
    mutate,
    isPending: isPendingSignup,
    isError: isErrorSignup
  } = useMutation({
    mutationFn: postSignup,
    onSuccess: () => {
      notify('success', '회원가입 되었습니다')
      resetState()
      navigate('/login')
    },
    onError: (error: Error) => {
      notify('error', error.message)
    }
  })

  const submitSignup = () => {
    const data: SignupForm = {
      email: email,
      username: id,
      password: password,
      nickName: nickName,
      sex: gender as 'MAN' | 'FEMALE',
      birth: birthDay,
      name: name
    }

    const validation = validateSignupForm(data, emailConfirm, passwordConfirm)
    if (!validation.isValid) {
      notify('error', validation.message!)
      return false
    }

    mutate(data)
  }

  return { submitSignup, isPendingSignup, isErrorSignup }
}
