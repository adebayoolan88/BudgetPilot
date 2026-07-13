import axios from "axios"
import { useEffect } from "react"
import { useAuth } from "@clerk/react"

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
})

export const useApi = () => {
  const { getToken } = useAuth()

  useEffect(() => {
    const interceptorId = api.interceptors.request.use(async (config) => {
      const token = await getToken()
      if (token) {
        config.headers.Authorization = `Bearer ${token}`
      }
      return config
    })

    return () => api.interceptors.request.eject(interceptorId)
  }, [getToken])

  return api
}
