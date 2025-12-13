<template>
  <div class="main-container">
    <div class="header-container">
      <SearchHotels
        :user-location="userLocation"
        @place-selected="handlePlaceSelected"
        @navigate="handleNavigation"
      />
    </div>

    <div class="filters-chips-container">
      <div class="filters-chips-wrapper">
        <div class="filter-chips">
          <div v-for="cert in selectedCertifications" :key="cert.id" class="filter-chip">
            <span>{{ cert.certification }}</span>
            <button @click="removeCertification(cert.id)" class="chip-remove">
              <i class="mdi mdi-close"></i>
            </button>
          </div>

          <div v-if="filters.settlement" class="filter-chip">
            <span>{{ getSettlementName(filters.settlement) }}</span>
            <button @click="removeSettlement" class="chip-remove">
              <i class="mdi mdi-close"></i>
            </button>
          </div>

          <div v-if="filters.useLocation" class="filter-chip">
            <span>Cerca de mí</span>
            <button @click="removeLocation" class="chip-remove">
              <i class="mdi mdi-close"></i>
            </button>
          </div>

          <div v-if="textSearchMode" class="filter-chip text-search-chip">
            <span>Búsqueda: {{ textSearchQuery }}</span>
            <button @click="removeTextSearch" class="chip-remove">
              <i class="mdi mdi-close"></i>
            </button>
          </div>

          <button v-if="hasActiveFilters" @click="clearAllFilters" class="clear-filters-btn">
            <i class="mdi mdi-filter-remove"></i>
            Limpiar filtros
          </button>
        </div>
      </div>
    </div>

    <div class="content-layout">
      <aside class="filters-sidebar">
        <div class="filters-sidebar-content">
          <h3 class="sidebar-title">Filtros</h3>

          <div class="filter-section sustainable-toggle">
            <v-switch
              v-model="sustainableFilter"
              color="success"
              label="Sostenibles"
              hide-details
              dense
              @change="handleSustainableChange"
            ></v-switch>
          </div>

          <div class="filter-divider"></div>

          <div class="filter-section">
            <h4 class="filter-section-title">Certificaciones</h4>
            <v-checkbox
              v-for="cert in certifications"
              :key="cert.id"
              v-model="tempCertifications"
              :value="cert.id"
              :label="cert.certification"
              :disabled="!sustainableFilter"
              color="success"
              hide-details
              dense
              @change="applyFilters"
              class="certification-checkbox-item"
            ></v-checkbox>
          </div>

          <div class="filter-divider"></div>

          <div class="filter-section">
            <h4 class="filter-section-title">Ubicación</h4>

            <v-radio-group
              v-model="locationType"
              @change="handleLocationTypeChange"
              :disabled="!sustainableFilter"
            >
              <v-radio label="Por municipio" value="settlement" color="success"></v-radio>

              <v-radio label="Cerca de mí" value="location" color="success"></v-radio>

              <v-radio label="Sin filtro de ubicación" value="none" color="success"></v-radio>
            </v-radio-group>

            <v-select
              v-if="locationType === 'settlement'"
              ref="settlementSelect"
              v-model="tempSettlement"
              :items="settlements"
              item-title="settlement"
              item-value="id"
              label="Selecciona un municipio"
              outlined
              dense
              :disabled="!sustainableFilter"
              color="success"
              @update:modelValue="applyFilters"
              class="mt-2"
              :menu-props="{ auto: true, offsetY: true }"
            ></v-select>
          </div>
        </div>
      </aside>

      <main class="main-content">
        <div class="hotels-list">
          <div
            v-for="hotel in hotels"
            :key="hotel.id"
            class="hotel-card-wrapper"
            :class="{ 'hotel-selected': selectedHotel === hotel.place.id }"
          >
            <div
              v-if="hotel.certifications && hotel.certifications.length > 0"
              class="certifications-container"
            >
              <div class="certifications-title">
                <i class="mdi mdi-leaf"></i>
                <span>Certificaciones sostenibles</span>
              </div>

              <div class="certifications-list">
                <a
                  v-for="cert in hotel.certifications"
                  :key="cert.id"
                  :href="cert.institutionWebsite"
                  target="_blank"
                  rel="noopener noreferrer"
                  class="certification-badge"
                >
                  <i class="mdi mdi-leaf-circle"></i>
                  <span>{{ cert.certification }}</span>
                  <i class="mdi mdi-open-in-new"></i>
                </a>
              </div>
            </div>

            <PlaceCard
              :place="hotel.place"
              :is-favorite="isFavorite(hotel.place.id)"
              :logo-url="logoUrl"
              @select-place="selectPlace"
              @toggle-favorite="toggleFavorite"
            />

            <button
              @click="selectHotel(hotel)"
              class="btn-select-hotel"
              :class="{ selected: selectedHotel === hotel.place.id }"
            >
              <i
                class="mdi"
                :class="selectedHotel === hotel.place.id ? 'mdi-check-circle' : 'mdi-plus-circle'"
              ></i>
              {{ selectedHotel === hotel.place.id ? 'Hotel Seleccionado' : 'Seleccionar Hotel' }}
            </button>
          </div>

          <div v-if="hotels.length === 0" class="no-results">
            <i class="mdi mdi-hotel no-results-icon"></i>
            <p class="no-results-text">No se encontraron hoteles</p>
            <p class="no-results-subtext">Intenta ajustar los filtros</p>
          </div>
        </div>

        <v-pagination
          v-if="pagination.totalPages > 1"
          v-model="currentPageModel"
          :length="pagination.totalPages"
          @update:modelValue="handlePageChange"
          class="my-5"
        ></v-pagination>
      </main>
    </div>

    <NavButtom :has-hotel-selected="!!selectedHotel" />
  </div>
