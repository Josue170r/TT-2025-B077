export default {
  setFavoritesIds(state, data) {
    state.favoriteIds = (data?.content ?? []).map((fav) => fav?.place?.id).filter(Boolean)
  },

  toggleFavorite(state, placeId) {
    const index = state.favoriteIds.indexOf(placeId)
    if (index > -1) {
      state.favoriteIds.splice(index, 1)
    } else {
      state.favoriteIds.push(placeId)
    }
  },

  setStates(state, states) {
    state.states = states
  },

  setTripTitle(state, title) {
    state.newItinerary.tripTitle = title
  },

  setSelectedState(state, stateId) {
    state.newItinerary.selectedState = stateId
  },

  setStartDate(state, date) {
    state.newItinerary.startDate = date ? date.toISOString() : null
  },

  setEndDate(state, date) {
    state.newItinerary.endDate = date ? date.toISOString() : null
  },

  setCertifications(state, certifications) {
    state.certifications = certifications
  },

  setSettlements(state, settlements) {
    state.settlements = settlements
  },

  setHotels(state, hotels) {
    state.hotels = hotels
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

  setFilterCertifications(state, certifications) {
    state.filters.certifications = certifications
  },

  setFilterSettlement(state, settlement) {
    state.filters.settlement = settlement
  },

  setFilterUseLocation(state, useLocation) {
    state.filters.useLocation = useLocation
  },

  setFilterCoordinates(state, { latitude, longitude }) {
    state.filters.latitude = latitude
    state.filters.longitude = longitude
  },

  clearFilters(state) {
    state.filters = {
      certifications: [],
      settlement: null,
      useLocation: false,
      latitude: null,
      longitude: null,
    }
  },
}
