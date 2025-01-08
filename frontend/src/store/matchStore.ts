import { create } from 'zustand'
import { persist } from 'zustand/middleware'

interface MatchingData {
  status: string
  code: string
  nickname: string
  sex: string
  profileImg: string | null
  distance: number
  matchAcptToken: string
  timestamp: string
}

interface MatchState {
  isLocationGranted: boolean
  isConnecting: boolean
  isConnected: boolean
  isOpenModal: boolean
  matchingData: MatchingData
  setIsLocationGranted: (value: boolean) => void
  setIsConnecting: (value: boolean) => void
  setIsConnected: (value: boolean) => void
  setIsOpenModal: (value: boolean) => void
  setMatchingData: (data: MatchingData) => void
}

export const useMatchStore = create<MatchState>()(
  persist(
    set => ({
      isLocationGranted: true,
      isConnecting: false,
      isConnected: false,
      isOpenModal: false,
      matchingData: {
        status: '',
        code: '',
        nickname: '',
        sex: '',
        profileImg: null,
        distance: 0,
        matchAcptToken: '',
        timestamp: ''
      },
      setIsLocationGranted: (value: boolean) =>
        set({ isLocationGranted: value }),
      setIsConnecting: (value: boolean) => set({ isConnecting: value }),
      setIsConnected: (value: boolean) => set({ isConnected: value }),
      setIsOpenModal: (value: boolean) => set({ isOpenModal: value }),
      setMatchingData: (data: MatchingData) => set({ matchingData: data })
    }),
    {
      name: 'matchStore'
    }
  )
)
