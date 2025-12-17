<template>
  <div class="itinerary-container">
    
    <div v-if="isLoading" class="loading-container">
      <img src="/icons8-hilandero.gif" alt="Cargando..." />
      <h3>Cargando itinerario...</h3>
      <p>Por favor espera un momento</p>
    </div>
    
    <div v-else-if="!isLoading && !currentItinerary" class="error-container">
      <i class="fa-solid fa-exclamation-triangle"></i>
      <h3>Itinerario no encontrado</h3>
      <p>El itinerario que buscas no existe o no pudo ser cargado.</p>
      <button class="btn btn-primary" @click="goBack">
        <i class="fa-solid fa-arrow-left"></i>
        Volver
      </button>
    </div>

    <template v-else-if="!isLoading && currentItinerary">
      <NavTopIDescription
        :travelTitle="currentItinerary.tripTitle"
        :placesCount="`[${totalPlaces} lugares]`"
        :travelState="getStateName()"
        :dateRange="formatDateRange()"
        :environmentalScore="currentItinerary.averageSustainableIndex || 0"
      />

      <div class="itinerary-content container py-4">
        <div v-if="currentItinerary.hotelPlace" class="day-section mb-5">
          <div class="day-header bg-white">
            <h2 class="day-title mb-2">
              <i class="fa-solid fa-hotel me-2"></i>
              Hotel
            </h2>
            <p class="day-date text-muted mb-0">
              {{ currentItinerary.hotelPlace.name }}
              <span
                v-if="
                  currentItinerary.certificatedHotel &&
                  currentItinerary.certificatedHotel.certifications &&
                  currentItinerary.certificatedHotel.certifications.length > 0
                "
                class="cert-count"
              >
                • {{ currentItinerary.certificatedHotel.certifications.length }} certificación<span
                  v-if="currentItinerary.certificatedHotel.certifications.length > 1"
                  >es</span
                >
              </span>
            </p>
          </div>

          <div class="places-grid">
            <div class="draggable-item">
              <div class="place-card-wrapper">
                <PlaceCard
                  :place="currentItinerary.hotelPlace"
                  :is-favorite="isFavorite(currentItinerary.hotelPlace.id)"
                  :logo-url="getDefaultImage()"
                  @select-place="selectPlace"
                  @toggle-favorite="toggleFavorite"
                  @show-details="showDetails"
                />
              </div>

              <div
                v-if="
                  currentItinerary.certificatedHotel &&
                  currentItinerary.certificatedHotel.certifications &&
                  currentItinerary.certificatedHotel.certifications.length > 0
                "
                class="certifications-info"
              >
                <div class="cert-title">
                  <i class="mdi mdi-leaf"></i>
                  <span>Certificaciones sostenibles</span>
                </div>
                <div class="cert-links">
                  <a
                    v-for="cert in currentItinerary.certificatedHotel.certifications"
                    :key="cert.id"
                    :href="cert.institutionWebsite"
                    target="_blank"
                    rel="noopener noreferrer"
                    class="cert-badge"
                  >
                    <i class="mdi mdi-certificate"></i>
                    <span>{{ cert.certification }}</span>
                    <i class="mdi mdi-open-in-new"></i>
                  </a>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div v-for="(day, dayIndex) in sortedItineraryDays" :key="day.id" class="day-section mb-5">
          <div class="day-header">
            <h2 class="day-title mb-2">
              <i class="fa-solid fa-calendar-day me-2"></i>
              Día {{ dayIndex + 1 }}
            </h2>
            <p class="day-date mb-0">
              {{ formatDate(day.itineraryDate) }} • {{ day.places?.length || 0 }} lugares
            </p>
          </div>

          <div class="places-grid" v-if="day.places && day.places.length > 0">
            <draggable
              v-model="day.places"
              @end="onDragEnd(day)"
              item-key="id"
              :animation="200"
              :disabled="!reorderMode"
              class="drag-area"
            >
              <template #item="{ element: itineraryPlace }">
                <div 
                  class="draggable-item" 
                  :class="{ 'reorder-active': reorderMode }"
                >
                  <div v-if="itineraryPlace.isVisited" class="visited-badge">
                    <i class="fa-solid fa-check-circle"></i>
                    <span>Visitado</span>
                  </div>

                  <div class="time-info">
                    <div class="time-display">
                      <i class="fa-regular fa-clock"></i>
                      <span
                        >{{ itineraryPlace.arrivalTime || '--:--' }} -
                        {{ itineraryPlace.leavingTime || '--:--' }}</span
                      >
                    </div>
                    <div class="place-actions">
                      <v-menu offset-y transition="slide-y-transition">
                        <template v-slot:activator="{ props }">
                          <button class="btn-icon btn-menu" v-bind="props">
                            <i class="fa-solid fa-ellipsis-vertical"></i>
                          </button>
                        </template>

                        <v-list class="actions-menu-list">
                          <v-list-item @click="toggleReorderMode(day.id)">
                            <template v-slot:prepend>
                              <i class="fa-solid fa-arrows-up-down menu-icon"></i>
                            </template>
                            <v-list-item-title>
                              {{ reorderMode ? 'Terminar reorden' : 'Reordenar' }}
                            </v-list-item-title>
                          </v-list-item>

                          <v-divider class="my-2"></v-divider>

                          <v-list-item @click="openTimeModal(day.id, itineraryPlace)">
                            <template v-slot:prepend>
                              <i class="fa-regular fa-clock menu-icon"></i>
                            </template>
                            <v-list-item-title>Cambiar horario</v-list-item-title>
                          </v-list-item>

                          <v-list-item @click="openChangePlaceModal(day.id, itineraryPlace)">
                            <template v-slot:prepend>
                              <i class="fa-solid fa-exchange-alt menu-icon"></i>
                            </template>
                            <v-list-item-title>Cambiar lugar</v-list-item-title>
                          </v-list-item>

                          <v-list-item @click="openDirections(itineraryPlace.place)">
                            <template v-slot:prepend>
                              <i class="fa-solid fa-directions menu-icon"></i>
                            </template>
                            <v-list-item-title>Cómo llegar</v-list-item-title>
                          </v-list-item>

                          <v-list-item
                            v-if="!itineraryPlace.isVisited"
                            @click="markAsVisited(day.id, itineraryPlace)"
                          >
                            <template v-slot:prepend>
                              <i class="fa-solid fa-check menu-icon"></i>
                            </template>
                            <v-list-item-title>Marcar como visitado</v-list-item-title>
                          </v-list-item>

                          <v-divider class="my-2"></v-divider>

                          <v-list-item
                            @click="showDeleteDialog(day.id, itineraryPlace)"
                            class="delete-item"
                          >
                            <template v-slot:prepend>
                              <i class="fa-solid fa-trash menu-icon"></i>
                            </template>
                            <v-list-item-title>Eliminar</v-list-item-title>
                          </v-list-item>
                        </v-list>
                      </v-menu>
                    </div>
                  </div>

                  <!-- Card del lugar -->
                  <div class="place-card-wrapper">
                    <PlaceCard
                      v-if="itineraryPlace.place"
                      :place="itineraryPlace.place"
                      :is-favorite="isFavorite(itineraryPlace.place.id)"
                      :logo-url="getDefaultImage()"
                      @select-place="selectPlace"
                      @toggle-favorite="toggleFavorite"
                      @show-details="showDetails"
                    />
                  </div>
                </div>
              </template>
            </draggable>

            <div
              v-if="(day.places?.length || 0) < 6"
              class="add-place-card"
              @click="openAddPlaceModal(day.id)"
            >
              <div class="add-place-content">
                <i class="fa-solid fa-plus"></i>
                <span>Agregar lugar</span>
              </div>
            </div>
          </div>
          <div v-else class="empty-day">
            <p>No hay lugares asignados para este día</p>
          </div>
        </div>
      </div>
    </template>

    <v-dialog v-model="showTimeModal" max-width="400px">
      <v-card>
        <v-card-title class="modal-title">
          <i class="fa-regular fa-clock me-2"></i>
          Modificar horario
        </v-card-title>

        <v-card-text class="modal-body">
          <div class="time-input-section">
            <label class="time-label">Hora de llegada</label>
            <div class="time-input-wrapper">
              <span class="time-display-value">{{ editingTime.arrivalTime || '--:--' }}</span>
              <button class="time-input-btn" @click="openTimePickerModal('arrival')">
                <i class="fa-regular fa-clock"></i>
              </button>
            </div>
          </div>

          <v-divider class="my-4"></v-divider>

          <div class="time-input-section">
            <label class="time-label">Hora de salida</label>
            <div class="time-input-wrapper">
              <span class="time-display-value">{{ editingTime.leavingTime || '--:--' }}</span>
              <button class="time-input-btn" @click="openTimePickerModal('leaving')">
                <i class="fa-regular fa-clock"></i>
              </button>
            </div>
          </div>
        </v-card-text>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn text @click="closeTimeModal">Cancelar</v-btn>
          <v-btn color="#1b515e" dark @click="saveTimeChanges">Guardar</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-dialog v-model="showTimePickerModal" max-width="400px">
      <v-card>
        <v-card-title class="modal-title">
          <i class="fa-regular fa-clock me-2"></i>
          Seleccionar {{ timePickerType === 'arrival' ? 'hora de llegada' : 'hora de salida' }}
        </v-card-title>

        <v-card-text class="modal-picker-body">
          <v-time-picker v-model="tempTime"></v-time-picker>
        </v-card-text>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn text @click="closeTimePickerModal">Cancelar</v-btn>
          <v-btn color="#1b515e" dark @click="confirmTimeSelection">Aceptar</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-dialog v-model="showDeleteConfirmDialog" max-width="400px">
      <v-card>
        <v-card-title class="modal-title error-title">
          <i class="fa-solid fa-exclamation-circle me-2"></i>
          ¿Eliminar lugar?
        </v-card-title>

        <v-card-text class="modal-body">
          <p class="mb-0">
            ¿Estás seguro de que deseas eliminar "<strong>{{ placeToDelete?.place?.name }}</strong
            >" de este día?
          </p>
        </v-card-text>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn text @click="showDeleteConfirmDialog = false">Cancelar</v-btn>
          <v-btn
            color="error"
            dark
            @click="deletePlace(pendingDeleteData.dayId, pendingDeleteData.placeId)"
          >
            Eliminar
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import NavTopIDescription from '@/components/NavTopDescription.vue'
import PlaceCard from '@/components/placecard.vue'
import draggable from 'vuedraggable'
import { getErrorDetails } from '@/utils/utils'
import { mapActions, mapGetters, mapMutations } from 'vuex'
import { isLogged } from '@/modules/auth/store/getters'

