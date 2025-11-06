export default {
  setFavoritesIds(state, data) {
    state.favoriteIds = data
  },

  setFavoritePlaces(state, places) {
    state.favoritePlaces = places
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

  setSelectedHotel(state, { hotelPlaceId, isCertificatedHotel }) {
    state.newItinerary.hotelPlaceId = hotelPlaceId
    state.newItinerary.isCertificatedHotel = isCertificatedHotel
  },

  clearSelectedHotel(state) {
    state.newItinerary.hotelPlaceId = null
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

  setHotelIds(state, hotelIds) {
    state.hotelIds = hotelIds
  },

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

  setTextSearchMode(state, isActive) {
    state.textSearchMode = isActive
  },

  setTextSearchQuery(state, query) {
    state.textSearchQuery = query
  },

  setTextSearchPlaceId(state, placeId) {
    state.textSearchPlaceId = placeId
  },

  clearTextSearch(state) {
    state.textSearchMode = false
    state.textSearchQuery = ''
    state.textSearchPlaceId = null
  },

  setUserItineraries(state, itineraries) {
    state.userItineraries = itineraries
  },

  setCurrentItinerary(state, itinerary) {
    state.currentItinerary = itinerary
  },

  setCurrentItineraryId(state, itineraryId) {
    state.currentItineraryId = itineraryId
  },

  updateItineraryDay(state, { dayId, updatedDay }) {
    if (state.currentItinerary) {
      const dayIndex = state.currentItinerary.itineraryDays.findIndex(d => d.id === dayId)
      if (dayIndex !== -1) {
        state.currentItinerary.itineraryDays[dayIndex] = updatedDay
      }
    }
  },

  updateItineraryPlace(state, { dayId, placeId, updatedPlace }) {
    if (state.currentItinerary) {
      const day = state.currentItinerary.itineraryDays.find(d => d.id === dayId)
      if (day) {
        const placeIndex = day.places.findIndex(p => p.id === placeId)
        if (placeIndex !== -1) {
          day.places[placeIndex] = updatedPlace
        }
      }
    }
  },

  deleteItinerayById(state, { itineraryId }) {
    const index = state.userItineraries.findIndex((itinerary) => {
      return itinerary.id == itineraryId
    })
    if (index != -1) {
      state.userItineraries.splice(index, 1)
    }
  },

  clearCurrentItinerary(state) {
    state.currentItinerary = null
  },

  clearNewItinerary(state) {
    state.newItinerary = {
      tripTitle: '',
      selectedState: '',
      startDate: '',
      endDate: '',
      hotelPlaceId: null,
      isCertificatedHotel: false,
    }
  },
}