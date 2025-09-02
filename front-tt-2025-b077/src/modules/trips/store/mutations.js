export default {
  setFavoritesIds(state, data) {
    state.favoriteIds = (data?.content ?? [])
      .map(fav => fav?.place?.id)
      .filter(Boolean);
  },
  toggleFavorite(state, placeId) {
    const index = state.favoriteIds.indexOf(placeId)
    if (index > -1) {
      state.favoriteIds.splice(index, 1)
    } else {
      state.favoriteIds.push(placeId)
    }
  }
}