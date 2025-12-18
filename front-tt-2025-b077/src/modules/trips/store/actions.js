import axios from 'axios'
import { acoApi } from '@/plugins/axios'

export async function fetchFavoriteIds({ commit }) {
  return new Promise((resolve, reject) => {
    axios
      .get('/favorites/ids')
      .then((response) => {
        const data = response.data
        commit('setFavoritesIds', data.data)
        resolve()
      })
      .catch((error) => {
        reject(error)
      })
      .finally(() => {
        commit('setLoading', false, { root: true })
      })
  })
}

export async function fetchFavorites({ commit }, { page = 0, size = 10 } = {}) {
  commit(
    'setLoading',
    {
      isLoading: true,
      msg: 'Cargando favoritos',
    },
    { root: true },
  )
  return new Promise((resolve, reject) => {
    axios
      .get(`/favorites?page=${page}&size=${size}`)
      .then((response) => {
        const data = response.data
        resolve(data)
      })
      .catch((error) => {
        reject(error)
      })
      .finally(() => {
        commit('setLoading', false, { root: true })
      })
  })
}

export async function toggleFavoritePlace({ commit }, placeId) {
  commit(
    'setLoading',
    {
      isLoading: true,
      msg: 'Actualizando favorito',
    },
    { root: true },
  )
  return new Promise((resolve, reject) => {
    axios
      .post(`/favorites?place_id=${placeId}`)
      .then(() => {
        commit('toggleFavorite', placeId)
        resolve()
      })
      .catch((error) => {
        reject(error)
      })
      .finally(() => {
        commit('setLoading', false, { root: true })
      })
  })
}

export async function fetchStates({ commit }) {
  return new Promise((resolve, reject) => {
    axios
      .get('/catalogs/states')
      .then((response) => {
        const data = response.data.data
        commit('setStates', data)
        resolve()
      })
      .catch((error) => {
        reject(error)
      })
      .finally(() => {
        commit('setLoading', false, { root: true })
      })
  })
}

export async function fetchCertifications({ commit }) {
  return new Promise((resolve, reject) => {
    axios
      .get('/catalogs/certifications')
      .then((response) => {
        const data = response.data.data
        commit('setCertifications', data)
        resolve()
      })
      .catch((error) => {
        reject(error)
      })
      .finally(() => {
        commit('setLoading', false, { root: true })
      })
  })
}

export async function fetchSettlements({ commit }, stateId) {
  return new Promise((resolve, reject) => {
    axios
      .get(`/catalogs/settlements?state=${stateId}`)
      .then((response) => {
        const data = response.data.data
        commit('setSettlements', data)
        resolve()
      })
      .catch((error) => {
        reject(error)
      })
      .finally(() => {
        commit('setLoading', false, { root: true })
      })
  })
}

export async function fetchHotels({ commit, state }, { page = 0, size = 10, showLoading = true }) {
  if (showLoading) {
    commit(
      'setLoading',
      {
        isLoading: true,
        msg: 'Cargando hoteles',
      },
      { root: true },
    )
  }
  return new Promise((resolve, reject) => {
    const params = {
      state: state.newItinerary.selectedState,
      page,
      size,
    }
    if (state.filters.certifications.length > 0) {
      params.certifications = state.filters.certifications.join(',')
    }
    if (state.filters.useLocation && state.filters.latitude && state.filters.longitude) {
      params.latitude = state.filters.latitude
      params.longitude = state.filters.longitude
    } else if (state.filters.settlement) {
      params.settlement = state.filters.settlement
    }
    axios
      .get('/hotels/search', { params })
      .then((response) => {
        const data = response.data
        commit('setHotels', data.content)
        commit('setPagination', data)
        resolve()
      })
      .catch((error) => {
        reject(error)
      })
      .finally(() => {
        commit('setLoading', false, { root: true })
      })
  })
}

export async function searchHotelsByText({ commit }, { query, latitude, longitude }) {
  commit(
    'setLoading',
    {
      isLoading: true,
      msg: 'Buscando hoteles',
    },
    { root: true },
  )
  return new Promise((resolve, reject) => {
    axios
      .post('/place/search-by-text', {
        query: query,
        location: `${latitude},${longitude}`,
        radius: 3000,
        type: 'lodging',
        language: 'es',
        region: 'mx',
      })
      .then((response) => {
        const data = response.data.data
        resolve(data)
      })
      .catch((error) => {
        reject(error)
      })
      .finally(() => {
        commit('setLoading', false, { root: true })
      })
  })
}

export async function generateItinerary({ commit, state }) {
  commit(
    'setLoading',
    {
      isLoading: true,
      msg: 'Generando itinerario sostenible',
    },
    { root: true },
  )
  return new Promise((resolve, reject) => {
    acoApi
      .post('/itinerary/generate', state.newItinerary)
      .then((response) => {
        commit('clearNewItinerary')
        resolve(response.data)
      })
      .catch((error) => {
        reject(error)
      })
      .finally(() => {
        commit('setLoading', false, { root: true })
      })
  })
}

export async function fetchUserItineraries(
  { commit },
  { page = 0, size = 7, showLoading = true } = {},
) {
  if (showLoading) {
    commit(
      'setLoading',
      {
        isLoading: true,
        msg: 'Cargando itinerarios',
      },
      { root: true },
    )
  }
  return new Promise((resolve, reject) => {
    axios
      .get(`/itineraries/user?page=${page}&size=${size}`)
      .then((response) => {
        const data = response.data
        commit('setPagination', data)
        commit('setUserItineraries', data.content)
        resolve(data)
      })
      .catch((error) => {
        reject(error)
      })
      .finally(() => {
        if (showLoading) {
          commit('setLoading', false, { root: true })
        }
      })
  })
}

