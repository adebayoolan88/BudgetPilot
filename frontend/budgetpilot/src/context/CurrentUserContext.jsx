import { createContext, useContext } from "react"

export const CurrentUserContext = createContext(null)

export function useCurrentUser() {
  const currentUser = useContext(CurrentUserContext)
  if (!currentUser) {
    throw new Error("useCurrentUser must be used within AppLayout")
  }
  return currentUser
}
