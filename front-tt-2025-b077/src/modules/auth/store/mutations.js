export default {
  setUserTokens: (state, { tokenAccess, tokenRefresh }) => {
    state.tokenAccess = tokenAccess
    state.tokenRefresh = tokenRefresh
  },
  setTokenAccess: (state, tokenAccess) => {
    state.tokenAccess = tokenAccess
  },
  clearTokens: (state) => {
    state.tokenAccess = null
    state.tokenRefresh = null
  },
  setUser: (state, user) => {
    state.user = user
  },
  logout: (state) => {
    state.tokenAccess = null
    state.tokenRefresh = null
    state.user = null
    window.localStorage.clear()
  },
}
