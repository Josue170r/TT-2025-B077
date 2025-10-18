<template>
  <nav class="travel-navbar-container">
    <div class="navbar-content">
      <div class="d-flex align-items-center w-100 px-2 px-sm-3 py-2">
        <!-- Menú Hamburguesa -->
        <div class="menu-wrapper flex-shrink-0 me-3">
          <Hamburgermenu />
        </div>

        <div class="travel-info flex-grow-1 d-flex flex-column">
          <div class="d-flex align-items-center justify-content-between mb-1">
            <h1 class="travel-title mb-0">
              {{ travelTitle || 'Título del viaje' }}
              <span class="places-count">{{ placesCount || '[0 lugares]' }}</span>
            </h1>
            <div class="d-flex align-items-center gap-2 flex-shrink-0">
              <button class="btn-icon edit-btn" @click="editTravel" title="Editar viaje">
                <i class="fa-solid fa-pen"></i>
              </button>
              <div 
                class="environmental-indicator" 
                :class="impactClass" 
                :title="`Impacto ambiental: ${formattedScore}`"
              >
                <i class="fa-solid fa-leaf"></i>
              </div>
            </div>
          </div>

          <div class="travel-details d-flex align-items-center gap-2 flex-wrap">
            <span class="detail-item">{{ travelState || 'Estado' }}</span>
            <span class="separator">·</span>
            <span class="detail-item">{{ dateRange || 'Rango de fechas' }}</span>
          </div>

          <a 
            href="#" 
            class="environmental-link" 
            :class="impactClass" 
            @click.prevent="showEnvironmentalInfo"
          >
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
    travelTitle: {
      type: String,
      default: 'Título del viaje'
    },
    placesCount: {
      type: String,
      default: '[0 lugares]'
    },
    travelState: {
      type: String,
      default: 'Estado'
    },
    dateRange: {
      type: String,
      default: 'Rango de fechas'
    },
    environmentalScore: {
      type: Number,
      default: 0,
      validator: (value) => value >= 0 && value <= 1
    }
  },
  computed: {
    validScore() {
      // Asegura que el score sea válido
      const score = parseFloat(this.environmentalScore);
      if (isNaN(score)) return 0;
      if (score < 0) return 0;
      if (score > 1) return 1;
      return score;
    },

    formattedScore() {
      return (this.validScore * 100).toFixed(0) + '%';
    },

    impactClass() {
      if (this.validScore >= 0.7) return 'low-impact';
      if (this.validScore >= 0.4) return 'medium-impact';
      return 'high-impact';
    },

    environmentalMessage() {
      const score = this.formattedScore;
      if (this.validScore >= 0.7) {
        return `Este viaje tiene un bajo impacto medioambiental, con una calificación de sostenibilidad de ${score}`;
      } else if (this.validScore >= 0.4) {
        return `Este viaje tiene un impacto medioambiental moderado, con una calificación de sostenibilidad de ${score}`;
      } else {
        return `Este viaje tiene un alto impacto medioambiental, con una calificación de sostenibilidad de ${score}`;
      }
    }
  },
  methods: {
    editTravel() {
      this.$emit('edit-travel');
    },
    showEnvironmentalInfo() {
      this.$emit('show-environmental-info', this.validScore);
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

.travel-info {
  min-width: 0;
}

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