import TermsAgreement from './TermsAgreement'
import EmailAuth from './EmailAuth'
import SignupComplete from './SignupComplete'
import { usePageStore } from '@/store/pageStore'
import { useNavigate } from 'react-router-dom'

export default function index() {
  const navigate = useNavigate()
  const { currentPage, setCurrentPage } = usePageStore()
  const handleNext = () => {
    if (currentPage < 3) {
      setCurrentPage(currentPage + 1)
    } else {
      setCurrentPage(1)
      navigate('/login')
    }
  }
  return (
    <>
      {currentPage === 1 && <TermsAgreement />}
      {currentPage === 2 && <EmailAuth />}
      {currentPage === 3 && <SignupComplete />}

      <button
        onClick={() => setCurrentPage(currentPage - 1)}
        disabled={currentPage === 1}>
        이전
      </button>

      <button onClick={handleNext}>
        {currentPage === 3 ? '완료' : '다음'}
      </button>
    </>
  )
}
