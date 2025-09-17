import axios from "axios";

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
    { root: true }
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