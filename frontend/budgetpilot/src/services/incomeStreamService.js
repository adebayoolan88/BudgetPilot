export const incomeStreamService = {
  getAllForUser: async (api, userId) => {
    const response = await api.get(`/api/income-streams/user/${userId}`)
    return response.data
  },

  create: async (api, incomeStream) => {
    const response = await api.post("/api/income-streams", incomeStream)
    return response.data
  },

  update: async (api, id, incomeStream) => {
    const response = await api.put(`/api/income-streams/${id}`, incomeStream)
    return response.data
  },

  delete: async (api, id) => {
    await api.delete(`/api/income-streams/${id}`)
  }
}
