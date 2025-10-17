<template>
  <div class="itinerary-container">
    <!-- Navbar de descripción del viaje -->
    <NavTopIDescription 
      :travelTitle="travelData.title"
      :placesCount="`[${travelData.places.length} lugares]`"
      :travelState="travelData.destination_state"
      :dateRange="`${travelData.start_date} - ${travelData.end_date}`"
      :environmentalScore="travelData.sustainability_index"
      @edit-travel="handleEdit"
      @show-environmental-info="handleEnvironmentalInfo"
    />

    <!-- Espaciador adicional - NO QUITAR -->
    <div class="main-spacer"></div>

    <!-- Contenedor principal del itinerario -->
    <div class="itinerary-content container py-4">
      <!-- Itinerario por días -->
      <div 
        v-for="(day, dayIndex) in itineraryByDays" 
        :key="dayIndex" 
        class="day-section mb-5"
      >
        <!-- Encabezado del día -->
        <div class="day-header bg-white">
          <h2 class="day-title mb-2">
            <i class="fa-solid fa-calendar-day me-2"></i>
            Día {{ dayIndex + 1 }}
          </h2>
          <p class="day-date text-muted mb-0">
            {{ day.date }} • {{ day.places.length }} lugares
          </p>
        </div>

        <!-- Grid de lugares del día -->
        <div class="places-grid">
          <PlaceCard
            v-for="place in day.places"
            :key="place.id"
            :place="place"
            :is-favorite="isFavorite(place.id)"
            :logo-url="logoUrl"
            @select-place="selectPlace"
            @toggle-favorite="toggleFavorite"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import NavTopIDescription from "@/components/NavTopDescription.vue";
import PlaceCard from '@/components/placecard.vue';

export default {
  name: "ItineraryView",
  components: { 
    NavTopIDescription,
    PlaceCard
  },
  data() {
    return {
      travelData: {
        title: "Aventura en Oaxaca", 
        places: [],
        destination_state: "Oaxaca", 
        start_date: "15 Mar 2025", 
        end_date: "18 Mar 2025", 
        sustainability_index: 0.8
      },
      logoUrl: "https://www.turismomexico.es/wp-content/uploads/2015/07/chichen_itza.jpg",
      favorites: [1, 5, 12, 18],
      places: []
    };
  },
  computed: {
    itineraryByDays() {
      const days = [];
      const placesPerDay = 5;
      const dates = ['15 Mar 2025', '16 Mar 2025', '17 Mar 2025', '18 Mar 2025'];
      
      for (let i = 0; i < 4; i++) {
        const startIndex = i * placesPerDay;
        const endIndex = startIndex + placesPerDay;
        
        days.push({
          date: dates[i],
          places: this.places.slice(startIndex, endIndex)
        });
      }
      
      return days;
    }
  },
  created() {
    // TODO: CONECTAR CON BACKEND - Reemplazar datos de ejemplo con llamada a API
    // this.fetchItineraryData();
    
    // Datos de ejemplo (eliminar cuando conectes con backend)
    this.generateSamplePlaces();
    this.travelData.places = this.places;
  },
  methods: {
    // async fetchItineraryData() {
    //   try {
    //     // LLAMADA A TU API - Obtener datos del itinerario
    //     const travelId = this.$route.params.id; // o desde donde obtengas el ID
    //     const response = await fetch(`TU_API_URL/itinerary/${travelId}`);
    //     const data = await response.json();
    //     
    //     // Asignar datos reales del backend
    //     this.travelData = {
    //       title: data.title,
    //       places: data.places,
    //       destination_state: data.destination_state,
    //       start_date: data.start_date,
    //       end_date: data.end_date,
    //       sustainability_index: data.sustainability_index
    //     };
    //     
    //     this.places = data.places;
    //     
    //     // Si los favoritos vienen del backend
    //     // this.favorites = data.user_favorites || [];
    //   } catch (error) {
    //     console.error('Error al obtener datos del itinerario:', error);
    //   }
    // },
    
    generateSamplePlaces() {
      const categories = ['Museo', 'Restaurante', 'Parque', 'Mirador', 'Playa', 'Mercado', 'Ruinas', 'Café'];
      const cities = ['Oaxaca de Juárez', 'Monte Albán', 'Hierve el Agua', 'Mitla', 'Puerto Escondido'];
      
      for (let i = 1; i <= 20; i++) {
        const category = categories[Math.floor(Math.random() * categories.length)];
        const city = cities[Math.floor(Math.random() * cities.length)];
        
        this.places.push({
          id: i,
          name: `${category} ${i}`,
          description: `Descripción del lugar número ${i}. Un hermoso sitio para visitar en tu viaje.`,
          category: category,
          address: `Calle Principal #${i * 10}, ${city}, Oaxaca`,
          rating: parseFloat((3.5 + Math.random() * 1.5).toFixed(1)),
          price: ['$', '$$', '$$$'][Math.floor(Math.random() * 3)],
          duration: `${Math.floor(Math.random() * 3) + 1} hrs`,
          image: `https://via.placeholder.com/400x250?text=${category}+${i}`,
          openingHours: '9:00 AM - 6:00 PM'
        });
      }
    },
    
    isFavorite(placeId) {
      return this.favorites.includes(placeId);
    },
    
    selectPlace(place) {
      console.log('Lugar seleccionado:', place);
      // TODO: CONECTAR CON BACKEND - Navegar a detalle o realizar acción
    },
    
    // ========================================================================
    // TODO: CONECTAR CON BACKEND - Guardar/eliminar favoritos en la base de datos
    // ========================================================================
    async toggleFavorite(placeId) {
      const index = this.favorites.indexOf(placeId);
      
      // DESCOMENTA PARA CONECTAR CON BACKEND:
      // try {
      //   if (index > -1) {
      //     // Eliminar favorito en backend
      //     await fetch(`TU_API_URL/favorites/${placeId}`, { 
      //       method: 'DELETE',
      //       headers: {
      //         'Authorization': `Bearer ${token}`, // Si usas autenticación
      //         'Content-Type': 'application/json'
      //       }
      //     });
      //     this.favorites.splice(index, 1);
      //   } else {
      //     // Agregar favorito en backend
      //     await fetch(`TU_API_URL/favorites`, {
      //       method: 'POST',
      //       body: JSON.stringify({ placeId }),
      //       headers: {
      //         'Authorization': `Bearer ${token}`, // Si usas autenticación
      //         'Content-Type': 'application/json'
      //       }
      //     });
      //     this.favorites.push(placeId);
      //   }
      // } catch (error) {
      //   console.error('Error al actualizar favoritos:', error);
      //   // Revertir cambio en caso de error
      //   if (index > -1) {
      //     this.favorites.push(placeId);
      //   } else {
      //     const newIndex = this.favorites.indexOf(placeId);
      //     this.favorites.splice(newIndex, 1);
      //   }
      // }
      
      // Implementación temporal local (eliminar cuando conectes con backend)
      if (index > -1) {
        this.favorites.splice(index, 1);
        console.log(`Lugar ${placeId} eliminado de favoritos`);
      } else {
        this.favorites.push(placeId);
        console.log(`Lugar ${placeId} agregado a favoritos`);
      }
    },
    
    handleEdit() {
      console.log('Editar viaje');
      // TODO: CONECTAR CON BACKEND - Navegar a edición o abrir modal
    },
    
    handleEnvironmentalInfo(score) {
      console.log('Mostrar información ambiental:', score);
      alert(`Información de sostenibilidad: ${score} puntos`);
      // TODO: CONECTAR CON BACKEND - Obtener información detallada de sostenibilidad
    }
  }
};
</script>

