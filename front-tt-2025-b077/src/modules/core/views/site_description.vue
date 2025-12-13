<template>
  <div class="container-fluid p-0 place-detail-container">
    <div class="fixed-header">
      <div class="header-content">
        <hamburgermenu @navigate="handleNavigation" />
        <div class="header-title">
          <p class="mb-1">{{ placeName }}</p>
        </div>
        <div class="header-spacer"></div>
      </div>
    </div>

    <div class="container-lg my-12 mb-4">
      <div id="partearriba" class="row g-4 shadow-sm rounded-3 bg-white overflow-hidden">
        <div class="col-12 col-lg-6 p-0 position-relative">
          <v-defaults-provider :defaults="{ VBtn: { variant: 'outlined', color: '#eee' } }">
            <v-sheet class="overflow-hidden h-100">
              <div class="top-bar-overlay d-flex justify-content-between align-items-center p-3">
                <div class="d-flex align-items-center">
                  <button class="btn btn-icon-overlay me-3" @click="goBack">
                    <i class="fa-solid fa-arrow-left"></i>
                  </button>
                </div>
                <button class="btn btn-icon-overlay" @click="toggleFavorite">
                  <i
                    :class="isFavorite ? 'fa-solid fa-heart text-danger' : 'fa-regular fa-heart'"
                  ></i>
                </button>
              </div>

              <div v-if="imageItems.length === 0" class="default-image-container">
                <img :src="logoUrl" alt="Imagen por defecto" class="default-image" />
              </div>

              <v-carousel
                v-else
                v-model="currentImageIndex"
                direction="vertical"
                height="450"
                progress="#1B515E"
                vertical-arrows="left"
                vertical-delimiters="right"
                :hide-delimiters="true"
                class="main-carousel"
              >
                <v-carousel-item
                  v-for="(item, i) in imageItems"
                  :key="i"
                  :src="getPlaceImage(item)"
                  cover
                >
                </v-carousel-item>
                <v-overlay
                  :scrim="false"
                  content-class="w-100 h-100 d-flex flex-column align-center justify-space-between pointer-pass-through py-3"
                  contained
                  model-value
                  no-click-animation
                  persistent
                >
                  <v-scroll-x-transition mode="out-in" appear>
                    <v-sheet
                      v-if="currentImageItem"
                      :key="currentImageIndex"
                      rounded="xl"
                      class="image-info-overlay"
                      style="margin-top: 60px"
                    >
                    </v-sheet>
                  </v-scroll-x-transition>
                  <v-chip
                    :text="`${currentImageIndex + 1} / ${imageItems.length}`"
                    color="#1B515E"
                    size="small"
                    variant="flat"
                    class="image-counter"
                  ></v-chip>
                </v-overlay>
              </v-carousel>
            </v-sheet>
          </v-defaults-provider>
        </div>

        <div class="col-12 col-lg-6 place-info-section d-flex align-items-center">
          <div class="p-4 p-lg-5 w-100">
            <div class="row align-items-start mb-4">
              <div class="col-6 col-md-4">
                <h2 class="place-name mb-2">{{ placeName }}</h2>
                <p class="place-state mb-0">{{ placeAddress }}</p>
              </div>

              <div class="col-6 col-md-4 text-center">
                <div class="rating-section">
                  <div class="d-flex align-items-center justify-content-center mb-2">
                    <div class="stars me-2">
                      <i
                        v-for="n in 5"
                        :key="n"
                        :class="[
                          'bi',
                          n <= Math.floor(rating)
                            ? 'bi-star-fill'
                            : n - rating < 1
                              ? 'bi-star-half'
                              : 'bi-star',
                        ]"
                        class="text-warning me-1"
                      ></i>
                    </div>
                    <i class="fa-regular fa-star me-3 star-icon"></i>
                    <span class="rating-value">{{ rating }}</span>
                  </div>
                  <div class="contact-info">
                    <p v-if="placePhone" class="contact-item mb-2">
                      <i class="fa-solid fa-phone me-2"></i>
                      <a :href="'tel:' + placePhone" class="contact-link">{{ placePhone }}</a>
                    </p>
                    <p v-if="placeWebsite" class="contact-item mb-2">
                      <i class="fa-solid fa-globe me-2"></i>
                      <a :href="placeWebsite" target="_blank" class="contact-link">Sitio web</a>
                    </p>
                    <p v-if="googleMapsUrl" class="contact-item mb-0">
                      <i class="fa-brands fa-google me-2"></i>
                      <a :href="googleMapsUrl" target="_blank" class="contact-link">Ver en Maps</a>
                    </p>
                  </div>
                </div>
              </div>

              <div class="col-12 col-md-4 mt-3 mt-md-0 mobile-weather-section">
                <div
                  class="d-flex align-items-center justify-content-between justify-content-md-center"
                >
                  <div class="weather-info mx-2">
                    <div class="d-flex align-items-center">
                      <i :class="weather.iconClass + ' me-3'"></i>
                      <div class="weather-details">
                        <span class="weather-status">{{ weather.status }}</span>
                        <span class="temperature ms-2">{{ weather.temperature }}°C</span>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <div class="text-center d-flex justify-content-center">
              <button @click="goToMap" class="btn map-btn rounded-pill px-4 py-2 border-0">
                <i class="fa-solid fa-map-pin me-3"></i>
                Ver en el mapa
              </button>
              <AddToItineraryModal :place="selectedPlaceDetails" @place-added="handlePlaceAdded" />
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="container-lg content-section">
      <div class="bg-white rounded-3 shadow-sm p-4 p-lg-5 mb-4">
        <h5 class="section-title mb-4">Datos básicos</h5>
        <p class="basic-info-text mb-0">{{ basicInfo }}</p>
      </div>

      <div class="bg-white rounded-3 shadow-sm p-4 p-lg-5 mb-4">
        <div class="d-flex justify-content-between align-items-center mb-4">
          <h5 class="section-title mb-0">Reseñas</h5>
          <button class="btn btn-add-review" @click="openAddReviewModal">
            <i class="fa-solid fa-plus me-2"></i>
            Agregar reseña
          </button>
        </div>

        <div v-if="reviews.length === 0" class="no-reviews-state text-center py-5">
          <i class="fa-regular fa-comment-dots no-reviews-icon mb-3"></i>
          <h6 class="no-reviews-title mb-2">Sin reseñas aún</h6>
          <p class="no-reviews-text mb-3">
            Sé el primero en compartir tu experiencia sobre este lugar.
          </p>
          <button class="btn btn-primary" @click="openAddReviewModal">
            <i class="fa-solid fa-plus me-2"></i>
            Escribir primera reseña
          </button>
        </div>

        <v-carousel
          v-else
          height="250"
          show-arrows="hover"
          cycle
          :hide-delimiters="true"
          progress="#1B515E"
          class="reviews-carousel rounded-3"
        >
          <v-carousel-item
            v-for="(review, i) in reviews"
            :key="i"
            @click="checkIfLongReview(review)"
          >
            <v-sheet color="white" height="100%" class="review-sheet cursor-pointer">
              <div class="d-flex fill-height justify-center align-center p-4">
                <div class="review-content text-center">
                  <div class="d-flex align-items-center justify-content-center mb-3">
                    <ResilientAvatar
                      :src="review.profilePhotoUrl"
                      :size="50"
                      avatar-class="me-3 border border-2"
                      avatar-style="border-color: #1b515e"
                      :alt="review.authorName + ' avatar'"
                    />
                    <div class="text-start">
                      <h6 class="review-author mb-1 fw-semibold">{{ review.authorName }}</h6>
                      <small class="review-date text-muted">{{
                        formatReviewDate(review.reviewDateTime)
                      }}</small>
                    </div>
                  </div>

                  <div class="d-flex align-items-center justify-content-center mb-3">
                    <div class="stars me-3">
                      <i
                        v-for="n in 5"
                        :key="n"
                        :class="['bi', n <= review.rating ? 'bi-star-fill' : 'bi-star']"
                        class="text-warning me-1"
                      ></i>
                    </div>
                    <i class="fa-regular fa-star me-3 star-icon"></i>
                    <span class="review-rating fw-semibold">{{ review.rating.toFixed(1) }}</span>
                  </div>

                  <p class="review-text mb-0 lh-base fst-italic" @click="checkIfLongReview(review)">
                    "{{ review.shortText }}"
                    <span v-if="isReviewLong(review)" class="read-more-indicator">
                      <br /><small class="text-primary fw-semibold">Leer más...</small>
                    </span>
                  </p>
                </div>
              </div>
            </v-sheet>
          </v-carousel-item>
        </v-carousel>
      </div>

      <div class="bg-white rounded-3 shadow-sm p-4 p-lg-5 mb-5">
        <h5 class="section-title mb-4">Horario</h5>
        <div class="schedule-container">
          <div v-if="weekdaySchedule && Object.keys(weekdaySchedule).length > 0" class="row g-3">
            <div v-for="(hours, day) in weekdaySchedule" :key="day" class="col-6 col-md-4 col-lg-3">
              <div class="schedule-item">
                <div class="day-name">{{ day }}</div>
                <div class="day-hours">{{ hours }}</div>
              </div>
            </div>
          </div>
          <div v-else class="text-center text-muted py-3">
            <i class="fa-regular fa-clock me-2"></i>
            Horario no disponible
          </div>
        </div>
      </div>
    </div>

    <v-dialog v-model="reviewModalOpen" max-width="600px">
      <v-card v-if="selectedReview">
        <v-card-title class="d-flex align-items-center pa-4">
          <ResilientAvatar
            :src="selectedReview.profilePhotoUrl"
            :size="60"
            avatar-class="me-3 border border-2"
            avatar-style="border-color: #1b515e"
            :alt="selectedReview.authorName + ' avatar'"
          />
          <div>
            <h6 class="mb-1 fw-semibold">{{ selectedReview.authorName }}</h6>
            <small class="review-date text-muted">{{
              formatReviewDate(selectedReview.reviewDateTime)
            }}</small>
            <div class="d-flex align-items-center mt-2">
              <div class="stars me-2">
                <i
                  v-for="n in 5"
                  :key="n"
                  :class="['bi', n <= selectedReview.rating ? 'bi-star-fill' : 'bi-star']"
                  class="text-warning me-1"
                ></i>
              </div>
              <span class="fw-semibold">{{ selectedReview.rating.toFixed(1) }}</span>
            </div>
          </div>
        </v-card-title>
        <v-card-text class="pa-4">
          <p class="mb-0 lh-base">{{ selectedReview.text }}</p>
        </v-card-text>
        <v-card-actions class="pa-4">
          <v-spacer></v-spacer>
          <v-btn color="primary" variant="text" @click="reviewModalOpen = false"> Cerrar </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-dialog v-model="addReviewModalOpen" max-width="500px">
      <v-card>
        <v-card-title class="pa-4">
          <h6 class="mb-0">Agregar reseña</h6>
        </v-card-title>
        <v-card-text class="pa-4">
          <div class="mb-4">
            <label class="form-label fw-semibold mb-3">Calificación</label>
            <v-rating
              v-model="newReview.rating"
              color="warning"
              active-color="warning"
              size="large"
              half-increments
              hover
            ></v-rating>
          </div>

          <div class="mb-4">
            <label for="reviewText" class="form-label fw-semibold">Tu reseña</label>
            <v-textarea
              v-model="newReview.text"
              placeholder="Comparte tu experiencia sobre este lugar..."
              rows="4"
              counter="600"
              maxlength="600"
              variant="outlined"
              color="primary"
            ></v-textarea>
          </div>
        </v-card-text>
        <v-card-actions class="pa-4">
          <v-spacer></v-spacer>
          <v-btn variant="text" @click="closeAddReviewModal"> Cancelar </v-btn>
          <v-btn
            color="primary"
            @click="submitReview"
            :disabled="!canSubmitReview"
            :loading="submittingReview"
          >
            Publicar reseña
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import { getErrorDetails } from '@/utils/utils'
import { mapActions, mapState, mapGetters } from 'vuex'
import ResilientAvatar from '@/components/resilient-avatar.vue'
import hamburgermenu from '@/components/hamburgermenu.vue'
import AddToItineraryModal from '@/components/addtoitinerarymodal.vue'

