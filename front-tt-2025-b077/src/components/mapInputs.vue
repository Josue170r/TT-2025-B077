<template>
  <div class="floating-controls d-flex align-items-center gap-3">
    <!-- Inputs con Bootstrap -->
    <div class="route-card flex-fill p-3">
      <!-- Origen -->
      <div class="d-flex align-items-center mb-1">
        <span class="icon origin me-3 d-inline-block"></span>
        <input
          v-model="localOrigin"
          @input="$emit('update:origin', localOrigin)"
          placeholder="Punto de partida (puedes dejar vacío)"
          class="form-control route-input border-0"
        />
      </div>

      <!-- Línea divisoria con botón de intercambio -->
      <div class="d-flex align-items-center justify-content-between my-1">
        <span class="route-line flex-fill me-3"></span>
        <button class="btn swap-btn p-2" @click="handleSwap">⇆</button>
      </div>

      <!-- Destino -->
      <div class="d-flex align-items-center position-relative">
        <span class="icon destination me-3">
          <i class="fa-solid fa-location-dot"></i>
        </span>
        <input
          v-model="localDestination"
          @input="$emit('update:destination', localDestination)"
          placeholder="Destino"
          class="form-control route-input border-0"
        />
      </div>
    </div>

    <!-- Botón de búsqueda circular separado -->
    <button
      class="btn search-btn-circular d-flex align-items-center justify-content-center"
      @click="$emit('search')"
      :disabled="disabled"
      :class="{ disabled: disabled }"
    >
      🔍
    </button>
  </div>
</template>

<script>
export default {
  name: 'RouteInputs',
  props: {
    origin: {
      type: String,
      default: '',
    },
    destination: {
      type: String,
      default: '',
    },
  },
  data() {
    return {
      localOrigin: this.origin,
      localDestination: this.destination,
    }
  },
  watch: {
    origin(newVal) {
      this.localOrigin = newVal
    },
    destination(newVal) {
      this.localDestination = newVal
    },
  },
  methods: {
    handleSwap() {
      const temp = this.localOrigin
      this.localOrigin = this.localDestination
      this.localDestination = temp

      this.$emit('update:origin', this.localOrigin)
      this.$emit('update:destination', this.localDestination)
      this.$emit('swap')
    },
  },
}
</script>

<style scoped>
/* Controles flotantes superiores */
.floating-controls {
  position: absolute;
  top: 20px;
  left: 50%;
  transform: translateX(-50%);
  z-index: 10;
  width: 90%;
  max-width: 500px;
  animation: fadeInUp 0.6s ease-out;
}

/* Tarjeta de inputs con glassmorphism */
.route-card {
  background: rgba(255, 255, 255, 0.95) !important;
  backdrop-filter: blur(15px);
  border-radius: 16px !important;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.12) !important;
  border: 1px solid rgba(255, 255, 255, 0.2) !important;
  position: relative;
}

.route-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  border-radius: inherit;
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.4) 0%, rgba(255, 255, 255, 0.1) 100%);
  z-index: -1;
}

/* Inputs personalizados */
.route-input {
  background: transparent !important;
  font-weight: 500 !important;
  color: #1b515e !important;
  padding: 0.375rem 0 !important;
  line-height: 1.3 !important;
}

.route-input::placeholder {
  color: #7a9ba3 !important;
  font-weight: 400 !important;
}

.route-input:focus {
  background: transparent !important;
  box-shadow: none !important;
  color: #1b515e !important;
}

/* Iconos mejorados */
.icon {
  width: 24px;
  height: 24px;
  flex-shrink: 0;
  text-align: center;
}

.icon.origin {
  border: 3px solid #1b515e;
  background: white;
  border-radius: 50%;
}

.icon.destination i {
  color: rgb(216, 26, 26);
  font-size: 20px;
  position: relative;
  top: 2px;
}

.route-line {
  height: 2px;
  background: #1b515e;
  border-radius: 1px;
}

/* Botón de intercambio */
.swap-btn {
  background: rgba(247, 250, 252, 0.9) !important;
  border: 1px solid rgba(27, 81, 94, 0.2) !important;
  color: #1b515e !important;
  font-size: 1.1rem !important;
  border-radius: 8px !important;
  width: 40px;
  height: 40px;
  transition: all 0.3s ease !important;
}

.swap-btn:hover {
  background: rgba(237, 242, 247, 0.95) !important;
  transform: scale(1.1) rotate(180deg) !important;
  border-color: #1b515e !important;
}

/* Botón de búsqueda circular mejorado */
.search-btn-circular {
  background: white !important;
  border: none !important;
  color: #1b515e !important;
  font-size: 1.25rem !important;
  width: 64px !important;
  height: 64px !important;
  border-radius: 50% !important;
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.15) !important;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1) !important;
  flex-shrink: 0 !important;
}

.search-btn-circular:hover {
  background: #f8f9fa !important;
  color: #1b515e !important;
  transform: translateY(-3px) scale(1.05) !important;
  box-shadow: 0 12px 30px rgba(0, 0, 0, 0.2) !important;
}

.search-btn-circular:active {
  transform: translateY(-1px) scale(0.98) !important;
}

/* Animación de carga */
@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateX(-50%) translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateX(-50%) translateY(0);
  }
}

/* Responsive */
@media (max-width: 768px) {
  .floating-controls {
    width: 95%;
    top: 15px;
  }

  .search-btn-circular {
    width: 56px !important;
    height: 56px !important;
    font-size: 1.1rem !important;
  }
}

@media (max-width: 480px) {
  .floating-controls {
    width: 98%;
    top: 10px;
  }

  .route-input {
    font-size: 0.85rem !important;
    padding: 0.25rem 0 !important;
  }

  .route-input::placeholder {
    font-size: 0.85rem !important;
  }

  .icon {
    width: 20px;
    height: 20px;
  }

  .icon.origin {
    border-width: 2px;
  }

  .icon.destination i {
    font-size: 16px;
  }

  .swap-btn {
    width: 32px !important;
    height: 32px !important;
    font-size: 0.95rem !important;
  }

  .search-btn-circular {
    width: 48px !important;
    height: 48px !important;
    font-size: 0.95rem !important;
    flex-shrink: 0;
  }
}

@media (min-width: 1200px) {
  .floating-controls {
    max-width: 600px;
  }

  .search-btn-circular {
    width: 72px !important;
    height: 72px !important;
    font-size: 1.4rem !important;
  }
}
</style>
