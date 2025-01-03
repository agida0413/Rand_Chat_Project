import { Outlet, useNavigation } from 'react-router-dom'
import Loading from './Loading'
import Setting from '../pages/setting'
import styles from './SettingDefault.module.scss'

export default function SettingDefaultLayout() {
  const navigation = useNavigation()
  if (navigation.state === 'loading') {
    return <Loading />
  }

  return (
    <span className={styles.settingContainer}>
      <Setting
        onDeactivateModal={() => console.log('Open Deactivate Modal')}
        onDeleteModal={() => console.log('Open Delete Modal')}
      />
      <Outlet />
    </span>
  )
}
