<template>
  <div class="pb-16">
    <nav
      id="mainNav"
      class="d-flex flex-column align-items-center mt-0 position-fixed w-100 bg-white shadow-sm top-0 start-0"
    >
      <div
        class="search-container d-flex align-items-center justify-content-between bg-white rounded-pill px-4 py-2 w-75 w-md-50 w-lg-60 mt-2"
      >
        <hamburgermenu />
        <div class="input-container flex-grow-1 mx-3">
          <Inputexplore
            :api-key="googleApiKey"
            @place-selected="handlePlaceSelected"
            @search-error="handleSearchError"
          />
        </div>
        <div class="d-flex flex-column align-items-center map-section">
          <i class="fa-solid fa-location-dot map-icon"></i>
          <span class="map-text">Mapa</span>
        </div>
      </div>

      <topnavbar @filter-change="handleFilterChange" />
    </nav>

    <div class="p-4 content-section">
      <div class="d-flex align-items-center justify-content-between mb-4">
        <h5 class="mb-0 text-dark">Lugares Recomendados</h5>
        <v-btn
          class="btn-refresh"
          variant="outlined"
          size="small"
          prepend-icon="mdi-refresh"
          @click="refreshPlaces"
        >
          Refrescar
        </v-btn>
      </div>

      <div v-if="error" class="alert alert-warning" role="alert">
        <i class="mdi mdi-alert me-2"></i>
        {{ error }}
        <button @click="retrySearch" class="btn btn-sm btn-outline-warning ms-2">
          <i class="mdi mdi-refresh"></i> Reintentar
        </button>
      </div>

      <div v-if="places.length > 0" class="places-grid">
        <PlaceCard
          v-for="place in places"
          :key="place.id"
          :place="place"
          :is-favorite="isFavorite(place.id)"
          :logo-url="logoUrl"
          @select-place="selectPlace"
          @toggle-favorite="toggleFavorite"
        />
      </div>

      <v-pagination
        v-if="pagination.totalPages > 1"
        v-model="currentPage"
        :length="pagination.totalPages"
        @update:modelValue="handlePageChange"
        class="mb-5 d-flex justify-center"
      ></v-pagination>

      <div v-if="places.length === 0 && !error" class="text-center py-4">
        <i class="mdi mdi-map-marker text-muted mb-3" style="font-size: 3rem"></i>
        <h6 class="text-muted">No se encontraron lugares</h6>
        <p class="text-muted">Intenta permitir la geolocalización o busca algo específico.</p>
        <button @click="retrySearch" class="btn btn-outline-success">
          <i class="mdi mdi-refresh me-2"></i>Buscar de nuevo
        </button>
      </div>
    </div>

    <BottomNavbar />
  </div>
</template>

<script>
import { mapActions, mapGetters, mapMutations } from 'vuex'
import BottomNavbar from '@/components/bottomnavbar.vue'
import hamburgermenu from '@/components/hamburgermenu.vue'
import topnavbar from '@/components/topnavbar.vue'
import Inputexplore from '@/components/inputexplore.vue'
import PlaceCard from '@/components/placecard.vue'

