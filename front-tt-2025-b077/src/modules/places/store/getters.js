export function places(state) {
  return state.places
}

export function placesIds(state) {
  return state.placeIds
}

export function pagination(state) {
  return state.pagination
}

export function totalPlaces(state) {
  return state.pagination.totalElements
}

export function currentPage(state) {
  return state.pagination.pageNumber + 1
}

export function hasNextPage(state) {
  return !state.pagination.last
}

export function hasPreviousPage(state) {
  return !state.pagination.first
}
