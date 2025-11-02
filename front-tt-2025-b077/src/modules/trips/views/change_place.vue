<template>
  <div class="fixed-header mb-5">
    <div class="header-content">
      <hamburgermenu />
      <div class="header-title">
        <p class="mb-1">Selecciona un lugar</p>
      </div>
      <div class="header-spacer"></div>
    </div>
  </div>

  <div class="change-place-container">
    <div class="header-section">
      <button class="btn-back" @click="goBack">
        <i class="fa-solid fa-arrow-left"></i>
      </button>
      <div class="header-content">
        <h1 class="page-title">Cambiar lugar</h1>
        <br />
        <div>
          <h2 class="page-subtitle">Selecciona un nuevo lugar para tu itinerario</h2>
        </div>
      </div>
    </div>

    <div class="main-spacer"></div>

    <div class="content-wrapper container py-4">
      <div v-if="currentPlaceInfo" class="current-place-info mb-4">
        <h3 class="section-title">
          <i class="fa-solid fa-exchange-alt me-2"></i>
          Reemplazando
        </h3>
        <div class="current-place-card">
          <div class="place-name">{{ currentPlaceInfo.name }}</div>
          <div class="place-address">{{ currentPlaceInfo.address }}</div>
        </div>
      </div>

      <div class="suggested-places-section">
        <h3 class="section-title mb-3">
          <i class="fa-solid fa-lightbulb me-2"></i>
          Lugares sugeridos según tus preferencias
        </h3>

        <div v-if="places && places.length > 0" class="places-grid">
          <div v-for="place in places" :key="place.id" class="place-item">
            <PlaceCard
              :place="place"
              :is-favorite="isFavorite(place.id)"
              :logo-url="getDefaultImage()"
              @select-place="selectPlace"
              @toggle-favorite="toggleFavorite"
            />
            <button class="btn-select-place" @click="openConfirmModal(place)">
              <i class="fa-solid fa-check me-2"></i>
              Seleccionar este lugar
            </button>
          </div>
        </div>

        <div v-else-if="!loading" class="empty-state">
          <i class="fa-solid fa-map-marker-alt"></i>
          <p>No se encontraron lugares sugeridos</p>
        </div>

        <v-pagination
          v-if="pagination.totalPages > 1"
          v-model="currentPage"
          :length="pagination.totalPages"
          @update:modelValue="handlePageChange"
          class="mb-5 d-flex justify-center"
        ></v-pagination>
      </div>
    </div>

    <div v-if="showConfirmModal" class="modal-overlay" @click="closeConfirmModal">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>Confirmar cambio de lugar</h3>
          <button class="modal-close" @click="closeConfirmModal">
            <i class="fa-solid fa-times"></i>
          </button>
        </div>

        <div class="modal-body">
          <div class="new-place-preview">
            <h4>Nuevo lugar:</h4>
            <p class="place-name-preview">{{ selectedNewPlace?.name }}</p>
            <p class="place-address-preview">{{ selectedNewPlace?.formattedAddress }}</p>
          </div>

          <div class="form-group">
            <label>Hora de llegada <span class="required">*</span></label>
            <div class="time-input-wrapper">
              <span class="time-display-value">{{ newPlaceData.arrivalTime || '--:--' }}</span>
              <button type="button" class="time-input-btn" @click="openTimePickerModal('arrival')">
                <i class="fa-regular fa-clock"></i>
              </button>
            </div>
          </div>

          <div class="form-group">
            <label>Hora de salida</label>
            <div class="time-input-wrapper">
              <span class="time-display-value">{{ newPlaceData.leavingTime || '--:--' }}</span>
              <button type="button" class="time-input-btn" @click="openTimePickerModal('leaving')">
                <i class="fa-regular fa-clock"></i>
              </button>
            </div>
          </div>
        </div>

        <div class="modal-footer">
          <button class="btn btn-secondary" @click="closeConfirmModal">Cancelar</button>
          <button
            class="btn btn-primary"
            @click="confirmChangePlace"
            :disabled="!newPlaceData.arrivalTime"
          >
            <i class="fa-solid fa-check me-2"></i>
            Confirmar cambio
          </button>
        </div>
      </div>
    </div>

    <v-dialog v-model="showTimePickerModal" max-width="400px">
      <v-card>
        <v-card-title class="modal-title">
          <i class="fa-regular fa-clock me-2"></i>
          Seleccionar {{ timePickerType === 'arrival' ? 'hora de llegada' : 'hora de salida' }}
        </v-card-title>

        <v-card-text class="modal-body-vuetify">
          <v-time-picker
            v-model="tempTime"
          ></v-time-picker>
        </v-card-text>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn text @click="closeTimePickerModal">Cancelar</v-btn>
          <v-btn color="#1b515e" dark @click="confirmTimeSelection">Aceptar</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import Hamburgermenu from '@/components/hamburgermenu.vue'
