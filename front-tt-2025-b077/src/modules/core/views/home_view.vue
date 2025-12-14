<template>
  <div>
    <nav
      id="mainNav"
      class="d-flex flex-column align-items-center mt-0 position-fixed w-100 bg-white shadow-sm top-0 start-0"
    >
      <div
        class="search-container d-flex align-items-center justify-content-between bg-white rounded-pill px-4 py-2 w-75 w-md-50 w-lg-60 mt-2"
      >
        <hamburgermenu />
        <div class="input-container flex-grow-1 mx-3">
          <Inputexplore @search-error="handleSearchError" />
        </div>
        <div class="d-flex flex-column align-items-center map-section">
          <i class="fa-solid fa-location-dot map-icon"></i>
          <button class="map-text" @click="goToMap()">Mapa</button>
        </div>
      </div>

      <div class="filters-container w-100 px-4 py-3">
        <v-slide-group v-model="currentFilterIndex" class="filters-slide" show-arrows>
          <v-slide-group-item
            v-for="(filter, idx) in filters"
            :key="filter.id"
            v-slot="{ isSelected }"
          >
            <v-chip
              :class="{ 'filter-chip-active': isSelected, 'filter-chip': !isSelected }"
              @click="selectFilter(filter, idx)"
              label
              size="small"
            >
              <i :class="filter.icon" class="me-2"></i>
              {{ filter.label }}
            </v-chip>
          </v-slide-group-item>
        </v-slide-group>
      </div>
    </nav>

    <div class="px-6 pt-6 content-section">
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
        class="mb-3 d-flex justify-center"
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
    <preferences-modal 
      :show="showPreferencesModal" 
      @close="showPreferencesModal = false" 
    />
  </div>
</template>

<script>
import { mapActions, mapGetters, mapMutations } from 'vuex'
import { getErrorDetails } from '@/utils/utils'
import BottomNavbar from '@/components/bottomnavbar.vue'
import hamburgermenu from '@/components/hamburgermenu.vue'
import Inputexplore from '@/components/inputexplore.vue'
import PlaceCard from '@/components/placecard.vue'
import PreferencesModal from '@/components/PreferencesModal .vue'

