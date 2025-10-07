<template>
  <div class="min-vh-100 bg-light">
    <TopNavbar @ver-itinerarios="handleViewItineraries" />

    <div class="d-flex align-items-center py-4 pb-navbar" style="min-height: calc(100vh - 70px)">
      <transition name="fade" mode="out-in">
        <div v-if="showForm" class="container">
          <div class="row justify-content-center">
            <div class="col-12 col-lg-10 col-xl-9">
              <div class="row g-4 g-lg-5">
                <div class="col-12 col-md-6 order-1 order-md-1">
                  <div v-if="!isFormValid" class="mt-3">
                    <div class="alert alert-info border-0 rounded-3 py-2 px-3">
                      <small>
                        <i class="fas fa-info-circle me-2"></i>
                        <span v-if="missingFields.length > 1">
                          Completa: {{ missingFields.join(' , ') }}
                        </span>
                        <span v-else> Completa: {{ missingFields[0] }} </span>
                      </small>
                    </div>
                  </div>
                  <form @submit.prevent="submitForm" class="py-3">
                    <div class="mb-4">
                      <div class="input-group input-group-lg">
                        <span class="input-group-text bg-white border-primary">
                          <i class="fas fa-pen text-primary"></i>
                        </span>
                        <div class="form-floating">
                          <input
                            type="text"
                            id="tripTitle"
                            v-model="tripTitle"
                            @input="updateTripTitle"
                            class="form-control border-primary border-start-0"
                            placeholder="Título de tu viaje"
                            required
                          />
                          <label for="tripTitle" class="text-muted">Título de tu viaje</label>
                        </div>
                      </div>
                    </div>

                    <div class="mb-4">
                      <div class="input-group input-group-lg">
                        <span class="input-group-text bg-white border-primary">
                          <i class="fas fa-map-marker-alt text-primary"></i>
                        </span>
                        <div class="form-floating flex-fill">
                          <div
                            class="form-control border-primary border-start-0 d-flex align-items-center justify-content-between h-100"
                            style="cursor: pointer"
                            @click="toggleStateMenu"
                            :class="{ 'border-success': stateMenuOpen }"
                          >
                            <span v-if="!selectedState" class="text-muted"
                              >Selecciona un estado</span
                            >
                            <span v-else class="text-dark fw-medium">
                              <i class="fas fa-map-marker-alt me-2 text-primary"></i>
                              {{ getStateName(selectedState) }}
                            </span>
                            <i
                              class="fas fa-chevron-down text-primary transition-all"
                              :class="{ 'rotate-180': stateMenuOpen }"
                            ></i>
                          </div>
                          <label class="text-muted">Destino</label>

                          <div
                            v-if="stateMenuOpen"
                            class="dropdown-menu show position-absolute w-100 mt-1 shadow-lg border-0"
                            style="z-index: 1050"
                          >
                            <div
                              v-for="stateOption in states"
                              :key="stateOption.id"
                              class="dropdown-item d-flex align-items-center py-3 border-bottom border-light"
                              style="cursor: pointer"
                              @click="selectState(stateOption.id)"
                            >
                              <i class="fas fa-map-marker-alt me-3 text-primary fs-5"></i>
                              <span class="fw-medium">{{ stateOption.state }}</span>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>

                    <div class="mb-4">
                      <div class="d-flex align-items-center gap-3">
                        <div class="status-indicator">
                          <i
                            :class="dateRangeStatusIcon"
                            :style="{ color: dateRangeStatusColor }"
                          ></i>
                        </div>
                        <div class="flex-grow-1">
                          <small class="text-muted d-block">Fechas del viaje</small>
                          <span v-if="!hasValidDateRange" class="text-muted">
                            Selecciona las fechas en el calendario
                          </span>
                          <span v-else class="fw-medium text-success">
                            {{ formattedDateRange }}
                          </span>
                        </div>
                      </div>
                    </div>
                  </form>

                  <div class="d-none d-lg-block mt-4">
                    <div class="d-flex justify-content-center">
                      <button
                        type="button"
                        class="btn btn-primary btn-lg"
                        @click="viewAccommodations"
                        :disabled="!isFormValid"
                      >
                        <i class="fas fa-bed me-2"></i>
                        Ver lugares de hospedaje
                      </button>
                    </div>
                  </div>
                </div>

                <div class="col-12 col-md-6 order-2 order-md-2">
                  <Calendar
                    v-model="selectedDateRange"
                    @rangeApplied="handleDateRangeApplied"
                    @rangeCleared="handleDateRangeCleared"
                  />

                  <div class="d-grid mt-4 d-lg-none">
                    <button
                      type="button"
                      class="btn btn-lg px-4"
                      :class="buttonClass"
                      @click="viewAccommodations"
                      :disabled="!isFormValid"
                    >
                      <i class="fas fa-bed me-2"></i>
                      Ver lugares de hospedaje
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </transition>
      <BottomNavbar />
    </div>
  </div>