export default {
  name: 'ItineraryView',
  components: {
    NavTopIDescription,
    PlaceCard,
    draggable,
  },

  data() {
    return {
      isLoading: true,
      showTimeModal: false,
      showDeleteConfirmDialog: false,
      showTimePickerModal: false,
      timePickerType: null,
      tempTime: '',
      reorderMode: false,
      currentReorderDayId: null,
      editingTime: {
        dayId: null,
        placeId: null,
        arrivalTime: '',
        leavingTime: '',
      },
      placeToDelete: null,
      pendingDeleteData: {
        dayId: null,
        placeId: null,
      },
    }
  },

  computed: {
    ...mapGetters('trips', [
      'currentItinerary',
      'itineraryDays',
      'currentItineraryId',
      'favoriteIds',
    ]),

    sortedItineraryDays() {
      if (!this.currentItinerary?.itineraryDays) return []
      return [...this.currentItinerary.itineraryDays]
        .filter((day) => day && day.itineraryDate)
        .sort((a, b) => new Date(a.itineraryDate) - new Date(b.itineraryDate))
    },

    totalPlaces() {
      if (!this.currentItinerary?.itineraryDays) return 0
      return this.currentItinerary.itineraryDays.reduce((total, day) => {
        return total + (day?.places?.length || 0)
      }, 0)
    },
  },

  mounted() {
    const itineraryId = this.currentItineraryId
    if (itineraryId) {
      this.loadItinerary(itineraryId)
    } else {
      console.error('No se encontró el ID del itinerario')
    }
  },

  methods: {
    ...mapActions('trips', {
      fetchItineraryById: 'fetchItineraryById',
      updateVisitOrder: 'updateVisitOrder',
      updatePlaceTime: 'updatePlaceTime',
      toggleFavoritePlace: 'toggleFavoritePlace',
      setPlaceVisited: 'setPlaceVisited',
      deletePlaceFromDay: 'deletePlaceFromDay',
    }),
    ...mapMutations('places', {
      setSelectedPlaceId: 'setSelectedPlaceId',
    }),

    async loadItinerary(id) {
      this.isLoading = true
      this.fetchItineraryById(id)
        .then(() => {
          this.isLoading = false
        })
        .catch((error) => {
          this.isLoading = false
          this.$alert.error({
            title: 'Error al cargar el itinerario',
            text: getErrorDetails(error),
          })
        })
    },

    toggleReorderMode(dayId) {
      this.reorderMode = !this.reorderMode
      this.currentReorderDayId = this.reorderMode ? dayId : null
      
      if (this.reorderMode) {
        this.$alert.info({
          title: 'Modo reordenar activado',
          text: 'Ahora puedes arrastrar los lugares para cambiar su orden',
        })
      }
    },

    isFavorite(placeId) {
      return this.favoriteIds?.includes(placeId) || false
    },

    selectPlace(place) {
      if (this.reorderMode) return
      
      this.setSelectedPlaceId(place.placeId)
      this.$router.push({
        name: 'site_description',
        query: { from: 'description-itinerary' },
      })
    },

    showDetails(place) {
      if (this.reorderMode) return
      
      this.setSelectedPlaceId(place.placeId)
      this.$router.push({
        name: 'site_description',
        query: { from: 'description-itinerary' },
      })
    },

    async toggleFavorite(place) {
      if (this.reorderMode) return
      
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

    getDefaultImage() {
      return 'https://www.turismomexico.es/wp-content/uploads/2015/07/chichen_itza.jpg'
    },

    async onDragEnd(day) {
      const placeIds = day.places.map((p) => p.id)

      this.updateVisitOrder({
        itineraryId: this.currentItinerary.id,
        dayId: day.id,
        placeIds: placeIds,
      })
        .then((response) => {
          this.$alert.success({
            title: 'Orden actualizado',
            text: response.message,
          })
          this.reorderMode = false
        })
        .catch((error) => {
          this.$alert.error({
            title: 'Error al actualizar el orden',
            text: getErrorDetails(error),
          })
        })
    },

    openTimeModal(dayId, itineraryPlace) {
      this.editingTime = {
        dayId: dayId,
        placeId: itineraryPlace.id,
        arrivalTime: itineraryPlace.arrivalTime || '',
        leavingTime: itineraryPlace.leavingTime || '',
      }
      this.showTimeModal = true
    },

    closeTimeModal() {
      this.showTimeModal = false
      this.showTimePickerModal = false
      this.tempTime = ''
      this.timePickerType = null
    },

    openTimePickerModal(type) {
      this.timePickerType = type
      this.tempTime =
        type === 'arrival' ? this.editingTime.arrivalTime : this.editingTime.leavingTime
      this.showTimePickerModal = true
    },

    closeTimePickerModal() {
      this.showTimePickerModal = false
      this.tempTime = ''
      this.timePickerType = null
    },

    confirmTimeSelection() {
      if (this.timePickerType === 'arrival') {
        this.editingTime.arrivalTime = this.tempTime
      } else if (this.timePickerType === 'leaving') {
        this.editingTime.leavingTime = this.tempTime
      }
      this.closeTimePickerModal()
    },

    async saveTimeChanges() {
      this.updatePlaceTime({
        itineraryId: this.currentItinerary.id,
        dayId: this.editingTime.dayId,
        placeId: this.editingTime.placeId,
        arrivalTime: this.editingTime.arrivalTime,
        leavingTime: this.editingTime.leavingTime,
      })
        .then((response) => {
          this.$alert.success({
            title: 'Horario actualizado',
            text: response.message,
          })
          this.closeTimeModal()
        })
        .catch((error) => {
          this.$alert.error({
            title: 'Error al actualizar la hora',
            text: getErrorDetails(error),
          })
        })
    },

    openChangePlaceModal(dayId, itineraryPlace) {
      this.$router.push({
        name: 'change-place',
        query: {
          itineraryId: this.currentItineraryId,
          dayId: dayId,
          itineraryPlaceId: itineraryPlace.id,
          latitude: itineraryPlace.place.lat,
          longitude: itineraryPlace.place.lng,
          placeName: itineraryPlace.place.name,
          placeAddress: itineraryPlace.place.formattedAddress,
          arrivalTime: itineraryPlace.arrivalTime,
          leavingTime: itineraryPlace.leavingTime,
        },
      })
    },

    openAddPlaceModal(dayId) {
      this.$router.push({
        name: 'add-place',
        query: {
          itineraryId: this.currentItineraryId,
          dayId: dayId,
          latitude: this.currentItinerary.hotelPlace.lat,
          longitude: this.currentItinerary.hotelPlace.lng,
          hotelName: this.currentItinerary.hotelPlace.name,
          hotelAddress: this.currentItinerary.hotelPlace.formattedAddress,
        },
      })
    },

    openDirections(place) {
      let mapsUrl = ''

      if (place.googleMapsUrl) {
        mapsUrl = place.googleMapsUrl
      } else if (place.lat && place.lng) {
        mapsUrl = `https://www.google.com/maps?q=${place.lat},${place.lng}`
      } else {
        this.$alert.error({
          title: 'Ubicación no disponible',
          text: 'No se pudo obtener la ubicación de este lugar',
        })
        return
      }

      window.open(mapsUrl, '_blank')
    },

    async markAsVisited(dayId, itineraryPlace) {
      this.setPlaceVisited({
        itineraryId: this.currentItinerary.id,
        dayId: dayId,
        placeId: itineraryPlace.id,
      })
        .then((response) => {
          this.$alert.success({
            title: 'Lugar visitado',
            text: response.message,
          })
        })
        .catch((error) => {
          this.$alert.error({
            title: 'Error al marcar como visitado',
            text: getErrorDetails(error),
          })
        })
    },

    showDeleteDialog(dayId, itineraryPlace) {
      this.placeToDelete = itineraryPlace
      this.pendingDeleteData = {
        dayId: dayId,
        placeId: itineraryPlace.id,
      }
      this.showDeleteConfirmDialog = true
    },

    async deletePlace(dayId, placeId) {
      this.deletePlaceFromDay({
        itineraryId: this.currentItinerary.id,
        dayId: dayId,
        placeId: placeId,
      })
        .then((response) => {
          this.$alert.success({
            title: 'Lugar eliminado',
            text: response.message,
          })
          this.showDeleteConfirmDialog = false
        })
        .catch((error) => {
          this.$alert.error({
            title: 'Error al eliminar el lugar',
            text: getErrorDetails(error),
          })
        })
    },

    formatDate(dateString) {
      if (!dateString) return ''
      const date = new Date(dateString)
      const options = { day: 'numeric', month: 'long', year: 'numeric' }
      return date.toLocaleDateString('es-MX', options)
    },

    formatDateRange() {
      if (!this.currentItinerary?.itineraryDays?.length) {
        return ''
      }

      const sortedDays = [...this.currentItinerary.itineraryDays]
        .filter((day) => day && day.itineraryDate)
        .sort((a, b) => new Date(a.itineraryDate) - new Date(b.itineraryDate))

      if (sortedDays.length === 0) return ''

      const firstDate = new Date(sortedDays[0].itineraryDate)
      const lastDate = new Date(sortedDays[sortedDays.length - 1].itineraryDate)

      const options = { day: 'numeric', month: 'short' }
      const firstDateStr = firstDate.toLocaleDateString('es-MX', options)
      const lastDateStr = lastDate.toLocaleDateString('es-MX', options)

      return `${firstDateStr} - ${lastDateStr}`
    },

    getStateName() {
      if (!this.currentItinerary?.certificatedHotel?.settlement?.state?.state) {
        return ''
      }
      return this.currentItinerary.certificatedHotel.settlement.state.state
    },

    goBack() {
      this.$router.go(-1)
    },
  },
}
</script>

<style scoped>
.itinerary-container {
  min-height: 100vh;
  background-color: #f8f9fa;
  padding-top: 100px;
}

.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 50vh;
  text-align: center;
  padding: 2rem;
}