import PlaceCard from '@/components/placecard.vue'
import { mapActions, mapGetters, mapMutations } from 'vuex'
import { getErrorDetails } from '@/utils/utils'

export default {
  name: 'ChangePlace',
  components: {
    Hamburgermenu,
    PlaceCard,
  },

  data() {
    return {
      currentPlaceInfo: null,
      itineraryId: null,
      dayId: null,
      itineraryPlaceId: null,
      currentPage: 1,
      showConfirmModal: false,
      showTimePickerModal: false,
      selectedNewPlace: null,
      newPlaceData: {
        arrivalTime: '',
        leavingTime: '',
      },
      tempTime: '',
      timePickerType: null,
      loading: false,
    }
  },

  computed: {
    ...mapGetters('trips', ['favoriteIds', 'places', 'pagination', 'placesIds']),
  },

  mounted() {
    const {
      itineraryId,
      dayId,
      itineraryPlaceId,
      latitude,
      longitude,
      placeName,
      placeAddress,
      arrivalTime,
      leavingTime,
    } = this.$route.query

    if (!itineraryId || !dayId || !itineraryPlaceId || !latitude || !longitude) {
      this.$alert.error('Faltan parámetros')
      this.goBack()
      return
    }

    this.itineraryId = itineraryId
    this.dayId = dayId
    this.itineraryPlaceId = itineraryPlaceId
    this.currentPlaceInfo = {
      name: placeName || 'Lugar actual',
      address: placeAddress || '',
    }
    this.newPlaceData.arrivalTime = arrivalTime || ''
    this.newPlaceData.leavingTime = leavingTime || ''

    this.loadSuggestedPlaces(parseFloat(latitude), parseFloat(longitude))
  },

  methods: {
    ...mapActions('places', {
      fetchPreferencePlaces: 'fetchPreferencePlaces',
      fetchPlacesByIds: 'fetchPlacesByIds',
    }),
    ...mapActions('trips', {
      toggleFavoritePlace: 'toggleFavoritePlace',
      updatePlace: 'updatePlace',
    }),
    ...mapMutations('places', {
      setSelectedPlaceId: 'setSelectedPlaceId',
    }),
    ...mapMutations('trips', ['setPlaces', 'setPagination', 'setPlaceIds']),

    loadSuggestedPlaces(latitude, longitude) {
      this.loading = true

      this.fetchPreferencePlaces({
        latitude: latitude,
        longitude: longitude,
      }).then((response) => {
        this.setPlaceIds(response)
        this.fetchPlacesByIds({
          place_ids: this.placesIds,
        }).then((response) => {
          this.setPlaces(response.content)
          this.setPagination(response)
          this.loading = false
        })
      })
    },

    handlePageChange(page) {
      this.fetchPlacesByIds({
        place_ids: this.placesIds,
        page: page - 1,
        size: this.pagination.pageSize,
      }).then((response) => {
        this.setPlaces(response.content)
        this.setPagination(response)
        window.scrollTo({ top: 0, behavior: 'smooth' })
      }).catch((error) => {
        console.error('Error al cambiar página:', error)
      })
    },

    isFavorite(placeId) {
      return this.favoriteIds.includes(placeId)
    },

    getDefaultImage() {
      return 'https://www.turismomexico.es/wp-content/uploads/2015/07/chichen_itza.jpg'
    },

    selectPlace(place) {
      this.setSelectedPlaceId(place.placeId)
      this.$router.push({ name: 'site_description' })
    },

    toggleFavorite(place) {
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

    openConfirmModal(place) {
      this.selectedNewPlace = place
      this.showConfirmModal = true
    },

    closeConfirmModal() {
      this.showConfirmModal = false
      this.selectedNewPlace = null
      this.closeTimePickerModal()
    },

    openTimePickerModal(type) {
      this.timePickerType = type
      this.tempTime = type === 'arrival' ? this.newPlaceData.arrivalTime : this.newPlaceData.leavingTime
      this.showTimePickerModal = true
    },

    closeTimePickerModal() {
      this.showTimePickerModal = false
      this.tempTime = ''
      this.timePickerType = null
    },

    confirmTimeSelection() {
      if (this.timePickerType === 'arrival') {
        this.newPlaceData.arrivalTime = this.tempTime
      } else if (this.timePickerType === 'leaving') {
        this.newPlaceData.leavingTime = this.tempTime
      }
      this.closeTimePickerModal()
    },

    extractPostalCode(address) {
      const match = address.match(/\b(\d{5})\b/)
      return match ? match[1] : null
    },

    confirmChangePlace() {
      if (!this.newPlaceData.arrivalTime) {
        this.$alert.error('La hora de llegada es obligatoria')
        return
      }
      try {
        const postalCode = this.extractPostalCode(this.selectedNewPlace.formattedAddress)

        const placeData = {
          newPlaceId: this.selectedNewPlace.id,
          postalCode: postalCode,
          arrivalTime: this.newPlaceData.arrivalTime,
          leavingTime: this.newPlaceData.leavingTime || null,
        }
        this.updatePlace({
          itineraryId: this.itineraryId,
          dayId: this.dayId,
          placeId: this.itineraryPlaceId,
          placeData: placeData,
        }).then((response) => {
          this.$alert.success({
            title: 'Lugar actualizado',
            text: response.message,
          })
          setTimeout(() => {
            this.$router.push({ name: 'description-itinerary' })
          }, 1000)
        }).catch((error) => {
          this.$alert.error(getErrorDetails(error))
        })
      } catch (error) {
        this.$alert.error(getErrorDetails(error))
      }
    },

    goBack() {
      this.$router.push({ name: 'description-itinerary' })
    },
  },
}
</script>

<style scoped>
.fixed-header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  background-color: white;
  box-shadow: 0 2px 4px rgba(9, 128, 54, 0.1);
}

