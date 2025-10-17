<!-- eslint-disable vue/multi-word-component-names -->
<template>
  <NavTopIItinerary />

  <div style="padding-top: 55px"></div>

  <div v-if="userItineraries.length > 0" class="cards-container">
    <div v-for="itinerary in userItineraries" :key="itinerary.id" class="itinerary-card">
      <v-carousel
        v-if="itinerary.images && itinerary.images.length > 0"
        class="itinerary-carousel"
        height="200"
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

      <div class="itinerary-info">
        <h3>{{ itinerary.tripTitle }}</h3>
        <ul>
          <li>
            <i class="fa-regular fa-calendar"></i>
            {{ formatDateRange(itinerary.startDate, itinerary.endDate) }}
          </li>
          <li>
            <i class="fa-solid fa-location-dot"></i>
            {{ itinerary.totalPlaces }} lugares para visitar
          </li>
          <li v-if="itinerary.averageSustainableIndex" class="sustainability-item">
            <i class="fa-solid fa-leaf"></i>
            <div
              class="sustainability-badge"
              :class="getSustainabilityClass(itinerary.averageSustainableIndex)"
            >
              {{ formatSustainability(itinerary.averageSustainableIndex) }}
            </div>
          </li>
        </ul>
        <button class="btn-view" @click="viewItinerary(itinerary.id)">
          Ver itinerario
          <i class="fa-solid fa-arrow-right"></i>
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

  <BottomNavbar />
</template>

<script>
import BottomNavbar from '@/components/bottomnavbar.vue'
import NavTopIItinerary from '@/components/NavTopIItinerary.vue'
import { mapActions, mapGetters, mapMutations } from 'vuex'

export default {
  components: { BottomNavbar, NavTopIItinerary },

  computed: {
    ...mapGetters('trips', ['userItineraries']),
  },

  mounted() {
    this.fetchItineraries()
  },

  methods: {
    ...mapActions('trips', {
      fetchUserItineraries: 'fetchUserItineraries',
    }),
    ...mapMutations('trips', {
      setCurrentItineraryId: 'setCurrentItineraryId',
    }),

    async fetchItineraries() {
      try {
        await this.fetchUserItineraries()
      } catch (error) {
        console.error('Error cargando itinerarios:', error)
      }
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

    createNewTrip() {
      this.$router.push({ name: 'new-trips' })
    },
  },
}
</script>

<style scoped>
.cards-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
  padding: 20px;
  padding-bottom: 80px;
}

.itinerary-card {
  display: flex;
  background: #fff;
  border-radius: 15px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  width: 100%;
  max-width: 500px;
  transition:
    transform 0.3s,
    box-shadow 0.3s;
}

.itinerary-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.15);
}

.itinerary-carousel {
  width: 40%;
  min-height: 200px;
}

.image-placeholder {
  width: 40%;
  min-height: 200px;
  background: #f0f0f0;
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

.itinerary-info {
  padding: 15px;
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.itinerary-info h3 {
  color: #1a3c40;
  margin-bottom: 12px;
  font-size: 1.1rem;
  font-weight: 600;
}

.itinerary-info ul {
  list-style: none;
  padding: 0;
  margin: 0 0 15px 0;
  flex: 1;
}

.itinerary-info li {
  color: #555;
  font-size: 14px;
  margin-bottom: 8px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.itinerary-info li i {
  color: #1a3c40;
  width: 16px;
  text-align: center;
}

.sustainability-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.sustainability-item i {
  color: #4caf50;
}

.sustainability-badge {
  padding: 4px 10px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 600;
  color: white;
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

.btn-view {
  background-color: #1a3c40;
  color: white;
  border: none;
  border-radius: 8px;
  padding: 10px 16px;
  cursor: pointer;
  transition: all 0.3s;
  font-size: 14px;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  font-weight: 500;
  margin-top: auto;
}

.btn-view:hover {
  background-color: #0e2325;
  transform: translateX(2px);
}

.btn-view i {
  font-size: 12px;
  transition: transform 0.3s;
}

.btn-view:hover i {
  transform: translateX(4px);
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

@media (min-width: 768px) {
  .cards-container {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 20px;
    padding: 30px 20px;
    padding-bottom: 100px;
    max-width: 1200px;
    margin: 0 auto;
  }

  .itinerary-card {
    max-width: 100%;
  }
}

@media (min-width: 1024px) {
  .cards-container {
    gap: 25px;
    padding: 40px 40px;
    padding-bottom: 120px;
    max-width: 1400px;
  }

  .itinerary-carousel {
    width: 280px;
    min-height: 250px;
  }

  .image-placeholder {
    width: 280px;
    min-height: 250px;
  }
}

@media (max-width: 480px) {
  .itinerary-card {
    flex-direction: column;
  }

  .itinerary-carousel,
  .image-placeholder {
    width: 100%;
    height: 180px;
    min-height: unset;
  }

  .itinerary-info {
    min-height: 180px;
  }
}
</style>