.loading-container h3 {
  color: #1b515e;
  margin-bottom: 0.5rem;
}

.loading-container p {
  color: #666;
  margin-bottom: 1.5rem;
}

.error-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 50vh;
  text-align: center;
  padding: 2rem;
}

.error-container i {
  font-size: 4rem;
  color: #f44336;
  margin-bottom: 1rem;
}

.error-container h3 {
  color: #1b515e;
  margin-bottom: 0.5rem;
}

.error-container p {
  color: #666;
  margin-bottom: 1.5rem;
}

.itinerary-content {
  max-width: 1400px;
  margin: 0 auto;
}

.day-section {
  background: transparent;
  border-radius: 0;
  padding: 0;
  box-shadow: none;
  overflow: visible;
  margin-bottom: 3rem !important;
}

.day-header {
  border-bottom: 2px solid #e9ecef;
  padding: 1rem 1.5rem;
  background: white;
  border-radius: 12px 12px 0 0;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.day-title {
  color: #1b515e;
  font-size: 1.5rem;
  font-weight: bold;
  display: flex;
  align-items: center;
}

.day-title i {
  color: #4caf50;
}

.day-date {
  font-size: 0.95rem;
  padding-left: 2.2rem;
}

.cert-count {
  color: #28a745;
  font-weight: 500;
}

.places-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 1.5rem;
  padding: 2rem;
  background: white;
  border-radius: 0 0 12px 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  overflow: visible;
}

