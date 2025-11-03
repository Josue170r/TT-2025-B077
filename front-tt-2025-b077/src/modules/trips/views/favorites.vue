<template>
  <div class="pb-16">
    <nav
      id="mainNav"
      class="d-flex flex-column align-items-center mt-0 position-fixed w-100 bg-white shadow-sm top-0 start-0"
      style="z-index: 1000;"
    >
      <div
        class=" d-flex align-items-center justify-content-between bg-white px-6 py-4 w-75 w-md-50 w-lg-60 mt-2 nav-container"
      >
        <div class="hamburger-wrapper">
          <hamburgermenu />
        </div>
        <div class="flex-grow-1 text-center title-wrapper">
          <h5 class="mb-0 color-text">Mis lugares favoritos</h5>
        </div>
      </div>
    </nav>

    <div class="p-4 content-section" style="margin-top: 50px;">
      <div class="d-flex align-items-center justify-content-between mb-4">
        <h5 class="mb-0"></h5>
        <v-btn
          class="btn-refresh"
          variant="outlined"
          size="small"
          prepend-icon="mdi-refresh"
          @click="loadFavoritesPlaces"
        >
          Actualizar
        </v-btn>
      </div>

      <div v-if="loading" class="text-center py-5">
        <v-progress-circular
          indeterminate
          color="primary"
          size="64"
        ></v-progress-circular>
        <p class="text-muted mt-3">Cargando favoritos...</p>
      </div>

      <div v-else-if="error" class="alert alert-warning" role="alert">
        <i class="mdi mdi-alert me-2"></i>
        {{ error }}
      </div>

      <div v-else-if="favoritePlaces.length > 0" class="places-grid">
        <PlaceCard
          v-for="place in favoritePlaces"
          :key="place.id"
          :place="place"
          :is-favorite="true"
          :logo-url="logoUrl"
          @select-place="selectPlace"
          @toggle-favorite="toggleFavorite"
        />
      </div>

      <div v-else class="text-center py-5">
        <i class="mdi mdi-heart-off text-muted mb-3" style="font-size: 3rem"></i>
        <h6 class="text-muted">No tienes lugares favoritos</h6>
        <p class="text-muted">Agrega algunos lugares desde la pantalla principal.</p>
        <router-link to="/" class="btn btn-outline-success mt-3">
          <i class="mdi mdi-arrow-left me-2"></i>Explorar lugares
        </router-link>
      </div>
    </div>

    <BottomNavbar />
  </div>
</template>

<script>
import BottomNavbar from '@/components/bottomnavbar.vue'
import hamburgermenu from '@/components/hamburgermenu.vue'
import PlaceCard from '@/components/placecard.vue'

export default {
  name: 'FavoritesView',
  components: {
    BottomNavbar,
    hamburgermenu,
    PlaceCard,
  },
  data() {
    return {
      loading: false,
      error: null,
      logoUrl: '/logo-letras.png',
      favoritePlaces: []
    }
  },
  mounted() {
    this.loadFavoritesPlaces()
  },
  methods: {
    async loadFavoritesPlaces() {
      this.loading = true
      this.error = null
      
      try {
        // const favorites = await this.$store.dispatch('loadFavorites')
        // this.favoritePlaces = favorites
        //Quiotar los ejemplos
        await new Promise(resolve => setTimeout(resolve, 500))
        
        this.favoritePlaces = [
          {
            id: '1',
            name: 'Bosque de Chapultepec',
            address: 'Ciudad de México, CDMX',
            rating: 4.7,
            types: ['park', 'tourist_attraction'],
          },
          {
            id: '2',
            name: 'Café El Jarocho',
            address: 'Av. Cuauhtémoc 123, CDMX',
            rating: 4.5,
            types: ['cafe', 'restaurant'],
          },
        ]
      } catch (err) {
        this.error = 'No se pudieron cargar los favoritos. Intenta nuevamente.'
        console.error('Error al cargar favoritos:', err)
      } finally {
        this.loading = false
      }
    },
    
    selectPlace(place) {
      this.$router.push({
        name: 'site_description', 
        params: { id: place.id }
      })
    },
    
    toggleFavorite(place) {
      // Remover de favoritos
      this.favoritePlaces = this.favoritePlaces.filter(p => p.id !== place.id)
      
      // this.$store.dispatch('removeFavorite', place.id)
      
      console.log('Lugar removido de favoritos:', place.name)
    },
  },
}
</script>

<style scoped>

.color-text{
  color: #1b515e;
  font-weight: bold;
}

.map-section {
  color: #1b515e;
}

.map-icon {
  font-size: 1.2rem;
}

.map-text {
  font-size: 14px;
}

.content-section {
  min-height: calc(100vh - 200px);
  overflow-y: auto;
}

.places-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 1rem;
  padding: 0.5rem 0;
}

@media (max-width: 768px) {
  .places-grid {
    grid-template-columns: 1fr;
  }
  
  .nav-container {
    padding-left: 0.5rem !important;
    padding-right: 1rem !important;
  }
  
  .hamburger-wrapper {
    margin-right: 1rem;
  }
  
  .title-wrapper {
    margin-right: auto;
  }
}

.btn-refresh {
  border: 1px solid #1b515e !important;
  color: #1b515e !important;
  text-transform: uppercase;
  letter-spacing: 0.04em;
  font-weight: 600;
}

.btn-refresh:hover {
  background-color: #1b515e !important;
  color: white !important;
}

.btn-outline-success {
  color: #1b515e;
  border-color: #1b515e;
}

.btn-outline-success:hover {
  background-color: #1b515e;
  color: white;
  border-color: #1b515e;
}
</style>