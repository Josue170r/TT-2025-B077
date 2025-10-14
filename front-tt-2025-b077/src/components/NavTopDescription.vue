<template>
  <nav class="travel-navbar-container">
    <div class="navbar-content">
      <div class="d-flex align-items-center w-100 px-2 px-sm-3 py-2">
        <!-- Menú Hamburguesa -->
        <div class="menu-wrapper flex-shrink-0 me-3">
          <Hamburgermenu />
        </div>

        <!-- Información del viaje -->
        <div class="travel-info flex-grow-1 d-flex flex-column">
          <!-- Título y cantidad de lugares -->
          <div class="d-flex align-items-center justify-content-between mb-1">
            <h1 class="travel-title mb-0">
              [ {{ travelTitle }} ]
              <span class="places-count">{{ placesCount }}</span>
            </h1>
            <!-- Íconos de editar y semáforo ambiental -->
            <div class="d-flex align-items-center gap-2 flex-shrink-0">
              <button class="btn-icon edit-btn" @click="editTravel" title="Editar viaje">
                <i class="fa-solid fa-pen"></i>
              </button>
              <div class="environmental-indicator" :class="impactClass" :title="`Impacto ambiental: ${environmentalScore}`">
                <i class="fa-solid fa-leaf"></i>
              </div>
            </div>
          </div>

          <!-- Estado y fechas -->
          <div class="travel-details d-flex align-items-center gap-2 flex-wrap">
            <span class="detail-item">{{ travelState }}</span>
            <span class="separator">·</span>
            <span class="detail-item">{{ dateRange }}</span>
          </div>

          <!-- Leyenda de impacto ambiental -->
          <a href="#" class="environmental-link" :class="impactClass" @click.prevent="showEnvironmentalInfo">
            <i class="fa-solid fa-leaf leaf-icon"></i>
            {{ environmentalMessage }}
          </a>
        </div>
      </div>
    </div>
  </nav>
</template>

<script>
import Hamburgermenu from '@/components/hamburgermenu.vue';

export default {
  name: "TravelNavbar",
  components: {
    Hamburgermenu
  },
  props: {
    // NOTA: Estos son datos de ejemplo
    travelTitle: {
      type: String,
      default: 'TITULO DE VIAJE' // Ejemplo: Se obtendrá de la API como response.data.title
    },
    placesCount: {
      type: String,
      default: '[# DE LUGARES]' // Ejemplo: Se obtendrá de la API como response.data.places_count o calculado
    },
    travelState: {
      type: String,
      default: '[ESTADO A VIAJAR]' // Ejemplo: Se obtendrá de la API como response.data.destination_state
    },
    dateRange: {
      type: String,
      default: '[RANGO DE FECHA]' // Ejemplo: Se obtendrá de la API como response.data.start_date + ' - ' + response.data.end_date
    },
    environmentalScore: {
      type: Number,
      default: 0.8, // Ejemplo: Se obtendrá de la API como response.data.sustainability_index
      validator: (value) => value >= 0 && value <= 1
    }
  },
  // CONEXIÓN CON BACKEND (Descomentar cuando se implemente)
  // mounted() {
  //   this.fetchTravelData();
  // },
  // methods: {
  //   async fetchTravelData() {
  //     try {
  //       const response = await fetch(` -- --  API -- `);
  //       const data = await response.json();
  //       
  //       this.travelData = {
  //         title: data.title,
  //         placesCount: `[${data.places.length} lugares]`,
  //         state: data.destination_state,
  //         dateRange: `${this.formatDate(data.start_date)} - ${this.formatDate(data.end_date)}`,
  //         sustainabilityIndex: data.sustainability_index
  //       };
  //     } catch (error) {
  //       console.error('Error al obtener datos del viaje:', error);
  //     }
  //   },
  //   formatDate(dateString) {
  //     // Formato de fecha personalizado
  //     const date = new Date(dateString);
  //     return date.toLocaleDateString('es-MX', { day: 'numeric', month: 'short' });
  //   }
  // },
  computed: {
    impactClass() {
      // Calcula la clase CSS basada en el índice de sostenibilidad
      if (this.environmentalScore >= 0.7) return 'low-impact';
      if (this.environmentalScore >= 0.4) return 'medium-impact';
      return 'high-impact';
    },
    environmentalMessage() {
      // Genera el mensaje dinámico según el índice de sostenibilidad
      const score = this.environmentalScore.toFixed(1);
      if (this.environmentalScore >= 0.7) {
        return `Este viaje tiene un bajo impacto medioambiental, con una calificación de sostenibilidad de ${score} puntos`;
      } else if (this.environmentalScore >= 0.4) {
        return `Este viaje tiene un impacto medioambiental moderado, con una calificación de sostenibilidad de ${score} puntos`;
      } else {
        return `Este viaje tiene un alto impacto medioambiental, con una calificación de sostenibilidad de ${score} puntos`;
      }
    }
  },
  methods: {
    editTravel() {
      this.$emit('edit-travel');
    },
    showEnvironmentalInfo() {
      this.$emit('show-environmental-info', this.environmentalScore);
    }
  }
};
</script>