<style scoped>
.itinerary-container {
  min-height: 100vh;
  background-color: #f8f9fa;
  padding-top: 100px;
}


.navbar-spacer {
  height: 140px;
}

.itinerary-content {
  max-width: 1400px;
  margin: 0 auto;
}

.day-section {
  background: transparent;
  border-radius: 0;
  padding: 0;
  box-shadow: none;
  overflow: visible;
  margin-bottom: 3rem !important;
}

.day-header {
  border-bottom: 2px solid #e9ecef;
  padding: 1rem 1.5rem;
  background: white;
  border-radius: 12px 12px 0 0;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.day-title {
  color: #1b515e;
  font-size: 1.5rem;
  font-weight: bold;
  display: flex;
  align-items: center;
}

.day-title i {
  color: #4caf50;
}

.day-date {
  font-size: 0.95rem;
  padding-left: 2.2rem;
}

.places-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 1.5rem;
  padding: 2rem;
  background: white;
  border-radius: 0 0 12px 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

@media (max-width: 768px) {
  .itinerary-container {
    padding-top: 100px;
  }
  
  .navbar-spacer {
    height: 160px;
  }
  
  .day-section {
    margin-bottom: 2rem !important;
  }
  
  .day-header {
    padding: 1rem 1.25rem;
  }
  
  .day-title {
    font-size: 1.25rem;
  }
  
  .day-date {
    font-size: 0.85rem;
  }
  
  .places-grid {
    grid-template-columns: 1fr;
    gap: 1rem;
    padding: 1.5rem;
  }
}

@media (max-width: 576px) {
  .itinerary-content {
    padding: 0.75rem !important;
  }
  
  .day-section {
    margin-bottom: 1rem !important;
  }
  
  .day-header {
    padding: 1rem 1.25rem 0.75rem 1.25rem;
  }
  
  .day-title {
    font-size: 1.1rem;
  }
  
  .day-date {
    font-size: 0.8rem;
    padding-left: 1.8rem;
  }
  
  .places-grid {
    padding: 1rem;
  }
}

/* Para tablets en modo landscape */
@media (min-width: 769px) and (max-width: 1024px) {
  .places-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}
</style>