export default {
  components: {
    BottomNavbar,
    hamburgermenu,
    Inputexplore,
    PlaceCard,
    PreferencesModal,
  },
  data() {
    return {
      filters: [
        {
          id: 'best',
          label: 'Mejores',
          icon: 'fa-solid fa-star',
          types: ['tourist_attraction'],
          isPreferences: false,
        },
        {
          id: 'preferences',
          label: 'Preferencias',
          icon: 'fa-solid fa-heart',
          types: [],
          isPreferences: true,
        },
        {
          id: 'restaurant',
          label: 'Restaurantes',
          icon: 'fa-solid fa-utensils',
          types: ['restaurant'],
          isPreferences: false,
        },
        {
          id: 'hotel',
          label: 'Hoteles',
          icon: 'fa-solid fa-hotel',
          types: ['lodging'],
          isPreferences: false,
        },
        {
          id: 'park',
          label: 'Parques',
          icon: 'fa-solid fa-tree',
          types: ['park'],
          isPreferences: false,
        },
        {
          id: 'museum',
          label: 'Museos',
          icon: 'fa-solid fa-building-columns',
          types: ['museum'],
          isPreferences: false,
        },
        {
          id: 'library',
          label: 'Librerías',
          icon: 'fa-solid fa-book',
          types: ['library'],
          isPreferences: false,
        },
        {
          id: 'cafe',
          label: 'Cafés',
          icon: 'fa-solid fa-coffee',
          types: ['cafe'],
          isPreferences: false,
        },
        {
          id: 'shopping',
          label: 'Compras',
          icon: 'fa-solid fa-shopping-bag',
          types: ['shopping_mall'],
          isPreferences: false,
        },
        {
          id: 'entertainment',
          label: 'Entretenimiento',
          icon: 'fa-solid fa-gamepad',
          types: ['amusement_park', 'entertainment'],
          isPreferences: false,
        },
        {
          id: 'gallery',
          label: 'Galerías',
          icon: 'fa-solid fa-image',
          types: ['art_gallery'],
          isPreferences: false,
        },
        {
          id: 'spa',
          label: 'Spa & Bienestar',
          icon: 'fa-solid fa-spa',
          types: ['spa', 'gym'],
          isPreferences: false,
        },
      ],
      googleApiKey: '',
      error: null,
      userLocation: null,
      forceRefresh: false,
      logoUrl: '/logo-letras.png',
    }
    showPreferencesModal: false
  },
  computed: {
    ...mapGetters('places', [
      'places',
      'pagination',
      'placesIds',
      'currentPage',
      'currentFilterIndex',
    ]),
    ...mapGetters('trips', ['favoriteIds']),
    currentFilter() {
      return this.filters[this.currentFilterIndex]
    },
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
    this.checkUserPreferences()
  },
  methods: {
    ...mapActions('places', {
      fetchNearbyPlaces: 'fetchNearbyPlaces',
      fetchPreferencePlaces: 'fetchPreferencePlaces',
      fetchPlacesByIds: 'fetchPlacesByIds',
    }),
    ...mapActions('trips', {
      fetchFavoriteIds: 'fetchFavoriteIds',
      toggleFavoritePlace: 'toggleFavoritePlace',
    }),
    ...mapMutations('places', {
      setSelectedPlaceId: 'setSelectedPlaceId',
      setPlaceIds: 'setPlaceIds',
      setPlaces: 'setPlaces',
      setPagination: 'setPagination',
      setCurrentFilterIndex: 'setCurrentFilterIndex',
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
    goToMap() {
      this.$router.push({ name: 'interactive_map' })
    },
    async loadFavorites() {
      try {
        await this.fetchFavoriteIds()
      } catch (error) {
        console.error('Error cargando favoritos:', error)
      }
    },

    async loadNearPreferencePlaces() {
      await this.fetchPreferencePlaces({
        latitude: this.userLocation.lat,
        longitude: this.userLocation.lng,
      }).then((response) => {
        this.setPlaceIds(response)
        this.fetchPlacesByIds({
          place_ids: this.placesIds,
        }).then((response) => {
          this.setPlaces(response.content)
          this.setPagination(response)
        })
      })
    },

    async loadNearPlaces(types) {
      await this.fetchNearbyPlaces({
        latitude: this.userLocation.lat,
        longitude: this.userLocation.lng,
        types: types,
      }).then((response) => {
        this.setPlaceIds(response)
        this.fetchPlacesByIds({
          place_ids: this.placesIds,
        }).then((response) => {
          this.setPlaces(response.content)
          this.setPagination(response)
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
        if (this.currentFilter.isPreferences) {
          await this.loadNearPreferencePlaces()
        } else {
          await this.loadNearPlaces(this.currentFilter.types)
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

    async selectFilter(filter, idx) {
      this.setCurrentFilterIndex(idx)
      await this.refreshPlaces()
    },

    async handlePageChange(page) {
      try {
        const response = await this.fetchPlacesByIds({
          place_ids: this.placesIds,
          page: page - 1,
          size: this.pagination.pageSize,
        })
        this.setPlaces(response.content)
        this.setPagination(response)
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
      this.$router.push({ name: 'site_description' })
    },

    handleSearchError(error) {
      console.error('Error en búsqueda:', error)
    },

    async toggleFavorite(place) {
      this.toggleFavoritePlace(place.id)
        .then(() => {
          const isFav = this.isFavorite(place.id)
          this.$alert.success({
            title: isFav ? 'Agregado a favoritos' : 'Eliminado de favoritos',
            text: isFav
              ? 'El lugar ha sido agregado a tus favoritos'
              : 'El lugar ha sido eliminado de tus favoritos',
          })
        })
        .catch((error) => {
          this.$alert.error({
            title: 'Error al actualizar favoritos',
            text: getErrorDetails(error),
          })
        })
    },

    isFavorite(placeId) {
      return this.favoriteIds.includes(placeId)
    },
    ...mapActions('user', {
      fetchUserPreferences: 'fetchUserPreferences',
    }),
    
    checkUserPreferences() {
      this.fetchUserPreferences()
        .then((response) => {
          const userPreferences = response.data.data || []
          
          // Si no hay preferencias, mostrar el modal
          if (userPreferences.length === 0) {
            this.showPreferencesModal = true
          }
        })
        .catch((error) => {
          console.error('Error checking preferences:', error)
        })
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

.input-container :deep(.form-control):focus::after {
  color: #1b515e;
}

.content-section {
  min-height: calc(100vh - var(--nav-height, 280px));
  overflow-y: auto;
  padding-bottom: 100px;
}

.places-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 1rem;
  padding: 0.5rem 0;
}

hr {
  margin-top: 0;
  height: 2px;
  background-color: #1b515e;
  border-color: #b0d4a1;
}

#mainNav {
  z-index: 1000;
}

.filters-container {
  border-top: 1px solid #e9ecef;
  background: white;
}

.filters-slide {
  width: 100%;
}

.filter-chip {
  background: transparent !important;
  border: none !important;
  color: #999 !important;
  cursor: pointer;
  transition: all 0.2s ease;
  border-bottom: 2px solid transparent !important;
  padding: 0.5rem 0.7rem !important;
  margin: 0 3px !important;
  font-weight: 400;
  font-size: 0.85rem !important;
  height: auto !important;
}

.filter-chip:hover {
  padding: 1rem;
  color: #1b515e !important;
  background: transparent !important;
}

.filter-chip-active {
  padding: 0.25rem 0.5rem !important;
  background: transparent !important;
  color: #abcd9e !important;
  font-weight: 400;
  font-size: 0.85rem !important;
  height: auto !important;
  border-bottom: 2px solid #abcd9e !important;
  margin: 0 8px;
}

@media (max-width: 480px) {
  .places-grid {
    grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
    gap: 0.75rem;
  }

  .content-section {
    height: calc(100vh - 320px);
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
