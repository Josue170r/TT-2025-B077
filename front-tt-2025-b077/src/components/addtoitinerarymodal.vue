<template>
  <div>
    <button
      @click="openAddToItineraryModal"
      class="btn map-btn rounded-pill px-4 py-2 border-0 ms-3"
    >
      <i class="fa-solid fa-calendar-plus me-2"></i>
      Agregar al itinerario
    </button>

    <v-dialog v-model="addToItineraryModalOpen" max-width="700px">
      <v-card>
        <v-card-title v-if="userItineraries.length === 0" class="d-flex align-items-center pa-4">
          <i
            class="fa-regular fa-calendar-xmark me-2"
            style="color: #1b515e; font-size: 1.5rem"
          ></i>
          <h6 class="mb-0">No hay itinerarios</h6>
        </v-card-title>

        <v-card-text v-if="userItineraries.length === 0" class="text-center py-5">
          <p class="text-muted mb-4">
            No tienes ningún itinerario creado. Crea uno para agregar este lugar.
          </p>
        </v-card-text>

        <v-card-actions
          v-if="userItineraries.length === 0"
          class="pa-4 d-flex justify-content-center"
        >
          <v-btn color="primary" @click="redirectToNewTrips">
            <i class="fa-solid fa-plus me-2"></i>
            Crear nuevo itinerario
          </v-btn>
          <v-btn variant="text" @click="addToItineraryModalOpen = false">Cancelar</v-btn>
        </v-card-actions>

        <template v-else>
          <v-card-title class="pa-4">
            <h6 class="mb-0">Seleccionar itinerario</h6>
          </v-card-title>

          <v-card-text class="pa-4" style="max-height: 600px; overflow-y: auto">
            <div v-for="itinerary in userItineraries" :key="itinerary.id" class="mb-4">
              <v-card class="itinerary-card cursor-pointer" @click="selectItinerary(itinerary)">
                <v-carousel
                  v-if="itinerary.images && itinerary.images.length > 0"
                  height="250"
                  hide-delimiters
                  class="itinerary-carousel"
                >
                  <v-carousel-item
                    v-for="(image, idx) in itinerary.images.slice(0, 3)"
                    :key="idx"
                    :src="image"
                    cover
                  ></v-carousel-item>
                </v-carousel>

                <v-card-text class="pa-4">
                  <div class="d-flex justify-content-between align-items-start mb-3">
                    <div class="flex-grow-1">
                      <h5 class="mb-2" style="color: #1b515e; font-weight: 700">
                        {{ itinerary.tripTitle }}
                      </h5>
                      <p class="mb-2 text-muted">
                        <i class="fa-solid fa-calendar-days me-2"></i>
                        {{ formatDateRange(itinerary.startDate, itinerary.endDate) }}
                      </p>
                      <p class="mb-2 text-muted">
                        <i class="fa-solid fa-map-pin me-2"></i>
                        {{ itinerary.totalPlaces }} lugares
                      </p>
                    </div>

                    <div class="text-center">
                      <div
                        class="sustainability-badge"
                        :style="getSustainabilityStyle(itinerary.averageSustainableIndex)"
                      >
                        <small class="fw-bold"
                          >{{ (itinerary.averageSustainableIndex * 100).toFixed(0) }}%</small
                        >
                      </div>
                      <small class="text-muted d-block mt-1">Sostenible</small>
                    </div>
                  </div>

                  <div class="d-flex justify-content-end">
                    <v-btn
                      color="white"
                      variant="outlined"
                      size="small"
                      @click.stop="selectItinerary(itinerary)"
                      class="select-btn"
                    >
                      <i class="fa-solid fa-check me-2"></i>
                      Seleccionar
                    </v-btn>
                  </div>
                </v-card-text>
              </v-card>
            </div>
          </v-card-text>

          <v-card-actions class="pa-4">
            <v-spacer></v-spacer>
            <v-btn variant="text" @click="addToItineraryModalOpen = false">Cancelar</v-btn>
          </v-card-actions>
        </template>
      </v-card>
    </v-dialog>

    <v-dialog v-model="selectDayModalOpen" max-width="600px">
      <v-card>
        <v-card-title class="pa-4">
          <h6 class="mb-0">Seleccionar día</h6>
        </v-card-title>

        <v-card-text class="pa-4">
          <v-skeleton-loader v-if="loadingDays" type="list-item@5" />

          <div v-else>
            <p class="trip-name-modal">
              <strong>{{ selectedItinerary?.tripTitle }}</strong>
            </p>

            <div v-if="itineraryDays.length === 0" class="text-center py-5">
              <p class="text-muted">No hay días disponibles en este itinerario.</p>
            </div>

            <div v-else class="days-grid-modal">
              <div
                v-for="(day, idx) in itineraryDays"
                :key="day.itineraryDayId"
                class="day-item-modal"
                @click="selectDay(day, idx)"
              >
                <div class="day-badge">{{ idx + 1 }}</div>
                <div class="day-info">
                  <div class="day-label">Día {{ idx + 1 }}</div>
                  <div class="day-date-text">{{ formatDate(day.itineraryDate) }}</div>
                </div>
                <i class="fa-solid fa-chevron-right"></i>
              </div>
            </div>
          </div>
        </v-card-text>

        <v-card-actions class="pa-4">
          <v-spacer></v-spacer>
          <v-btn variant="text" @click="selectDayModalOpen = false">Cancelar</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-dialog v-model="confirmAddModalOpen" max-width="600px">
      <v-card>
        <v-card-title class="pa-4">
          <h6 class="mb-0">Confirmar agregación de lugar</h6>
        </v-card-title>

        <v-card-text class="pa-4">
          <div class="info-section mb-4">
            <p class="fw-bold mb-2" style="color: #1b515e">Itinerario:</p>
            <p class="mb-1">{{ selectedItinerary?.tripTitle }}</p>
            <small class="text-muted">{{ formatDate(selectedDay?.itineraryDate) }}</small>
          </div>

          <div class="info-section mb-4">
            <p class="fw-bold mb-2" style="color: #1b515e">Lugar:</p>
            <p class="mb-1">{{ selectedPlaceDetails?.name }}</p>
            <small class="text-muted">{{ selectedPlaceDetails?.formattedAddress }}</small>
          </div>

          <div class="time-section mb-4">
            <label class="form-label fw-bold mb-3">Hora de llegada *</label>
            <v-btn
              variant="outlined"
              color="#1b515e"
              class="w-100"
              @click="openTimePicker('arrival')"
            >
              <i class="fa-solid fa-clock me-2"></i>
              {{ placeFormData.arrivalTime || 'Seleccionar hora' }}
            </v-btn>
          </div>

          <div class="time-section mb-4">
            <label class="form-label fw-bold mb-3">Hora de salida</label>
            <v-btn
              variant="outlined"
              color="#1b515e"
              class="w-100"
              @click="openTimePicker('departure')"
            >
              <i class="fa-solid fa-clock me-2"></i>
              {{ placeFormData.leavingTime || 'Seleccionar hora (opcional)' }}
            </v-btn>
          </div>
        </v-card-text>

        <v-card-actions class="pa-4">
          <v-spacer></v-spacer>
          <v-btn variant="text" @click="confirmAddModalOpen = false">Cancelar</v-btn>
          <v-btn
            color="primary"
            @click="confirmAddPlace"
            :loading="submittingPlace"
            :disabled="!placeFormData.arrivalTime"
          >
            <i class="fa-solid fa-check me-2"></i>
            Agregar lugar
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-dialog v-model="showTimePickerModal" max-width="400px">
      <v-card>
        <v-card-title class="modal-title pa-4">
          <i class="fa-regular fa-clock me-2"></i>
          Seleccionar {{ timePickerType === 'arrival' ? 'hora de llegada' : 'hora de salida' }}
        </v-card-title>

        <v-card-text class="modal-body-vuetify pa-4">
          <v-time-picker v-model="tempTime"></v-time-picker>
        </v-card-text>

        <v-card-actions class="pa-4">
          <v-spacer></v-spacer>
          <v-btn text @click="closeTimePickerModal">Cancelar</v-btn>
          <v-btn color="#1b515e" @click="confirmTimeSelection">Aceptar</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import { mapActions, mapState, mapGetters } from 'vuex'