</template>

<script>
import { mapActions, mapMutations, mapState } from 'vuex'
import Calendar from '@/components/calendar.vue'
import BottomNavbar from '@/components/bottomnavbar.vue'
import TopNavbar from '@/components/topnavbaritinerary.vue'

export default {
  components: { Calendar, BottomNavbar, TopNavbar },
  name: 'NuevoItinerario',
  data() {
    return {
      showForm: false,
      tripTitle: '',
      selectedState: '',
      stateMenuOpen: false,
      selectedDateRange: { start: null, end: null },
    }
  },
  computed: {
    ...mapState('trips', ['states', 'newItinerary']),

    hasValidDateRange() {
      return this.selectedDateRange && this.selectedDateRange.start && this.selectedDateRange.end
    },

    isFormValid() {
      return this.tripTitle.trim() && this.selectedState && this.hasValidDateRange
    },

    missingFields() {
      const missing = []
      if (!this.tripTitle.trim()) missing.push('título del viaje')
      if (!this.selectedState) missing.push('destino')
      if (!this.hasValidDateRange) missing.push('fechas del viaje')
      return missing
    },

    buttonClass() {
      return this.isFormValid ? 'btn-primary' : 'btn-outline-secondary'
    },

    dateRangeStatusIcon() {
      return this.hasValidDateRange ? 'fas fa-check-circle' : 'far fa-calendar-alt'
    },

    dateRangeStatusColor() {
      return this.hasValidDateRange ? '#28a745' : '#6c757d'
    },

    formattedDateRange() {
      if (!this.hasValidDateRange) return ''

      const formatDate = (date) => {
        const months = [
          'enero',
          'febrero',
          'marzo',
          'abril',
          'mayo',
          'junio',
          'julio',
          'agosto',
          'septiembre',
          'octubre',
          'noviembre',
          'diciembre',
        ]
        return `${date.getDate()} ${months[date.getMonth()]}`
      }

      const start = formatDate(this.selectedDateRange.start)
      const end = formatDate(this.selectedDateRange.end)
      const diffTime = Math.abs(this.selectedDateRange.end - this.selectedDateRange.start)
      const daysDifference = Math.ceil(diffTime / (1000 * 60 * 60 * 24)) + 1

      return `${start} - ${end} (${daysDifference} días)`
    },
  },
  mounted() {
    this.fetchStates()

    if (this.newItinerary.tripTitle) {
      this.tripTitle = this.newItinerary.tripTitle
    }
    if (this.newItinerary.selectedState) {
      this.selectedState = this.newItinerary.selectedState
    }
    if (this.newItinerary.startDate && this.newItinerary.endDate) {
      this.selectedDateRange = {
        start: new Date(this.newItinerary.startDate),
        end: new Date(this.newItinerary.endDate),
      }
    }

    setTimeout(() => {
      this.showForm = true
    }, 100)
    document.addEventListener('click', this.handleClickOutside)
  },
  beforeUnmount() {
    document.removeEventListener('click', this.handleClickOutside)
  },

  methods: {
    ...mapActions('trips', {
      fetchStates: 'fetchStates',
    }),
    ...mapMutations('trips', {
      setSelectedState: 'setSelectedState',
      setStartDate: 'setStartDate',
      setEndDate: 'setEndDate',
      setTripTitle: 'setTripTitle',
    }),
    handleDateRangeApplied(range) {
      const diffTime = Math.abs(range.end - range.start)
      const daysDifference = Math.ceil(diffTime / (1000 * 60 * 60 * 24)) + 1

      if (daysDifference > 7) {
        this.$alert.error('No puedes seleccionar más de 7 días para tu viaje')
        this.selectedDateRange = { start: null, end: null }
        this.setStartDate('')
        this.setEndDate('')
        return
      }

      this.selectedDateRange = { ...range }
      this.setStartDate(range.start)
      this.setEndDate(range.end)
    },

    handleDateRangeCleared() {
      this.selectedDateRange = { start: null, end: null }
      this.setStartDate('')
      this.setEndDate('')
    },

    handleViewItineraries() {
      console.log('Ver itinerarios desde navbar')
    },

    viewAccommodations() {
      if (!this.isFormValid) {
        const message =
          this.missingFields.length === 1
            ? `Por favor selecciona ${this.missingFields[0]}`
            : `Por favor completa: ${this.missingFields.join(', ')}`

        this.$alert.error(message)
        return
      }
      this.$router.push({ name: 'hotels' })
    },

    getStateName(stateId) {
      const state = this.states.find((s) => s.id === stateId)
      return state ? state.state : ''
    },

    toggleStateMenu() {
      this.stateMenuOpen = !this.stateMenuOpen
    },

    selectState(stateId) {
      this.selectedState = stateId
      this.setSelectedState(stateId)
      this.stateMenuOpen = false
    },

    updateTripTitle() {
      this.setTripTitle(this.tripTitle)
    },

    handleClickOutside(event) {
      const inputGroup = event.target.closest('.input-group')
      const dropdownMenu = event.target.closest('.dropdown-menu')

      if (!inputGroup && !dropdownMenu && this.stateMenuOpen) {
        this.stateMenuOpen = false
      }
    },
  },
}
</script>

