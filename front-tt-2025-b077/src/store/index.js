import { createStore } from 'vuex'
import auth from '@/modules/auth/store'

import VuexPersistence from 'vuex-persist'

const vuexLocal = new VuexPersistence({
  storage: window.localStorage,
  key: 'vuex',
  reducer: (state) => {
    if (!state.auth.tokenAccess) {
      return null
    }
    return {
      auth: state.auth,
    }
  },
})
export default createStore({
  state: {
    activeLoading: false,
    loadingPageMsg: '',
  },
  mutations: {
    setLoading(state, { isLoading, msg }) {
      state.activeLoading = isLoading
      state.loadingPageMsg = msg
    },
  },
  modules: {
    auth,
  },
  plugins: [vuexLocal.plugin],
})