.header-content {
  align-items: center;
  padding: 0.5rem;
  max-width: 100%;
}

.header-title {
  flex: 1;
  text-align: center;
}

.header-title p {
  font-weight: bold;
  font-size: 1.2rem;
  color: #1b515e;
}

.header-spacer {
  width: 0px;
}

.change-place-container {
  min-height: 100vh;
  background: #f8f9fa;
  padding-top: 70px;
}

.menu-wrapper {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
}

.header-section {
  background: white;
  margin-top: 0.5rem;
  padding: 1rem;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  display: flex;
  align-items: center;
  gap: 1rem;
}

.btn-back {
  background: #f0f0f0;
  border: none;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s;
  color: #333;
  font-size: 1.1rem;
  flex-shrink: 0;
}

.btn-back:hover {
  background: #1b515e;
  color: white;
}

.header-content {
  flex: 1;
  min-width: 0;
}

.page-title {
  font-size: 1.75rem;
  font-weight: 700;
  color: #1b515e;
  margin: 0;
}

.page-subtitle {
  font-size: 0.95rem;
  color: #666;
  margin: 0.25rem 0 0 0;
}

.main-spacer {
  height: 20px;
}

.content-wrapper {
  max-width: 1200px;
  margin: 0 auto;
  padding-left: 1rem;
  padding-right: 1rem;
}

