export const categoryService = {
  getDefaults: async (api) => {
    const response = await api.get("/api/categories/defaults")
    return response.data
  }
}
