<template>
  <div class="pb-16">
    <!-- Primera fila: hamburger menu + input + mapa distribuidos -->
    <div class="d-flex justify-content-center mt-1">
      <div class="search-container d-flex align-items-center justify-content-between bg-white rounded-pill px-4 py-2 w-75 w-md-50 w-lg-60 mt-2" style="max-width: 1100px; min-width: 420px;">
        <hamburgermenu />
        <div class="input-container flex-grow-1 mx-3">
          <Inputexplore
            :api-key="googleApiKey"
            @place-selected="handlePlaceSelected"
            @search-error="handleSearchError"
          />
        </div>
        <div class="d-flex flex-column align-items-center map-section">
          <i class="fa-solid fa-location-dot map-icon"></i>
          <span class="map-text">Mapa</span>
        </div>
      </div>
    </div>
    
    <!-- TopNavbar centrado debajo -->
    <topnavbar @filter-change="handleFilterChange" />
    <hr>
   
    <!-- Contenido -->
    <div class="p-4 content-section">
      <h5 class="mb-4 text-dark">Lugares Recomendados</h5>
      
      <!-- Loading spinner -->
      <div v-if="loading" class="text-center py-4">
        <div class="spinner-border text-success" role="status">
          <span class="visually-hidden">Cargando...</span>
        </div>
        <p class="mt-2 text-muted">Buscando lugares cerca de ti...</p>
      </div>
      
      <!-- Error message -->
      <div v-if="error" class="alert alert-warning" role="alert">
        <i class="fa-solid fa-exclamation-triangle me-2"></i>
        {{ error }}
        <button @click="retrySearch" class="btn btn-sm btn-outline-warning ms-2">
          <i class="fa-solid fa-refresh"></i> Reintentar
        </button>
      </div>
      
      <!-- Lista vertical de lugares -->
      <div v-if="!loading && places.length > 0" class="places-grid">
        <div 
          v-for="place in places" 
          :key="place.place_id"
          class="place-card-vertical"
          @click="selectPlace(place)"
        >
          <!-- Imagen del lugar -->
          <div class="place-image-container-vertical">
            <img 
              :src="getPlacePhoto(place)" 
              :alt="place.name"
              class="place-image-vertical"
              @error="handleImageError"
            >
            <div class="favorite-btn" @click.stop="toggleFavorite(place)">
              <i :class="isFavorite(place.place_id) ? 'fa-solid fa-heart text-danger' : 'fa-regular fa-heart text-white'"></i>
            </div>
            <div class="rating-badge" v-if="place.rating">
              <i class="fa-solid fa-star text-warning"></i>
              <span class="rating-text">{{ place.rating.toFixed(1) }}</span>
            </div>

          </div>
          
          <!-- Información del lugar -->
          <div class="place-info-vertical">
            <h6 class="place-name">{{ place.name }}</h6>
            <p class="place-address">
              <i class="fa-solid fa-location-dot me-1"></i>
              {{ getShortAddress(place.vicinity || place.formatted_address) }}
            </p>
            <div class="place-details">
              <span v-if="place.price_level" class="price-level">
                {{ getPriceLevel(place.price_level) }}
              </span>
              <span v-if="place.types && place.types.length > 0" class="place-type">
                {{ getPlaceType(place.types[0]) }}
              </span>
              <span v-if="place.user_ratings_total" class="reviews-count">
                {{ place.user_ratings_total }} reseñas
              </span>
            </div>
          </div>
        </div>
      </div>
      
      <!-- No results message -->
      <div v-if="!loading && places.length === 0 && !error" class="text-center py-4">
        <i class="fa-solid fa-map-marker-alt text-muted mb-3" style="font-size: 3rem;"></i>
        <h6 class="text-muted">No se encontraron lugares</h6>
        <p class="text-muted">Intenta permitir la geolocalización o busca algo específico.</p>
        <button @click="retrySearch" class="btn btn-outline-success">
          <i class="fa-solid fa-refresh me-2"></i>Buscar de nuevo
        </button>
      </div>
    </div>
   
    <!-- Bottom navbar -->
    <BottomNavbar />
  </div>
</template>

<script>
import BottomNavbar from "@/components/bottomnavbar.vue";
import hamburgermenu from "@/components/hamburgermenu.vue";
import topnavbar from "@/components/topnavbar.vue";
import Inputexplore from "@/components/inputexplore.vue";