.empty-day {
  padding: 2rem;
  text-align: center;
  background: white;
  border-radius: 0 0 12px 12px;
  color: #999;
}

.drag-area {
  display: contents;
}

.draggable-item {
  position: relative;
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
  min-width: 0;
  transition: all 0.3s ease;
}

.draggable-item.reorder-active {
  animation: shake 0.5s infinite;
  cursor: grab;
}

.draggable-item.reorder-active:active {
  cursor: grabbing;
}

@keyframes shake {
  0%, 100% {
    transform: translateX(0) rotate(0deg);
  }
  25% {
    transform: translateX(-2px) rotate(-1deg);
  }
  75% {
    transform: translateX(2px) rotate(1deg);
  }
}

.visited-badge {
  position: absolute;
  top: -8px;
  left: 5px;
  z-index: 10;
  background: #28a745;
  color: white;
  padding: 6px 10px;
  border-radius: 6px;
  display: flex;
  align-items: center;
  gap: 0.3rem;
  font-size: 0.85rem;
  box-shadow: 0 2px 6px rgba(40, 167, 69, 0.3);
}

.place-card-wrapper {
  width: 100%;
  min-width: 0;
  pointer-events: auto;
}

.reorder-active .place-card-wrapper {
  pointer-events: none;
}

.time-info {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #f8f9fa;
  padding: 0.75rem 1rem;
  border-radius: 8px;
  border: 1px solid #e0e0e0;
}

