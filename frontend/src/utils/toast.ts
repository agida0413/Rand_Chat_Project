import { toast } from 'react-toastify'

type notifyState = 'success' | 'error' | 'info' | 'warning'

export const notify = (type: notifyState, message: string) => {
  switch (type) {
    case 'success':
      toast.success(message)
      break
    case 'error':
      toast.error(message)
      break
    case 'info':
      toast.info(message)
      break
    case 'warning':
      toast.warning(message)
      break
  }
}
