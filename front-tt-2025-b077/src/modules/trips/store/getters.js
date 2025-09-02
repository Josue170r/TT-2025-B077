export function favoriteIds(state) {
  return state.favoriteIds
}

export function isFavorite(state) {
  return (placeId) => state.favoriteIds.includes(placeId)
}

export function totalFavorites(state) {
  return state.favoriteIds.length
}