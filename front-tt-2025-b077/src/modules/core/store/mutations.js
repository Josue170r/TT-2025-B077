export default {
  setPlaces(state, places) {
    state.places = places
  },
  setPlaceIds(state, ids) {
    state.placeIds = ids
  },
  setPagination(state, paginationData) {
    state.pagination = {
      pageNumber: paginationData.pageable.pageNumber,
      pageSize: paginationData.pageable.pageSize,
      totalElements: paginationData.totalElements,
      totalPages: paginationData.totalPages,
      first: paginationData.first,
      last: paginationData.last,
      numberOfElements: paginationData.numberOfElements,
      empty: paginationData.empty,
    }
  },
  setSelectedPlaceId(state, placeId) {
    state.selectedPlaceId = placeId
  },
  setSelectedPlaceDetails(state, data) {
    state.selectedPlaceDetails = data
  },
  setSelectedPlaceName(state, data) {
    state.selectedPlaceName = data
  },
}