</template>

<script>
import { mapActions, mapGetters, mapMutations, mapState } from 'vuex'
import { getErrorDetails } from '@/utils/utils'
import NavButtom from '@/components/NavButtom.vue'
import PlaceCard from '@/components/placecard.vue'
import SearchHotels from '@/components/SearchHotels.vue'

export default {
  components: {
    NavButtom,
    PlaceCard,
    SearchHotels,
  },
  data() {
    return {
      tempCertifications: [],
      tempSettlement: null,
      locationType: 'none',
      currentPageModel: 1,
      logoUrl: '/logo-letras.png  ',
      userLocation: null,
      sustainableFilter: null,
    }
  },
  computed: {
    ...mapGetters('trips', [
      'hotels',
      'hotelIds',
      'certifications',
      'settlements',
      'pagination',
      'currentPage',
      'filters',
      'newItinerary',
      'isSustainable',
      'favoriteIds',
      'textSearchMode',
      'textSearchQuery',
    ]),
    ...mapState('places', {
      selectedPlaceDetails: 'selectedPlaceDetails',
    }),
    selectedHotel() {
      return this.newItinerary.hotelPlaceId
    },
    selectedCertifications() {
      return this.certifications.filter((cert) => this.filters.certifications.includes(cert.id))
    },
    hasActiveFilters() {
      return (
        this.filters.certifications.length > 0 ||
        this.filters.settlement ||
        this.filters.useLocation ||
        this.textSearchMode
      )
    },
  },
  async mounted() {
    try {
      await this.getUserLocation()
      await this.fetchCertifications()

      if (this.newItinerary.selectedState) {
        await this.fetchSettlements(this.newItinerary.selectedState)

        await this.$nextTick()
        this.restoreFilters()
        this.sustainableFilter = this.isSustainable
      }
    } catch (error) {
      console.error('Error al cargar datos:', error)
      this.$alert.error('Error al cargar la información')
    }
  },
  methods: {
    ...mapActions('trips', {
      fetchCertifications: 'fetchCertifications',
      fetchSettlements: 'fetchSettlements',
      fetchHotels: 'fetchHotels',
      toggleFavoritePlace: 'toggleFavoritePlace',
    }),
    ...mapActions('places', {
      fetchNearbyPlaces: 'fetchNearbyPlaces',
      fetchPlacesByIds: 'fetchPlacesByIds',
      fetchPlaceDetails: 'fetchPlaceDetails',
    }),
    ...mapMutations('trips', {
      setFilterCertifications: 'setFilterCertifications',
      setFilterSettlement: 'setFilterSettlement',
      setFilterUseLocation: 'setFilterUseLocation',
      setFilterCoordinates: 'setFilterCoordinates',
      clearFilters: 'clearFilters',
      clearSelectedHotel: 'clearSelectedHotel',
      setHotels: 'setHotels',
      setHotelIds: 'setHotelIds',
      setPagination: 'setPagination',
      setSelectedHotel: 'setSelectedHotel',
      setIsSustainable: 'setIsSustainable',
      setTextSearchMode: 'setTextSearchMode',
      setTextSearchQuery: 'setTextSearchQuery',
      setTextSearchPlaceId: 'setTextSearchPlaceId',
      clearTextSearch: 'clearTextSearch',
    }),
    ...mapMutations('places', {
      setSelectedPlaceId: 'setSelectedPlaceId',
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

    restoreFilters() {
      if (this.filters.certifications && this.filters.certifications.length > 0) {
        this.tempCertifications = [...this.filters.certifications]
      }

      if (this.filters.settlement) {
        this.locationType = 'settlement'
        this.tempSettlement = this.filters.settlement
      } else if (this.filters.useLocation) {
        this.locationType = 'location'
      } else {
        this.locationType = 'none'
      }
    },

    handleNavigation(route) {
      this.$router.push(route)
    },

    async handlePlaceSelected(place) {
      try {
        this.setTextSearchMode(true)
        this.setTextSearchQuery(place.mainText)
        this.setTextSearchPlaceId(place.placeId)

        await this.fetchPlaceDetails()

        const hotelFromSearch = {
          id: place.placeId,
          place: this.selectedPlaceDetails,
          certifications: [],
        }

        this.setHotels([hotelFromSearch])
        this.setPagination({
          pageable: { pageNumber: 0, pageSize: 1 },
          totalElements: 1,
          totalPages: 1,
          first: true,
          last: true,
          numberOfElements: 1,
          empty: false,
        })

        this.setIsSustainable(false)
        this.tempCertifications = []
        this.tempSettlement = null
        this.locationType = 'none'
      } catch (error) {
        console.error('Error al cargar detalles del lugar:', error)
        this.$alert.error('Error al cargar los detalles del hotel')
      }
    },

    removeTextSearch() {
      this.clearTextSearch()
      this.setIsSustainable(true)
      this.reloadHotels()
    },

    async handleSustainableChange() {
      if (!this.sustainableFilter) {
        this.clearAllFilters()
        this.setIsSustainable(false)
      } else {
        await this.reloadHotels()
        this.setIsSustainable(true)
      }
    },

    handleLocationTypeChange() {
      if (this.locationType === 'settlement') {
        this.$nextTick(() => {
          if (this.$refs.settlementSelect) {
            this.$refs.settlementSelect.activateMenu()
          }
        })
      } else if (this.locationType === 'location') {
        this.applyFilters()
      } else if (this.locationType === 'none') {
        this.applyFilters()
      }
    },

    isFavorite(placeId) {
      return this.favoriteIds.includes(placeId)
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

    selectPlace(place) {
      this.setSelectedPlaceId(place.placeId)
      this.$router.push({
        name: 'site_description',
        query: { from: 'hotels' },
      })
    },

    selectHotel(hotel) {
      const hotelId = hotel.place.id

      if (this.selectedHotel === hotelId) {
        this.selectedHotel = null
        this.setSelectedHotel({ hotelPlaceId: null, isCertificatedHotel: false })
      } else {
        this.selectedHotel = hotelId
        this.setSelectedHotel({
          hotelPlaceId: hotelId,
          isCertificatedHotel: this.sustainableFilter,
        })
      }
    },

    async handlePageChange(page) {
      try {
        if (this.sustainableFilter) {
          await this.fetchHotels({
            page: page - 1,
            size: this.pagination.pageSize,
          })
        } else {
          const response = await this.fetchPlacesByIds({
            place_ids: this.hotelIds,
            page: page - 1,
            size: this.pagination.pageSize,
          })

          const hotels = response.content.map((place) => ({
            id: place.id,
            place: place,
            certifications: [],
          }))

          this.setHotels(hotels)
          this.setPagination(response)
        }
        window.scrollTo({ top: 0, behavior: 'smooth' })
      } catch (error) {
        console.error('Error al cambiar página:', error)
        this.$alert.error('Error al cargar la página')
      }
    },

    removeCertification(certId) {
      const updated = this.filters.certifications.filter((id) => id !== certId)
      this.setFilterCertifications(updated)
      this.tempCertifications = updated
      this.reloadHotels()
    },

    removeSettlement() {
      this.setFilterSettlement(null)
      this.tempSettlement = null
      this.locationType = 'none'
      this.reloadHotels()
    },

    removeLocation() {
      this.setFilterUseLocation(false)
      this.setFilterCoordinates({ latitude: null, longitude: null })
      this.locationType = 'none'
      this.reloadHotels()
    },

    async applyFilters() {
      if (!this.sustainableFilter) return

      try {
        this.setFilterCertifications(this.tempCertifications)

        if (this.locationType === 'settlement') {
          this.setFilterSettlement(this.tempSettlement)
          this.setFilterUseLocation(false)
          this.setFilterCoordinates({ latitude: null, longitude: null })
        } else if (this.locationType === 'location') {
          await this.applyUserLocation()
          this.setFilterSettlement(null)
        } else {
          this.setFilterSettlement(null)
          this.setFilterUseLocation(false)
          this.setFilterCoordinates({ latitude: null, longitude: null })
        }

        await this.reloadHotels()
      } catch (error) {
        console.error('Error al aplicar filtros:', error)
        this.$alert.error('Error al aplicar los filtros')
      }
    },

    async applyUserLocation() {
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

        this.setFilterUseLocation(true)
        this.setFilterCoordinates({
          latitude: position.coords.latitude,
          longitude: position.coords.longitude,
        })
      } catch {
        this.setFilterUseLocation(true)
        this.setFilterCoordinates({
          latitude: 19.4326,
          longitude: -99.1332,
        })
      }
    },

    clearAllFilters() {
      if (this.textSearchMode) {
        this.removeTextSearch()
      }
      this.clearFilters()
      this.tempCertifications = []
      this.tempSettlement = null
      this.locationType = 'none'
      this.reloadHotels()
    },

    async loadHotels() {
      if (this.sustainableFilter) {
        await this.fetchHotels({ page: 0, size: 10 })
      } else {
        await this.loadNearbyHotels()
      }
    },

    async loadNearbyHotels() {
      try {
        if (!this.userLocation) {
          await this.getUserLocation()
        }

        await this.fetchNearbyPlaces({
          latitude: this.userLocation.lat,
          longitude: this.userLocation.lng,
          types: ['lodging'],
        }).then((response) => {
          this.setHotelIds(response)
          this.fetchPlacesByIds({
            place_ids: this.hotelIds,
          }).then((response) => {
            const hotels = response.content.map((place) => ({
              id: place.id,
              place: place,
              certifications: [],
            }))

            this.setHotels(hotels)
            this.setPagination(response)
          })
        })
      } catch (error) {
        console.error('Error al cargar hoteles cercanos:', error)
        this.$alert.error('Error al cargar hoteles de Google')
      }
    },

    async reloadHotels() {
      this.currentPageModel = 1
      this.clearSelectedHotel()
      await this.loadHotels()
    },

    getSettlementName(settlementId) {
      const settlement = this.settlements.find((s) => s.id === settlementId)
      return settlement ? settlement.settlement : ''
    },
  },
}
</script>

<style scoped>
.main-container {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding-top: 200px;
  padding-bottom: 80px;
}

.header-container {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  background: white;
  border-bottom: 1px solid #e0e0e0;
  z-index: 100;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.04);
}

.filters-chips-container {
  position: fixed;
  top: 130px;
  left: 0;
  right: 0;
  background: white;
  border-bottom: 1px solid #e0e0e0;
  padding: 1px 0;
  z-index: 90;
  height: 60px;
  overflow-y: auto;
  overflow-x: hidden;
}

.filters-chips-wrapper {
  max-width: 100%;
  margin: 0 auto;
  padding: 0 15px;
}

.filter-chips {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
  align-items: center;
}

.filter-chip {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  background: #e8f5e9;
  color: #2e7d32;
  border: 1px solid #4caf50;
  border-radius: 16px;
  padding: 6px 12px;
  font-size: 0.875rem;
  font-weight: 500;
}

.text-search-chip {
  background: #e3f2fd;
  color: #1976d2;
  border: 1px solid #2196f3;
}

.chip-remove {
  background: none;
  border: none;
  color: #2e7d32;
  cursor: pointer;
  padding: 0;
  display: flex;
  align-items: center;
  font-size: 1.1rem;
  transition: color 0.2s;
}

.chip-remove:hover {
  color: #1b5e20;
}

.clear-filters-btn {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  background: #ffebee;
  color: #c62828;
  border: 1px solid #ef5350;
  border-radius: 16px;
  padding: 6px 12px;
  font-size: 0.875rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
}

.clear-filters-btn:hover {
  background: #ffcdd2;
}

.content-layout {
  display: flex;
  gap: 20px;
  max-width: 1400px;
  margin: 0 auto;
  padding: 0px 20px 15px 15px;
  margin-top: 0;
}

.filters-sidebar {
  width: 280px;
  flex-shrink: 0;
  position: sticky;
  top: 200px;
  height: fit-content;
  max-height: calc(100vh - 220px);
  overflow-y: auto;
}

.filters-sidebar-content {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.sidebar-title {
  font-size: 1.2rem;
  font-weight: 600;
  color: #333;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 2px solid #35aa06;
}

.sustainable-toggle {
  margin-bottom: 15px;
}

.filter-section {
  margin-bottom: 20px;
}

.filter-section-title {
  font-size: 0.95rem;
  font-weight: 600;
  color: #555;
  margin-bottom: 12px;
}

.filter-divider {
  height: 1px;
  background: #e0e0e0;
  margin: 20px 0;
}

.certification-checkbox-item {
  margin-bottom: 8px;
}

.main-content {
  flex: 1;
  min-width: 0;
}

.hotels-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 1rem;
  margin-bottom: 30px;
  padding: 0.5rem 0;
}

.hotel-card-wrapper {
  display: flex;
  flex-direction: column;
  gap: 0;
  position: relative;
  transition:
    transform 0.2s,
    box-shadow 0.2s;
}

.hotel-card-wrapper.hotel-selected {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(53, 170, 6, 0.2);
}

.btn-select-hotel {
  width: 100%;
  padding: 12px 16px;
  border: 2px solid #1b515e;
  background: white;
  color: #1b515e;
  font-weight: 600;
  font-size: 0.95rem;
  border-radius: 0 0 8px 8px;
  cursor: pointer;
  transition: all 0.3s;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.btn-select-hotel:hover {
  background: #e8f2f4;
}

.btn-select-hotel.selected {
  background: #1b515e;
  color: white;
  border-color: #1b515e;
}

.btn-select-hotel.selected:hover {
  background: #164450;
  border-color: #164450;
}

.btn-select-hotel i {
  font-size: 1.2rem;
}

.certifications-container {
  background: white;
  border-radius: 8px 8px 0 0;
  padding: 12px 16px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  border-bottom: 1px solid #e0e0e0;
}

.certifications-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 0.9rem;
  font-weight: 600;
  color: #2e7d32;
  margin-bottom: 10px;
}

.certifications-title i {
  font-size: 1.2rem;
  color: #4caf50;
}

.certifications-list {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.certification-badge {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 12px;
  background: #f1f8f4;
  border: 1px solid #c8e6c9;
  border-radius: 6px;
  color: #2e7d32;
  text-decoration: none;
  font-size: 0.85rem;
  transition: all 0.2s;
}

.certification-badge:hover {
  background: #e8f5e9;
  border-color: #4caf50;
  transform: translateX(2px);
}

.certification-badge i:first-child {
  font-size: 1.1rem;
  color: #4caf50;
}

.certification-badge span {
  flex: 1;
  font-weight: 500;
}

.certification-badge i:last-child {
  font-size: 0.9rem;
  color: #66bb6a;
}

.no-results {
  grid-column: 1 / -1;
  text-align: center;
  padding: 60px 20px;
}

.no-results-icon {
  font-size: 4rem;
  color: #ccc;
  margin-bottom: 20px;
}

.no-results-text {
  font-size: 1.2rem;
  font-weight: 600;
  color: #666;
  margin-bottom: 8px;
}

.no-results-subtext {
  font-size: 0.95rem;
  color: #999;
}

@media (max-width: 1024px) {
  .filters-sidebar {
    width: 240px;
  }

  .hotels-list {
    grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  }
}

@media (max-width: 768px) {
  .content-layout {
    flex-direction: column;
    margin-top: 0;
    padding: 15px 10px;
  }

  .filters-sidebar {
    width: 100%;
    position: static;
    max-height: none;
    margin-bottom: 20px;
  }

  .filters-sidebar-content {
    padding: 15px;
  }

  .hotels-list {
    grid-template-columns: 1fr;
    gap: 1rem;
  }
}

@media (max-width: 576px) {
  .main-container {
    padding-top: 170px;
    padding-bottom: 85px;
  }

  .header-container {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    background: white;
    border-bottom: 1px solid #e0e0e0;
    z-index: 100;
    box-shadow: none;
  }

  .filters-chips-container {
    position: fixed;
    top: 100px;
    left: 0;
    right: 0;
    background: white;
    border-bottom: 1px solid #e0e0e0;
    padding: 8px 0;
    z-index: 90;
    height: 60px;
    overflow-y: auto;
    overflow-x: hidden;
    margin: 0;
    border-top: none;
  }

  .filters-chips-wrapper {
    padding: 0 10px;
  }

  .content-layout {
    flex-direction: column;
    padding: 10px 8px;
    margin-top: 0;
  }

  .filters-sidebar {
    width: 100%;
    position: static;
    max-height: none;
    margin-bottom: 20px;
  }

  .filters-sidebar-content {
    padding: 12px;
  }

  .sidebar-title {
    font-size: 1.1rem;
    margin-bottom: 15px;
  }

  .filter-section-title {
    font-size: 0.9rem;
  }

  .hotels-list {
    grid-template-columns: 1fr;
    gap: 0.75rem;
  }

  .hotel-card-wrapper {
    max-width: 100%;
    width: 100%;
  }

  .certifications-container {
    padding: 10px 12px;
  }

  .certification-badge {
    font-size: 0.8rem;
    padding: 6px 10px;
  }

  .btn-select-hotel {
    font-size: 0.9rem;
    padding: 10px 14px;
  }
}

@media (max-width: 480px) {
  .hotels-list {
    grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
    gap: 0.75rem;
  }
}

@media (max-width: 400px) {
  .filter-chip,
  .clear-filters-btn {
    font-size: 0.8rem;
    padding: 5px 10px;
  }

  .hotels-list {
    gap: 0.75rem;
  }
}

@media (min-width: 769px) and (max-width: 1024px) {
  .hotels-list {
    grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  }
}

@media (min-width: 1025px) and (max-width: 1440px) {
  .hotels-list {
    grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
    gap: 1.25rem;
  }
}

@media (min-width: 1441px) {
  .hotels-list {
    grid-template-columns: repeat(auto-fill, minmax(340px, 1fr));
    gap: 1.5rem;
  }
}
</style>
