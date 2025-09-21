import axios from "axios";

export async function fetchNearbyPlaces({ commit }, { latitude, longitude, types }) {
  commit(
      'setLoading',
      {
      isLoading: true,
      msg: 'Buscando lugares cercanos',
      },
      { root: true }
  )
  return new Promise((resolve, reject) => {
      axios
      .post('/place/nearby-search', {
          latitude,
          longitude,
          types
      })
      .then((response) => {
        const data = response.data
        commit('setPlaceIds', data.data)
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

export async function fetchPreferencePlaces({ commit }, { latitude, longitude }) {
  commit(
    'setLoading',
    {
      isLoading: true,
      msg: 'Cargando lugares según tus preferencias',
    },
    { root: true }
  )
  return new Promise((resolve, reject) => {
    axios
      .post('/place/nearby-preferences', {
        latitude,
        longitude
      })
      .then((response) => {
        const data = response.data
        commit('setPlaceIds', data.data)
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

export async function fetchPlacesByIds({ commit }, { place_ids, page = 0, size = 10}) {
  commit(
    'setLoading',
    {
      isLoading: true,
      msg: 'Cargando información de lugares',
    },
    { root: true }
  )
  return new Promise((resolve, reject) => {
    axios
      .post(`/place/allById?page=${page}&size=${size}`, {
        place_ids
      })
      .then((response) => {
        const data = response.data
        commit('setPlaces', data.content)
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

export async function fetchPlaceDetails({ commit, state }) {
  commit(
    'setLoading',
    {
      isLoading: true,
      msg: 'Cargando detalles del lugar',
    },
    { root: true }
  )
  return new Promise((resolve, reject) => {
    axios
      .get(`/place/details?place_id=${state.selectedPlaceId}`)
      .then((response) => {
        const data = response.data
        commit('setSelectedPlaceDetails', data.data)
        commit('setSelectedPlaceName', data.data.name)
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

// eslint-disable-next-line no-unused-vars
export async function searchPlacesByName({ commit }, input) {
  return new Promise((resolve, reject) => {
    axios
      .post('/place/search-by-name', { input })
      .then((response) => {
        resolve(response)
      })
      .catch((error) => {
        reject(error)
      })
  })
}

export async function submitPlaceReview({ commit }, { placeId, reviewData }) {
  commit(
    'setLoading',
    {
      isLoading: true,
      msg: 'Enviando reseña',
    },
    { root: true }
  )
  return new Promise((resolve, reject) => {
    axios
      .post(`/reviews/${placeId}`, reviewData)
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

// eslint-disable-next-line no-unused-vars
export async function getPlaceWeather({ commit }, { lat, lng }) {
  return new Promise((resolve, reject) => {
    axios
      .get('/weather/coordinates', {
        params: {
          lat,
          lng
        }
      })
      .then((response) => {
        resolve(response.data)
      })
      .catch((error) => {
        reject(error)
      })
  })
}


