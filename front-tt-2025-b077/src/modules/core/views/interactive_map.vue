<template>
  <div class="map-container">
    <RouteInputs
      :origin="originInput"
      :destination="destinationInput"
      :disabled="!isMapReady"
      :suppressDestinationSearch="suppressDestinationSearch"
      @update:origin="originInput = $event"
      @update:destination="destinationInput = $event"
      @search="calculateRoute"
      @swap="handleSwap"
      @clear-route="clearRoute"
    />

    <div id="map" class="fullscreen-map"></div>

    <transition name="slide-up">
      <div v-if="routeInfo" class="floating-info">
        <div class="card info-card border-0">
          <div class="card-body p-4">
            <div
              class="info-header-with-toggle d-flex align-items-center justify-content-between mb-0"
            >
              <div class="info-header-main flex-fill">
                <div class="d-flex align-items-center justify-content-between">
                  <div class="flex-fill">
                    <h5 class="card-title mb-1 fw-bold">{{ routeInfo.name }}</h5>
                    <div class="duration-inline d-flex align-items-center gap-2 mt-1" v-if="isRouteActive">
                      <span class="fw-semibold text-primary">{{ routeInfo.duration }}</span>
                      <span class="text-muted small">• {{ routeInfo.distance }}</span>
                    </div>
                  </div>
                  <div class="d-flex gap-2 ms-3">
                    <button
                      v-if="isRouteActive"
                      class="btn toggle-btn p-2 d-flex align-items-center justify-content-center"
                      @click="toggleCardExpanded"
                      title="Minimizar/Expandir"
                    >
                      <span class="toggle-icon" :class="{ expanded: isCardExpanded }">
                        {{ isCardExpanded ? '▼' : '▲' }}
                      </span>
                    </button>
                    <button
                      v-if="isRouteActive"
                      class="btn toggle-btn p-2 ms-2 d-flex align-items-center justify-content-center"
                      @click="clearRoute"
                      title="Limpiar ruta"
                    >
                      <span class="toggle-icon">✕</span>
                    </button>
                    <button
                      v-else
                      class="btn toggle-btn p-2 d-flex align-items-center justify-content-center"
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
            </div>

            <transition name="expand">
              <div v-show="isCardExpanded" class="expandable-content pt-3">
                <div class="info-details mb-3">
                  <p class="card-text text-muted small mb-2">{{ routeInfo.address }}</p>

                  <div class="row g-2 mb-3" v-if="isRouteActive">
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
<div
  v-if="routeInfo.ecoRecommendation"
  class="alert alert-success py-2 px-3 small d-flex align-items-center gap-2 mt-2"
>
  <i
    class="fa-solid"
    :class="[routeInfo.ecoRecommendation.icon, routeInfo.ecoRecommendation.color]"
  ></i>
  <span>
    Recomendación ecológica:
    <strong>{{ routeInfo.ecoRecommendation.text }}</strong>
  </span>
</div>

                </div>

                <div class="info-actions d-flex gap-2 flex-column">
                  <div v-if="isRouteActive" class="dropdown-manual w-100">
                    <div class="d-flex align-items-center gap-2 w-100">
                      <button
                        class="btn btn-light border dropdown-toggle flex-fill text-start py-2 px-3 small d-flex align-items-center justify-content-between"
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
                      <span v-if="routeInfo.rating && routeInfo.rating !== 'N/A'" class="badge bg-light text-dark rating flex-shrink-0"
                        ><i class="fa-solid fa-star m-1"></i> {{ routeInfo.rating }}</span
                      >
                    </div>

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

                  <div class="d-flex gap-2 flex-column flex-sm-row">
                    <button
                      v-if="!isRouteActive"
                      class="btn btn-route text-nowrap py-2 px-3 small flex-fill"
                      @click="setAsDestination"
                    >
                      Ver ruta
                    </button>
                    <button
                      class="btn btn-details text-nowrap py-2 px-3 small flex-fill"
                      @click="goToPlaceDetails"
                    >
                      Ver detalles
                    </button>
                  </div>
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
import { mapMutations } from 'vuex'
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
      userAddress: '',
      travelMode: 'DRIVING',
      map: null,
      directionsService: null,
      directionsRenderer: null,
      routeInfo: null,
      isCardExpanded: true,
      isMapReady: false,
      isDropdownOpen: false,
      selectedPlaceId: null,
      selectedPlaceCoordinates: null,
      placeMarkers: [],
      userLocationMarker: null,
      isRouteActive: false,
      geocoder: null,
      suppressDestinationSearch: false,
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
  watch: {
    '$route.query': {
      handler(newQuery) {
        this.handleRouteQuery(newQuery)
      },
      deep: true,
    },
  },
  mounted() {
    this.loadBootstrap()

    if (!window.google) {
      this.loadGoogleMapsScript().then(this.getCurrentLocation)
    } else {
      this.getCurrentLocation()
    }

    this.handleRouteQuery(this.$route.query)
  },
  // REEMPLAZA TODA la sección methods de tu componente con esta versión corregida:

methods: {
  ...mapMutations('places', {
    setSelectedPlaceId: 'setSelectedPlaceId',
  }),

    loadBootstrap() {
      if (!document.querySelector('link[href*="bootstrap"]')) {
        const bootstrapCSS = document.createElement('link')
        bootstrapCSS.rel = 'stylesheet'
        bootstrapCSS.href =
          'https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.2/css/bootstrap.min.css'
        document.head.appendChild(bootstrapCSS)
      }
    },

    clearAllPlaceMarkers() {
      this.placeMarkers.forEach(marker => {
        marker.setMap(null)
      })
      this.placeMarkers = []
    },

    clearDirectionsRenderer() {
      if (this.directionsRenderer) {
        this.directionsRenderer.setDirections({ routes: [] })
      }
    },

    setTravelMode(mode) {
      this.travelMode = mode
      this.isDropdownOpen = false
      if (this.routeInfo && this.isRouteActive) {
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

        const script = document.createElement('script')
        const apiKey = import.meta.env.VITE_GOOGLE_MAP_KEY
        script.src = `https://maps.googleapis.com/maps/api/js?key=${apiKey}&libraries=places`
        script.async = true
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
            this.getAddressFromCoordinates(this.userLocation)
            this.initMap(this.userLocation)
          },
          () => {
            this.userLocation = { lat: -34.397, lng: 150.644 }
            this.userAddress = 'Tu ubicación'
            this.originInput = 'Tu ubicación'
            this.initMap(this.userLocation)
          },
        )
      } else {
        this.userLocation = { lat: -34.397, lng: 150.644 }
        this.userAddress = 'Tu ubicación'
        this.originInput = 'Tu ubicación'
        this.initMap(this.userLocation)
      }
    },

    getAddressFromCoordinates(coordinates) {
      if (this.geocoder) {
        this.geocoder.geocode({ location: coordinates }, (results, status) => {
          if (status === 'OK' && results[0]) {
            this.userAddress = results[0].formatted_address
            this.originInput = this.userAddress
          } else {
            this.userAddress = 'Tu ubicación'
            this.originInput = 'Tu ubicación'
          }
        })
      }
    },

    initMap(center) {
      this.map = new google.maps.Map(document.getElementById('map'), {
        center,
        zoom: 14,
      })

      this.geocoder = new google.maps.Geocoder()
      this.directionsService = new google.maps.DirectionsService()
      this.directionsRenderer = new google.maps.DirectionsRenderer({
        suppressMarkers: true,
        suppressInfoWindows: true,
      })
      this.directionsRenderer.setMap(this.map)

      this.userLocationMarker = new google.maps.Marker({
        position: center,
        map: this.map,
        title: 'Tu ubicación actual',
      })

      this.map.addListener('click', (event) => {
        this.handleMapClick(event)
      })

      this.isMapReady = true
    },

    handleMapClick(event) {
      this.clearAllPlaceMarkers()
      this.clearDirectionsRenderer()

      const latLng = event.latLng
      const placeId = event.placeId

      if (placeId) {
        this.getPlaceDetailsByPlaceId(placeId, latLng)
      } else {
        this.searchNearbyPlace(latLng)
      }
    },

    getPlaceDetailsByPlaceId(placeId, latLng) {
      const placesService = new google.maps.places.PlacesService(this.map)

      placesService.getDetails(
        {
          placeId: placeId,
          fields: ['name', 'formatted_address', 'rating', 'place_id', 'geometry'],
        },
        (place, status) => {
          if (status === google.maps.places.PlacesServiceStatus.OK) {
            this.createMarkerAndShowInfo(place, latLng)
          } else {
            console.log('Error obteniendo detalles del lugar:', status)
            this.searchNearbyPlace(latLng)
          }
        }
      )
    },

    searchNearbyPlace(latLng) {
      const placesService = new google.maps.places.PlacesService(this.map)

      placesService.nearbySearch(
        {
          location: latLng,
          radius: 50,
        },
        (places, status) => {
          if (places && places.length > 0) {
            this.createMarkerAndShowInfo(places[0], latLng)
          }
        }
      )
    },

    createMarkerAndShowInfo(place, latLng) {
      const marker = new google.maps.Marker({
        position: latLng,
        map: this.map,
        title: place.name,
      })

      this.placeMarkers.push(marker)

      this.selectedPlaceCoordinates = latLng
      this.routeInfo = {
        name: place.name,
        address: place.formatted_address || place.vicinity || 'Dirección no disponible',
        rating: place.rating || 'N/A',
        distance: '',
        duration: '',
        placeId: place.place_id,
      }

      this.selectedPlaceId = place.place_id
      this.isCardExpanded = true
      this.isRouteActive = false
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

      this.clearAllPlaceMarkers()

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
            this.updateRouteInfo(place, leg)
          })
        } else {
          console.error('Error al calcular la ruta:', status)
          alert('No se pudo calcular la ruta.')
        }
      })
    },

    getDistanceInKm(distanceText) {
      const match = distanceText.match(/(\d+\.?\d*)\s*(km|m)/i)
      if (!match) return 0
      
      const value = parseFloat(match[1])
      const unit = match[2].toLowerCase()
      
      return unit === 'km' ? value : value / 1000
    },

    getEcoRecommendation(distanceKm) {
      if (distanceKm <= 2) {
        return {
          mode: 'WALKING',
          text: 'A pie (cero emisiones)',
          icon: 'fa-person-walking',
          color: 'text-success',
        }
      }
      if (distanceKm <= 5) {
        return {
          mode: 'BICYCLING',
          text: 'Bicicleta (baja huella de carbono)',
          icon: 'fa-bicycle',
          color: 'text-warning',
        }
      }
      if (distanceKm <= 15) {
        return {
          mode: 'TRANSIT',
          text: 'Transporte público (opción sostenible)',
          icon: 'fa-bus',
          color: 'text-info',
        }
      }
      return {
        mode: 'DRIVING',
        text: 'Automóvil (puedes escoger una opción menos contaminante)',
        icon: 'fa-car',
        color: 'text-danger',
      }
    },

    updateRouteInfo(place, leg) {
      const distanceKm = this.getDistanceInKm(leg.distance.text)
      const eco = this.getEcoRecommendation(distanceKm)

      this.routeInfo = {
        name: place.name || leg.end_address.split(',')[0],
        address: leg.end_address,
        rating: place.rating || 'N/A',
        distance: leg.distance.text,
        duration: leg.duration.text,
        placeId: place.place_id || null,
        ecoRecommendation: eco,
      }

      // Solo cambiar automáticamente al modo ecológico en la PRIMERA vez
      // Si el usuario ya seleccionó manualmente un modo, respetar su elección
      if (!this.isRouteActive) {
        this.travelMode = eco.mode
      }

      this.selectedPlaceId = place.place_id || null
      this.isCardExpanded = true
      this.isRouteActive = true
    },

    setAsDestination() {
      if (this.routeInfo && this.selectedPlaceCoordinates) {
        this.clearAllPlaceMarkers()

        const origin = this.originInput.trim() !== '' ? this.originInput : this.userLocation

        const request = {
          origin,
          destination: this.selectedPlaceCoordinates,
          travelMode: this.travelMode,
        }

        this.directionsService.route(request, (result, status) => {
          if (status === 'OK') {
            this.directionsRenderer.setDirections(result)

            const leg = result.routes[0].legs[0]
            const distanceKm = this.getDistanceInKm(leg.distance.text)
            const eco = this.getEcoRecommendation(distanceKm)

            this.routeInfo = {
              name: this.routeInfo.name,
              address: leg.end_address,
              rating: this.routeInfo.rating,
              distance: leg.distance.text,
              duration: leg.duration.text,
              placeId: this.routeInfo.placeId,
              ecoRecommendation: eco,
            }

            // Cambiar al modo más ecológico sugerido
            this.travelMode = eco.mode

            this.suppressDestinationSearch = true
            this.destinationInput = this.routeInfo.name

            this.$nextTick(() => {
              setTimeout(() => {
                this.suppressDestinationSearch = false
              }, 50)
            })

            this.isDropdownOpen = false
            this.isCardExpanded = true
            this.isRouteActive = true
          } else {
            console.error('Error al calcular la ruta con coordenadas:', status)
            alert('No se pudo calcular la ruta.')
          }
        })
      }
    },

    clearRoute() {
      this.routeInfo = null
      this.destinationInput = ''
      this.isRouteActive = false
      this.selectedPlaceCoordinates = null
      this.isDropdownOpen = false
      this.suppressDestinationSearch = false

      this.clearAllPlaceMarkers()
      this.clearDirectionsRenderer()
    },

    handleRouteQuery(query) {
      if (query.name && query.lat && query.lng && query.placeId) {
        if (!this.isMapReady) {
          setTimeout(() => this.handleRouteQuery(query), 500)
          return
        }

        const lat = parseFloat(query.lat)
        const lng = parseFloat(query.lng)
        const coordinates = { lat, lng }

        this.map.setCenter(coordinates)
        this.map.setZoom(16)

        this.clearAllPlaceMarkers()

        const marker = new google.maps.Marker({
          position: coordinates,
          map: this.map,
          title: query.name,
        })

        this.placeMarkers.push(marker)
        this.selectedPlaceCoordinates = coordinates

        this.routeInfo = {
          name: query.name,
          address: query.address || 'Dirección no disponible',
          rating: query.rating || 'N/A',
          distance: '',
          duration: '',
          placeId: query.placeId,
        }

        this.selectedPlaceId = query.placeId
        this.isCardExpanded = true
        this.isRouteActive = false

        this.clearDirectionsRenderer()
      }
    },

    goToPlaceDetails() {
      this.setSelectedPlaceId(this.selectedPlaceId)
      this.$router.push({ name: 'site_description' })
    },
  }
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

.toggle-btn {
  background: rgba(27, 81, 94, 0.1) !important;
  border: 1px solid rgba(27, 81, 94, 0.2) !important;
  color: #1b515e !important;
  border-radius: 50% !important;
  width: 40px;
  height: 40px;
  transition: all 0.3s ease !important;
  flex-shrink: 0;
  font-size: 1.1rem;
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

.info-actions {
  display: flex;
  gap: 10px;
  flex-direction: column;
}

.btn-route {
  background: #abcdab !important;
  border: none !important;
  border-radius: 25px !important;
  color: #1b515e !important;
  font-weight: 500 !important;
  transition: all 0.3s ease !important;
}

.btn-route:hover {
  background: #9bc19b !important;
  transform: translateY(-2px) !important;
  box-shadow: 0 4px 12px rgba(171, 205, 171, 0.3) !important;
}

.btn-details {
  background: #1b515e !important;
  border: none !important;
  border-radius: 25px !important;
  color: white !important;
  font-weight: 500 !important;
  transition: all 0.3s ease !important;
}

.btn-details:hover {
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