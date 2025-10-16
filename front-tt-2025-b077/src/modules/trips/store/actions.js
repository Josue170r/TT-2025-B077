import axios from 'axios'

export async function fetchFavorites({ commit }) {
  return new Promise((resolve, reject) => {
    axios
      .get('/favorites')
      .then((response) => {
        const data = response.data
        commit('setFavoritesIds', data)
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

export async function fetchHotels({ commit, state }, { page = 0, size = 10 }) {
  commit(
    'setLoading',
    {
      isLoading: true,
      msg: 'Buscando hoteles',
    },
    { root: true },
  )
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
