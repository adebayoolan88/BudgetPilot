export const userBudgetService = {
  getForUser: async (api, userId) => {
    try {
      const response = await api.get(`/api/user-budgets/user/${userId}`)
      return response.data
    } catch (error) {
      if (error.response?.status === 404) {
        return null
      }
      throw error
    }
  },

  create: async (api, userBudget) => {
    const response = await api.post("/api/user-budgets", userBudget)
    return response.data
  },

  update: async (api, userId, userBudget) => {
    const response = await api.put(`/api/user-budgets/${userId}`, userBudget)
    return response.data
  },

  getStrategies: async (api) => {
    const response = await api.get("/api/user-budgets/strategies")
    return response.data
  }
}
