<template>
  <div class="container-fluid p-0 place-detail-container">
    <div class="container-lg my-4">
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

              <!-- Imagen por defecto cuando no hay imágenes -->
              <div v-if="imageItems.length === 0" class="default-image-container">
                <img :src="logoUrl" alt="Imagen por defecto" class="default-image" />
              </div>

              <!-- Carousel de imágenes cuando sí hay -->
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
                  :src="item.photoUrl"
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
                  <p class="distance-info mb-0">
                    <i class="fa-solid fa-road me-3"></i>
                    A {{ distance }} km
                  </p>
                </div>
              </div>

              <div class="col-12 col-md-4 mt-3 mt-md-0 mobile-weather-section">
                <div
                  class="d-flex align-items-center justify-content-between justify-content-md-center"
                >
                  <div class="weather-info">
                    <div class="d-flex align-items-center">
                      <i class="fa-solid fa-temperature-half me-3"></i>
                      <div class="weather-details">
                        <span class="weather-status">{{ weather.status }}</span>
                        <span class="temperature ms-2">{{ weather.temperature }}°C</span>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <div class="text-center">
              <button class="btn map-btn rounded-pill px-4 py-2 border-0">
                <i class="fa-solid fa-map-pin me-3"></i>
                Ver en el mapa
              </button>
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

        <!-- Estado vacío cuando no hay reseñas -->
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

        <!-- Carousel de reseñas cuando sí hay -->
        <v-carousel
          v-else
          height="250"
          show-arrows="hover"
          cycle
          hide-delimiter-background
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
                    <v-avatar size="50" class="me-3 border border-2" style="border-color: #1b515e">
                      <v-img
                        v-if="review.profilePhotoUrl"
                        :src="review.profilePhotoUrl"
                        @error="handleImageError"
                      >
                        <template v-slot:placeholder>
                          <v-icon icon="mdi-account" size="30"></v-icon>
                        </template>
                      </v-img>
                      <v-icon v-else icon="mdi-account" size="30"></v-icon>
                    </v-avatar>
                    <div class="text-start">
                      <h6 class="review-author mb-1 fw-semibold">{{ review.authorName }}</h6>
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
        <h5 class="section-title mb-4">Cerca de {{ placeName }}</h5>
        <div class="row g-3">
          <div v-for="place in nearbyPlaces" :key="place.id" class="col-12 col-md-6">
            <div class="card h-100 border border-light shadow-sm nearby-place-card">
              <div class="row g-0 h-100">
                <div class="col-4">
                  <img
                    :src="place.image"
                    class="img-fluid h-100 object-fit-cover"
                    :alt="place.name"
                  />
                </div>
                <div class="col-8">
                  <div class="card-body p-3">
                    <h6 class="card-title fw-semibold mb-2 text-truncate">{{ place.name }}</h6>
                    <div class="d-flex align-items-center mb-2">
                      <i class="fa-regular fa-star me-3 star-icon"></i>
                      <span class="small text-muted">{{ place.rating }}</span>
                    </div>
                    <p class="card-text small text-muted mb-0 lh-sm">{{ place.description }}</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Modal para reseña completa -->
    <v-dialog v-model="reviewModalOpen" max-width="600px">
      <v-card v-if="selectedReview">
        <v-card-title class="d-flex align-items-center pa-4">
          <v-avatar size="60" class="me-3 border border-2" style="border-color: #1b515e">
            <v-img
              v-if="selectedReview.profilePhotoUrl"
              :src="selectedReview.profilePhotoUrl"
              @error="handleImageError"
            >
              <template v-slot:placeholder>
                <v-icon icon="mdi-account" size="35"></v-icon>
              </template>
            </v-img>
            <v-icon v-else icon="mdi-account" size="35"></v-icon>
          </v-avatar>
          <div>
            <h6 class="mb-1 fw-semibold">{{ selectedReview.authorName }}</h6>
            <div class="d-flex align-items-center">
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

    <!-- Modal para agregar reseña -->
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
import { mapActions, mapState } from 'vuex'

export default {
  name: 'PlaceDetail',
  data() {
    return {
      currentImageIndex: 0,
      isFavorite: false,
      weather: { status: 'Despejado', temperature: 21 },
      distance: 36.25,
      logoUrl: '/logo-letras.png',
      reviewModalOpen: false,
      selectedReview: null,
      addReviewModalOpen: false,
      submittingReview: false,
      newReview: {
        text: '',
        rating: 0,
      },
      nearbyPlaces: [
        {
          id: 1,
          name: 'Estación de Amatitán',
          image: 'https://images.unsplash.com/photo-1544928147-79a2dbc1f389?w=150&h=100&fit=crop',
          rating: '4.2',
          description:
            'La estación de Amatitán es un importante punto de partida para recorrer los campos de agave y vivir todo sin olvidar su sabor tradicional.',
        },
        {
          id: 2,
          name: 'Cascada Los Azules',
          image:
            'https://images.unsplash.com/photo-1439066615861-d1af74d74000?w=150&h=100&fit=crop',
          rating: '4.5',
          description:
            'Es un lugar espectacular donde corre el río proveniente de los terrenos y montañas.',
        },
      ],
    }
  },
  computed: {
    ...mapState('places', {
      selectedPlaceDetails: 'selectedPlaceDetails',
    }),
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
  },
  async mounted() {
    try {
      await this.fetchPlaceDetails()
    } catch (error) {
      console.error('Error cargando detalles del lugar:', error)
    }
  },
  methods: {
    ...mapActions('places', {
      fetchPlaceDetails: 'fetchPlaceDetails',
      submitPlaceReview: 'submitPlaceReview',
    }),
    toggleFavorite() {
      this.isFavorite = !this.isFavorite
    },
    goBack() {
      this.$router.push({ name: 'home' })
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
    handleImageError() {
      console.log('Error cargando imagen de perfil')
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
          reviewDate: new Date().toISOString().split('T')[0], // Formato YYYY-MM-DD
          rating: this.newReview.rating,
        }

        const response = await this.submitPlaceReview({
          placeId: this.selectedPlaceDetails.id,
          reviewData,
        })
        console.log(response)
        this.$alert.success(response.message)

        await this.fetchPlaceDetails()
        this.closeAddReviewModal()
      } catch (error) {
        this.$alert.error(getErrorDetails(error))
      } finally {
        this.submittingReview = false
      }
    },
  },
}
</script>

<style scoped>
.place-detail-container {
  background-color: #f8f9fa;
  min-height: 100vh;
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
  backdrop-filter: blur(2px);
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

.distance-info {
  color: #6c757d;
  font-size: 0.9rem;
  font-weight: 500;
}

.weather-icon {
  color: #ffc107;
  font-size: 1.3rem;
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

.btn-rating {
  background: none;
  border: none;
  padding: 0;
  margin: 0;
}

.rating-star {
  color: #ffc107;
  font-size: 1.5rem;
  transition: all 0.2s ease;
}

.btn-rating:hover .rating-star {
  transform: scale(1.1);
}

.nearby-place-card {
  transition: all 0.3s ease;
  border-radius: 8px !important;
  overflow: hidden;
}

.nearby-place-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.1) !important;
  border-color: #1b515e !important;
}

.nearby-place-card .card-title {
  color: #1b515e;
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

  .distance-info {
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

  .distance-info {
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
}
</style>
