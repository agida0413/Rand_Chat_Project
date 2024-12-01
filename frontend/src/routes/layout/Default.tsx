import { Outlet, ScrollRestoration } from 'react-router-dom'
import Header from '@/components/Header'

export default function DefaultLayout() {
  return (
    <>
      <Header />
      <Outlet />
      <ScrollRestoration />
    </>
  )
}
