<template>
  <div class="itinerary-container">
    <div v-if="!currentItinerary" class="error-container">
      <i class="fa-solid fa-exclamation-triangle"></i>
      <h3>Itinerario no encontrado</h3>
      <p>El itinerario que buscas no existe o no pudo ser cargado.</p>
      <button class="btn btn-primary" @click="goBack">
        <i class="fa-solid fa-arrow-left"></i>
        Volver
      </button>
    </div>

    <template v-else>
      <NavTopIDescription
        :travelTitle="currentItinerary.tripTitle"
        :placesCount="`[${totalPlaces} lugares]`"
        :travelState="getStateName()"
        :dateRange="formatDateRange()"
        :environmentalScore="currentItinerary.averageSustainableIndex || 0"
        @edit-travel="handleEdit"
        @show-environmental-info="handleEnvironmentalInfo"
      />

      <div class="main-spacer"></div>

      <div class="itinerary-content container py-4">
        <div v-for="(day, dayIndex) in sortedItineraryDays" :key="day.id" class="day-section mb-5">
          <div class="day-header bg-white">
            <h2 class="day-title mb-2">
              <i class="fa-solid fa-calendar-day me-2"></i>
              Día {{ dayIndex + 1 }}
            </h2>
            <p class="day-date text-muted mb-0">
              {{ formatDate(day.itineraryDate) }} • {{ day.places?.length || 0 }} lugares
            </p>
          </div>

          <div class="places-grid" v-if="day.places && day.places.length > 0">
            <draggable
              v-model="day.places"
              @end="onDragEnd(day)"
              item-key="id"
              :animation="200"
              handle=".drag-handle"
              class="drag-area"
            >
              <template #item="{ element: itineraryPlace }">
                <div class="draggable-item">
                  <div class="drag-handle-wrapper">
                    <div class="drag-handle">
                      <i class="fa-solid fa-grip-vertical"></i>
                    </div>
                  </div>

                  <div class="place-card-wrapper">
                    <PlaceCard
                      v-if="itineraryPlace.place"
                      :place="itineraryPlace.place"
                      :is-favorite="isFavorite(itineraryPlace.place.id)"
                      :logo-url="getPlaceImage(itineraryPlace.place)"
                      @select-place="selectPlace"
                      @toggle-favorite="toggleFavorite"
                    />
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
                      <button
                        class="btn-icon"
                        @click="openTimeModal(day.id, itineraryPlace)"
                        title="Cambiar horario"
                      >
                        <i class="fa-regular fa-clock"></i>
                      </button>
                      <button
                        class="btn-icon"
                        @click="openChangePlaceModal(day.id, itineraryPlace)"
                        title="Cambiar lugar"
                      >
                        <i class="fa-solid fa-exchange-alt"></i>
                      </button>
                    </div>
                  </div>
                </div>
              </template>
            </draggable>
          </div>
          <div v-else class="empty-day">
            <p>No hay lugares asignados para este día</p>
          </div>
        </div>
      </div>
    </template>

    <div v-if="showTimeModal" class="modal-overlay" @click="closeTimeModal">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>Modificar horario</h3>
          <button class="modal-close" @click="closeTimeModal">
            <i class="fa-solid fa-times"></i>
          </button>
        </div>

        <div class="modal-body">
          <div class="form-group">
            <label>Hora de llegada</label>
            <input type="time" v-model="editingTime.arrivalTime" class="form-control" />
          </div>

          <div class="form-group">
            <label>Hora de salida</label>
            <input type="time" v-model="editingTime.leavingTime" class="form-control" />
          </div>
        </div>

        <div class="modal-footer">
          <button class="btn btn-secondary" @click="closeTimeModal">Cancelar</button>
          <button class="btn btn-primary" @click="saveTimeChanges">Guardar</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import NavTopIDescription from '@/components/NavTopDescription.vue'
import PlaceCard from '@/components/placecard.vue'
import draggable from 'vuedraggable'
import { getErrorDetails } from '@/utils/utils'
import { mapActions, mapGetters, mapMutations } from 'vuex'

