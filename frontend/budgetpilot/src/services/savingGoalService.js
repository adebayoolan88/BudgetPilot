export const savingGoalService = {
  getAllForUser: async (api, userId) => {
    const response = await api.get(`/api/saving-goals/user/${userId}`)
    return response.data
  },

  create: async (api, savingGoal) => {
    const response = await api.post("/api/saving-goals", savingGoal)
    return response.data
  },

  update: async (api, id, savingGoal) => {
    const response = await api.put(`/api/saving-goals/${id}`, savingGoal)
    return response.data
  },

  delete: async (api, id) => {
    await api.delete(`/api/saving-goals/${id}`)
  }
}
