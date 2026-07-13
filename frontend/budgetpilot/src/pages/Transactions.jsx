import { useEffect, useState } from "react"
import { useApi } from "@/services/api"
import { transactionService } from "@/services/transactionService"
import { categoryService } from "@/services/categoryService"
import { useCurrentUser } from "@/context/CurrentUserContext"
import { formatCurrency } from "@/lib/format"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import { Trash2, Wrench, Plus } from "lucide-react"

const emptyForm = { categoryId: "", amount: "", description: "", date: "" }

function Transactions() {
  const api = useApi()
  const currentUser = useCurrentUser()
  const [transactions, setTransactions] = useState([])
  const [categories, setCategories] = useState([])
  const [loading, setLoading] = useState(true)
  const [createOpen, setCreateOpen] = useState(false)
  const [fixOpen, setFixOpen] = useState(false)
  const [fixingId, setFixingId] = useState(null)
  const [form, setForm] = useState(emptyForm)
  const [saving, setSaving] = useState(false)

  async function loadData() {
    const [transactionData, categoryData] = await Promise.all([
      transactionService.getAllForUser(api, currentUser.id),
      categoryService.getDefaults(api),
    ])
    setTransactions(transactionData)
    setCategories(categoryData)
    setLoading(false)
  }

  useEffect(() => {
    // eslint-disable-next-line react-hooks/set-state-in-effect -- fetch-on-mount
    loadData()
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [])

  function openCreateDialog() {
    setForm({ ...emptyForm, categoryId: categories[0]?.id ?? "", date: new Date().toISOString().slice(0, 10) })
    setCreateOpen(true)
  }

  function openFixDialog(transaction) {
    setFixingId(transaction.id)
    setForm({
      categoryId: transaction.category?.id ?? "",
      amount: transaction.amount,
      description: transaction.description ?? "",
      date: transaction.date,
    })
    setFixOpen(true)
  }

  async function handleCreate(event) {
    event.preventDefault()
    setSaving(true)
    try {
      await transactionService.create(api, {
        userId: currentUser.id,
        categoryId: form.categoryId,
        amount: Number(form.amount),
        description: form.description,
        date: form.date,
      })
      setCreateOpen(false)
      await loadData()
    } finally {
      setSaving(false)
    }
  }

  async function handleFix(event) {
    event.preventDefault()
    setSaving(true)
    try {
      await transactionService.update(api, fixingId, {
        categoryId: form.categoryId,
        amount: Number(form.amount),
        description: form.description,
        date: form.date,
      })
      setFixOpen(false)
      await loadData()
    } finally {
      setSaving(false)
    }
  }

  async function handleDelete(id) {
    await transactionService.delete(api, id)
    await loadData()
  }

  const total = transactions.reduce((sum, transaction) => sum + Number(transaction.amount), 0)

  return (
    <div className="flex flex-col gap-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-semibold">Transactions</h1>
          <p className="text-muted-foreground text-sm">A record of money that has already moved.</p>
        </div>
        <Dialog open={createOpen} onOpenChange={setCreateOpen}>
          <DialogTrigger asChild>
            <Button onClick={openCreateDialog} disabled={categories.length === 0}>
              <Plus /> Add Transaction
            </Button>
          </DialogTrigger>
          <DialogContent>
            <form onSubmit={handleCreate} className="flex flex-col gap-4">
              <DialogHeader>
                <DialogTitle>Add Transaction</DialogTitle>
              </DialogHeader>
              <TransactionFields form={form} setForm={setForm} categories={categories} />
              <DialogFooter>
                <Button type="submit" disabled={saving}>
                  {saving ? "Saving..." : "Save"}
                </Button>
              </DialogFooter>
            </form>
          </DialogContent>
        </Dialog>
      </div>

      <Dialog open={fixOpen} onOpenChange={setFixOpen}>
        <DialogContent>
          <form onSubmit={handleFix} className="flex flex-col gap-4">
            <DialogHeader>
              <DialogTitle>Fix Transaction</DialogTitle>
              <DialogDescription>
                For correcting a data-entry mistake only — transactions represent money that has already moved.
              </DialogDescription>
            </DialogHeader>
            <TransactionFields form={form} setForm={setForm} categories={categories} />
            <DialogFooter>
              <Button type="submit" disabled={saving}>
                {saving ? "Saving..." : "Save correction"}
              </Button>
            </DialogFooter>
          </form>
        </DialogContent>
      </Dialog>

      <Card>
        <CardHeader>
          <CardTitle>Total: {formatCurrency(total)}</CardTitle>
        </CardHeader>
        <CardContent>
          {loading ? (
            <p className="text-muted-foreground text-sm">Loading...</p>
          ) : transactions.length === 0 ? (
            <p className="text-muted-foreground text-sm">No transactions yet.</p>
          ) : (
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>Date</TableHead>
                  <TableHead>Description</TableHead>
                  <TableHead>Category</TableHead>
                  <TableHead>Amount</TableHead>
                  <TableHead className="text-right">Actions</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {transactions.map((transaction) => (
                  <TableRow key={transaction.id}>
                    <TableCell>{transaction.date}</TableCell>
                    <TableCell>{transaction.description || "-"}</TableCell>
                    <TableCell>{transaction.category?.name}</TableCell>
                    <TableCell>{formatCurrency(transaction.amount)}</TableCell>
                    <TableCell className="text-right">
                      <Button variant="ghost" size="icon-sm" title="Fix a mistake" onClick={() => openFixDialog(transaction)}>
                        <Wrench />
                      </Button>
                      <Button variant="ghost" size="icon-sm" onClick={() => handleDelete(transaction.id)}>
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

function TransactionFields({ form, setForm, categories }) {
  return (
    <>
      <div className="flex flex-col gap-2">
        <Label>Category</Label>
        <Select value={form.categoryId} onValueChange={(value) => setForm({ ...form, categoryId: value })}>
          <SelectTrigger className="w-full">
            <SelectValue placeholder="Select a category" />
          </SelectTrigger>
          <SelectContent>
            {categories.map((category) => (
              <SelectItem key={category.id} value={category.id}>
                {category.name}
              </SelectItem>
            ))}
          </SelectContent>
        </Select>
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
        <Label htmlFor="description">Description</Label>
        <Input
          id="description"
          placeholder="e.g. Grocery run"
          value={form.description}
          onChange={(e) => setForm({ ...form, description: e.target.value })}
        />
      </div>
      <div className="flex flex-col gap-2">
        <Label htmlFor="date">Date</Label>
        <Input
          id="date"
          type="date"
          value={form.date}
          onChange={(e) => setForm({ ...form, date: e.target.value })}
          required
        />
      </div>
    </>
  )
}

export default Transactions
