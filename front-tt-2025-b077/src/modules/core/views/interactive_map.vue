<template>
  <div class="map-container">
    <RouteInputs
      :origin="originInput"
      :destination="destinationInput"
      :disabled="!isMapReady"
      @update:origin="originInput = $event"
      @update:destination="destinationInput = $event"
      @search="calculateRoute"
      @swap="handleSwap"
    />

    <!-- Mapa -->
    <div id="map" class="fullscreen-map"></div>

    <!-- Card de información -->
    <transition name="slide-up">
      <div v-if="routeInfo" class="floating-info">
        <div class="card info-card border-0">
          <div class="card-body p-4">
            <!-- Header siempre visible con botón de colapso -->
            <div
              class="info-header-with-toggle d-flex align-items-center justify-content-between mb-0"
            >
              <div class="info-header-main flex-fill">
                <div class="d-flex align-items-center justify-content-between">
                  <div class="flex-fill">
                    <h5 class="card-title mb-1 fw-bold">{{ routeInfo.name }}</h5>
                    <div class="duration-inline d-flex align-items-center gap-2 mt-1">
                      <span class="fw-semibold text-primary">{{ routeInfo.duration }}</span>
                      <span class="text-muted small">• {{ routeInfo.distance }}</span>
                    </div>
                  </div>
                  <button
                    class="btn toggle-btn p-2 ms-3 d-flex align-items-center justify-content-center"
                    @click="toggleCardExpanded"
                    :title="isCardExpanded ? 'Contraer información' : 'Expandir información'"
                  >
                    <span class="toggle-icon" :class="{ expanded: isCardExpanded }">
                      {{ isCardExpanded ? '▼' : '▲' }}
                    </span>
                  </button>
                </div>
              </div>
            </div>

            <!-- Dropdown -->
            <transition name="expand">
              <div v-show="isCardExpanded" class="expandable-content pt-3">
                <div class="info-details mb-3">
                  <p class="card-text text-muted small mb-2">{{ routeInfo.address }}</p>

                  <div class="row g-2 mb-3">
                    <div class="col-6">
                      <div class="detail-item text-center">
                        <small class="text-muted">Llegas en:</small>
                        <div class="fw-semibold">{{ routeInfo.duration }}</div>
                      </div>
                    </div>
                    <div class="col-6">
                      <div class="detail-item text-center">
                        <small class="text-muted">Distancia:</small>
                        <div class="fw-semibold">{{ routeInfo.distance }}</div>
                      </div>
                    </div>
                  </div>

                  <div>
                    <span class="badge bg-light text-dark rating"
                      ><i class="fa-solid fa-star m-1"></i> {{ routeInfo.rating || 'N/A' }}</span
                    >
                  </div>
                </div>

                <!-- Dropdown manual sin Bootstrap JS -->
                <div class="info-actions d-flex gap-2 flex-column flex-md-row">
                  <div class="dropdown-manual w-100 w-md-auto">
                    <button
                      class="btn btn-light border dropdown-toggle w-100 text-start py-2 px-3 small d-flex align-items-center justify-content-between"
                      type="button"
                      @click="isDropdownOpen = !isDropdownOpen"
                    >
                      <div class="d-flex align-items-center gap-2">
                        <i v-if="travelMode === 'DRIVING'" class="fa-solid fa-car text-primary"></i>
                        <i
                          v-else-if="travelMode === 'WALKING'"
                          class="fa-solid fa-person-walking text-success"
                        ></i>
                        <i
                          v-else-if="travelMode === 'BICYCLING'"
                          class="fa-solid fa-bicycle text-warning"
                        ></i>
                        <i
                          v-else-if="travelMode === 'TRANSIT'"
                          class="fa-solid fa-bus text-info"
                        ></i>
                        <span>{{ travelModeText }}</span>
                      </div>
                    </button>

                    <ul v-show="isDropdownOpen" class="dropdown-menu-custom w-100 show">
                      <li>
                        <button
                          class="dropdown-item d-flex align-items-center gap-2"
                          @click="setTravelMode('DRIVING')"
                        >
                          <i class="fa-solid fa-car text-primary"></i> Automóvil
                        </button>
                      </li>
                      <li>
                        <button
                          class="dropdown-item d-flex align-items-center gap-2"
                          @click="setTravelMode('WALKING')"
                        >
                          <i class="fa-solid fa-person-walking text-success"></i> A pie
                        </button>
                      </li>
                      <li>
                        <button
                          class="dropdown-item d-flex align-items-center gap-2"
                          @click="setTravelMode('BICYCLING')"
                        >
                          <i class="fa-solid fa-bicycle text-warning"></i> Bicicleta
                        </button>
                      </li>
                      <li>
                        <button
                          class="dropdown-item d-flex align-items-center gap-2"
                          @click="setTravelMode('TRANSIT')"
                        >
                          <i class="fa-solid fa-bus text-info"></i> Transporte
                        </button>
                      </li>
                    </ul>
                  </div>
                  <button class="btn add-btn text-nowrap py-2 px-3 small w-100 w-md-auto">
                    Agregar a itinerario
                  </button>
                </div>
              </div>
            </transition>
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
import RouteInputs from '@/components/mapInputs.vue'

