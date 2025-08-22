<template>
  <v-app>
    <v-main>
      <router-view />
    </v-main>

    <v-overlay v-model="activeLoading" persistent class="d-flex align-center justify-center">
      <div class="text-center">
        <v-progress-circular
          indeterminate
          color="primary"
          size="60"
          width="6"
        />
        <div class="mt-4 text-h6 text-white">{{ loadingPageMsg || 'Cargando...' }}</div>
      </div>
    </v-overlay>
  </v-app>
</template>

<script>
import { mapActions, mapGetters, mapState, mapMutations } from 'vuex'

export default {
  name: 'App',
  computed: {
    ...mapState(['activeLoading', 'loadingPageMsg']),
    ...mapGetters('auth', { isLogged: 'isLogged' }),
  },
  mounted() {
    this.updateTitle(this.$route)
  },
  watch: {
    $route(to) {
      this.updateTitle(to)
    }
  },
  created() {
    if (this.isLogged) {
      this.getProfile().then((response) => {
        this.setUser(response.data)
      })
    }
  },
  methods: {
    ...mapActions('auth', { getProfile: 'getProfile' }),
    ...mapMutations('auth', { setUser: 'setUser' }),
    updateTitle(route) {
      const nearest = route.matched.slice().reverse().find(r => r.meta && r.meta.title)
      document.title = (nearest && nearest.meta.title) || route.meta?.title || 'Huellas por México'
    }
  }
}
</script>