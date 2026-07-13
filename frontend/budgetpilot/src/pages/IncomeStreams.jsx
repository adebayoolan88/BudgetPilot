import { useEffect, useState } from "react"
import { useApi } from "@/services/api"
import { incomeStreamService } from "@/services/incomeStreamService"
import { useCurrentUser } from "@/context/CurrentUserContext"
import { formatCurrency, toMonthlyAmount, FREQUENCY_OPTIONS } from "@/lib/format"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
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
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import { Trash2, Pencil, Plus } from "lucide-react"

const emptyForm = { name: "", amount: "", frequency: "monthly" }

function IncomeStreams() {
  const api = useApi()
  const currentUser = useCurrentUser()
  const [incomeStreams, setIncomeStreams] = useState([])
  const [loading, setLoading] = useState(true)
  const [dialogOpen, setDialogOpen] = useState(false)
  const [editingId, setEditingId] = useState(null)
  const [form, setForm] = useState(emptyForm)
  const [saving, setSaving] = useState(false)

  async function loadIncomeStreams() {
    const data = await incomeStreamService.getAllForUser(api, currentUser.id)
    setIncomeStreams(data)
    setLoading(false)
  }

  useEffect(() => {
    // eslint-disable-next-line react-hooks/set-state-in-effect -- fetch-on-mount
    loadIncomeStreams()
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [])

  function openCreateDialog() {
    setEditingId(null)
    setForm(emptyForm)
    setDialogOpen(true)
  }

  function openEditDialog(incomeStream) {
    setEditingId(incomeStream.id)
    setForm({
      name: incomeStream.name,
      amount: incomeStream.amount,
      frequency: incomeStream.frequency,
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
        amount: Number(form.amount),
        frequency: form.frequency,
      }
      if (editingId) {
        await incomeStreamService.update(api, editingId, payload)
      } else {
        await incomeStreamService.create(api, payload)
      }
      setDialogOpen(false)
      await loadIncomeStreams()
    } finally {
      setSaving(false)
    }
  }

  async function handleDelete(id) {
    await incomeStreamService.delete(api, id)
    await loadIncomeStreams()
  }

  const totalMonthly = incomeStreams.reduce(
    (sum, income) => sum + toMonthlyAmount(income.amount, income.frequency),
    0
  )

  return (
    <div className="flex flex-col gap-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-semibold">Income Streams</h1>
          <p className="text-muted-foreground text-sm">Track every source of income you rely on.</p>
        </div>
        <Dialog open={dialogOpen} onOpenChange={setDialogOpen}>
          <DialogTrigger asChild>
            <Button onClick={openCreateDialog}>
              <Plus /> Add Income
            </Button>
          </DialogTrigger>
          <DialogContent>
            <form onSubmit={handleSubmit} className="flex flex-col gap-4">
              <DialogHeader>
                <DialogTitle>{editingId ? "Edit Income Stream" : "Add Income Stream"}</DialogTitle>
              </DialogHeader>
              <div className="flex flex-col gap-2">
                <Label htmlFor="name">Name</Label>
                <Input
                  id="name"
                  placeholder="e.g. Salary"
                  value={form.name}
                  onChange={(e) => setForm({ ...form, name: e.target.value })}
                  required
                />
              </div>
              <div className="flex flex-col gap-2">
                <Label htmlFor="amount">Amount</Label>
                <Input
                  id="amount"
                  type="number"
                  step="0.01"
                  min="0"
                  value={form.amount}
                  onChange={(e) => setForm({ ...form, amount: e.target.value })}
                  required
                />
              </div>
              <div className="flex flex-col gap-2">
                <Label>Frequency</Label>
                <Select value={form.frequency} onValueChange={(value) => setForm({ ...form, frequency: value })}>
                  <SelectTrigger className="w-full">
                    <SelectValue />
                  </SelectTrigger>
                  <SelectContent>
                    {FREQUENCY_OPTIONS.map((option) => (
                      <SelectItem key={option.value} value={option.value}>
                        {option.label}
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>
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

      <Card>
        <CardHeader>
          <CardTitle>Total monthly income: {formatCurrency(totalMonthly)}</CardTitle>
        </CardHeader>
        <CardContent>
          {loading ? (
            <p className="text-muted-foreground text-sm">Loading...</p>
          ) : incomeStreams.length === 0 ? (
            <p className="text-muted-foreground text-sm">
              No income streams yet. Add your first one to get started.
            </p>
          ) : (
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>Name</TableHead>
                  <TableHead>Amount</TableHead>
                  <TableHead>Frequency</TableHead>
                  <TableHead className="text-right">Actions</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {incomeStreams.map((income) => (
                  <TableRow key={income.id}>
                    <TableCell>{income.name}</TableCell>
                    <TableCell>{formatCurrency(income.amount)}</TableCell>
                    <TableCell className="capitalize">{income.frequency}</TableCell>
                    <TableCell className="text-right">
                      <Button variant="ghost" size="icon-sm" onClick={() => openEditDialog(income)}>
                        <Pencil />
                      </Button>
                      <Button variant="ghost" size="icon-sm" onClick={() => handleDelete(income.id)}>
                        <Trash2 />
                      </Button>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          )}
        </CardContent>
      </Card>
    </div>
  )
}

export default IncomeStreams