.time-display {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: #555;
  font-size: 0.9rem;
}

.time-display i {
  color: #1b515e;
}

.place-actions {
  display: flex;
  gap: 0.5rem;
  position: relative;
  z-index: 20;
}

.btn-icon {
  background: white;
  border: 1px solid #ddd;
  border-radius: 6px;
  padding: 0.5rem 0.75rem;
  cursor: pointer;
  transition: all 0.3s;
  color: #555;
}

.btn-icon:hover {
  background: #1b515e;
  color: white;
  border-color: #1b515e;
}

.btn-menu {
  min-width: 40px;
}

.add-place-card {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 200px;
  max-height: 300px;
  border: 2px dashed #4caf50;
  border-radius: 12px;
  background: rgba(76, 175, 80, 0.05);
  cursor: pointer;
  transition: all 0.3s;
}

.add-place-card:hover {
  border-color: #1b515e;
  background: rgba(27, 81, 94, 0.08);
  transform: translateY(-2px);
}

.add-place-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.75rem;
  color: #4caf50;
  font-weight: 600;
  transition: color 0.3s;
}

.add-place-card:hover .add-place-content {
  color: #1b515e;
}

.add-place-content i {
  font-size: 2.5rem;
}

.actions-menu-list {
  min-width: 280px !important;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.2) !important;
  border-radius: 8px !important;
}

