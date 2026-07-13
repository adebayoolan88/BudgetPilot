export const aiConversationService = {
  getAllForUser: async (api, userId) => {
    const response = await api.get(`/api/conversations/user/${userId}`)
    return response.data
  },

  create: async (api, userId, message) => {
    const response = await api.post(`/api/conversations/user/${userId}`, { message })
    return response.data
  }
}
