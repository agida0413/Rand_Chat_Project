import { create } from 'zustand'
import {
  // combine,
  devtools
  // persist
} from 'zustand/middleware'
import { immer } from 'zustand/middleware/immer'

interface ChatState {}
interface ChatActions {
  resetState: () => void
}

const initialState = {} as ChatState

export const useChatStore = create<ChatState & { actions: ChatActions }>()(
  devtools(
    immer(set => ({
      ...initialState,
      actions: {
        resetState: () => set(initialState)
      }
    }))
  )
)
