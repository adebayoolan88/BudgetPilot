import { useEffect, useState } from "react"
import { useApi } from "@/services/api"
import { savingGoalService } from "@/services/savingGoalService"
import { useCurrentUser } from "@/context/CurrentUserContext"
import { formatCurrency } from "@/lib/format"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardFooter, CardHeader, CardTitle } from "@/components/ui/card"
import {
  Dialog,
  DialogContent,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Progress } from "@/components/ui/progress"
import { Trash2, Pencil, Plus, PiggyBank } from "lucide-react"

const emptyForm = { name: "", targetAmount: "", currentAmount: "0", deadline: "" }

function SavingGoals() {
  const api = useApi()
  const currentUser = useCurrentUser()
  const [goals, setGoals] = useState([])
  const [loading, setLoading] = useState(true)
  const [dialogOpen, setDialogOpen] = useState(false)
  const [editingId, setEditingId] = useState(null)
  const [form, setForm] = useState(emptyForm)
  const [saving, setSaving] = useState(false)
  const [contributingId, setContributingId] = useState(null)
  const [contributionAmount, setContributionAmount] = useState("")

  async function loadGoals() {
    const data = await savingGoalService.getAllForUser(api, currentUser.id)
    setGoals(data)
    setLoading(false)
  }

  useEffect(() => {
    // eslint-disable-next-line react-hooks/set-state-in-effect -- fetch-on-mount
    loadGoals()
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [])

  function openCreateDialog() {
    setEditingId(null)
    setForm(emptyForm)
    setDialogOpen(true)
  }

  function openEditDialog(goal) {
    setEditingId(goal.id)
    setForm({
      name: goal.name,
      targetAmount: goal.targetAmount,
      currentAmount: goal.currentAmount ?? 0,
      deadline: goal.deadline ?? "",
    })
    setDialogOpen(true)
  }

  async function handleSubmit(event) {
    event.preventDefault()
    setSaving(true)
    try {
      const payload = {
        userId: currentUser.id,
        name: form.name,
        targetAmount: Number(form.targetAmount),
        currentAmount: Number(form.currentAmount) || 0,
        deadline: form.deadline || null,
      }
      if (editingId) {
        await savingGoalService.update(api, editingId, payload)
      } else {
        await savingGoalService.create(api, payload)
      }
      setDialogOpen(false)
      await loadGoals()
    } finally {
      setSaving(false)
    }
  }

  async function handleDelete(id) {
    await savingGoalService.delete(api, id)
    await loadGoals()
  }

  async function handleContribute(goal) {
    const amount = Number(contributionAmount)
    if (!amount) return
    await savingGoalService.update(api, goal.id, {
      name: goal.name,
      targetAmount: goal.targetAmount,
      currentAmount: Number(goal.currentAmount ?? 0) + amount,
      deadline: goal.deadline,
    })
    setContributingId(null)
    setContributionAmount("")
    await loadGoals()
  }

  return (
    <div className="flex flex-col gap-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-semibold">Saving Goals</h1>
          <p className="text-muted-foreground text-sm">Track progress toward what you're saving for.</p>
        </div>
        <Dialog open={dialogOpen} onOpenChange={setDialogOpen}>
          <DialogTrigger asChild>
            <Button onClick={openCreateDialog}>
              <Plus /> Add Goal
            </Button>
          </DialogTrigger>
          <DialogContent>
            <form onSubmit={handleSubmit} className="flex flex-col gap-4">
              <DialogHeader>
                <DialogTitle>{editingId ? "Edit Saving Goal" : "Add Saving Goal"}</DialogTitle>
              </DialogHeader>
              <div className="flex flex-col gap-2">
                <Label htmlFor="name">Name</Label>
                <Input
                  id="name"
                  placeholder="e.g. Emergency Fund"
                  value={form.name}
                  onChange={(e) => setForm({ ...form, name: e.target.value })}
                  required
                />
              </div>
              <div className="flex flex-col gap-2">
                <Label htmlFor="targetAmount">Target amount</Label>
                <Input
                  id="targetAmount"
                  type="number"
                  step="0.01"
                  min="0"
                  value={form.targetAmount}
                  onChange={(e) => setForm({ ...form, targetAmount: e.target.value })}
                  required
                />
              </div>
              <div className="flex flex-col gap-2">
                <Label htmlFor="currentAmount">Current amount</Label>
                <Input
                  id="currentAmount"
                  type="number"
                  step="0.01"
                  min="0"
                  value={form.currentAmount}
                  onChange={(e) => setForm({ ...form, currentAmount: e.target.value })}
                />
              </div>
              <div className="flex flex-col gap-2">
                <Label htmlFor="deadline">Deadline</Label>
                <Input
                  id="deadline"
                  type="date"
                  value={form.deadline}
                  onChange={(e) => setForm({ ...form, deadline: e.target.value })}
                />
              </div>
              <DialogFooter>
                <Button type="submit" disabled={saving}>
                  {saving ? "Saving..." : "Save"}
                </Button>
              </DialogFooter>
            </form>
          </DialogContent>
        </Dialog>
      </div>

      {loading ? (
        <p className="text-muted-foreground text-sm">Loading...</p>
      ) : goals.length === 0 ? (
        <Card>
          <CardContent className="text-muted-foreground text-sm">
            No saving goals yet. Add your first one to start tracking progress.
          </CardContent>
        </Card>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          {goals.map((goal) => {
            const current = Number(goal.currentAmount ?? 0)
            const target = Number(goal.targetAmount) || 1
            const pct = Math.min(100, Math.round((current / target) * 100))
            return (
              <Card key={goal.id}>
                <CardHeader>
                  <CardTitle className="flex items-center gap-2">
                    <PiggyBank className="size-4" /> {goal.name}
                  </CardTitle>
                </CardHeader>
                <CardContent className="flex flex-col gap-3">
                  <Progress value={pct} />
                  <p className="text-sm text-muted-foreground">
                    {formatCurrency(current)} of {formatCurrency(target)} ({pct}%)
                  </p>
                  {goal.deadline && (
                    <p className="text-xs text-muted-foreground">Target date: {goal.deadline}</p>
                  )}
                  {contributingId === goal.id ? (
                    <div className="flex gap-2">
                      <Input
                        type="number"
                        step="0.01"
                        placeholder="Amount"
                        value={contributionAmount}
                        onChange={(e) => setContributionAmount(e.target.value)}
                        autoFocus
                      />
                      <Button size="sm" onClick={() => handleContribute(goal)}>
                        Add
                      </Button>
                    </div>
                  ) : (
                    <Button variant="outline" size="sm" onClick={() => setContributingId(goal.id)}>
                      Add contribution
                    </Button>
                  )}
                </CardContent>
                <CardFooter className="justify-end gap-1">
                  <Button variant="ghost" size="icon-sm" onClick={() => openEditDialog(goal)}>
                    <Pencil />
                  </Button>
                  <Button variant="ghost" size="icon-sm" onClick={() => handleDelete(goal.id)}>
                    <Trash2 />
                  </Button>
                </CardFooter>
              </Card>
            )
          })}
        </div>
      )}
    </div>
  )
}

export default SavingGoals
