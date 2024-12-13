import { TERM_DATA } from '@/constants'
import { create } from 'zustand'
import { persist } from 'zustand/middleware'

interface UserState {
  email: string
  emailConfirm: string
  id: string
  password: string
  passwordConfirm: string
  nickName: string
  gender: 'MAN' | 'FEMALE' | undefined
  birthDay: string
  name: string
}

interface AuthState {
  isAllChecked: boolean
  isCodeCheckd: boolean
  isAuthCodeVerified: boolean
  isRunning: boolean
  currentPage: number
  checkedItems: boolean[]
  authCode: string
  authTime: number
}

interface SignupState {
  user: UserState
  auth: AuthState
}

const initialState = {
  auth: {
    isAllChecked: false,
    isCodeCheckd: false,
    isAuthCodeVerified: false,
    isRunning: false,
    currentPage: 1,
    checkedItems: new Array(TERM_DATA.length).fill(false),
    authCode: '',
    authTime: 0
  },
  user: {
    email: '',
    emailConfirm: '',
    id: '',
    password: '',
    passwordConfirm: '',
    nickName: '',
    gender: undefined,
    birthDay: '',
    name: ''
  }
}

interface SignupActions {
  setUser: (updates: Partial<UserState>) => void
  setAuth: (updates: Partial<AuthState>) => void
  handleAllCheck: (checked: boolean) => void
  handleSingleCheck: (index: number, checked: boolean) => void
  resetState: () => void
}

export const useSignupStore = create<SignupState & SignupActions>()(
  persist(
    set => ({
      ...initialState,

      setAuth: updates => {
        set(state => ({
          auth: {
            ...state.auth,
            ...updates
          }
        }))
      },

      setUser: updates => {
        set(state => ({
          user: {
            ...state.user,
            ...updates
          }
        }))
      },

      handleAllCheck: (checked: boolean) => {
        set(state => ({
          auth: {
            ...state.auth,
            isAllChecked: checked,
            checkedItems: new Array(TERM_DATA.length).fill(checked)
          }
        }))
      },

      handleSingleCheck: (index: number, checked: boolean) => {
        set(state => {
          const newCheckedItems = [...state.auth.checkedItems]
          newCheckedItems[index] = checked
          const allChecked = newCheckedItems.every(item => item)
          return {
            auth: {
              ...state.auth,
              checkedItems: newCheckedItems,
              isAllChecked: allChecked
            }
          }
        })
      },

      resetState: () => set(initialState)
    }),
    {
      name: 'signupStore'
    }
  )
)