export default {
  name: 'GoogleMapDirectionsWithInfo',
  components: {
    RouteInputs,
  },
  data() {
    return {
      originInput: '',
      destinationInput: '',
      userLocation: null,
      travelMode: 'DRIVING',
      map: null,
      directionsService: null,
      directionsRenderer: null,
      routeInfo: null,
      isCardExpanded: true,
      isMapReady: false,
      isDropdownOpen: false,
    }
  },
  computed: {
    travelModeText() {
      switch (this.travelMode) {
        case 'DRIVING':
          return 'Automóvil'
        case 'WALKING':
          return 'A pie'
        case 'BICYCLING':
          return 'Bicicleta'
        case 'TRANSIT':
          return 'Transporte'
        default:
          return ''
      }
    },
  },
  mounted() {
    this.loadBootstrap()

    if (!window.google) {
      this.loadGoogleMapsScript().then(this.getCurrentLocation)
    } else {
      this.getCurrentLocation()
    }
  },
  methods: {
    loadBootstrap() {
      if (!document.querySelector('link[href*="bootstrap"]')) {
        const bootstrapCSS = document.createElement('link')
        bootstrapCSS.rel = 'stylesheet'
        bootstrapCSS.href =
          'https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.2/css/bootstrap.min.css'
        document.head.appendChild(bootstrapCSS)
      }
    },

    setTravelMode(mode) {
      this.travelMode = mode
      this.isDropdownOpen = false
      if (this.routeInfo) {
        this.calculateRoute()
      }
    },

    handleSwap() {
      console.log('Inputs intercambiados')
    },

    toggleCardExpanded() {
      this.isCardExpanded = !this.isCardExpanded
    },

    loadGoogleMapsScript() {
      return new Promise((resolve, reject) => {
        if (window.google && window.google.maps && window.google.maps.places) {
          resolve()
          return
        }

        const existingScript = document.querySelector("script[src*='maps.googleapis.com']")
        if (existingScript) existingScript.remove()
        //Llamar a la API
        const script = document.createElement('script')
        ;(script.src = ''), (script.async = true)
        script.defer = true
        script.onload = resolve
        script.onerror = reject
        document.head.appendChild(script)
      })
    },

    getCurrentLocation() {
      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(
          (position) => {
            this.userLocation = {
              lat: position.coords.latitude,
              lng: position.coords.longitude,
            }
            this.initMap(this.userLocation)
          },
          () => {
            this.userLocation = { lat: -34.397, lng: 150.644 }
            this.initMap(this.userLocation)
          },
        )
      } else {
        this.userLocation = { lat: -34.397, lng: 150.644 }
        this.initMap(this.userLocation)
      }
    },

    initMap(center) {
      this.map = new google.maps.Map(document.getElementById('map'), {
        center,
        zoom: 14,
      })

      this.directionsService = new google.maps.DirectionsService()
      this.directionsRenderer = new google.maps.DirectionsRenderer()
      this.directionsRenderer.setMap(this.map)

      new google.maps.Marker({
        position: center,
        map: this.map,
        title: 'Tu ubicación actual',
      })

      this.isMapReady = true
    },

    calculateRoute() {
      if (!this.destinationInput) {
        alert('Por favor, ingresa un destino.')
        return
      }

      if (!this.directionsService || !this.directionsRenderer) {
        alert('El mapa aún se está cargando. Por favor, espera un momento.')
        return
      }

      const origin = this.originInput.trim() !== '' ? this.originInput : this.userLocation

      const request = {
        origin,
        destination: this.destinationInput,
        travelMode: this.travelMode,
      }

      this.directionsService.route(request, (result, status) => {
        if (status === 'OK') {
          this.directionsRenderer.setDirections(result)

          const leg = result.routes[0].legs[0]
          const destinationLatLng = leg.end_location

          const placesService = new google.maps.places.PlacesService(this.map)
          const placesRequest = {
            location: destinationLatLng,
            radius: 50,
          }

          placesService.nearbySearch(placesRequest, (places, status) => {
            const place = places?.[0] || {}

            this.routeInfo = {
              name: leg.end_address.split(',')[0],
              address: leg.end_address,
              rating: place.rating,
              distance: leg.distance.text,
              duration: leg.duration.text,
            }

            this.isCardExpanded = true
          })
        } else {
          console.error('Error al calcular la ruta:', status)
          alert('No se pudo calcular la ruta.')
        }
      })
    },
  },
}
</script>

<style scoped>
.map-container {
  position: relative;
  width: 100vw;
  height: 100vh;
  overflow: hidden;
}

.fullscreen-map {
  width: 100%;
  height: 100%;
  position: absolute;
  top: 0;
  left: 0;
  z-index: 1;
}

.floating-info {
  position: absolute;
  bottom: 20px;
  left: 50%;
  transform: translateX(-50%);
  z-index: 10;
  width: 90%;
  max-width: 450px;
  animation: fadeInUp 0.6s ease-out;
}

