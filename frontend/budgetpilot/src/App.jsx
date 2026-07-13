import { Routes, Route } from 'react-router-dom'
import AiAssistant from './pages/AiAssistant'
import LandingPage from './pages/LandingPage'
import Dashboard from './pages/Dashboard'
import Expenses from './pages/Expenses'
import IncomeStreams from './pages/IncomeStreams'
import SavingGoals from './pages/SavingGoals'
import Transactions from './pages/Transactions'
import AppLayout from './layouts/AppLayout'

function App() {
  return (
    <Routes>
      <Route path="/" element={<LandingPage/>} />
      <Route element={<AppLayout/>}>
        <Route path="/dashboard" element={<Dashboard/>} />
        <Route path="/income" element={<IncomeStreams/>} />
        <Route path="/expenses" element={<Expenses/>} />
        <Route path="/transactions" element={<Transactions/>} />
        <Route path="/goals" element={<SavingGoals/>} />
        <Route path="/ai" element={<AiAssistant/>} />
      </Route>
    </Routes>
  )
}

export default App