export default {
  name: 'ItineraryView',
  components: {
    NavTopIDescription,
    PlaceCard,
    draggable,
  },

  data() {
    return {
      showTimeModal: false,
      editingTime: {
        dayId: null,
        placeId: null,
        arrivalTime: '',
        leavingTime: '',
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
    }),
    ...mapMutations('places', {
      setSelectedPlaceId: 'setSelectedPlaceId',
    }),
    async loadItinerary(id) {
      this.fetchItineraryById(id)
        .then(() => {})
        .catch((error) => {
          this.$alert.error({
            title: 'Error al cargar el itinerario',
            text: getErrorDetails(error),
          })
        })
    },

    isFavorite(placeId) {
      return this.favoriteIds?.includes(placeId) || false
    },

    selectPlace(place) {
      this.setSelectedPlaceId(place.placeId)
      this.$router.push({ name: 'site_description' })
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

    getPlaceImage(place) {
      if (place?.images && place.images.length > 0) {
        return place.images[0]
      }
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
          this.loadItinerary(this.currentItinerary.id)
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
          this.showTimeModal = false
          this.loadItinerary(this.currentItinerary.id)
        })
        .catch((error) => {
          this.$alert.error({
            title: 'Error al actualizar la hora',
            text: getErrorDetails(error),
          })
        })
    },

    openChangePlaceModal(dayId, itineraryPlace) {
      console.log('Abrir modal para cambiar lugar:', dayId, itineraryPlace)
      // TODO: Implementar modal para cambiar lugar
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

      const start = new Date(sortedDays[0].itineraryDate)
      const end = new Date(sortedDays[sortedDays.length - 1].itineraryDate)

      const options = { day: 'numeric', month: 'short' }
      return `${start.toLocaleDateString('es-MX', options)} - ${end.toLocaleDateString('es-MX', { ...options, year: 'numeric' })}`
    },

    getStateName() {
      return 'Estado'
    },

    handleEdit() {
      console.log('Editar viaje')
    },

    handleEnvironmentalInfo(score) {
      console.log('Información ambiental:', score)
    },

    goBack() {
      this.$router.push({ name: 'itinerary' })
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

.main-spacer {
  height: 20px;
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
  overflow: visible; /* Cambiado para evitar cortes */
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

.places-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
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
  gap: 0.5rem;
}

.drag-handle-wrapper {
  position: absolute;
  top: -10px;
  right: -18px;
  z-index: 10;
  height: 0;
}

.drag-handle {
  cursor: grab;
  background: rgba(255, 255, 255, 0.95);
  padding: 6px 10px;
  border-radius: 6px;
  color: #999;
  font-size: 1rem;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.15);
  border: 1px solid #e0e0e0;
  transition: all 0.3s;
}

.drag-handle:hover {
  background: #fff;
  box-shadow: 0 3px 8px rgba(0, 0, 0, 0.2);
  color: #666;
}

.drag-handle:active {
  cursor: grabbing;
  transform: scale(0.95);
}

.place-card-wrapper {
  width: 100%;
}

.time-info {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #f8f9fa;
  padding: 0.75rem 1rem;
  border-radius: 8px;
  margin-top: 0.5rem;
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

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
}

.modal-content {
  background: white;
  border-radius: 12px;
  min-width: 350px;
  max-width: 90%;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.3);
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
  font-size: 1.25rem;
}

.modal-close {
  background: none;
  border: none;
  font-size: 1.5rem;
  color: #999;
  cursor: pointer;
  padding: 0;
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  transition: all 0.3s;
}

.modal-close:hover {
  background: #f0f0f0;
  color: #333;
}

.modal-body {
  padding: 1.5rem;
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

.form-control {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 1rem;
  transition: border-color 0.3s;
}

.form-control:focus {
  outline: none;
  border-color: #1b515e;
}

.modal-footer {
  padding: 1rem 1.5rem;
  border-top: 1px solid #e9ecef;
  display: flex;
  justify-content: flex-end;
  gap: 0.75rem;
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

@media (max-width: 768px) {
  .itinerary-container {
    padding-top: 100px;
    overflow-x: hidden;
  }

  .itinerary-content {
    padding-left: 0.5rem !important;
    padding-right: 0.5rem !important;
  }

  .day-section {
    margin-bottom: 2rem !important;
    overflow: visible;
  }

  .day-header {
    padding: 1rem 1.25rem;
    border-radius: 8px 8px 0 0;
  }

  .day-title {
    font-size: 1.25rem;
  }

  .day-date {
    font-size: 0.85rem;
  }

  .places-grid {
    grid-template-columns: 1fr;
    gap: 2rem;
    padding: 1rem;
    padding-right: 0.75rem;
    padding-left: 0.75rem;
    overflow: visible;
  }

  .drag-handle-wrapper {
    top: -8px;
    right: 5px;
  }

  .drag-handle {
    padding: 5px 8px;
    font-size: 0.9rem;
  }

  .draggable-item {
    width: 100%;
    max-width: 100%;
  }

  .place-card-wrapper {
    width: 100%;
    overflow: visible;
  }

  .time-info {
    padding: 0.5rem 0.75rem;
    font-size: 0.85rem;
  }

  .time-display {
    font-size: 0.85rem;
  }

  .btn-icon {
    padding: 0.4rem 0.6rem;
    font-size: 0.9rem;
  }

  .modal-content {
    min-width: unset;
    width: 90%;
    margin: 1rem;
  }
}

@media (max-width: 576px) {
  .itinerary-content {
    padding: 0.5rem !important;
  }

  .day-section {
    margin-bottom: 1.5rem !important;
  }

  .day-header {
    padding: 0.75rem 1rem;
    border-radius: 6px 6px 0 0;
  }

  .day-title {
    font-size: 1.1rem;
  }

  .day-date {
    font-size: 0.8rem;
    padding-left: 1.8rem;
  }

  .places-grid {
    padding: 0.75rem;
    padding-right: 0.5rem;
    padding-left: 0.5rem;
    gap: 0.75rem;
    border-radius: 0 0 6px 6px;
  }

  .drag-handle-wrapper {
    right: 3px;
    top: -6px;
  }

  .drag-handle {
    padding: 4px 6px;
    font-size: 0.85rem;
  }

  .time-info {
    padding: 0.5rem;
    flex-wrap: wrap;
    gap: 0.5rem;
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
}

@media (min-width: 769px) and (max-width: 1024px) {
  .places-grid {
    grid-template-columns: repeat(2, 1fr);
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
  }
}
</style>
