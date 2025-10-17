export function favoriteIds(state) {
  return state.favoriteIds
}

export function isFavorite(state) {
  return (placeId) => state.favoriteIds.includes(placeId)
}

export function totalFavorites(state) {
  return state.favoriteIds.length
}

export function states(state) {
  return state.states
}

export function certifications(state) {
  return state.certifications
}

export function settlements(state) {
  return state.settlements
}

export function hotels(state) {
  return state.hotels
}

export function hotelIds(state) {
  return state.hotelIds
}

export function newItinerary(state) {
  return state.newItinerary
}

export function selectedHotelId(state) {
  return state.newItinerary.hotelPlaceId
}

export function isCertificatedHotel(state) {
  return state.newItinerary.isCertificatedHotel
}

export function pagination(state) {
  return state.pagination
}

export function currentPage(state) {
  return state.pagination.pageNumber + 1
}

export function totalPages(state) {
  return state.pagination.totalPages
}

export function totalHotels(state) {
  return state.pagination.totalElements
}

export function hasNextPage(state) {
  return !state.pagination.last
}

export function hasPreviousPage(state) {
  return !state.pagination.first
}

export function filters(state) {
  return state.filters
}

export function activeFiltersCount(state) {
  let count = 0
  if (state.filters.certifications.length > 0) count += state.filters.certifications.length
  if (state.filters.settlement) count++
  if (state.filters.useLocation) count++
  return count
}

export function textSearchMode(state) {
  return state.textSearchMode
}

export function textSearchQuery(state) {
  return state.textSearchQuery
}

export function textSearchPlaceId(state) {
  return state.textSearchPlaceId
}

export function userItineraries(state) {
  return state.userItineraries
}

export function currentItinerary(state) {
  return state.currentItinerary
}

export function currentItineraryId(state) {
  return state.currentItineraryId
}

export function itineraryDays(state) {
  return state.currentItinerary?.itineraryDays || []
}

export function totalItineraries(state) {
  return state.userItineraries.length
}