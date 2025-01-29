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
      <Setting
        onDeactivateModal={() => setOpenDeactivateModal(true)}
        onDeleteModal={() => setOpenDeleteModal(true)}
      />
      <Outlet />
    </span>
  )
}