export default {
  components: { 
    BottomNavbar, 
    hamburgermenu, 
    topnavbar, 
    Inputexplore,
    PlaceCard
  },
  data() {
    return {
      googleApiKey: '',
      error: null,
      userLocation: null,
      currentFilter: 'recommended',
      forceRefresh: false,
      logoUrl: '/logo-letras.png',
    }
  },
  computed: {
    ...mapGetters('places', ['places', 'pagination', 'placesIds', 'currentPage']),
    ...mapGetters('trips', ['favoriteIds']),
  },
  async created() {
    await this.getUserLocation()
    await this.loadFavorites()
    await this.loadRecommendedPlaces()
  },
  mounted() {
    const nav = document.getElementById('mainNav')
    if (nav) {
      document.documentElement.style.setProperty('--nav-height', nav.offsetHeight + 'px')
    }
  },
  methods: {
    ...mapActions('places', {
      fetchNearbyPlaces: 'fetchNearbyPlaces',
      fetchPreferencePlaces: 'fetchPreferencePlaces',
      fetchPlacesByIds: 'fetchPlacesByIds',
    }),
    ...mapActions('trips', {
      fetchFavorites: 'fetchFavorites',
      toggleFavoritePlace: 'toggleFavoritePlace',
    }),
    ...mapMutations('places', {
      setSelectedPlaceId: 'setSelectedPlaceId'
    }),
    async getUserLocation() {
      try {
        if (!navigator.geolocation) {
          throw new Error('Geolocalización no disponible')
        }

        const position = await new Promise((resolve, reject) => {
          navigator.geolocation.getCurrentPosition(resolve, reject, {
            timeout: 15000,
            enableHighAccuracy: false,
            maximumAge: 600000,
          })
        })

        this.userLocation = {
          lat: position.coords.latitude,
          lng: position.coords.longitude,
        }
      } catch {
        this.userLocation = { lat: 19.4326, lng: -99.1332 }
      }
    },

    async loadFavorites() {
      try {
        await this.fetchFavorites()
      } catch (error) {
        console.error('Error cargando favoritos:', error)
      }
    },

    async loadNearPreferencePlaces() {
      await this.fetchPreferencePlaces({
        latitude: this.userLocation.lat,
        longitude: this.userLocation.lng,
      }).then(() => {
        this.fetchPlacesByIds({
          place_ids: this.placesIds,
        })
      })
    },

    async loadNearPlaces(types) {
      await this.fetchNearbyPlaces({
        latitude: this.userLocation.lat,
        longitude: this.userLocation.lng,
        types: types,
      }).then(() => {
        this.fetchPlacesByIds({
          place_ids: this.placesIds,
        })
      })
    },

    async loadRecommendedPlaces() {
      if (!this.userLocation) {
        this.error = 'No se pudo obtener la ubicación'
        return
      }

      if (!this.forceRefresh && Array.isArray(this.places) && this.places.length > 0) {
        return
      }

      this.error = null

      try {
        if (this.currentFilter === 'preferences') {
          await this.loadNearPreferencePlaces()
        } else {
          const types = this.getTypesForFilter(this.currentFilter)
          await this.loadNearPlaces(types)
        }
      } catch (e) {
        console.error(e)
        this.error = 'No se pudieron cargar los lugares'
      }
    },

    async refreshPlaces() {
      this.forceRefresh = true
      await this.loadRecommendedPlaces()
      this.forceRefresh = false
    },

    getTypesForFilter(filter) {
      const typeMap = {
        recommended: ['restaurant', 'tourist_attraction', 'shopping_mall', 'park'],
        restaurant: ['restaurant'],
        hotel: ['lodging'],
        park: ['park'],
        shopping: ['shopping_mall'],
        cafe: ['cafe'],
      }
      return typeMap[filter] || typeMap.recommended
    },

    async handlePageChange(page) {
      try {
        await this.fetchPlacesByIds({
          place_ids: this.placesIds,
          page: page - 1,
          size: this.pagination.pageSize,
        })
      } catch (error) {
        console.error('Error al cambiar página:', error)
        this.error = 'Error al cargar la página'
      }
    },

    async retrySearch() {
      await this.getUserLocation()
      await this.refreshPlaces()
    },

    selectPlace(place) {
      this.setSelectedPlaceId(place.placeId)
      this.$router.push({name: 'site_description'})
    },

    async handleFilterChange(filter) {
      this.currentFilter = filter
      await this.refreshPlaces()
    },

    handlePlaceSelected(data) {
      if (data.place?.geometry?.location) {
        console.log(data)
      }
    },

    handleSearchError(error) {
      console.error('Error en búsqueda:', error)
    },

    async toggleFavorite(place) {
      try {
        await this.toggleFavoritePlace(place.id)
      } catch (error) {
        console.error('Error al cambiar favorito:', error)
      }
    },

    isFavorite(placeId) {
      return this.favoriteIds.includes(placeId)
    },
  },
}
</script>

<style scoped>
.appbar {
  position: sticky;
  top: 0;
  z-index: 10;
  background: #fff !important;
  border-bottom: 1px solid #d9e9d9;
}

.search-pill {
  background: #ffffff;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.04);
}

.menu-wrapper :deep(svg),
.menu-wrapper :deep(i) {
  color: #1b515e !important;
  font-size: 28px;
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

.menu-wrapper {
  width: 40px;
  height: 40px;
  color: #1b515e;
  display: flex;
  align-items: center;
  justify-content: center;
}

.btn-refresh {
  border: 1px solid #1b515e !important;
  color: #1b515e !important;
  text-transform: uppercase;
  letter-spacing: 0.04em;
  font-weight: 600;
}

.btn-refresh:hover {
  background: rgba(27, 81, 94, 0.06) !important;
}
.btn-refresh .v-icon {
  color: #1b515e !important;
}

.search-container {
  gap: 0;
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

.input-container :deep(.form-control):focus::after {
  color: #1b515e;
}

.content-section {
  height: calc(100vh - 200px);
  overflow-y: auto;
}

/* Estilos para la grilla vertical de lugares */
.places-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 1rem;
  padding: 0.5rem 0;
}

hr {
  margin-top: 0;
  height: 2px;
  background-color: #35aa06;
  border-color: #b0d4a1;
}

#mainNav {
  z-index: 1000;
}

/* Responsive Design */
@media (max-width: 480px) {
  .places-grid {
    grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
    gap: 0.75rem;
  }
}

@media (min-width: 481px) and (max-width: 768px) {
  .places-grid {
    grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  }
}

@media (min-width: 769px) and (max-width: 1024px) {
  .places-grid {
    grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  }

  .map-text {
    font-size: 16px;
  }

  .map-icon {
    font-size: 1.4rem;
  }
}

@media (min-width: 1025px) and (max-width: 1440px) {
  .search-container {
    gap: 100px;
  }

  .input-container {
    margin: 0 !important;
  }

  .map-text {
    font-size: 18px;
  }

  .map-icon {
    font-size: 1.6rem;
  }

  .places-grid {
    grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
    gap: 1.25rem;
  }
}

@media (min-width: 1441px) {
  .search-container {
    gap: 100px;
  }

  .input-container {
    margin: 0 !important;
  }

  .map-text {
    font-size: 20px;
  }

  .map-icon {
    font-size: 1.8rem;
  }

  .places-grid {
    grid-template-columns: repeat(auto-fill, minmax(340px, 1fr));
    gap: 1.5rem;
  }
}
.content-section {
  margin-top: var(--nav-height);
}
</style>