import { getErrorDetails } from '@/utils/utils'

export default {
  name: 'AddToItineraryModal',
  props: {
    place: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      addToItineraryModalOpen: false,
      selectDayModalOpen: false,
      confirmAddModalOpen: false,
      showTimePickerModal: false,
      loadingDays: false,
      submittingPlace: false,
      selectedItinerary: null,
      selectedDay: null,
      selectedDayIndex: null,
      itineraryDays: [],
      tempTime: null,
      timePickerType: 'arrival',
      placeFormData: {
        arrivalTime: null,
        leavingTime: null,
      },
    }
  },
  computed: {
    ...mapState('places', {
      selectedPlaceDetails: 'selectedPlaceDetails',
    }),
    ...mapGetters('trips', ['userItineraries']),
  },
  methods: {
    ...mapActions('trips', {
      fetchUserItineraries: 'fetchUserItineraries',
      fetchItineraryDays: 'fetchItineraryDays',
      addPlaceToDay: 'addPlaceToDay',
    }),
    openAddToItineraryModal() {
      this.addToItineraryModalOpen = true
    },
    formatDateRange(startDate, endDate) {
      const start = new Date(startDate).toLocaleDateString('es-ES', {
        year: 'numeric',
        month: 'short',
        day: 'numeric',
      })
      const end = new Date(endDate).toLocaleDateString('es-ES', {
        year: 'numeric',
        month: 'short',
        day: 'numeric',
      })
      return `${start} - ${end}`
    },
    formatDate(date) {
      return new Date(date).toLocaleDateString('es-ES', {
        weekday: 'long',
        year: 'numeric',
        month: 'long',
        day: 'numeric',
      })
    },
    getSustainabilityStyle(index) {
      const percentage = index * 100
      return {
        backgroundColor: percentage >= 70 ? '#28a745' : percentage >= 40 ? '#ffc107' : '#dc3545',
        color: 'white',
        borderRadius: '50%',
        width: '60px',
        height: '60px',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
      }
    },
    async selectItinerary(itinerary) {
      this.selectedItinerary = itinerary
      this.addToItineraryModalOpen = false
      this.loadingDays = true
      this.selectDayModalOpen = true

      try {
        const days = await this.fetchItineraryDays(itinerary.id)
        this.itineraryDays = days
      } catch (error) {
        this.$alert.error('Error al cargar los días del itinerario')
        console.error(error)
      } finally {
        this.loadingDays = false
      }
    },
    selectDay(day, index) {
      this.selectedDay = day
      this.selectedDayIndex = index
      this.selectDayModalOpen = false
      this.confirmAddModalOpen = true
      this.placeFormData = {
        arrivalTime: null,
        leavingTime: null,
      }
    },
    openTimePicker(type) {
      this.timePickerType = type
      this.tempTime =
        type === 'arrival' ? this.placeFormData.arrivalTime : this.placeFormData.leavingTime
      this.showTimePickerModal = true
    },
    closeTimePickerModal() {
      this.showTimePickerModal = false
      this.tempTime = null
    },
    confirmTimeSelection() {
      if (this.timePickerType === 'arrival') {
        this.placeFormData.arrivalTime = this.tempTime
      } else {
        this.placeFormData.leavingTime = this.tempTime
      }
      this.closeTimePickerModal()
    },
    extractPostalCode(address) {
      const match = address.match(/\b(\d{5})\b/)
      return match ? match[1] : null
    },
    async confirmAddPlace() {
      if (!this.placeFormData.arrivalTime) {
        this.$alert.error('Debe seleccionar una hora de llegada')
        return
      }

      this.submittingPlace = true
      const postalCode = this.extractPostalCode(this.selectedPlaceDetails.formattedAddress)
      try {
        const placeData = {
          newPlaceId: this.selectedPlaceDetails.id,
          postalCode: postalCode,
          arrivalTime: this.placeFormData.arrivalTime,
          leavingTime: this.placeFormData.leavingTime || null,
        }

        await this.addPlaceToDay({
          itineraryId: this.selectedItinerary.id,
          dayId: this.selectedDay.itineraryDayId,
          placeData,
        })

        this.$alert.success('Lugar agregado exitosamente al itinerario')
        this.confirmAddModalOpen = false
        this.addToItineraryModalOpen = false

        await this.fetchUserItineraries()
        this.$emit('place-added')
      } catch (error) {
        this.$alert.error(getErrorDetails(error))
      } finally {
        this.submittingPlace = false
      }
    },
    redirectToNewTrips() {
      this.addToItineraryModalOpen = false
      this.$router.push({ name: 'new-trips' })
    },
  },
}
</script>