.info-card {
  background: rgba(255, 255, 255, 0.95) !important;
  backdrop-filter: blur(15px) !important;
  border-radius: 20px !important;
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.15) !important;
  border: 1px solid rgba(255, 255, 255, 0.3) !important;
  overflow: hidden;
  position: relative;
}

.info-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  border-radius: inherit;
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.4) 0%, rgba(255, 255, 255, 0.1) 100%);
  z-index: -1;
}

/* Botón de toggle*/
.toggle-btn {
  background: rgba(27, 81, 94, 0.1) !important;
  border: 1px solid rgba(27, 81, 94, 0.2) !important;
  color: #1b515e !important;
  border-radius: 50% !important;
  width: 40px;
  height: 40px;
  transition: all 0.3s ease !important;
  flex-shrink: 0;
}

.toggle-btn:hover {
  background: rgba(27, 81, 94, 0.2) !important;
  transform: scale(1.1) !important;
  border-color: #1b515e !important;
}

.toggle-icon {
  font-size: 0.9rem;
  font-weight: bold;
  transition: transform 0.3s ease;
}

.toggle-icon.expanded {
  transform: rotate(180deg);
}

.expand-enter-active,
.expand-leave-active {
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  overflow: hidden;
}

.expand-enter-from,
.expand-leave-to {
  max-height: 0;
  opacity: 0;
  transform: translateY(-10px);
}

.expand-enter-to,
.expand-leave-from {
  max-height: 500px;
  opacity: 1;
  transform: translateY(0);
}

.dropdown-manual {
  position: relative;
}

.dropdown-menu-custom {
  position: absolute;
  bottom: 100%;
  left: 0;
  z-index: 1000;
  display: none;
  min-width: 100%;
  padding: 0.5rem 0;
  margin: 0.125rem 0 0;
  font-size: 1rem;
  color: #212529;
  text-align: left;
  list-style: none;
  background-color: #fff;
  background-clip: padding-box;
  border: 1px solid rgba(0, 0, 0, 0.15);
  border-radius: 0.375rem;
  box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.175);
}

.dropdown-menu-custom.show {
  display: block;
}

.dropdown-menu-custom .dropdown-item {
  display: block;
  width: 100%;
  padding: 0.5rem 1rem;
  clear: both;
  font-weight: 400;
  color: #212529;
  text-align: inherit;
  text-decoration: none;
  white-space: nowrap;
  background-color: transparent;
  border: 0;
  cursor: pointer;
  transition: background-color 0.15s ease-in-out;
}

.dropdown-menu-custom .dropdown-item:hover,
.dropdown-menu-custom .dropdown-item:focus {
  color: #1e2125;
  background-color: #e9ecef;
}

.detail-item {
  padding: 0.5rem;
  background: rgba(27, 81, 94, 0.05);
  border-radius: 8px;
  border: 1px solid rgba(27, 81, 94, 0.1);
}

.rating {
  font-size: 0.9rem !important;
  padding: 0.5rem 1rem !important;
  border-radius: 20px !important;
  background: rgba(27, 81, 94, 0.1) !important;
  color: #1b515e !important;
  border: 1px solid rgba(27, 81, 94, 0.2) !important;
}

.transport-select {
  background: #abcdab !important;
  border: none !important;
  border-radius: 25px !important;
  color: #1b515e !important;
  font-weight: 500 !important;
}

.transport-select:focus {
  background: #9bc19b !important;
  box-shadow: 0 0 0 0.2rem rgba(171, 205, 171, 0.5) !important;
  border: none !important;
}

.add-btn {
  background: #1b515e !important;
  border: none !important;
  border-radius: 25px !important;
  color: white !important;
  font-weight: 500 !important;
  transition: all 0.3s ease !important;
}

.add-btn:hover {
  background: #163f49 !important;
  transform: translateY(-2px) !important;
  box-shadow: 0 4px 12px rgba(27, 81, 94, 0.3) !important;
}

.slide-up-enter-active {
  transition: all 0.5s cubic-bezier(0.25, 0.8, 0.25, 1);
}
.slide-up-leave-active {
  transition: all 0.4s cubic-bezier(0.25, 0.8, 0.25, 1);
}
.slide-up-enter-from,
.slide-up-leave-to {
  transform: translateX(-50%) translateY(100%);
  opacity: 0;
}
.slide-up-enter-to,
.slide-up-leave-from {
  transform: translateX(-50%) translateY(0);
  opacity: 1;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@media (max-width: 768px) {
  .floating-info {
    width: 95%;
    bottom: 15px;
  }

  .toggle-btn {
    width: 36px;
    height: 36px;
  }

  .toggle-icon {
    font-size: 0.8rem;
  }
}

@media (max-width: 480px) {
  .floating-info {
    width: 98%;
    bottom: 10px;
  }

  .card-title {
    font-size: 1.1rem !important;
  }

  .toggle-btn {
    width: 32px;
    height: 32px;
  }
}

@media (min-width: 1200px) {
  .floating-info {
    max-width: 500px;
  }
}
</style>
