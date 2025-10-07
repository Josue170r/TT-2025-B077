export default function () {
  return {
    states: [],
    certifications: [],
    settlements: [],
    hotels: [],
    newItinerary: {
      tripTitle: '',
      selectedState: '',
      startDate: '',
      endDate: '',
    },
    pagination: {
      pageNumber: 0,
      pageSize: 10,
      totalElements: 0,
      totalPages: 0,
      first: true,
      last: true,
      numberOfElements: 0,
      empty: true,
    },
    filters: {
      certifications: [],
      settlement: null,
      useLocation: false,
      latitude: null,
      longitude: null,
    },
  }
}
