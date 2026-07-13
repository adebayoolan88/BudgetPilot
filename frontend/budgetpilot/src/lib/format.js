const currencyFormatter = new Intl.NumberFormat("en-US", {
  style: "currency",
  currency: "USD",
})

export function formatCurrency(amount) {
  const value = Number(amount)
  return currencyFormatter.format(Number.isFinite(value) ? value : 0)
}

const MONTHLY_MULTIPLIER = {
  weekly: 52 / 12,
  biweekly: 26 / 12,
  monthly: 1,
  yearly: 1 / 12,
}

export function toMonthlyAmount(amount, frequency) {
  const value = Number(amount)
  if (!Number.isFinite(value)) return 0
  const multiplier = MONTHLY_MULTIPLIER[frequency] ?? 1
  return value * multiplier
}

export const FREQUENCY_OPTIONS = [
  { value: "weekly", label: "Weekly" },
  { value: "biweekly", label: "Biweekly" },
  { value: "monthly", label: "Monthly" },
  { value: "yearly", label: "Yearly" },
]