export default {
  name: 'PlaceDetail',
  components: {
    ResilientAvatar,
    hamburgermenu,
    AddToItineraryModal,
  },
  data() {
    return {
      currentImageIndex: 0,
      weather: {
        status: 'Cargando...',
        temperature: '--',
        iconClass: 'fa-solid fa-sun',
      },
      logoUrl: '/logo-letras.png',
      reviewModalOpen: false,
      selectedReview: null,
      addReviewModalOpen: false,
      submittingReview: false,
      newReview: {
        text: '',
        rating: 0,
      },
    }
  },
  computed: {
    ...mapState('places', {
      selectedPlaceDetails: 'selectedPlaceDetails',
    }),
    ...mapGetters('trips', ['favoriteIds', 'userItineraries']),
    placeName() {
      return this.selectedPlaceDetails?.name || 'Cargando...'
    },
    placeAddress() {
      return this.selectedPlaceDetails?.formattedAddress || 'Cargando dirección...'
    },
    rating() {
      return this.selectedPlaceDetails?.rating || 0
    },
    basicInfo() {
      return this.selectedPlaceDetails?.description || 'Información no disponible'
    },
    imageItems() {
      return this.selectedPlaceDetails?.images || []
    },
    reviews() {
      return this.selectedPlaceDetails?.reviews || []
    },
    currentImageItem() {
      return this.imageItems[this.currentImageIndex] || this.imageItems[0]
    },
    canSubmitReview() {
      return this.newReview.text.trim().length > 0 && this.newReview.rating > 0
    },
    isFavorite() {
      return (
        this.selectedPlaceDetails?.id && this.favoriteIds.includes(this.selectedPlaceDetails.id)
      )
    },
    placePhone() {
      return this.selectedPlaceDetails?.formattedPhoneNumber || null
    },
    placeWebsite() {
      return this.selectedPlaceDetails?.website || null
    },
    googleMapsUrl() {
      return this.selectedPlaceDetails?.googleMapsUrl || null
    },
    weekdaySchedule() {
      return this.selectedPlaceDetails?.weekdayText || {}
    },
  },
  async mounted() {
    try {
      await this.fetchPlaceDetails()
      await this.loadWeatherData()
    } catch (error) {
      console.error('Error cargando detalles del lugar:', error)
    }
  },
  methods: {
    ...mapActions('places', {
      fetchPlaceDetails: 'fetchPlaceDetails',
      submitPlaceReview: 'submitPlaceReview',
      getPlaceWeather: 'getPlaceWeather',
    }),
    ...mapActions('trips', {
      toggleFavoritePlace: 'toggleFavoritePlace',
    }),
    async toggleFavorite() {
      if (!this.selectedPlaceDetails?.id) return

      try {
        await this.toggleFavoritePlace(this.selectedPlaceDetails.id)
      } catch (error) {
        this.$alert.error(getErrorDetails(error))
      }
    },
    goBack() {
      this.$router.push({ name: 'home' })
    },
    goToMap() {
      const place = this.selectedPlaceDetails
      this.$router.push({
        name: 'interactive_map',
        query: {
          name: place.name,
          lat: place.lat,
          lng: place.lng,
          placeId: place.placeId,
          address: place.formattedAddress,
          rating: place.rating,
        },
      })
    },
    getPlaceImage(image) {
      const baseURL = import.meta.env.DEV
        ? 'http://127.0.0.1:8080/api/'
        : import.meta.env.VITE_API_BACK

      if (image.origin === "Google") {
        return `${baseURL}place/photo?photoReference=${image.photoReference}`
      } else {
        return image.photoUrl
      }
    },
    openReviewModal(review) {
      this.selectedReview = review
      this.reviewModalOpen = true
    },
    checkIfLongReview(review) {
      if (this.isReviewLong(review)) {
        this.openReviewModal(review)
      }
    },
    isReviewLong(review) {
      return review.text && review.shortText && review.text.length > review.shortText.length + 20
    },
    handleImageError(event) {
      event.target.style.display = 'none'
    },
    getProcessedImageUrl(url) {
      if (!url) return null

      if (url.includes('googleusercontent.com')) {
        return url.replace(/=s\d+/, '=s64').replace(/=w\d+/, '=w64')
      }

      return url
    },
    async loadWeatherData() {
      try {
        const placeCoords = {
          lat: this.selectedPlaceDetails?.lat,
          lng: this.selectedPlaceDetails?.lng,
        }
        if (!placeCoords.lat || !placeCoords.lng) return

        const response = await this.getPlaceWeather(placeCoords)

        if (response && response.data) {
          const weatherData = response.data
          this.weather = {
            status: weatherData.description,
            temperature: Math.round(weatherData.temperature),
            iconClass: this.getWeatherIcon(weatherData.temperature),
          }
        }
      } catch (error) {
        console.error('Error cargando clima:', error)
        this.weather = {
          status: 'No disponible',
          temperature: '--',
          iconClass: 'fa-solid fa-question',
        }
      }
    },
    getWeatherIcon(temperature) {
      if (temperature <= 0) return 'fa-solid fa-snowflake weather-icon-cold'
      if (temperature <= 10) return 'fa-solid fa-icicles weather-icon-cold'
      if (temperature <= 20) return 'fa-solid fa-cloud weather-icon-cool'
      if (temperature <= 30) return 'fa-solid fa-cloud-sun weather-icon-warm'
      return 'fa-solid fa-fire weather-icon-hot'
    },
    formatReviewDate(dateTime) {
      if (!dateTime) return ''

      const date = new Date(dateTime)
      const now = new Date()
      const diffTime = now - date
      const diffDays = Math.floor(diffTime / (1000 * 60 * 60 * 24))

      if (diffDays === 0) return 'Hoy'
      if (diffDays === 1) return 'Ayer'
      if (diffDays < 7) return `Hace ${diffDays} días`
      if (diffDays < 30)
        return `Hace ${Math.floor(diffDays / 7)} semana${Math.floor(diffDays / 7) > 1 ? 's' : ''}`
      if (diffDays < 365)
        return `Hace ${Math.floor(diffDays / 30)} mes${Math.floor(diffDays / 30) > 1 ? 'es' : ''}`

      return date.toLocaleDateString('es-ES', { year: 'numeric', month: 'long', day: 'numeric' })
    },
    openAddReviewModal() {
      this.addReviewModalOpen = true
    },
    closeAddReviewModal() {
      this.addReviewModalOpen = false
      this.newReview = {
        text: '',
        rating: 0,
      }
    },
    async submitReview() {
      if (!this.canSubmitReview) return

      this.submittingReview = true

      try {
        const reviewData = {
          text: this.newReview.text.trim(),
          reviewDate: new Date().toISOString().split('T')[0],
          rating: this.newReview.rating,
        }

        const response = await this.submitPlaceReview({
          placeId: this.selectedPlaceDetails.id,
          reviewData,
        })
        this.$alert.success(response.message)

        await this.fetchPlaceDetails()
        this.closeAddReviewModal()
      } catch (error) {
        this.$alert.error(getErrorDetails(error))
      } finally {
        this.submittingReview = false
      }
    },
    handleNavigation(route) {
      this.$router.push({ name: route })
    },
    handlePlaceAdded() {
      console.log('Lugar agregado exitosamente')
    },
  },
}
</script>

