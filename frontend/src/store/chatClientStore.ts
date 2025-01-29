import { create } from 'zustand'
import { Client } from '@stomp/stompjs'

type ChatClientState = {
  clients: Map<string, Client>
  connectedRooms: string[]
  addClient: (chatRoomId: string, client: Client) => void
  removeClient: (chatRoomId: string) => void
  addRoom: (chatRoomId: string) => void
  removeRoom: (chatRoomId: string) => void
}

export const useChatClientStore = create<ChatClientState>(set => ({
  clients: new Map(),
  connectedRooms: [],
  addClient: (chatRoomId, client) =>
    set(state => {
      const updatedClients = new Map(state.clients)
      updatedClients.set(chatRoomId, client)
      return { clients: updatedClients }
    }),
  removeClient: chatRoomId =>
    set(state => {
      const updatedClients = new Map(state.clients)
      updatedClients.delete(chatRoomId)
      return { clients: updatedClients }
    }),
  addRoom: chatRoomId =>
    set(state => ({
      connectedRooms: [...state.connectedRooms, chatRoomId]
    })),
  removeRoom: chatRoomId =>
    set(state => ({
      connectedRooms: state.connectedRooms.filter(room => room !== chatRoomId)
    }))
}))
