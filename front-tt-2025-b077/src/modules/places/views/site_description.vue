<template>
  <div class="container-fluid p-0 place-detail-container">
    <!-- Sección superior con márgenes mejorados -->
    <div class="container-lg my-4">
      <div id="partearriba" class="row g-4 shadow-sm rounded-3 bg-white overflow-hidden">
        <!-- Carrusel de imágenes -->
        <div class="col-12 col-lg-6 p-0 position-relative">
          <v-defaults-provider :defaults="{ VBtn: { variant: 'outlined', color: '#eee' } }">
            <v-sheet class="overflow-hidden h-100">
              <!-- Barra superior con iconos -->
              <div class="top-bar-overlay d-flex justify-content-between align-items-center p-3">
                <div class="d-flex align-items-center">
                  <button class="btn btn-icon-overlay me-3" @click="goBack">
                    <i class="fa-solid fa-arrow-left"></i>
                  </button>
                </div>
                <button class="btn btn-icon-overlay" @click="toggleFavorite">
                  <i :class="isFavorite ? 'fa-solid fa-heart text-danger' : 'fa-regular fa-heart'"></i>
                </button>
              </div>
              <v-carousel
                v-model="currentImageIndex"
                direction="vertical"
                height="450"
                progress="#1B515E"
                vertical-arrows="left"
                vertical-delimiters="right"
                hide-delimiter-background
                class="main-carousel"
              >
                <v-carousel-item
                  v-for="(item, i) in imageItems"
                  :key="i"
                  :src="item.src"
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
                      style="margin-top: 60px;"
                    >
                    </v-sheet>
                  </v-scroll-x-transition>
                  <v-chip
                    :text="`${ currentImageIndex + 1 } / ${imageItems.length }`"
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

        <!-- Información del lugar -->
        <div class="col-12 col-lg-6 place-info-section d-flex align-items-center">
          <div class="p-4 p-lg-5 w-100">
            <!-- Layout principal: Lugar | Calificación | Clima -->
            <div class="row align-items-start mb-4">
              <!-- Columna izquierda: Nombre y estado -->
              <div class="col-6 col-md-4">
                <h2 class="place-name mb-2">{{ placeName }}</h2>
                <p class="place-state mb-0">{{ state }}</p>
              </div>
              
              <!-- Columna centro: Calificación y distancia -->
              <div class="col-6 col-md-4 text-center">
                <div class="rating-section">
                  <div class="d-flex align-items-center justify-content-center mb-2">
                    <div class="stars me-2">
                      <i v-for="n in 5" :key="n" 
                         :class="['bi', n <= Math.floor(rating) ? 'bi-star-fill' : (n - rating < 1 ? 'bi-star-half' : 'bi-star')]"
                         class="text-warning me-1"></i>
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
              
              <!-- Columna derecha: Clima y favorito -->
              <div class="col-12 col-md-4 mt-3 mt-md-0 mobile-weather-section">
                <div class="d-flex align-items-center justify-content-between justify-content-md-center">
                  <!-- Clima -->
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

            <!-- Botón Ver en el mapa -->
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

    <!-- Datos básicos -->
    <div class="container-lg content-section">
      <div class="bg-white rounded-3 shadow-sm p-4 p-lg-5 mb-4">
        <h5 class="section-title mb-4">Datos básicos</h5>
        <p class="basic-info-text mb-0">{{ basicInfo }}</p>
      </div>

      <!-- Reseñas -->
      <div class="bg-white rounded-3 shadow-sm p-4 p-lg-5 mb-4">
        <h5 class="section-title mb-4">Reseñas</h5>
        <v-carousel
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
          >
            <v-sheet
              color="white"
              height="100%"
              class="review-sheet"
            >
              <div class="d-flex fill-height justify-center align-center p-4">
                <div class="review-content text-center">
                  <!-- Avatar y nombre -->
                  <div class="d-flex align-items-center justify-content-center mb-3">
                    <v-avatar size="50" class="me-3 border border-2" style="border-color: #1B515E;">
                      <v-img :src="`https://randomuser.me/api/portraits/${review.avatarId}.jpg`"></v-img>
                    </v-avatar>
                    <div class="text-start">
                      <h6 class="review-author mb-1 fw-semibold">{{ review.authorName }}</h6>
                    </div>
                  </div>
                  
                  <!-- Estrellas y rating -->
                  <div class="d-flex align-items-center justify-content-center mb-3">
                    <div class="stars me-3">
                      <i v-for="n in 5" :key="n" 
                         :class="['bi', n <= review.rating ? 'bi-star-fill' : 'bi-star']"
                         class="text-warning me-1"></i>
                    </div>
                    <i class="fa-regular fa-star me-3 star-icon"></i>
                    <span class="review-rating fw-semibold">{{ review.rating }}.0</span>
                  </div>
                  
                  <!-- Texto de la reseña -->
                  <p class="review-text mb-0 lh-base fst-italic">
                    "{{ review.text }}"
                  </p>
                </div>
              </div>
            </v-sheet>
          </v-carousel-item>
        </v-carousel>
      </div>

      <!-- Cerca de Tequila -->
      <div class="bg-white rounded-3 shadow-sm p-4 p-lg-5 mb-5">
        <h5 class="section-title mb-4">Cerca de Tequila</h5>
        <div class="row g-3">
          <div v-for="place in nearbyPlaces" :key="place.id" class="col-12 col-md-6">
            <div class="card h-100 border border-light shadow-sm nearby-place-card">
              <div class="row g-0 h-100">
                <div class="col-4">
                  <img :src="place.image" class="img-fluid h-100 object-fit-cover" :alt="place.name">
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
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'