<style scoped>
.place-detail-container {
  background-color: #f8f9fa;
  min-height: 100vh;
  padding-top: 60px;
}

.fixed-header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  background-color: white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  z-index: 1000;
  height: 60px;
}

.header-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 100%;
  padding: 0 1rem;
}

.header-title {
  flex: 1;
  text-align: center;
}

.header-title p {
  font-weight: 600;
  color: #1b515e;
  font-size: 1.1rem;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 100%;
  padding: 0 1rem;
}

.header-spacer {
  width: 40px;
}

#partearriba {
  border: 1px solid #e9ecef;
}

.main-carousel {
  border-radius: 0;
}

.default-image-container {
  width: 100%;
  height: 450px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f8f9fa;
}

.default-image {
  max-width: 60%;
  max-height: 60%;
  object-fit: contain;
  opacity: 0.7;
}

.top-bar-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  z-index: 10;
  background: linear-gradient(
    180deg,
    rgba(0, 0, 0, 0.6) 0%,
    rgba(0, 0, 0, 0.3) 50%,
    rgba(0, 0, 0, 0) 100%
  );
}

.btn-icon-overlay {
  background: rgba(255, 255, 255, 0.9);
  border: none;
  border-radius: 50%;
  width: 44px;
  height: 44px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
  transition: all 0.3s ease;
  backdrop-filter: blur(10px);
}

