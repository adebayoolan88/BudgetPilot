export const expenseService = {
  getAllForUser: async (api, userId) => {
    const response = await api.get(`/api/expenses/user/${userId}`)
    return response.data
  },

  create: async (api, expense) => {
    const response = await api.post("/api/expenses", expense)
    return response.data
  },

  update: async (api, id, expense) => {
    const response = await api.put(`/api/expenses/${id}`, expense)
    return response.data
  },

  delete: async (api, id) => {
    await api.delete(`/api/expenses/${id}`)
  }
}