// Datos del lugar principal
const placeName = ref('Tequila')
const state = ref('Jalisco')
const rating = ref(4.73)
const distance = ref(36.25)
const weather = ref({ status: 'Despejado', temperature: 21 })

// Estados para los iconos superiores
const isFavorite = ref(false)
const currentDate = ref('')

// Carrusel de imágenes
const currentImageIndex = ref(0)
const imageItems = ref([
  {
    authorName: 'Bettany Nichols',
    avatarId: 'women/31',
    subtitle: '31k followers',
    src: 'https://cdn.vuetifyjs.com/images/carousel/squirrel.jpg',
  },
  {
    authorName: 'Greg Kovalsky',
    avatarId: 'men/61',
    subtitle: '412 followers',
    src: 'https://cdn.vuetifyjs.com/images/carousel/sky.jpg',
  },
  {
    authorName: 'Emma Kathleen',
    avatarId: 'women/34',
    subtitle: '521 followers',
    src: 'https://cdn.vuetifyjs.com/images/carousel/bird.jpg',
  },
  {
    authorName: 'Anthony McKenzie',
    avatarId: 'men/78',
    subtitle: '6k followers',
    src: 'https://cdn.vuetifyjs.com/images/carousel/planet.jpg',
  },
])

const currentImageItem = computed(() => {
  return imageItems.value[currentImageIndex.value] || imageItems.value[0]
})

// Información básica
const basicInfo = ref(`Tequila es una ciudad del estado de Jalisco, en el centro de México. Es conocida por la producción de tequila. La ciudad está cerca de los campos de cultivo de agave azul y la rodean campos del ingrediente principal de este licor, la planta del agave azul. El proceso de producción del tequila se realiza en varias destilerías y haciendas. El Museo Nacional del Tequila y el Museo Los Abuelos explican la historia de la producción de esta bebida.`)

// Reseñas
const reviews = ref([
  {
    authorName: 'María González',
    avatarId: 'women/23',
    followers: '1.2k seguidores',
    rating: 5,
    text: 'Hermoso lugar, la comida deliciosa, música y baile por todos los lados. Buena vibra. Recomiendo visitarlo una fin de semana durante la feria del pueblo.'
  },
  {
    authorName: 'Carlos Mendoza',
    avatarId: 'men/42',
    followers: '845 seguidores',
    rating: 4,
    text: 'Experiencia increíble aprendiendo sobre el proceso del tequila. Los guías son muy conocedores y amables. El paisaje de agaves es impresionante.'
  },
  {
    authorName: 'Ana López',
    avatarId: 'women/56',
    followers: '2.3k seguidores',
    rating: 5,
    text: 'El tour por las destilerías fue lo más destacado. Terminamos con una degustación de diferentes tequilas. ¡Volvería sin duda!'
  },
  {
    authorName: 'Javier Ramírez',
    avatarId: 'men/67',
    followers: '3.1k seguidores',
    rating: 4,
    text: 'El pueblo tiene un encanto especial. Recomiendo quedarse al menos una noche para disfrutar de la vida nocturna y los restaurantes locales.'
  }
])

