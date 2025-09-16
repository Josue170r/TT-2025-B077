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

export async function searchPlacesByName(input) {
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