.actions-menu-list .v-list-item {
  padding: 0.75rem 1rem !important;
  height: auto !important;
  min-height: 48px !important;
}

.actions-menu-list .v-list-item-title {
  font-size: 0.95rem !important;
  color: #333 !important;
  font-weight: 500 !important;
}

.actions-menu-list .menu-icon {
  width: 24px;
  text-align: center;
  color: #1b515e;
  margin-right: 0.75rem;
}

.actions-menu-list .delete-item {
  color: #dc3545 !important;
}

.actions-menu-list .delete-item .menu-icon {
  color: #dc3545 !important;
}

.actions-menu-list .v-list-item:hover {
  background-color: #f8f9fa !important;
}

.modal-title {
  color: #1b515e !important;
  font-size: 1.25rem !important;
  font-weight: 600 !important;
  padding: 1rem 1.5rem !important;
  display: flex;
  align-items: center;
}

.modal-title.error-title {
  color: #dc3545 !important;
}

.modal-body {
  padding: 1rem 1.5rem !important;
}

.modal-picker-body {
  padding: 0.2rem 0.5rem !important;
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-picker-body :deep(.v-picker__header) {
  max-width: 100px !important;
  max-height: 80px;
  margin: 0 auto;
}

.modal-picker-body :deep(.v-time-picker-controls__time) {
  max-width: 150px !important;
  max-height: 90px;
  margin: 0 auto;
}

.modal-picker-body :deep(.v-theme--light .v-btn--size-default) {
  width: 90px !important;
  height: 80px !important;
}

.modal-picker-body :deep(.v-time-picker-controls__ampm) {
  width: 10px !important;
  height: 10px !important;
  margin-bottom: 100px;
}

.modal-picker-body :deep(.v-picker__body) {
  margin-left: 35px;
  padding: 2px !important;
  max-height: 250px;
  max-width: 250px;
}

.modal-picker-body :deep(.v-btn__content) {
  max-height: 250px;
  max-width: 250px;
}

.modal-picker-body :deep(.v-time-picker) {
  align-items: center;
}

.time-input-section {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.time-label {
  font-size: 0.9rem;
  font-weight: 600;
  color: #1b515e;
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

.certifications-info {
  margin-top: 0.75rem;
  padding: 1rem;
  background: #f0f8f5;
  border-radius: 8px;
  border-left: 4px solid #28a745;
}

.cert-title {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: #28a745;
  font-weight: 600;
  font-size: 0.95rem;
  margin-bottom: 0.75rem;
}

.cert-title i {
  font-size: 1.1rem;
}

.cert-links {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.cert-badge {
  display: inline-flex;
  align-items: center;
  gap: 0.4rem;
  padding: 0.5rem 0.75rem;
  background: white;
  border: 1px solid #28a745;
  border-radius: 20px;
  color: #28a745;
  text-decoration: none;
  font-size: 0.85rem;
  transition: all 0.3s;
}

.cert-badge:hover {
  background: #28a745;
  color: white;
  transform: translateY(-2px);
  box-shadow: 0 2px 8px rgba(40, 167, 69, 0.3);
}

.cert-badge i:first-child {
  font-size: 1rem;
}

.cert-badge i:last-child {
  font-size: 0.75rem;
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

.btn-secondary {
  background: #6c757d;
  color: white;
}

.btn-secondary:hover {
  background: #5a6268;
}

.btn-primary {
  background: #1b515e;
  color: white;
}

.btn-primary:hover {
  background: #0e2325;
}

@media (max-width: 1024px) {
  .places-grid {
    grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
  }
}

@media (max-width: 768px) {
  .itinerary-container {
    padding-top: 100px;
    overflow-x: hidden;
    width: 100%;
  }

  .itinerary-content {
    padding-left: 0.5rem !important;
    padding-right: 0.5rem !important;
    max-width: 100%;
    overflow-x: hidden;
  }

  .day-section {
    margin-bottom: 2rem !important;
    overflow: visible;
    max-width: 100%;
  }

  .day-header {
    padding: 1rem 1.25rem;
    border-radius: 8px 8px 0 0;
    max-width: 100%;
  }

  .day-title {
    font-size: 1.25rem;
  }

  .day-date {
    font-size: 0.85rem;
  }

  .places-grid {
    grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
    gap: 2rem;
    padding: 1rem;
    padding-right: 0.75rem;
    padding-left: 0.75rem;
    overflow: visible;
    max-width: 100%;
  }

  .drag-area {
    max-width: 100%;
    overflow: visible;
  }

  .draggable-item {
    width: 100%;
    max-width: 100%;
    overflow: visible;
    box-sizing: border-box;
  }

  .place-card-wrapper {
    width: 100%;
    max-width: 100%;
    overflow: visible;
    box-sizing: border-box;
  }

  .time-info {
    padding: 0.5rem 0.75rem;
    font-size: 0.85rem;
    max-width: 100%;
    box-sizing: border-box;
  }

  .time-display {
    font-size: 0.85rem;
  }

  .btn-icon {
    padding: 0.4rem 0.6rem;
    font-size: 0.9rem;
  }

  .visited-badge {
    top: -6px;
    left: 5px;
    padding: 4px 8px;
    font-size: 0.8rem;
  }

  .certifications-info {
    padding: 0.75rem;
    max-width: 100%;
    box-sizing: border-box;
  }

  .cert-title {
    font-size: 0.9rem;
  }

  .cert-badge {
    font-size: 0.8rem;
    padding: 0.4rem 0.6rem;
    word-wrap: break-word;
  }

  .actions-menu-list {
    min-width: 240px !important;
  }

  .add-place-card {
    min-height: 160px;
  }

  .add-place-content i {
    font-size: 2rem;
  }
}

@media (max-width: 576px) {
  .itinerary-content {
    padding: 0.5rem !important;
    max-width: 100%;
  }

  .day-section {
    margin-bottom: 1.5rem !important;
    max-width: 100%;
  }

  .day-header {
    padding: 0.75rem 1rem;
    border-radius: 6px 6px 0 0;
    max-width: 100%;
  }

  .day-title {
    font-size: 1.1rem;
  }

  .day-date {
    font-size: 0.8rem;
    padding-left: 1.8rem;
  }

  .places-grid {
    grid-template-columns: 1fr;
    padding: 0.75rem;
    padding-right: 0.5rem;
    padding-left: 0.5rem;
    gap: 0.75rem;
    border-radius: 0 0 6px 6px;
    max-width: 100%;
  }

  .drag-area {
    max-width: 100%;
  }

  .draggable-item {
    max-width: 100%;
  }

  .place-card-wrapper {
    max-width: 100%;
  }

  .time-info {
    padding: 0.5rem;
    flex-wrap: wrap;
    gap: 0.5rem;
    max-width: 100%;
  }

  .time-display {
    font-size: 0.8rem;
  }

  .place-actions {
    gap: 0.25rem;
  }

  .btn-icon {
    padding: 0.35rem 0.5rem;
    font-size: 0.85rem;
  }

  .visited-badge {
    top: -4px;
    left: 3px;
    padding: 3px 6px;
    font-size: 0.75rem;
  }

  .certifications-info {
    padding: 0.5rem;
    max-width: 100%;
  }

  .cert-title {
    font-size: 0.85rem;
  }

  .cert-badge {
    font-size: 0.75rem;
    padding: 0.35rem 0.5rem;
  }

  .cert-links {
    flex-wrap: wrap;
  }

  .actions-menu-list {
    min-width: 200px !important;
  }

  .add-place-card {
    min-height: 140px;
  }

  .add-place-content i {
    font-size: 1.75rem;
  }

  .add-place-content {
    font-size: 0.9rem;
  }
}

* {
  box-sizing: border-box;
}

@media (max-width: 768px) {
  .draggable-item,
  .place-card-wrapper,
  .time-info {
    transform: translateZ(0);
    backface-visibility: hidden;
    max-width: 100%;
    overflow: visible;
  }
}
</style>