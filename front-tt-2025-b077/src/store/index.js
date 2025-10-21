import { createStore } from 'vuex'
import auth from '@/modules/auth/store'
import places from '@/modules/core/store'
import trips from '@/modules/trips/store'
import user from '@/modules/user/store'

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
      places: state.places,
      trips: state.trips,
      user: state.user
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
    places,
    trips,
    user,
  },
  plugins: [vuexLocal.plugin],
})
