import { Outlet, useNavigate, useNavigation } from 'react-router-dom'
import Header from '@/components/header'
import Loading from './Loading'
import styles from './Defalut.module.scss'
import { useMatchStore } from '@/store/matchStore'
import MatchProfile from '@/components/matchProfile'
import { useEffect } from 'react'
import { useLocationPolling } from '@/hooks/useLocationPolling'
import { useUserStore } from '@/store/userStore'
import { getUserInfo } from '@/api/login'

export default function DefaultLayout() {
  const isMobile = window.innerWidth <= 1023
  const userStore = useUserStore()
  const { actions } = userStore
  const { setUser } = actions

  const { isOpenModal } = useMatchStore()
  const { getCurrentPosition } = useLocationPolling()

  const navigation = useNavigation()
  const navigate = useNavigate()
  if (navigation.state === 'loading') {
    return <Loading />
  }

  useEffect(() => {
    const fetchData = async () => {
      try {
        const userData = await getUserInfo()
        if(userData.data.status === 401 || userData.data.status === 500){
          navigate('/login')
        }
        
        setUser(userData)
      } catch (error) {
        console.error('Error fetching user info:', error)
      }
    }
  
    fetchData()
  
    const intervalId = setInterval(getCurrentPosition, 300000)
    getCurrentPosition()
  
    return () => {
      clearInterval(intervalId)
    }
  }, [])
  

  useEffect(() => {
    if (performance.navigation.type === 1) navigate('/')
  }, [navigate])

  return (
    <main className={styles.mainContainer}>
      {isOpenModal ? <MatchProfile /> : ''}
      {!isMobile && (
        <>
          <Header />
          <Outlet />
        </>
      )}
      {isMobile && (
        <div className={styles.mainDivContainer}>
          <Outlet />
          <Header />
        </div>
      )}
    </main>
  )
}
