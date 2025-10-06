<template>
  <div class="main-container">
    <SearchHotels @search-hotels="handleSearch" @place-selected="handlePlaceSelected" />
    
    <div class="hotels-container">
      <div class="hotels-list">
        <PlaceCard
          v-for="hotel in hotels"
          :key="hotel.id"
          :place="hotel"
          :is-favorite="isFavorite(hotel.id)"
          @select-place="handleSelectHotel"
          @toggle-favorite="handleToggleFavorite"
        />
        
        <div v-if="hotels.length === 0 && !isLoading" class="no-results">
          <i class="mdi mdi-hotel no-results-icon"></i>
          <p class="no-results-text">No se encontraron hoteles</p>
          <p class="no-results-subtext">Intenta buscar en otra ubicación</p>
        </div>
        
        <div v-if="isLoading" class="loading-container">
          <div class="loading-spinner-large"></div>
          <p class="loading-text">Buscando hoteles...</p>
        </div>
      </div>
    </div>
    
    <NavButtom />
  </div>
</template>

<script>
import NavButtom from '@/components/NavButtom.vue'
import SearchHotels from '@/components/SearchHotels.vue'
import PlaceCard from '@/components/placecard.vue'

export default {
  components: { 
    NavButtom, 
    SearchHotels,
    PlaceCard 
  },
  data() {
    return {
      hotels: [],
      isLoading: false,
      favorites: []
    }
  },
  mounted() {
    this.loadHotels()
    this.loadFavorites()
  },
  methods: {
    async loadHotels() {
      this.isLoading = true
      try {
        const response = await fetch('')
        this.hotels = await response.json()
        this.isLoading = false
      } catch (error) {
        console.error('Error loading hotels:', error)
        this.isLoading = false
      }
    },
    
    handleSearch(query) {
      console.log('Searching for:', query)
      this.loadHotels()
    },
    
    handlePlaceSelected(place) {
      console.log('Place selected:', place)
    },
    
    handleSelectHotel(hotel) {
      console.log('Hotel selected:', hotel)
    },
    
    handleToggleFavorite(hotel) {
      const index = this.favorites.indexOf(hotel.id)
      if (index > -1) {
        this.favorites.splice(index, 1)
      } else {
        this.favorites.push(hotel.id)
      }
      this.saveFavorites()
    },
    
    isFavorite(hotelId) {
      return this.favorites.includes(hotelId)
    },
    
    loadFavorites() {
      const saved = localStorage.getItem('favoriteHotels')
      if (saved) {
        this.favorites = JSON.parse(saved)
      }
    },
    
    saveFavorites() {
      localStorage.setItem('favoriteHotels', JSON.stringify(this.favorites))
    }
  }
}
</script>

<style scoped>
.main-container {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding-top: 70px;
  padding-bottom: 80px;
}

.hotels-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px 15px;
}

.hotels-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.no-results {
  grid-column: 1 / -1;
  text-align: center;
  padding: 60px 20px;
}

.no-results-icon {
  font-size: 4rem;
  color: #ccc;
  margin-bottom: 20px;
}

.no-results-text {
  font-size: 1.2rem;
  font-weight: 600;
  color: #666;
  margin-bottom: 8px;
}

.no-results-subtext {
  font-size: 0.95rem;
  color: #999;
}

.loading-container {
  grid-column: 1 / -1;
  text-align: center;
  padding: 60px 20px;
}

.loading-spinner-large {
  width: 50px;
  height: 50px;
  border: 4px solid rgba(53, 170, 6, 0.2);
  border-top-color: #35aa06;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto 20px;
}

.loading-text {
  font-size: 1rem;
  color: #666;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

@media (max-width: 768px) {
  .main-container {
    padding-top: 65px;
  }
  
  .hotels-container {
    padding: 15px 12px;
  }
  
  .hotels-list {
    grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
    gap: 16px;
  }
}

@media (max-width: 576px) {
  .main-container {
    padding-top: 60px;
    padding-bottom: 75px;
  }
  
  .hotels-container {
    padding: 12px 10px;
  }
  
  .hotels-list {
    grid-template-columns: 1fr;
    gap: 12px;
  }
}
</style>