.btn-icon-overlay:hover {
  background: rgba(255, 255, 255, 1);
  transform: scale(1.05);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}

.btn-icon-overlay i {
  font-size: 1.1rem;
  color: #1b515e;
  transition: color 0.3s ease;
}

.btn-icon-overlay:hover i {
  color: #0f3940;
}

.btn-icon-overlay .fa-heart.text-danger {
  color: #dc3545 !important;
}

.image-info-overlay {
  background-color: rgba(27, 81, 94, 0.9) !important;
  color: white;
  backdrop-filter: blur(8px);
}

.image-counter {
  color: white !important;
  font-weight: 500;
}

.place-info-section {
  background-color: white;
  min-height: 450px;
}

.place-name {
  color: #1b515e;
  font-weight: 700;
  font-size: 1.8rem;
  line-height: 1.2;
}

.place-state {
  color: #6c757d;
  font-size: 1rem;
  font-weight: 500;
}

.rating-section {
  padding: 0 0.5rem;
}

.rating-value {
  font-weight: 600;
  color: #1b515e;
  font-size: 1.1rem;
}

.contact-info {
  margin-top: 0.5rem;
}

.contact-item {
  display: flex;
  align-items: center;
  font-size: 0.9rem;
  color: #495057;
}

.contact-link {
  color: #1b515e;
  text-decoration: none;
  transition: color 0.3s ease;
}

