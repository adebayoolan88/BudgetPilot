import { useEffect, useState } from "react"
import { Navigate, NavLink, Outlet } from "react-router-dom"
import { useAuth, useUser, UserButton } from "@clerk/react"
import { useApi } from "@/services/api"
import { userService } from "@/services/userService"
import { CurrentUserContext } from "@/context/CurrentUserContext"
import { cn } from "@/lib/utils"

const NAV_LINKS = [
  { to: "/dashboard", label: "Dashboard" },
  { to: "/income", label: "Income" },
  { to: "/expenses", label: "Expenses" },
  { to: "/transactions", label: "Transactions" },
  { to: "/goals", label: "Goals" },
  { to: "/ai", label: "AI Assistant" },
]

function AppLayout() {
  const { isLoaded, isSignedIn } = useAuth()
  const { user } = useUser()
  const api = useApi()
  const [currentUser, setCurrentUser] = useState(null)
  const [status, setStatus] = useState("loading")

  useEffect(() => {
    if (!isSignedIn || !user) return

    let cancelled = false

    async function syncUser() {
      try {
        let backendUser
        try {
          backendUser = await userService.getByClerkId(api, user.id)
        } catch (error) {
          if (error.response?.status === 404) {
            backendUser = await userService.create(api, {
              email: user.primaryEmailAddress?.emailAddress ?? "",
              firstName: user.firstName ?? "",
              lastName: user.lastName ?? "",
            })
          } else {
            throw error
          }
        }
        if (!cancelled) {
          setCurrentUser(backendUser)
          setStatus("ready")
        }
      } catch {
        if (!cancelled) setStatus("error")
      }
    }

    syncUser()
    return () => {
      cancelled = true
    }
  }, [isSignedIn, user, api])

  if (!isLoaded) return null
  if (!isSignedIn) return <Navigate to="/" replace />

  if (status === "loading") {
    return (
      <div className="min-h-screen flex items-center justify-center text-muted-foreground">
        Loading your account...
      </div>
    )
  }

  if (status === "error") {
    return (
      <div className="min-h-screen flex items-center justify-center text-destructive">
        Something went wrong loading your account. Please refresh.
      </div>
    )
  }

  return (
    <CurrentUserContext.Provider value={currentUser}>
      <div className="min-h-screen bg-background flex flex-col">
        <header className="border-b border-border sticky top-0 bg-background z-40">
          <div className="max-w-7xl mx-auto px-6 h-16 flex items-center justify-between gap-6">
            <span className="font-bold text-lg shrink-0">BudgetPilot</span>
            <nav className="flex items-center gap-6 overflow-x-auto">
              {NAV_LINKS.map((link) => (
                <NavLink
                  key={link.to}
                  to={link.to}
                  className={({ isActive }) =>
                    cn(
                      "text-sm whitespace-nowrap transition-colors",
                      isActive ? "text-foreground font-medium" : "text-muted-foreground hover:text-foreground"
                    )
                  }
                >
                  {link.label}
                </NavLink>
              ))}
            </nav>
            <UserButton />
          </div>
        </header>
        <main className="flex-1 max-w-7xl mx-auto w-full px-6 py-8">
          <Outlet />
        </main>
      </div>
    </CurrentUserContext.Provider>
  )
}

export default AppLayout
