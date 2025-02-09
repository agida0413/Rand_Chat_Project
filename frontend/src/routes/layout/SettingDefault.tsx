import { Outlet, useNavigation } from 'react-router-dom'
import Loading from './Loading'
import Setting from '../pages/setting'
import styles from './SettingDefault.module.scss'
import { useState } from 'react'
import DeactivateModal from '@/components/deactivateModal'
import DeleteModal from '@/components/deleteModal'

export default function SettingDefaultLayout() {
  const [openDeactivateModal, setOpenDeactivateModal] = useState(false)
  const [openDeleteModal, setOpenDeleteModal] = useState(false)

  const isMobile = window.innerWidth <= 1023
  const isSettingPage = location.pathname === '/setting'

  const navigation = useNavigation()
  if (navigation.state === 'loading') {
    return <Loading />
  }

  return (
    <span className={styles.settingContainer}>
      {openDeactivateModal && (
        <DeactivateModal handleClose={() => setOpenDeactivateModal(false)} />
      )}
      {openDeleteModal && (
        <DeleteModal handleClose={() => setOpenDeleteModal(false)} />
      )}

      {(!isMobile || isSettingPage) && (
        <Setting
          onDeactivateModal={() => setOpenDeactivateModal(true)}
          onDeleteModal={() => setOpenDeleteModal(true)}
        />
      )}
      {/* {(isMobile && !isSettingPage) || !isMobile ? <Outlet /> : null} */}

      <Outlet />
    </span>
  )
}