<style scoped>
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.6s ease;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

.text-primary {
  color: #1b515e !important;
}

.border-primary {
  border-color: #1b515e !important;
}

.btn-primary {
  background-color: #1b515e;
  border-color: #1b515e;
  color: white;
}

.btn-primary:hover:not(:disabled) {
  background-color: #48cd13;
  border-color: #48cd13;
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(72, 205, 19, 0.3);
}

.btn-primary:disabled {
  background-color: #6dad64;
  border-color: #6dad64;
}

.btn-outline-secondary {
  color: #1b515e;
  border-color: #1b515e;
  background-color: transparent;
}

.btn-outline-secondary:hover:not(:disabled) {
  color: #1b515e;
  background-color: #e2e6ea;
  border-color: #dae0e5;
}

.btn-outline-secondary:disabled {
  color: #abcdab;
  background-color: transparent;
  border-color: #abcdab;
  opacity: 0.65;
  cursor: not-allowed;
}

.status-indicator {
  min-width: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.status-indicator i {
  font-size: 1.1rem;
}

@media (max-width: 576px) {
  .btn-lg {
    padding: 0.5rem 1.5rem;
  }
}

.border-success {
  border-color: #48cd13 !important;
}

.dropdown-item:hover {
  background-color: rgba(72, 205, 19, 0.1);
  color: #48cd13;
}

.rotate-180 {
  transform: rotate(180deg);
}

.transition-all {
  transition: all 0.3s ease;
}

.form-control:focus,
.form-select:focus {
  border-color: #48cd13;
  box-shadow: 0 0 0 0.25rem rgba(72, 205, 19, 0.25);
}

.dropdown-menu {
  border-radius: 0.5rem;
  max-height: 250px;
  overflow-y: auto;
}

.dropdown-menu.show {
  animation: dropdownSlide 0.3s cubic-bezier(0.68, -0.55, 0.265, 1.55);
}

@keyframes dropdownSlide {
  from {
    opacity: 0;
    transform: scaleY(0) rotateX(-60deg);
    transform-origin: top center;
  }
  to {
    opacity: 1;
    transform: scaleY(1) rotateX(0deg);
    transform-origin: top center;
  }
}

.pb-navbar {
  padding-bottom: 100px !important;
}

@media (min-width: 800px) {
  .pb-navbar {
    padding-bottom: 130px !important;
  }
}

.alert-info {
  background-color: rgba(27, 81, 94, 0.1);
  color: #1b515e;
  border: 1px solid rgba(27, 81, 94, 0.2);
}
</style>
