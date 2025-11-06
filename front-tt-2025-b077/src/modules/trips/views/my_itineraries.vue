<!-- eslint-disable vue/multi-word-component-names -->
<template>
  <NavTopIItinerary />

  <div style="padding-top: 55px"></div>

  <div v-if="userItineraries.length > 0" class="cards-container">
    <div v-for="itinerary in userItineraries" :key="itinerary.id" class="itinerary-wrapper">
      <v-card class="itinerary-card" elevation="2">
        <v-row class="ma-0 h-100">
          <v-col cols="12" sm="5" class="pa-0">
            <v-carousel
              v-if="itinerary.images && itinerary.images.length > 0"
              class="itinerary-carousel"
              height="250"
              hide-delimiters
              show-arrows="hover"
            >
              <v-carousel-item
                v-for="(image, i) in itinerary.images"
                :key="i"
                :src="image"
                cover
              ></v-carousel-item>
            </v-carousel>
            <div v-else class="image-placeholder">
              <i class="fa-solid fa-image"></i>
              <p>Sin imagen</p>
            </div>
          </v-col>

          <v-col cols="12" sm="7" class="pa-4 d-flex flex-column justify-space-between">
            <div>
              <h3 class="itinerary-title mb-3">{{ itinerary.tripTitle }}</h3>

              <div class="itinerary-details">
                <div class="detail-item">
                  <i class="fa-regular fa-calendar"></i>
                  <span>{{ formatDateRange(itinerary.startDate, itinerary.endDate) }}</span>
                </div>

                <div class="detail-item">
                  <i class="fa-solid fa-location-dot"></i>
                  <span>{{ itinerary.totalPlaces }} lugares para visitar</span>
                </div>

                <div
                  v-if="itinerary.averageSustainableIndex"
                  class="detail-item sustainability-detail"
                >
                  <i class="fa-solid fa-leaf"></i>
                  <div
                    class="sustainability-badge"
                    :class="getSustainabilityClass(itinerary.averageSustainableIndex)"
                  >
                    {{ formatSustainability(itinerary.averageSustainableIndex) }}
                  </div>
                </div>
              </div>
            </div>
          </v-col>
        </v-row>
      </v-card>

      <div class="buttons-section">
        <button class="btn-view" @click="viewItinerary(itinerary.id)">
          Ver itinerario
          <i class="fa-solid fa-arrow-right"></i>
        </button>
        <button class="btn-delete" @click="deleteItinerary(itinerary.id)">
          <i class="fa-solid fa-trash"></i>
          Eliminar itinerario
        </button>
      </div>
    </div>
  </div>

  <div v-else class="empty-state">
    <div class="empty-content">
      <div class="empty-icon">
        <i class="fa-solid fa-map-location-dot"></i>
      </div>
      <h2>Aún no tienes itinerarios</h2>
      <p>Crea tu primer viaje y empieza a explorar destinos increíbles</p>
      <button class="btn-create-trip" @click="createNewTrip">
        <i class="fa-solid fa-plus"></i>
        Crear mi primer viaje
      </button>
    </div>
  </div>

  <v-pagination
    v-if="pagination.totalPages"
    v-model="currentPage"
    :length="pagination.totalPages"
    @update:modelValue="handlePageChange"
    class="d-flex justify-center pagination-container"
  ></v-pagination>

  <BottomNavbar />
</template>

<script>
import BottomNavbar from '@/components/bottomnavbar.vue'
import NavTopIItinerary from '@/components/NavTopIItinerary.vue'
import { mapActions, mapGetters, mapMutations } from 'vuex'