.contact-link:hover {
  color: #143e48;
  text-decoration: underline;
}

.weather-icon {
  font-size: 1.3rem;
}

.weather-icon-cold {
  color: #1b515e;
}

.weather-icon-cool {
  color: #1b515e;
}

.weather-icon-warm {
  color: #1b515e;
}

.weather-icon-hot {
  color: #1b515e;
}

.weather-status {
  color: #495057;
  font-weight: 500;
  font-size: 0.95rem;
}

.temperature {
  color: #1b515e;
  font-weight: 600;
  font-size: 0.95rem;
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

.content-section {
  padding-bottom: 2rem;
}

.section-title {
  color: #1b515e;
  font-weight: 700;
  font-size: 1.25rem;
  position: relative;
}

.section-title::after {
  content: '';
  position: absolute;
  bottom: -8px;
  left: 0;
  width: 40px;
  height: 3px;
  background: linear-gradient(90deg, #1b515e 0%, #abcd9e 100%);
  border-radius: 2px;
}

.basic-info-text {
  text-align: justify;
  line-height: 1.7;
  color: #495057;
  font-size: 1rem;
}

.btn-add-review {
  background: #1b515e;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 20px;
  font-size: 0.9rem;
  font-weight: 500;
  transition: all 0.3s ease;
}

.btn-add-review:hover {
  background: #143e48;
  transform: translateY(-1px);
  color: white;
}

.no-reviews-state {
  color: #6c757d;
}

.no-reviews-icon {
  font-size: 3rem;
  color: #dee2e6;
}

.no-reviews-title {
  color: #495057;
  font-weight: 600;
}

.no-reviews-text {
  color: #6c757d;
  max-width: 300px;
  margin: 0 auto;
}

.cursor-pointer {
  cursor: pointer;
}

.review-content {
  max-width: 400px;
  margin: 0 auto;
}

.review-author {
  color: #1b515e;
  font-size: 1rem;
}

.review-date {
  font-size: 0.75rem;
}

.review-rating {
  color: #1b515e;
  font-size: 1.1rem;
}

.review-text {
  color: #495057;
  font-size: 1rem;
  line-height: 1.6;
  max-width: 500px;
  margin: 0 auto;
}

.read-more-indicator {
  cursor: pointer;
}

.schedule-container {
  max-width: 100%;
}

.schedule-item {
  background-color: #f8f9fa;
  border-radius: 8px;
  padding: 1rem;
  text-align: center;
  border: 1px solid #e9ecef;
  transition: all 0.3s ease;
}

.schedule-item:hover {
  background-color: #e9ecef;
  transform: translateY(-2px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.day-name {
  font-weight: 600;
  color: #1b515e;
  text-transform: capitalize;
  margin-bottom: 0.5rem;
  font-size: 0.95rem;
}

.day-hours {
  color: #495057;
  font-size: 0.9rem;
}

.object-fit-cover {
  object-fit: cover;
}

.lh-sm {
  line-height: 1.25;
}

.lh-base {
  line-height: 1.5;
}

.star-icon {
  color: #ffc107;
}

@media (max-width: 992px) {
  .place-info-section {
    min-height: auto;
  }

  .place-name {
    font-size: 1.6rem;
  }

  #partearriba {
    border-radius: 0 !important;
  }

  .contact-item {
    font-size: 0.85rem;
  }
}

@media (max-width: 770px) {
  .container-lg {
    margin: 0;
    padding: 0;
  }

  .content-section {
    padding: 1rem;
  }

  .place-name {
    font-size: 1.4rem;
    margin-bottom: 0.5rem !important;
  }

  .place-state {
    font-size: 0.9rem;
  }

  .rating-section {
    padding: 0;
  }

  .rating-value {
    font-size: 1rem;
  }

  .contact-item {
    font-size: 0.8rem;
  }

  .weather-status,
  .temperature {
    font-size: 0.85rem;
  }

  .weather-icon {
    font-size: 1.1rem;
  }

  .mobile-weather-section {
    margin-top: 1rem !important;
    padding: 0.5rem 0;
    border-top: 1px solid #e9ecef;
  }

  .mobile-weather-section .d-flex {
    justify-content: space-between !important;
  }

  .review-content {
    padding: 0 1rem;
  }

  .review-text {
    font-size: 0.95rem;
    max-width: 100%;
  }

  .review-author {
    font-size: 0.95rem;
  }

  .top-bar-overlay {
    padding: 0.75rem 1rem;
  }

  .btn-icon-overlay {
    width: 40px;
    height: 40px;
  }

  .btn-icon-overlay i {
    font-size: 1rem;
  }

  .header-title p {
    font-size: 1rem;
  }

  .schedule-item {
    padding: 0.75rem;
  }

  .day-name {
    font-size: 0.85rem;
  }

  .day-hours {
    font-size: 0.8rem;
  }
}

@media (max-width: 576px) {
  .place-name {
    font-size: 1.3rem;
  }

  .place-state {
    font-size: 0.85rem;
  }

  .stars i {
    font-size: 0.8rem;
  }

  .rating-value {
    font-size: 0.9rem;
  }

  .contact-item {
    font-size: 0.75rem;
  }

  .weather-status,
  .temperature {
    font-size: 0.8rem;
  }

  .reviews-carousel {
    height: 280px !important;
  }

  .review-text {
    font-size: 0.9rem;
    line-height: 1.5;
  }

  .btn-icon-overlay {
    width: 36px;
    height: 36px;
  }

  .header-title p {
    font-size: 0.9rem;
  }
}
</style>