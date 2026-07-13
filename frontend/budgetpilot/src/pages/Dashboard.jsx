import { useEffect, useState } from "react"
import { Link } from "react-router-dom"
import { useApi } from "@/services/api"
import { incomeStreamService } from "@/services/incomeStreamService"
import { expenseService } from "@/services/expenseService"
import { savingGoalService } from "@/services/savingGoalService"
import { userBudgetService } from "@/services/userBudgetService"
import { useCurrentUser } from "@/context/CurrentUserContext"
import { formatCurrency, toMonthlyAmount } from "@/lib/format"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Progress } from "@/components/ui/progress"

function Dashboard() {
  const api = useApi()
  const currentUser = useCurrentUser()
  const [loading, setLoading] = useState(true)
  const [incomeStreams, setIncomeStreams] = useState([])
  const [expenses, setExpenses] = useState([])
  const [savingGoals, setSavingGoals] = useState([])
  const [userBudget, setUserBudget] = useState(null)
  const [strategies, setStrategies] = useState([])
  const [applyingStrategyId, setApplyingStrategyId] = useState(null)

  async function loadData() {
    const [incomeData, expenseData, goalData, budgetData] = await Promise.all([
      incomeStreamService.getAllForUser(api, currentUser.id),
      expenseService.getAllForUser(api, currentUser.id),
      savingGoalService.getAllForUser(api, currentUser.id),
      userBudgetService.getForUser(api, currentUser.id),
    ])
    setIncomeStreams(incomeData)
    setExpenses(expenseData)
    setSavingGoals(goalData)
    setUserBudget(budgetData)
    if (!budgetData) {
      setStrategies(await userBudgetService.getStrategies(api))
    }
    setLoading(false)
  }

  useEffect(() => {
    // eslint-disable-next-line react-hooks/set-state-in-effect -- fetch-on-mount
    loadData()
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [])

  async function applyStrategy(strategy) {
    setApplyingStrategyId(strategy.id)
    try {
      const created = await userBudgetService.create(api, {
        userId: currentUser.id,
        strategyId: strategy.id,
        needsPercent: strategy.needsPercent,
        wantsPercent: strategy.wantsPercent,
        savingsPercent: strategy.savingsPercent,
      })
      setUserBudget(created)
    } finally {
      setApplyingStrategyId(null)
    }
  }

  if (loading) {
    return <p className="text-muted-foreground text-sm">Loading your dashboard...</p>
  }

  const totalMonthlyIncome = incomeStreams.reduce(
    (sum, income) => sum + toMonthlyAmount(income.amount, income.frequency),
    0
  )
  const totalMonthlyExpenses = expenses.reduce(
    (sum, expense) => sum + toMonthlyAmount(expense.amount, expense.frequency),
    0
  )
  const net = totalMonthlyIncome - totalMonthlyExpenses

  const spendByType = expenses.reduce((acc, expense) => {
    const type = expense.category?.type ?? "other"
    acc[type] = (acc[type] ?? 0) + toMonthlyAmount(expense.amount, expense.frequency)
    return acc
  }, {})

  return (
    <div className="flex flex-col gap-6">
      <div>
        <h1 className="text-2xl font-semibold">
          Welcome back{currentUser.firstName ? `, ${currentUser.firstName}` : ""}
        </h1>
        <p className="text-muted-foreground text-sm">Here's where your money stands this month.</p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        <Card>
          <CardHeader>
            <CardTitle className="text-muted-foreground text-sm font-normal">Monthly income</CardTitle>
          </CardHeader>
          <CardContent className="text-2xl font-semibold">{formatCurrency(totalMonthlyIncome)}</CardContent>
        </Card>
        <Card>
          <CardHeader>
            <CardTitle className="text-muted-foreground text-sm font-normal">Monthly expenses</CardTitle>
          </CardHeader>
          <CardContent className="text-2xl font-semibold">{formatCurrency(totalMonthlyExpenses)}</CardContent>
        </Card>
        <Card>
          <CardHeader>
            <CardTitle className="text-muted-foreground text-sm font-normal">Net</CardTitle>
          </CardHeader>
          <CardContent className={`text-2xl font-semibold ${net < 0 ? "text-destructive" : ""}`}>
            {formatCurrency(net)}
          </CardContent>
        </Card>
      </div>

      {incomeStreams.length === 0 && expenses.length === 0 && (
        <Card>
          <CardContent className="flex items-center justify-between text-sm">
            <span className="text-muted-foreground">
              Add your income and expenses to see your full financial picture.
            </span>
            <div className="flex gap-2">
              <Button asChild size="sm" variant="outline">
                <Link to="/income">Add income</Link>
              </Button>
              <Button asChild size="sm" variant="outline">
                <Link to="/expenses">Add expenses</Link>
              </Button>
            </div>
          </CardContent>
        </Card>
      )}

      <Card>
        <CardHeader>
          <CardTitle>Budget strategy</CardTitle>
        </CardHeader>
        <CardContent>
          {userBudget ? (
            <div className="flex flex-col gap-4">
              {[
                { label: "Needs", target: userBudget.needsPercent, type: "need" },
                { label: "Wants", target: userBudget.wantsPercent, type: "want" },
                { label: "Savings", target: userBudget.savingsPercent, type: "savings" },
              ].map(({ label, target, type }) => {
                const spent = spendByType[type] ?? 0
                const actualPct = totalMonthlyExpenses > 0 ? Math.round((spent / totalMonthlyExpenses) * 100) : 0
                return (
                  <div key={label} className="flex flex-col gap-1">
                    <div className="flex justify-between text-sm">
                      <span>{label}</span>
                      <span className="text-muted-foreground">
                        {formatCurrency(spent)} · target {Number(target)}% · actual {actualPct}%
                      </span>
                    </div>
                    <Progress value={Math.min(100, actualPct)} />
                  </div>
                )
              })}
            </div>
          ) : strategies.length === 0 ? (
            <p className="text-muted-foreground text-sm">No budgeting strategies available yet.</p>
          ) : (
            <div className="flex flex-col gap-3">
              <p className="text-muted-foreground text-sm">Pick a budgeting strategy to track your spending against.</p>
              <div className="grid grid-cols-1 md:grid-cols-3 gap-3">
                {strategies.map((strategy) => (
                  <div key={strategy.id} className="border border-border rounded-lg p-4 flex flex-col gap-2">
                    <p className="font-medium">{strategy.name}</p>
                    <p className="text-xs text-muted-foreground">{strategy.description}</p>
                    <p className="text-xs">
                      {Number(strategy.needsPercent)}% / {Number(strategy.wantsPercent)}% / {Number(strategy.savingsPercent)}%
                    </p>
                    <Button
                      size="sm"
                      disabled={applyingStrategyId === strategy.id}
                      onClick={() => applyStrategy(strategy)}
                    >
                      {applyingStrategyId === strategy.id ? "Applying..." : "Use this strategy"}
                    </Button>
                  </div>
                ))}
              </div>
            </div>
          )}
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle>Saving goals</CardTitle>
        </CardHeader>
        <CardContent>
          {savingGoals.length === 0 ? (
            <div className="flex items-center justify-between text-sm">
              <span className="text-muted-foreground">No saving goals yet.</span>
              <Button asChild size="sm" variant="outline">
                <Link to="/goals">Add a goal</Link>
              </Button>
            </div>
          ) : (
            <div className="flex flex-col gap-4">
              {savingGoals.map((goal) => {
                const current = Number(goal.currentAmount ?? 0)
                const target = Number(goal.targetAmount) || 1
                const pct = Math.min(100, Math.round((current / target) * 100))
                return (
                  <div key={goal.id} className="flex flex-col gap-1">
                    <div className="flex justify-between text-sm">
                      <span>{goal.name}</span>
                      <span className="text-muted-foreground">
                        {formatCurrency(current)} / {formatCurrency(target)}
                      </span>
                    </div>
                    <Progress value={pct} />
                  </div>
                )
              })}
            </div>
          )}
        </CardContent>
      </Card>
    </div>
  )
}

export default Dashboard