export default {
  components: { BottomNavbar, NavTopIItinerary },

  computed: {
    ...mapGetters('trips', ['userItineraries', 'pagination', 'currentPage']),
  },

  mounted() {
    this.fetchItineraries()
  },

  methods: {
    ...mapActions('trips', {
      fetchUserItineraries: 'fetchUserItineraries',
      deleteItineraryAction: 'deleteItinerary',
    }),
    ...mapMutations('trips', {
      setCurrentItineraryId: 'setCurrentItineraryId',
      setUserItineraries: 'setUserItineraries',
      setPagination: 'setPagination',
    }),
    async fetchItineraries() {
      try {
        await this.fetchUserItineraries()
      } catch (error) {
        console.error('Error cargando itinerarios:', error)
      }
    },
    async handlePageChange(page) {
      console.log(page)
      await this.fetchUserItineraries({
        page: page - 1,
        size: this.pagination.pageSize,
      }).then((response) => {
        this.setUserItineraries(response.content)
        this.setPagination(response)
      })
    },
    formatDateRange(startDate, endDate) {
      const start = new Date(startDate)
      const end = new Date(endDate)

      const options = { day: 'numeric', month: 'long' }
      const startFormatted = start.toLocaleDateString('es-MX', options)
      const endFormatted = end.toLocaleDateString('es-MX', { ...options, year: 'numeric' })

      return `${startFormatted} al ${endFormatted}`
    },

    formatSustainability(index) {
      return (index * 100).toFixed(0) + '%'
    },

    getSustainabilityClass(index) {
      const percent = index * 100
      if (percent >= 75) return 'high'
      if (percent >= 50) return 'medium'
      return 'low'
    },

    viewItinerary(itineraryId) {
      this.setCurrentItineraryId(itineraryId)
      this.$router.push({
        name: 'description-itinerary',
      })
    },

    deleteItinerary(itineraryId) {
      this.deleteItineraryAction(itineraryId).then((response) => {
        this.$alert.success(response.message)
        this.fetchItineraries()
      })
    },

    createNewTrip() {
      this.$router.push({ name: 'new-trips' })
    },
  },
}
</script>

<style scoped>
.pagination-container {
  margin-bottom: 100px;
}

.cards-container {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 24px;
  padding: 30px 40px;
  max-width: 1200px;
  margin: 0 auto;
}

.itinerary-wrapper {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.itinerary-card {
  border-radius: 12px;
  overflow: hidden;
  transition: all 0.3s ease;
  height: 100%;
}

.itinerary-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15) !important;
}

.itinerary-carousel {
  width: 100%;
}

