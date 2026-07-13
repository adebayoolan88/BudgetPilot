export const userService = {
  getByClerkId: async (api, clerkId) => {
    const response = await api.get(`/api/users/clerk/${clerkId}`)
    return response.data
  },

  create: async (api, user) => {
    const response = await api.post("/api/users", user)
    return response.data
  },

  update: async (api, id, user) => {
    const response = await api.put(`/api/users/${id}`, user)
    return response.data
  }
}