export default {
  components: { BottomNavbar, hamburgermenu, topnavbar, Inputexplore },
  data() {
    return {
      googleApiKey: '',
      places: [],
      loading: true,
      error: null,
      userLocation: null,
      map: null,
      placesService: null,
      favorites: JSON.parse(localStorage.getItem('favoritePlaces') || '[]'),
      searchQueries: [
        { type: 'restaurant', name: 'Restaurantes' },
        { type: 'tourist_attraction', name: 'Atracciones' },
        { type: 'shopping_mall', name: 'Centros Comerciales' },
        { type: 'park', name: 'Parques' },
        { type: 'cafe', name: 'Cafeterías' },
        { type: 'museum', name: 'Museos' },
        { type: 'gym', name: 'Gimnasios' },
        { type: 'hospital', name: 'Hospitales' },
        { type: 'gas_station', name: 'Gasolineras' },
        { type: 'pharmacy', name: 'Farmacias' }
      ]
    }
  },
  async mounted() {
    await this.initializeGoogleMaps();
    await this.getUserLocation();
    await this.loadRecommendedPlaces();
  },
  methods: {
    async initializeGoogleMaps() {
      try {
        // Esperar a que Google Maps esté disponible
        if (typeof google === 'undefined') {
          await this.loadGoogleMapsScript();
        }
        
        // Crear un mapa oculto para usar con Places Service
        const mapDiv = document.createElement('div');
        this.map = new google.maps.Map(mapDiv, {
          center: { lat: 19.4326, lng: -99.1332 }, // Ciudad de México por defecto
          zoom: 13
        });
        
        this.placesService = new google.maps.places.PlacesService(this.map);
        
      } catch (error) {
        console.error('Error inicializando Google Maps:', error);
        this.error = 'Error inicializando Google Maps';
      }
    },

    loadGoogleMapsScript() {
      return new Promise((resolve, reject) => {
        if (typeof google !== 'undefined') {
          resolve();
          return;
        }

        const script = document.createElement('script');
        script.src = `https://maps.googleapis.com/maps/api/js?key=${this.googleApiKey}&libraries=places&language=es&region=MX`;
        script.async = true;
        script.defer = true;
        
        script.onload = () => resolve();
        script.onerror = () => reject(new Error('Error cargando Google Maps'));
        
        document.head.appendChild(script);
      });
    },

    async getUserLocation() {
      try {
        if (!navigator.geolocation) {
          throw new Error('Geolocalización no disponible');
        }

        const position = await new Promise((resolve, reject) => {
          navigator.geolocation.getCurrentPosition(
            resolve, 
            reject, 
            {
              timeout: 15000, // Aumentado a 15 segundos
              enableHighAccuracy: false, // Cambiado a false para móviles
              maximumAge: 600000 // 10 minutos
            }
          );
        });
        
        this.userLocation = {
          lat: position.coords.latitude,
          lng: position.coords.longitude
        };

        console.log('Ubicación obtenida:', this.userLocation);

        // Actualizar el centro del mapa
        if (this.map) {
          this.map.setCenter(this.userLocation);
        }
        
      } catch (error) {
        console.log('Error obteniendo ubicación del usuario:', error);
        // Usar ubicación por defecto (Ciudad de México)
        this.userLocation = { lat: 19.4326, lng: -99.1332 };
        
        if (error.code === 1) {
          console.log('Usuario denegó el acceso a la ubicación');
        } else if (error.code === 2) {
          console.log('Ubicación no disponible');
        } else if (error.code === 3) {
          console.log('Timeout obteniendo ubicación');
        }
      }
    },

    async loadRecommendedPlaces() {
      if (!this.placesService) {
        this.error = 'Error inicializando servicios de ubicación';
        this.loading = false;
        return;
      }
      
      console.log('Iniciando búsqueda de lugares...');
      console.log('Ubicación del usuario:', this.userLocation);
      
      this.loading = true;
      this.error = null;
      
      try {
        const allPlaces = [];
        
        // Buscar diferentes tipos de lugares con menos categorías para móvil
        const searchLimit = window.innerWidth < 768 ? 3 : 4;
        
        for (let i = 0; i < Math.min(this.searchQueries.length, searchLimit); i++) {
          const query = this.searchQueries[i];
          console.log(`Buscando ${query.type}...`);
          
          const places = await this.searchNearbyPlaces(query.type);
          console.log(`Encontrados ${places.length} lugares de tipo ${query.type}`);
          
          // Añadir máximo 3 lugares por categoría
          if (places.length > 0) {
            allPlaces.push(...places.slice(0, 3));
          }
          
          // Pausa más larga para móviles
          const delay = window.innerWidth < 768 ? 800 : 500;
          await new Promise(resolve => setTimeout(resolve, delay));
        }
        
        console.log(`Total de lugares encontrados: ${allPlaces.length}`);
        
        // Remover duplicados y ordenar por rating
        const uniquePlaces = this.removeDuplicates(allPlaces);
        const sortedPlaces = uniquePlaces.sort((a, b) => (b.rating || 0) - (a.rating || 0));
        
        this.places = sortedPlaces.slice(0, 12); // Mostrar máximo 12 lugares
        
        console.log(`Lugares finales: ${this.places.length}`);
        
        if (this.places.length === 0) {
          this.error = 'No se encontraron lugares cercanos. Intenta con otra ubicación.';
        }
        
      } catch (error) {
        console.error('Error cargando lugares:', error);
        this.error = 'Error al cargar lugares recomendados';
      } finally {
        this.loading = false;
      }
    },

    searchNearbyPlaces(type) {
      return new Promise((resolve, reject) => {
        if (!this.userLocation) {
          console.error('No hay ubicación del usuario disponible');
          resolve([]);
          return;
        }

        const request = {
          location: new google.maps.LatLng(this.userLocation.lat, this.userLocation.lng),
          radius: 3000, // Reducido a 3km para móviles
          type: type,
          openNow: false
        };

        console.log('Realizando búsqueda con:', request);

        this.placesService.nearbySearch(request, (results, status) => {
          console.log(`Status de búsqueda para ${type}:`, status);
          
          if (status === google.maps.places.PlacesServiceStatus.OK) {
            console.log(`Resultados para ${type}:`, results?.length || 0);
            // Filtrar lugares que tengan información mínima necesaria
            const validPlaces = results.filter(place => 
              place.name && 
              place.place_id && 
              place.geometry && 
              place.business_status !== 'CLOSED_PERMANENTLY'
            );
            resolve(validPlaces || []);
          } else if (status === google.maps.places.PlacesServiceStatus.ZERO_RESULTS) {
            console.log(`Sin resultados para ${type}`);
            resolve([]);
          } else {
            console.warn(`Places search failed for ${type}:`, status);
            resolve([]);
          }
        });
      });
    },

    removeDuplicates(places) {
      const seen = new Set();
      return places.filter(place => {
        if (seen.has(place.place_id)) {
          return false;
        }
        seen.add(place.place_id);
        return true;
      });
    },

    getPlacePhoto(place) {
      if (place.photos && place.photos.length > 0) {
        return place.photos[0].getUrl({
          maxWidth: 300,
          maxHeight: 200
        });
      }
      // Imagen por defecto basada en el tipo de lugar
      return this.getDefaultImage(place.types ? place.types[0] : 'establishment');
    },

    getDefaultImage(type) {
      const defaultImages = {
        restaurant: 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMzAwIiBoZWlnaHQ9IjIwMCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj48cmVjdCB3aWR0aD0iMzAwIiBoZWlnaHQ9IjIwMCIgZmlsbD0iI2ZmZjRlNiIvPjx0ZXh0IHg9IjE1MCIgeT0iMTAwIiBmb250LWZhbWlseT0iQXJpYWwsIHNhbnMtc2VyaWYiIGZvbnQtc2l6ZT0iNDAiIGZpbGw9IiNmZjk1MDAiIHRleHQtYW5jaG9yPSJtaWRkbGUiPvCfjY08L3RleHQ+PC9zdmc+',
        cafe: 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMzAwIiBoZWlnaHQ9IjIwMCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj48cmVjdCB3aWR0aD0iMzAwIiBoZWlnaHQ9IjIwMCIgZmlsbD0iI2Y5ZjJlNyIvPjx0ZXh0IHg9IjE1MCIgeT0iMTAwIiBmb250LWZhbWlseT0iQXJpYWwsIHNhbnMtc2VyaWYiIGZvbnQtc2l6ZT0iNDAiIGZpbGw9IiM4YjQ1MTMiIHRleHQtYW5jaG9yPSJtaWRkbGUiPuKYlTwvdGV4dD48L3N2Zz4=',
        park: 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMzAwIiBoZWlnaHQ9IjIwMCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj48cmVjdCB3aWR0aD0iMzAwIiBoZWlnaHQ9IjIwMCIgZmlsbD0iI2Y2ZmZmNiIvPjx0ZXh0IHg9IjE1MCIgeT0iMTAwIiBmb250LWZhbWlseT0iQXJpYWwsIHNhbnMtc2VyaWYiIGZvbnQtc2l6ZT0iNDAiIGZpbGw9IiMyMjc3MzMiIHRleHQtYW5jaG9yPSJtaWRkbGUiPvCfjLM8L3RleHQ+PC9zdmc+',
        shopping_mall: 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMzAwIiBoZWlnaHQ9IjIwMCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj48cmVjdCB3aWR0aD0iMzAwIiBoZWlnaHQ9IjIwMCIgZmlsbD0iI2ZmZjJmOCIvPjx0ZXh0IHg9IjE1MCIgeT0iMTAwIiBmb250LWZhbWlseT0iQXJpYWwsIHNhbnMtc2VyaWYiIGZvbnQtc2l6ZT0iNDAiIGZpbGw9IiNkOTQ2ZWYiIHRleHQtYW5jaG9yPSJtaWRkbGUiPvCfmr88L3RleHQ+PC9zdmc+'
      };
      
      return defaultImages[type] || 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMzAwIiBoZWlnaHQ9IjIwMCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj48cmVjdCB3aWR0aD0iMzAwIiBoZWlnaHQ9IjIwMCIgZmlsbD0iI2Y4ZjlmYSIvPjx0ZXh0IHg9IjE1MCIgeT0iMTAwIiBmb250LWZhbWlseT0iQXJpYWwsIHNhbnMtc2VyaWYiIGZvbnQtc2l6ZT0iMTQiIGZpbGw9IiM2Yzc1N2QiIHRleHQtYW5jaG9yPSJtaWRkbGUiPkx1Z2FyPC90ZXh0Pjwvc3ZnPg==';
    },

    handleImageError(event) {
      const place = this.places.find(p => p.name === event.target.alt);
      if (place && place.types) {
        event.target.src = this.getDefaultImage(place.types[0]);
      } else {
        event.target.src = this.getDefaultImage('establishment');
      }
    },

    getShortAddress(address) {
      if (!address) return 'Dirección no disponible';
      const parts = address.split(',');
      return parts.slice(0, 2).join(',').trim();
    },

    getPriceLevel(level) {
      const prices = ['Gratis', '$', '$$', '$$$', '$$$$'];
      return prices[level] || '';
    },

    getPlaceType(type) {
      const typeTranslations = {
        restaurant: 'Restaurante',
        cafe: 'Café',
        tourist_attraction: 'Atracción',
        shopping_mall: 'Centro Comercial',
        park: 'Parque',
        museum: 'Museo',
        bar: 'Bar',
        gym: 'Gimnasio',
        hospital: 'Hospital',
        gas_station: 'Gasolinera',
        pharmacy: 'Farmacia',
        establishment: 'Establecimiento'
      };
      return typeTranslations[type] || type.replace(/_/g, ' ');
    },

    async retrySearch() {
      await this.getUserLocation();
      await this.loadRecommendedPlaces();
    },

    selectPlace(place) {
      console.log('Lugar seleccionado:', place);
      this.$emit('place-selected', place);
    },

    handleFilterChange(filter) {
      console.log('Filtro seleccionado:', filter);
      // Aquí puedes filtrar los lugares basándote en el filtro seleccionado
    },

    handlePlaceSelected(data) {
      console.log('Lugar seleccionado desde input:', data.place);
      if (data.place.geometry && data.place.geometry.location) {
        console.log('Coordenadas:', data.place.geometry.location);
      }
    },

    handleSearchError(error) {
      console.error('Error en búsqueda:', error);
      this.error = 'Error en la búsqueda: ' + error.message;
    },

    toggleFavorite(place) {
      const placeId = place.place_id;
      const index = this.favorites.findIndex(fav => fav.place_id === placeId);
      
      if (index > -1) {
        // Remover de favoritos
        this.favorites.splice(index, 1);
      } else {
        // Añadir a favoritos
        const favoritePlace = {
          place_id: placeId,
          name: place.name,
          rating: place.rating,
          photos: place.photos ? [place.photos[0].getUrl({ maxWidth: 300, maxHeight: 200 })] : null,
          vicinity: place.vicinity || place.formatted_address,
          types: place.types,
          price_level: place.price_level,
          user_ratings_total: place.user_ratings_total,
          business_status: place.business_status,
          added_date: new Date().toISOString()
        };
        this.favorites.push(favoritePlace);
      }
      
      localStorage.setItem('favoritePlaces', JSON.stringify(this.favorites));
    },

    isFavorite(placeId) {
      return this.favorites.some(fav => fav.place_id === placeId);
    }
  }
};
</script>

