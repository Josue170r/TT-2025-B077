import axios from 'axios'
import store from '../store'
import router from '../router'


const baseURL = import.meta.env.DEV
  ? 'http://127.0.0.1:8080/api/'
  : import.meta.env.VITE_API_BACK
  
axios.defaults.baseURL = baseURL

// Token Refresh
let isAlreadyFetchingAccessToken = false
let subscribers = []

function onAccessTokenFetched(accessToken) {
  subscribers = subscribers.filter((callback) => callback(accessToken))
}

function addSubscriber(callback) {
  subscribers.push(callback)
}

// Request interceptor
axios.interceptors.request.use(
  (config) => {
    const tokenAccess = store.state['auth']['tokenAccess']
    switch (config.url) {
      case '/auth/login':
      case '/auth/verify-account':
      case '/auth/refresh':
      case '/auth/recover-password':
        return config
    }
    if (tokenAccess) {
      config.headers.Authorization = `Bearer ${tokenAccess}`
    }
    config.headers['Accept-Language'] = 'es-es'
    return config
  },
  (error) => {
    Promise.reject(error)
  }
)
// Response interceptor
axios.interceptors.response.use(
  function (response) {
    return response
  },
  function (error) {
    const { config, response } = error
    const originalRequest = config

    if (
      response &&
      response.status === 401 &&
      originalRequest.url === `/auth/refresh`
    ) {
      store.commit('auth/clearTokens')
      isAlreadyFetchingAccessToken = false
      router
        .push({
          name: 'authLogin',
        })
        .catch(() => {})
      return Promise.reject(error)
    } else if (
      response &&
      response.status === 401 &&
      config.url !== '/auth/login'
    ) {
      if (!isAlreadyFetchingAccessToken) {
        isAlreadyFetchingAccessToken = true
        store.dispatch('auth/refreshToken').then((accessToken) => {
          isAlreadyFetchingAccessToken = false
          onAccessTokenFetched(accessToken)
        })
      }

      const retryOriginalRequest = new Promise((resolve) => {
        addSubscriber(() => {
          resolve(axios(originalRequest))
        })
      })
      return retryOriginalRequest
    }

    return Promise.reject(error)
  }
)
export default axios;