<style scoped>
.travel-navbar-container {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  z-index: 1040;
  background-color: white;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.navbar-content {
  max-width: 1200px;
  margin: 0 auto;
  width: 100%;
}

.menu-wrapper {
  min-width: 40px;
}

/* Información del viaje */
.travel-info {
  min-width: 0;
}

/* Título */
.travel-title {
  font-size: 1.1rem;
  font-weight: bold;
  color: #1b515e;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  flex: 1;
  min-width: 0;
}

.places-count {
  color: #666;
  font-weight: 500;
  margin-left: 0.25rem;
}

/* Detalles (estado y fechas) */
.travel-details {
  font-size: 0.85rem;
  color: #666;
  line-height: 1.2;
}

.detail-item {
  white-space: nowrap;
}

.separator {
  color: #999;
}

/* Botones de iconos */
.btn-icon {
  background: transparent;
  border: none;
  padding: 0.4rem;
  cursor: pointer;
  border-radius: 6px;
  transition: all 0.2s;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
}

.edit-btn {
  color: #1b515e;
}

.edit-btn:hover {
  background-color: #f0f0f0;
}

/* Semáforo de impacto ambiental */
.environmental-indicator {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s;
  animation: pulse 2s ease-in-out infinite;
}

.environmental-indicator i {
  font-size: 1rem;
  color: white;
}

.environmental-indicator.low-impact {
  background-color: #4caf50;
  box-shadow: 0 0 10px rgba(76, 175, 80, 0.5);
}

.environmental-indicator.medium-impact {
  background-color: #ff9800;
  box-shadow: 0 0 10px rgba(255, 152, 0, 0.5);
}

.environmental-indicator.high-impact {
  background-color: #f44336;
  box-shadow: 0 0 10px rgba(244, 67, 54, 0.5);
}

/* Leyenda de impacto ambiental */
.environmental-link {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.8rem;
  color: #666;
  text-decoration: none;
  margin-top: 0.5rem;
  transition: all 0.2s;
  line-height: 1.3;
  padding: 0.4rem 0.6rem;
  border-radius: 8px;
}

.environmental-link:hover {
  color: #1b515e;
  text-decoration: underline;
}

.environmental-link .leaf-icon {
  font-size: 0.9rem;
  flex-shrink: 0;
  animation: pulse 2s ease-in-out infinite;
}

/* Colores y sombras según impacto */
.environmental-link.low-impact {
  box-shadow: 0 0 15px rgba(76, 175, 80, 0.4);
}

.environmental-link.low-impact .leaf-icon {
  color: #4caf50;
}

.environmental-link.medium-impact {
  box-shadow: 0 0 15px rgba(255, 152, 0, 0.4);
}

.environmental-link.medium-impact .leaf-icon {
  color: #ff9800;
}

.environmental-link.high-impact {
  box-shadow: 0 0 15px rgba(244, 67, 54, 0.4);
}

.environmental-link.high-impact .leaf-icon {
  color: #f44336;
}

/* Animación de parpadeo/pulso */
@keyframes pulse {
  0%, 100% {
    opacity: 1;
    transform: scale(1);
  }
  50% {
    opacity: 0.7;
    transform: scale(1.05);
  }
}

/* Responsive - Tablets pequeñas y móviles */
@media (max-width: 576px) {
  .travel-title {
    font-size: 0.95rem;
  }

  .travel-details {
    font-size: 0.75rem;
  }

  .environmental-indicator {
    width: 28px;
    height: 28px;
  }

  .environmental-indicator i {
    font-size: 0.9rem;
  }

  .environmental-link {
    font-size: 0.75rem;
  }

  .environmental-link .leaf-icon {
    font-size: 0.85rem;
  }

  .btn-icon {
    width: 28px;
    height: 28px;
    padding: 0.3rem;
  }
}

/* Responsive - Móviles medianos */
@media (max-width: 400px) {
  .navbar-content {
    padding: 0;
  }

  .travel-title {
    font-size: 0.85rem;
  }

  .places-count {
    font-size: 0.85rem;
  }

  .travel-details {
    font-size: 0.7rem;
  }

  .menu-wrapper {
    min-width: 35px;
    margin-right: 0.5rem;
  }

  .environmental-indicator {
    width: 26px;
    height: 26px;
  }

  .environmental-indicator i {
    font-size: 0.85rem;
  }

  .environmental-link {
    font-size: 0.7rem;
  }

  .environmental-link .leaf-icon {
    font-size: 0.8rem;
  }

  .btn-icon {
    width: 26px;
    height: 26px;
  }
}

/* Responsive - Móviles muy pequeños */
@media (max-width: 350px) {
  .travel-title {
    font-size: 0.75rem;
  }

  .places-count {
    font-size: 0.75rem;
  }

  .travel-details {
    font-size: 0.65rem;
  }

  .separator {
    display: none;
  }

  .detail-item {
    font-size: 0.65rem;
  }

  .environmental-link {
    font-size: 0.65rem;
  }

  .environmental-link .leaf-icon {
    font-size: 0.75rem;
  }
}

/* Pantallas grandes */
@media (min-width: 768px) {
  .travel-title {
    font-size: 1.2rem;
  }

  .travel-details {
    font-size: 0.9rem;
  }

  .environmental-indicator {
    width: 36px;
    height: 36px;
  }

  .environmental-indicator i {
    font-size: 1.1rem;
  }

  .environmental-link {
    font-size: 0.85rem;
  }

  .environmental-link .leaf-icon {
    font-size: 0.95rem;
  }
}

@media (min-width: 1200px) {
  .travel-title {
    font-size: 1.3rem;
  }

  .environmental-link {
    font-size: 0.9rem;
  }

  .environmental-link .leaf-icon {
    font-size: 1rem;
  }
}
</style>