<style scoped>
.search-container {
  gap: 0;
}

.map-section {
  color: #1B515E;
}

.map-icon {
  font-size: 1.2rem;
}

.map-text {
  font-size: 14px;
}

.input-container :deep(.form-control):focus::after {
  color: #1B515E;
}

.content-section {
  height: calc(100vh - 200px);
  overflow-y: auto;
}

/* Estilos para la grilla vertical de lugares */
.places-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 1rem;
  padding: 0.5rem 0;
  height: 100%;
}

.place-card-vertical {
  display: flex;
  flex-direction: column;
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
  cursor: pointer;
  min-height: 300px;
}

.place-card-vertical:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.place-image-container-vertical {
  position: relative;
  width: 100%;
  height: 180px;
  overflow: hidden;
}

.place-image-vertical {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.place-card-vertical:hover .place-image-vertical {
  transform: scale(1.05);
}

.favorite-btn {
  position: absolute;
  top: 8px;
  left: 8px;
  background: rgba(0, 0, 0, 0.5);
  width: 36px;
  height: 36px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.2s ease;
  backdrop-filter: blur(10px);
  z-index: 10;
}

.favorite-btn:hover {
  background: rgba(0, 0, 0, 0.7);
  transform: scale(1.1);
}

.favorite-btn i {
  font-size: 1rem;
  transition: all 0.2s ease;
}

.rating-badge {
  position: absolute;
  top: 8px;
  right: 8px;
  background: rgba(255, 255, 255, 0.95);
  padding: 4px 8px;
  border-radius: 20px;
  display: flex;
  align-items: center;
  gap: 2px;
  font-size: 0.75rem;
  font-weight: 600;
  backdrop-filter: blur(10px);
}

.status-badge {
  position: absolute;
  top: 50px;
  right: 8px;
  background: rgba(255, 255, 255, 0.95);
  padding: 4px 6px;
  border-radius: 20px;
  backdrop-filter: blur(10px);
}

.rating-text {
  color: #333;
}

.place-info-vertical {
  padding: 1rem;
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.place-name {
  font-size: 1rem;
  font-weight: 600;
  margin-bottom: 0.5rem;
  color: #333;
  line-height: 1.2;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.place-address {
  font-size: 0.85rem;
  color: #666;
  margin-bottom: 0.75rem;
  line-height: 1.3;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.place-details {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.price-level {
  font-size: 0.8rem;
  color: #35aa06;
  font-weight: 600;
}

.place-type {
  font-size: 0.75rem;
  background: #f8f9fa;
  color: #666;
  padding: 2px 8px;
  border-radius: 12px;
  text-transform: capitalize;
}

.reviews-count {
  font-size: 0.7rem;
  color: #999;
}

hr {
  margin-top: 0;
  height: 2px;
  background-color: #35aa06;
  border-color: #b0d4a1;
}

/* Responsive Design */
@media (max-width: 480px) {
  .places-grid {
    grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
    gap: 0.75rem;
  }
  
  .place-card-vertical {
    min-height: 260px;
  }
  
  .place-image-container-vertical {
    height: 140px;
  }
  
  .place-info-vertical {
    padding: 0.75rem;
  }
  
  .place-name {
    font-size: 0.9rem;
  }
  
  .place-address {
    font-size: 0.8rem;
  }
  
  .favorite-btn {
    width: 32px;
    height: 32px;
  }
  
  .favorite-btn i {
    font-size: 0.9rem;
  }
}

@media (min-width: 481px) and (max-width: 768px) {
  .places-grid {
    grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  }
  
  .place-card-vertical {
    min-height: 300px;
  }
  
  .place-image-container-vertical {
    height: 160px;
  }
}

@media (min-width: 769px) and (max-width: 1024px) {
  .places-grid {
    grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  }
  
  .place-card-vertical {
    min-height: 320px;
  }
  
  .place-image-container-vertical {
    height: 180px;
  }
  
  .map-text {
    font-size: 16px;
  }
 
  .map-icon {
    font-size: 1.4rem;
  }
}

@media (min-width: 1025px) and (max-width: 1440px) {
  .search-container {
    gap: 100px;
  }
 
  .input-container {
    margin: 0 !important;
  }
 
  .map-text {
    font-size: 18px;
  }
 
  .map-icon {
    font-size: 1.6rem;
  }
  
  .places-grid {
    grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
    gap: 1.25rem;
  }
  
  .place-card-vertical {
    min-height: 340px;
  }
  
  .place-image-container-vertical {
    height: 200px;
  }
}

@media (min-width: 1441px) {
  .search-container {
    gap: 100px;
  }
 
  .input-container {
    margin: 0 !important;
  }
 
  .map-text {
    font-size: 20px;
  }
 
  .map-icon {
    font-size: 1.8rem;
  }
  
  .places-grid {
    grid-template-columns: repeat(auto-fill, minmax(340px, 1fr));
    gap: 1.5rem;
  }
  
  .place-card-vertical {
    min-height: 360px;
  }
  
  .place-image-container-vertical {
    height: 220px;
  }
}
</style>