.image-placeholder {
  width: 100%;
  height: 250px;
  background: linear-gradient(135deg, #f0f0f0 0%, #e8e8e8 100%);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #999;
}

.image-placeholder i {
  font-size: 3rem;
  margin-bottom: 8px;
}

.image-placeholder p {
  margin: 0;
  font-size: 0.9rem;
}

.itinerary-title {
  color: #1a3c40;
  font-size: 1.25rem;
  font-weight: 600;
  margin: 0;
}

.itinerary-details {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.detail-item {
  display: flex;
  align-items: center;
  gap: 10px;
  color: #555;
  font-size: 0.95rem;
}

.detail-item i {
  color: #1a3c40;
  width: 18px;
  text-align: center;
  flex-shrink: 0;
  font-size: 1rem;
}

.sustainability-detail {
  gap: 8px;
}

.sustainability-detail i {
  color: #4caf50;
}

.sustainability-badge {
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 600;
  color: white;
  display: inline-block;
}

.sustainability-badge.high {
  background: linear-gradient(135deg, #4caf50, #66bb6a);
}

.sustainability-badge.medium {
  background: linear-gradient(135deg, #ff9800, #ffb74d);
}

.sustainability-badge.low {
  background: linear-gradient(135deg, #f44336, #ef5350);
}

.buttons-section {
  display: flex;
  gap: 10px;
  width: 100%;
}

.btn-view {
  background-color: #1a3c40;
  color: #f5f5f5;
  border: 2px solid #1a3c40;
  border-radius: 8px;
  padding: 12px 16px;
  cursor: pointer;
  transition: all 0.3s;
  font-size: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  font-weight: 500;
  flex: 1;
}

.btn-view:hover {
  background-color: #f5f5f5;
  color: #1a3c40;
}

.btn-view i {
  font-size: 12px;
}

.btn-delete {
  background-color: #f5f5f5;
  color: #f44336;
  border: 2px solid #f44336;
  border-radius: 8px;
  padding: 12px 16px;
  cursor: pointer;
  transition: all 0.3s;
  font-size: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  font-weight: 500;
  flex: 1;
}

.btn-delete:hover {
  background-color: #f44336;
  color: white;
}

.btn-delete i {
  font-size: 12px;
}

.empty-state {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: calc(100vh - 135px);
  padding: 20px;
}

.empty-content {
  text-align: center;
  max-width: 400px;
}

.empty-icon {
  font-size: 5rem;
  color: #1a3c40;
  margin-bottom: 1.5rem;
  animation: float 3s ease-in-out infinite;
}

@keyframes float {
  0%,
  100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-10px);
  }
}

.empty-content h2 {
  color: #1a3c40;
  font-size: 1.8rem;
  margin-bottom: 0.75rem;
  font-weight: 600;
}

.empty-content p {
  color: #666;
  font-size: 1rem;
  margin-bottom: 2rem;
  line-height: 1.5;
}

.btn-create-trip {
  background: linear-gradient(135deg, #1a3c40 0%, #2d5f66 100%);
  color: white;
  border: none;
  border-radius: 12px;
  padding: 14px 32px;
  font-size: 1.1rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s;
  box-shadow: 0 4px 12px rgba(26, 60, 64, 0.3);
  display: inline-flex;
  align-items: center;
  gap: 10px;
}

.btn-create-trip:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(26, 60, 64, 0.4);
}

.btn-create-trip i {
  font-size: 1.2rem;
}

/* Responsive */
@media (max-width: 1024px) {
  .cards-container {
    grid-template-columns: repeat(2, 1fr);
    gap: 20px;
    padding: 20px 20px;
  }
}

@media (max-width: 900px) {
  .cards-container {
    grid-template-columns: repeat(2, 1fr);
    gap: 16px;
    padding: 15px 15px;
  }

  .itinerary-carousel,
  .image-placeholder {
    height: 200px;
  }

  .itinerary-title {
    font-size: 1.15rem;
  }

  .detail-item {
    font-size: 0.9rem;
  }

  .buttons-section {
    gap: 8px;
  }

  .btn-view,
  .btn-delete {
    padding: 10px 12px;
    font-size: 13px;
  }

  .btn-view i,
  .btn-delete i {
    font-size: 11px;
  }
}

@media (max-width: 700px) {
  .cards-container {
    grid-template-columns: 1fr;
    gap: 14px;
    padding: 14px;
  }

  .itinerary-carousel,
  .image-placeholder {
    height: 220px;
  }

  .itinerary-title {
    font-size: 1.1rem;
    margin-bottom: 8px;
  }

  .itinerary-details {
    gap: 8px;
  }

  .detail-item {
    font-size: 0.9rem;
  }

  .buttons-section {
    gap: 10px;
  }

  .btn-view,
  .btn-delete {
    padding: 11px 14px;
    font-size: 14px;
  }

  .btn-view i,
  .btn-delete i {
    font-size: 12px;
  }
}

@media (max-width: 500px) {
  .pagination-container {
    margin-bottom: 70px;
  }
  .cards-container {
    grid-template-columns: 1fr;
    gap: 12px;
    padding: 16px;
  }

  .itinerary-card {
    border-radius: 10px;
  }

  .itinerary-carousel,
  .image-placeholder {
    height: 200px;
  }

  .itinerary-title {
    font-size: 1rem;
    margin-bottom: 6px;
  }

  .itinerary-details {
    gap: 6px;
  }

  .detail-item {
    font-size: 0.85rem;
  }

  .buttons-section {
    gap: 8px;
  }

  .btn-view,
  .btn-delete {
    padding: 10px 12px;
    font-size: 13px;
    border-radius: 6px;
  }

  .btn-view i,
  .btn-delete i {
    font-size: 11px;
  }
}
</style>
