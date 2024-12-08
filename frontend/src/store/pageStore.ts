import { create } from 'zustand'

interface PageStore {
  currentPage: number
  setCurrentPage: (currentPage: number) => void
}

export const usePageStore = create<PageStore>(set => ({
  currentPage: 1,
  setCurrentPage: (currentPage: number) => set({ currentPage })
}))