export async function fetchItineraryById({ commit }, { itineraryId, showLoading = true }) {
  if (showLoading) {
    commit(
      'setLoading',
      {
        isLoading: true,
        msg: 'Cargando itinerario',
      },
      { root: true },
    )
  }
  return new Promise((resolve, reject) => {
    axios
      .get(`/itineraries/${itineraryId}`)
      .then((response) => {
        const itinerary = response.data.data
        commit('setCurrentItinerary', itinerary)
        resolve(itinerary)
      })
      .catch((error) => {
        reject(error)
      })
      .finally(() => {
        commit('setLoading', false, { root: true })
      })
  })
}

export async function fetchItineraryDays({ commit }, itineraryId) {
  commit(
    'setLoading',
    {
      isLoading: true,
      msg: 'Cargando días del itinerario',
    },
    { root: true },
  )
  return new Promise((resolve, reject) => {
    axios
      .get(`/itineraries/${itineraryId}/days`)
      .then((response) => {
        const itinerary = response.data.data
        resolve(itinerary)
      })
      .catch((error) => {
        reject(error)
      })
      .finally(() => {
        commit('setLoading', false, { root: true })
      })
  })
}

export async function updateVisitOrder({ commit }, { itineraryId, dayId, placeIds }) {
  commit(
    'setLoading',
    {
      isLoading: true,
      msg: 'Actualizando orden',
    },
    { root: true },
  )
  return new Promise((resolve, reject) => {
    axios
      .put(`/itineraries/${itineraryId}/days/${dayId}/order`, { placeIds })
      .then((response) => {
        const data = response.data
        const updatedDay = data.data
        console.log(updatedDay)
        commit('updateItineraryDay', { dayId, updatedDay })
        resolve(updatedDay)
      })
      .catch((error) => {
        reject(error)
      })
      .finally(() => {
        commit('setLoading', false, { root: true })
      })
  })
}

export async function updatePlace({ commit }, { itineraryId, dayId, placeId, placeData }) {
  commit(
    'setLoading',
    {
      isLoading: true,
      msg: 'Actualizando lugar',
    },
    { root: true },
  )
  return new Promise((resolve, reject) => {
    axios
      .put(`/itineraries/${itineraryId}/days/${dayId}/places/${placeId}`, placeData)
      .then((response) => {
        const data = response.data
        const updatedPlace = data.data
        commit('updateItineraryPlace', { dayId, placeId, updatedPlace })
        resolve(data)
      })
      .catch((error) => {
        reject(error)
      })
      .finally(() => {
        commit('setLoading', false, { root: true })
      })
  })
}

export async function addPlaceToDay({ commit }, { itineraryId, dayId, placeData }) {
  commit(
    'setLoading',
    {
      isLoading: true,
      msg: 'Agregando lugar',
    },
    { root: true },
  )
  return new Promise((resolve, reject) => {
    axios
      .put(`/itineraries/${itineraryId}/days/${dayId}/places/add`, placeData)
      .then((response) => {
        resolve(response.data)
      })
      .catch((error) => {
        reject(error)
      })
      .finally(() => {
        commit('setLoading', false, { root: true })
      })
  })
}

export async function updatePlaceTime(
  { commit },
  { itineraryId, dayId, placeId, arrivalTime, leavingTime },
) {
  commit(
    'setLoading',
    {
      isLoading: true,
      msg: 'Actualizando horario',
    },
    { root: true },
  )
  return new Promise((resolve, reject) => {
    axios
      .put(`/itineraries/${itineraryId}/days/${dayId}/places/${placeId}/time`, {
        arrivalTime,
        leavingTime,
      })
      .then((response) => {
        const data = response.data
        const updatedPlace = data.data
        commit('updateItineraryPlace', { dayId, placeId, updatedPlace })
        resolve(data)
      })
      .catch((error) => {
        reject(error)
      })
      .finally(() => {
        commit('setLoading', false, { root: true })
      })
  })
}

export async function setPlaceVisited({ commit }, { itineraryId, dayId, placeId }) {
  commit(
    'setLoading',
    {
      isLoading: true,
      msg: 'Marcando lugar como visitado',
    },
    { root: true },
  )
  return new Promise((resolve, reject) => {
    axios
      .put(`/itineraries/${itineraryId}/days/${dayId}/places/${placeId}/visit`)
      .then((response) => {
        const data = response.data
        commit('setPlaceAsVisited', { dayId, placeId })
        resolve(data)
      })
      .catch((error) => {
        reject(error)
      })
      .finally(() => {
        commit('setLoading', false, { root: true })
      })
  })
}

export async function deletePlaceFromDay({ commit }, { itineraryId, dayId, placeId }) {
  commit(
    'setLoading',
    {
      isLoading: true,
      msg: 'Eliminando lugar',
    },
    { root: true },
  )
  return new Promise((resolve, reject) => {
    axios
      .delete(`/itineraries/${itineraryId}/days/${dayId}/places/${placeId}`)
      .then((response) => {
        const data = response.data
        commit('removePlaceFromDay', { dayId, placeId })
        resolve(data)
      })
      .catch((error) => {
        reject(error)
      })
      .finally(() => {
        commit('setLoading', false, { root: true })
      })
  })
}

export async function deleteItinerary({ commit }, itineraryId) {
  commit(
    'setLoading',
    {
      isLoading: true,
      msg: 'Eliminando itinerario',
    },
    { root: true },
  )
  return new Promise((resolve, reject) => {
    axios
      .delete(`/itineraries/${itineraryId}`)
      .then((response) => {
        resolve(response.data)
        commit('deleteItinerayById', { itineraryId })
      })
      .catch((error) => {
        reject(error)
      })
      .finally(() => {
        commit('setLoading', false, { root: true })
      })
  })
}
