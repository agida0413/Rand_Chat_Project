import { ApiResponse } from '@/api'
import { getUserInfoResponse } from '@/api/login'
import { create } from 'zustand'
import { devtools } from 'zustand/middleware'

interface userProps {
  email: string
  username: string
  nickname: string
  name: string
  profile_img: string
  sex: '남자' | '여자'
  birth: [number, number, number]
  manAge: number
}

const init = {
  email: '',
  username: '',
  nickname: '',
  name: '',
  profile_img: '',
  sex: '남자',
  birth: [0, 0, 0],
  manAge: 0
}

interface UserActions {
  setUser: (userData: {
    data: ApiResponse<getUserInfoResponse>;
    headers: Headers;
}) => void
  updateProfileImg: (newProfileImg: string) => void
}

export const useUserStore = create<
  { user: userProps } & { actions: UserActions }
>()(
  devtools(set => ({
    user: init,
    actions: {
      setUser: async (userData) => {

        set(() => ({
          user: userData.data.data
        }))

        return userData
      },

      updateProfileImg: (newProfileImg: string) => {
        set(state => ({
          user: { ...state.user, profile_img: newProfileImg }
        }))
      }
    }
  }))
)