.current-place-info {
  background: white;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.section-title {
  font-size: 1.25rem;
  font-weight: 600;
  color: #1b515e;
  margin-bottom: 1rem;
  display: flex;
  align-items: center;
  flex-wrap: wrap;
}

.current-place-card {
  background: #f8f9fa;
  padding: 1rem;
  border-radius: 8px;
  border-left: 4px solid #1b515e;
  word-wrap: break-word;
  overflow-wrap: break-word;
}

.place-name {
  font-weight: 600;
  color: #333;
  font-size: 1.1rem;
  margin-bottom: 0.25rem;
  word-wrap: break-word;
  overflow-wrap: break-word;
}

.place-address {
  color: #666;
  font-size: 0.9rem;
  word-wrap: break-word;
  overflow-wrap: break-word;
}

.suggested-places-section {
  background: white;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.places-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 1.5rem;
  margin-bottom: 2rem;
}

.place-item {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
  min-width: 0;
}

.btn-select-place {
  background: #1b515e;
  color: white;
  border: none;
  padding: 0.75rem 1.25rem;
  border-radius: 8px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.95rem;
}

.btn-select-place:hover {
  background: #0e2325;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(27, 81, 94, 0.3);
}

.empty-state {
  text-align: center;
  padding: 3rem 1rem;
  color: #999;
}

.empty-state i {
  font-size: 3rem;
  margin-bottom: 1rem;
  opacity: 0.5;
}

.empty-state p {
  font-size: 1.1rem;
  margin: 0;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 2000;
  padding: 1rem;
}

.modal-content {
  background: white;
  border-radius: 12px;
  min-width: 500px;
  max-width: 90%;
  max-height: 90vh;
  overflow-y: auto;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
}

.modal-header {
  padding: 1.5rem;
  border-bottom: 1px solid #e9ecef;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.modal-header h3 {
  margin: 0;
  color: #1b515e;
  font-size: 1.5rem;
  font-weight: 600;
}

.modal-close {
  background: transparent;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
  color: #999;
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  transition: all 0.3s;
  flex-shrink: 0;
}

.modal-close:hover {
  background: #f0f0f0;
  color: #333;
}

.modal-body {
  padding: 1.5rem;
}

.modal-body-vuetify {
  padding: 1rem 1.5rem !important;
}

.modal-title {
  color: #1b515e !important;
  font-size: 1.25rem !important;
  font-weight: 600 !important;
  padding: 1rem 1.5rem !important;
  display: flex;
  align-items: center;
}

.new-place-preview {
  background: #f8f9fa;
  padding: 1rem;
  border-radius: 8px;
  margin-bottom: 1.5rem;
  border-left: 4px solid #28a745;
  word-wrap: break-word;
  overflow-wrap: break-word;
}

.new-place-preview h4 {
  margin: 0 0 0.5rem 0;
  color: #28a745;
  font-size: 0.9rem;
  font-weight: 600;
  text-transform: uppercase;
}

.place-name-preview {
  font-weight: 600;
  color: #333;
  font-size: 1.1rem;
  margin-bottom: 0.25rem;
  word-wrap: break-word;
  overflow-wrap: break-word;
}

.place-address-preview {
  color: #666;
  font-size: 0.9rem;
  margin: 0;
  word-wrap: break-word;
  overflow-wrap: break-word;
}

.form-group {
  margin-bottom: 1rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  color: #555;
  font-weight: 500;
}

.required {
  color: #dc3545;
}

.time-input-wrapper {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.75rem 1rem;
  background: #f8f9fa;
  border-radius: 6px;
  border: 1px solid #ddd;
}

.time-display-value {
  flex: 1;
  font-size: 1rem;
  color: #333;
}

.time-input-btn {
  background: white;
  border: 1px solid #ddd;
  border-radius: 6px;
  padding: 0.5rem 0.75rem;
  cursor: pointer;
  transition: all 0.3s;
  color: #1b515e;
}

.time-input-btn:hover {
  background: #1b515e;
  color: white;
  border-color: #1b515e;
}

.modal-footer {
  padding: 1rem 1.5rem;
  border-top: 1px solid #e9ecef;
  display: flex;
  justify-content: flex-end;
  gap: 0.75rem;
  flex-wrap: wrap;
}

.btn {
  padding: 0.5rem 1.25rem;
  border: none;
  border-radius: 6px;
  font-size: 0.95rem;
  cursor: pointer;
  transition: all 0.3s;
  font-weight: 500;
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
}

.btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.btn-secondary {
  background: #6c757d;
  color: white;
}

.btn-secondary:hover:not(:disabled) {
  background: #5a6268;
}

.btn-primary {
  background: #1b515e;
  color: white;
}

.btn-primary:hover:not(:disabled) {
  background: #0e2325;
}

@media (max-width: 768px) {
  .header-section {
    padding: 1rem;
  }

  .page-title {
    font-size: 1.5rem;
    word-wrap: break-word;
  }

  .page-subtitle {
    font-size: 0.85rem;
    word-wrap: break-word;
  }

  .content-wrapper {
    padding-left: 0.75rem;
    padding-right: 0.75rem;
  }

  .current-place-info {
    padding: 1rem;
  }

  .suggested-places-section {
    padding: 1rem;
  }

  .section-title {
    font-size: 1.1rem;
  }

  .places-grid {
    grid-template-columns: 1fr;
    gap: 1rem;
  }

  .modal-content {
    min-width: unset;
    width: 95%;
  }

  .modal-header {
    padding: 1rem;
  }

  .modal-header h3 {
    font-size: 1.25rem;
    word-wrap: break-word;
    flex: 1;
    min-width: 0;
  }

  .modal-body {
    padding: 1rem;
  }

  .modal-footer {
    padding: 1rem;
  }
}

@media (max-width: 576px) {
  .header-content {
    padding: 0.75rem;
  }

  .header-title p {
    font-size: 1rem;
  }

  .content-wrapper {
    padding-left: 0.5rem;
    padding-right: 0.5rem;
  }

  .current-place-info {
    padding: 0.75rem;
    border-radius: 8px;
  }

  .suggested-places-section {
    padding: 0.75rem;
    border-radius: 8px;
  }

  .place-name {
    font-size: 1rem;
  }

  .place-address {
    font-size: 0.85rem;
  }

  .btn-select-place {
    padding: 0.65rem 1rem;
    font-size: 0.9rem;
  }

  .section-title {
    font-size: 1rem;
  }

  .modal-header h3 {
    font-size: 1.1rem;
  }

  .place-name-preview {
    font-size: 1rem;
  }

  .place-address-preview {
    font-size: 0.85rem;
  }
}

@media (max-width: 400px) {
  .places-grid {
    grid-template-columns: 1fr;
  }

  .header-section {
    padding: 0.75rem;
    gap: 0.5rem;
  }

  .btn-back {
    width: 36px;
    height: 36px;
    font-size: 1rem;
  }

  .page-title {
    font-size: 1.25rem;
  }
}
</style>