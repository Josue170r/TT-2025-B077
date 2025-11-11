import axios from 'axios'
import store from '@/store'

export async function updateUser({ commit }, userData) {
  commit(
    'setLoading',
    {
      isLoading: true,
      msg: 'Actualizando datos del usuario',
    },
    { root: true },
  )
  const user = store.state['auth']['user']
  return new Promise((resolve, reject) => {
    axios
      .patch(`/user/${user.id}`, userData)
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

export async function uploadUserPicture({ commit }, file) {
  commit(
    'setLoading',
    {
      isLoading: true,
      msg: 'Actualizando foto de perfil',
    },
    { root: true },
  )
  const formData = new FormData()
  formData.append('file', file)
  return new Promise((resolve, reject) => {
    axios
      .put('/user/profile/picture', formData, {
        headers: { 'Content-Type': 'multipart/form-data' },
      })
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

export async function changePassword({ commit }, data) {
  commit(
    'setLoading',
    {
      isLoading: true,
      msg: 'Cambiando contraseña',
    },
    { root: true },
  )
  return new Promise((resolve, reject) => {
    axios
      .post('/user/change-password', data)
      .then((response) => {
        resolve(response)
      })
      .catch((error) => {
        reject(error)
      })
      .finally(() => {
        commit('setLoading', false, { root: true })
      })
  })
}

export async function fetchCategoryPlaces({ commit }, data) {
  return new Promise((resolve, reject) => {
    axios
      .get('/catalogs/category-places', data)
      .then((response) => {
        resolve(response)
      })
      .catch((error) => {
        reject(error)
      })
      .finally(() => {
        commit('setLoading', false, { root: true })
      })
  })
}

export async function fetchUserPreferences({ commit }) {
  commit(
    'setLoading',
    {
      isLoading: true,
      msg: 'Cargando preferencias',
    },
    { root: true },
  )
  return new Promise((resolve, reject) => {
    axios
      .get('/preferences')
      .then((response) => {
        resolve(response)
      })
      .catch((error) => {
        reject(error)
      })
      .finally(() => {
        commit('setLoading', false, { root: true })
      })
  })
}

export async function saveUserPreferences({ commit }, data) {
  commit(
    'setLoading',
    {
      isLoading: true,
      msg: 'Guardando preferencias',
    },
    { root: true },
  )
  return new Promise((resolve, reject) => {
    axios
      .post('/preferences', data)
      .then((response) => {
        resolve(response)
      })
      .catch((error) => {
        reject(error)
      })
      .finally(() => {
        commit('setLoading', false, { root: true })
      })
  })
}

export async function fetchCarbonActivities({ commit }, { startDate = null, endDate = null } = {}) {
  commit(
    'setLoading',
    {
      isLoading: true,
      msg: 'Cargando actividades de carbono',
    },
    { root: true },
  )
  return new Promise((resolve, reject) => {
    axios
      .get('/carbon-activity', {
        params: { startDate, endDate },
      })
      .then((response) => {
        commit('setActivities', response.data.data || [])
        resolve(response)
      })
      .catch((error) => {
        reject(error)
      })
      .finally(() => {
        commit('setLoading', false, { root: true })
      })
  })
}

export async function fetchTotalCo2({ commit }, { startDate = null, endDate = null } = {}) {
  commit(
    'setLoading',
    {
      isLoading: true,
      msg: 'Calculando total de CO₂',
    },
    { root: true },
  )
  return new Promise((resolve, reject) => {
    axios
      .get('/carbon-activity/total-co2', {
        params: { startDate, endDate },
      })
      .then((response) => {
        commit('setTotalCo2', response.data.data || 0)
        resolve(response)
      })
      .catch((error) => {
        reject(error)
      })
      .finally(() => {
        commit('setLoading', false, { root: true })
      })
  })
}

