export const transactionService = {
  getAllForUser: async (api, userId) => {
    const response = await api.get(`/api/transactions/user/${userId}`)
    return response.data
  },

  create: async (api, transaction) => {
    const response = await api.post("/api/transactions", transaction)
    return response.data
  },

  update: async (api, id, transaction) => {
    const response = await api.put(`/api/transactions/${id}`, transaction)
    return response.data
  },

  delete: async (api, id) => {
    await api.delete(`/api/transactions/${id}`)
  }
}
