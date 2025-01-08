import { create } from 'zustand'
import { persist } from 'zustand/middleware'

interface MatchState {
  isLocationGranted: boolean
  isConnecting: boolean
  isConnected: boolean
  isOpenModal: boolean
  setIsLocationGranted: (value: boolean) => void
  setIsConnecting: (value: boolean) => void
  setIsConnected: (value: boolean) => void
  setIsOpenModal: (value: boolean) => void
}

export const useMatchStore = create<MatchState>()(
  persist(
    set => ({
      isLocationGranted: true,
      isConnecting: false,
      isConnected: false,
      isOpenModal: false,
      setIsLocationGranted: (value: boolean) =>
        set({ isLocationGranted: value }),
      setIsConnecting: (value: boolean) => set({ isConnecting: value }),
      setIsConnected: (value: boolean) => set({ isConnected: value }),
      setIsOpenModal: (value: boolean) => set({ isOpenModal: value })
    }),
    {
      name: 'matchStore'
    }
  )
)
