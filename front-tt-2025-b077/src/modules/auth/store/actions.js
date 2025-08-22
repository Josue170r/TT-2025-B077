import axios from "axios";

export async function login({ commit }, user) {
  // Set loading page animation
  commit(
    'setLoading',
    {
      isLoading: true,
      msg: 'Iniciando sesión',
    },
    { root: true }
  )

  return new Promise((resolve, reject) => {
    axios
      .post('/auth/login', user)
      .then((response) => {
        const data = response['data']
        const tokenAccess = data['accessToken']
        const tokenRefresh = data['refreshToken']
        commit('setUserTokens', {
          tokenAccess,
          tokenRefresh,
        })
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

export async function refreshToken({ commit, state }) {
  return new Promise((resolve, reject) => {
    axios
      .post('/auth/refresh', { refreshToken: state.tokenRefresh })
      .then((response) => {
        state.tokenAccess = response.data.accessToken
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

export async function forgotPassword({ commit }, email) {
  // Set loading page animation
  commit(
    'setLoading',
    {
      isLoading: true,
      msg: 'Enviando Correo',
    },
    { root: true }
  )

  return new Promise((resolve, reject) => {
    axios
      .post('/auth/forgot-password', { email })
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

export async function logout({ commit }) {
  commit('logout')
  window.location.reload()
}

export async function singUp({ commit }, data) {
  // Set loading page animation
  commit(
    'setLoading',
    {
      isLoading: true,
      msg: 'Registrando usuario',
    },
    { root: true }
  )
  return new Promise((resolve, reject) => {
    axios
      .post('/auth/register', data)
      .then((response) => {
        resolve(response)
      })
      .catch((error) => {
        reject(error)
      })
      .finally(() => {
        // Finish loading page animation
        commit('setLoading', false, { root: true })
      })
  })
}

export async function recoverPassword({ commit }, data) {
  // Set loading page animation
  commit(
    'setLoading',
    {
      isLoading: true,
      msg: 'Creando Contraseña',
    },
    { root: true }
  )
  return new Promise((resolve, reject) => {
    axios
      .post('/auth/recover-password', data)
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

export async function userActivation({ commit }, token) {
  // Set loading page animation
  commit(
    'setLoading',
    {
      isLoading: true,
      msg: 'Activando cuenta',
    },
    { root: true }
  )
  return new Promise((resolve, reject) => {
    axios
      .post('/auth/verify-account', { token })
      .then((response) => {
        resolve(response)
      })
      .catch((error) => {
        reject(error)
      })
      .finally(() => {
        // Finish loading page animation
        commit('setLoading', false, { root: true })
      })
  })
}

export async function getProfile() {
  return new Promise((resolve, reject) => {
    axios
      .get('/user/profile')
      .then((response) => {
        resolve(response.data)
      })
      .catch((error) => {
        reject(error)
      })
  })
}