// Lugares cercanos
const nearbyPlaces = ref([
  {
    id: 1,
    name: 'Estación de Amatitán',
    image: 'https://images.unsplash.com/photo-1544928147-79a2dbc1f389?w=150&h=100&fit=crop',
    rating: '4.2',
    description: 'La estación de Amatitán es un importante punto de partida para recorrer los campos de agave y vivir todo sin olvidar su sabor tradicional.'
  },
  {
    id: 2,
    name: 'Cascada Los Azules',
    image: 'https://images.unsplash.com/photo-1439066615861-d1af74d74000?w=150&h=100&fit=crop',
    rating: '4.5',
    description: 'Es un lugar espectacular donde corre el río proveniente de los terrenos y montañas.'
  }
])

// Métodos
const toggleFavorite = () => {
  isFavorite.value = !isFavorite.value
}

const goBack = () => {
  // Aquí implementarías la navegación hacia atrás
  // Ejemplo: router.go(-1) o router.push('/previous-route')
  console.log('Navegando hacia atrás...')
  window.history.back()
}

const formatCurrentDate = () => {
  const now = new Date()
  const options = { 
    weekday: 'long', 
    year: 'numeric', 
    month: 'long', 
    day: 'numeric' 
  }
  currentDate.value = now.toLocaleDateString('es-ES', options)
}

onMounted(() => {
  formatCurrentDate()
})
</script>

<style scoped>
.place-detail-container {
  background-color: #f8f9fa;
  min-height: 100vh;
}

/* Sección superior */
#partearriba {
  border: 1px solid #e9ecef;
}

.main-carousel {
  border-radius: 0;
}

/* Barra superior con iconos */
.top-bar-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  z-index: 10;
  background: linear-gradient(180deg, rgba(0,0,0,0.6) 0%, rgba(0,0,0,0.3) 50%, rgba(0,0,0,0) 100%);
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
  color: #1B515E;
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

/* Información del lugar */
.place-info-section {
  background-color: white;
  min-height: 450px;
}

.place-name {
  color: #1B515E;
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
  color: #1B515E;
  font-size: 1.1rem;
}

.distance-info {
  color: #6c757d;
  font-size: 0.9rem;
  font-weight: 500;
}

/* Clima y favorito */
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
  color: #1B515E;
  font-weight: 600;
  font-size: 0.95rem;
}

/* Botón de mapa */
.map-btn {
  background: #1B515E;
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

/* Secciones de contenido */
.content-section {
  padding-bottom: 2rem;
}

.section-title {
  color: #1B515E;
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
  background: linear-gradient(90deg, #1B515E 0%, #abcd9e 100%);
  border-radius: 2px;
}

.basic-info-text {
  text-align: justify;
  line-height: 1.7;
  color: #495057;
  font-size: 1rem;
}

.review-content {
  max-width: 400px;
  margin: 0 auto;
}

.review-author {
  color: #1B515E;
  font-size: 1rem;
}

.review-followers {
  color: #6c757d;
  font-size: 0.85rem;
}

.review-rating {
  color: #1B515E;
  font-size: 1.1rem;
}

.review-text {
  color: #495057;
  font-size: 1rem;
  line-height: 1.6;
  max-width: 500px;
  margin: 0 auto;
}

/* Lugares cercanos */
.nearby-place-card {
  transition: all 0.3s ease;
  border-radius: 8px !important;
  overflow: hidden;
}

.nearby-place-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.1) !important;
  border-color: #1B515E !important;
}

.nearby-place-card .card-title {
  color: #1B515E;
}

/* Utilidades */
.object-fit-cover {
  object-fit: cover;
}

.lh-sm {
  line-height: 1.25;
}

.lh-base {
  line-height: 1.5;
}

.star-icon{
  color: #ffc107;
}

/* Responsive */
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
  
  .weather-status, .temperature {
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
  
  /* Ajustes móviles para la barra superior */
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
  
  .current-date {
    font-size: 0.8rem;
  }
  
  .date-info {
    padding: 0.4rem 0.8rem;
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
  
  .weather-status, .temperature {
    font-size: 0.8rem;
  }
  
  .reviews-carousel {
    height: 280px !important;
  }
  
  .review-text {
    font-size: 0.9rem;
    line-height: 1.5;
  }
  
  /* Ajustes extra pequeños para la barra superior */
  .btn-icon-overlay {
    width: 36px;
    height: 36px;
  }
  
  .current-date {
    font-size: 0.75rem;
  }
  
  .date-info {
    padding: 0.3rem 0.6rem;
  }
}
</style>