<style scoped>
.trip-name-modal {
  color: #1b515e;
  font-size: 1rem;
  margin-bottom: 1.5rem;
  padding-bottom: 1rem;
  border-bottom: 2px solid #e9ecef;
}
.days-grid-modal {
  display: flex;
  flex-direction: column;
  gap: 10px;
}
.day-item-modal {
  background: white;
  border: 2px solid #e9ecef;
  border-radius: 10px;
  padding: 14px 16px;
  display: flex;
  align-items: center;
  gap: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
}
.day-item-modal:hover {
  border-color: #1b515e;
  background: #f8fafb;
  box-shadow: 0 2px 8px rgba(27, 81, 94, 0.1);
}

.day-badge {
  background: linear-gradient(135deg, #1b515e 0%, #2a7a8e 100%);
  color: white;
  width: 40px;
  height: 40px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  font-size: 1rem;
  flex-shrink: 0;
}

.day-info {
  flex: 1;
}

.day-label {
  font-weight: 600;
  color: #1b515e;
  font-size: 0.95rem;
}

.day-date-text {
  color: #6c757d;
  font-size: 0.85rem;
  margin-top: 2px;
  text-transform: capitalize;
}

.day-item-modal i {
  color: #1b515e;
  opacity: 0.5;
  transition: all 0.3s ease;
}

.day-item-modal:hover i {
  opacity: 1;
  transform: translateX(4px);
}
.map-btn {
  background: #1b515e;
  color: white;
  font-weight: 500;
  transition: all 0.3s ease;
  box-shadow: 0 2px 4px rgba(27, 81, 94, 0.2);
}

.map-btn:hover {
  background: #143e48;
  transform: translateY(-1px);
  box-shadow: 0 4px 8px rgba(27, 81, 94, 0.3);
  color: white;
}

.itinerary-card {
  transition: all 0.3s ease;
  border: 2px solid #e9ecef;
}

.itinerary-card:hover {
  border-color: #1b515e;
  box-shadow: 0 4px 12px rgba(27, 81, 94, 0.15);
}

.itinerary-carousel {
  border-radius: 8px;
}

.sustainability-badge {
  display: flex;
  align-items: center;
  justify-content: center;
}

.select-btn {
  border-color: #1b515e !important;
  color: #1b515e !important;
}

.select-btn:hover {
  background-color: #1b515e !important;
  color: white !important;
}

.info-section {
  padding: 12px;
  background-color: #f8f9fa;
  border-radius: 8px;
  border-left: 4px solid #1b515e;
}

.time-section {
  padding: 12px;
  background-color: #f8f9fa;
  border-radius: 8px;
}

.day-btn {
  border-color: #1b515e !important;
  color: #1b515e !important;
  text-transform: none !important;
  padding: 16px !important;
}

.day-btn:hover {
  background-color: #1b515e !important;
  color: white !important;
}

.modal-title {
  color: #1b515e;
  font-weight: 700;
}

.cursor-pointer {
  cursor: pointer;
}

.days-list {
  display: flex;
  flex-direction: column;
}
</style>
