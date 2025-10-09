<template>
  <nav class="bottom-navbar">
    <div class="container-fluid d-flex justify-content-center py-2">
      <button 
        @click="generateItinerary" 
        class="btn btn-primary btn-lg w-100 generate-btn"
        :disabled="!hasHotelSelected"
        :class="{ 'btn-disabled': !hasHotelSelected }"
      >
        <span class="btn-text">Generar itinerario</span>
        <i class="fas fa-route ms-2"></i>
      </button>
    </div>
  </nav>
</template>

<script>
export default {
  name: 'NavButtom',
  props: {
    hasHotelSelected: {
      type: Boolean,
      default: false,
    },
  },
  methods: {
    generateItinerary() {
      if (!this.hasHotelSelected) {
        return
      }
      console.log('¡Itinerario generado!')
      this.$router.push({ name: 'generate_itinerary' })
    },
  },
}
</script>

<style scoped>
.bottom-navbar {
  position: fixed;
  bottom: 0;
  left: 0;
  width: 100%;
  z-index: 1030;
  background-color: white;
  border-top: 3px solid #cbeabf;
  box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.05);
}

.generate-btn {
  max-width: 450px;
  font-weight: 600;
  border-radius: 50px;
  padding: 12px 24px;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

/* Efecto hover */
.generate-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(27, 81, 94, 0.3);
}

.generate-btn:active:not(:disabled) {
  transform: translateY(0);
}

/* Estado deshabilitado */
.generate-btn:disabled,
.btn-disabled {
  background-color: #ccc !important;
  border-color: #ccc !important;
  color: #666 !important;
  cursor: not-allowed !important;
  opacity: 0.6;
}

.generate-btn:disabled:hover,
.btn-disabled:hover {
  transform: none !important;
  box-shadow: none !important;
}

/* Efecto de ripple sutil al hacer clic */
.generate-btn::before {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  width: 0;
  height: 0;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.2);
  transform: translate(-50%, -50%);
  transition:
    width 0.6s,
    height 0.6s;
}

.generate-btn:active:not(:disabled)::before {
  width: 300px;
  height: 300px;
}

.btn-text {
  position: relative;
  z-index: 1;
}

.generate-btn i {
  position: relative;
  z-index: 1;
  transition: transform 0.3s ease;
}

.generate-btn:hover:not(:disabled) i {
  transform: translateX(3px);
}

.btn-primary {
  background-color: #1b515e;
  border-color: #1b515e;
}

.btn-primary:hover:not(:disabled) {
  background-color: #164450;
  border-color: #164450;
}

@media (max-width: 576px) {
  .generate-btn {
    font-size: 1rem;
    padding: 10px 20px;
    max-width: 70%;
  }
}
</style>