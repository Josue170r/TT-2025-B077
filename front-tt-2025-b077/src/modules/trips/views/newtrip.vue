<template>
  <div class="min-vh-100 bg-light">
    <!-- Top Navbar -->
    <TopNavbar @ver-itinerarios="handleVerItinerarios" />
    
    <div class="d-flex align-items-center py-4 pb-navbar" style="min-height: calc(100vh - 70px);">
      <transition name="fade" mode="out-in">
        <div v-if="showForm" class="container">
          <div class="row justify-content-center">
            <div class="col-12 col-lg-10 col-xl-9">

            <!-- Layout Responsivo -->
            <div class="row g-4 g-lg-5">
              
              <!-- Columna Izquierda: Formulario (orden 1 en móvil) -->
              <div class="col-12 col-md-6 order-1 order-md-1">
                
                <!-- Mensaje de ayuda cuando falta información -->
                <div v-if="!isFormValid" class="mt-3">
                  <div class="alert alert-info border-0 rounded-3 py-2 px-3">
                    <small>
                      <i class="fas fa-info-circle me-2"></i>
                      <span v-if="missingFields.length > 1">
                        Completa: {{ missingFields.join(' , ') }}
                      </span>
                      <span v-else>
                        Completa: {{ missingFields[0] }}
                      </span>
                    </small>
                  </div>
                </div>
                <!-- Formulario -->
                <form @submit.prevent="submitForm" class="py-3">
                  
                  <!-- Título de viaje -->
                  <div class="mb-4">
                    <div class="input-group input-group-lg">
                      <span class="input-group-text bg-white border-primary">
                        <i class="fas fa-pen text-primary"></i>
                      </span>
                      <div class="form-floating">
                        <input
                          type="text"
                          id="tituloViaje"
                          v-model="tituloViaje"
                          class="form-control border-primary border-start-0"
                          placeholder="Título de tu viaje"
                          required
                        />
                        <label for="tituloViaje" class="text-muted">Título de tu viaje</label>
                      </div>
                    </div>
                  </div>

                  <!-- Estado con menú personalizado -->
                  <div class="mb-4">
                    <div class="input-group input-group-lg">
                      <span class="input-group-text bg-white border-primary">
                        <i class="fas fa-map-marker-alt text-primary"></i>
                      </span>
                      <div class="form-floating flex-fill">
                        <div 
                          class="form-control border-primary border-start-0 d-flex align-items-center justify-content-between h-100"
                          style="cursor: pointer;"
                          @click="toggleEstadoMenu"
                          :class="{ 'border-success': estadoMenuOpen }"
                        >
                          <span v-if="!estado" class="text-muted">Selecciona un estado</span>
                          <span v-else class="text-dark fw-medium">
                            <i :class="getEstadoIcon(estado)" class="me-2 text-primary"></i>
                            {{ getEstadoNombre(estado) }}
                          </span>
                          <i class="fas fa-chevron-down text-primary transition-all" 
                             :class="{ 'rotate-180': estadoMenuOpen }"></i>
                        </div>
                        <label class="text-muted">Destino</label>
                        
                        <!-- Dropdown Menu -->
                        <div v-if="estadoMenuOpen" 
                             class="dropdown-menu show position-absolute w-100 mt-1 shadow-lg border-0"
                             style="z-index: 1050;">
                          <div 
                            v-for="estadoOption in estados"
                            :key="estadoOption.value"
                            class="dropdown-item d-flex align-items-center py-3 border-bottom border-light"
                            style="cursor: pointer;"
                            @click="selectEstado(estadoOption.value)"
                          >
                            <i :class="estadoOption.icon" class="me-3 text-primary fs-5"></i>
                            <span class="fw-medium">{{ estadoOption.nombre }}</span>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>

                  <!-- Indicador de estado del rango de fechas -->
                  <div class="mb-4">
                    <div class="d-flex align-items-center gap-3">
                      <div class="status-indicator">
                        <i :class="dateRangeStatusIcon" :style="{ color: dateRangeStatusColor }"></i>
                      </div>
                      <div class="flex-grow-1">
                        <small class="text-muted d-block">Fechas del viaje</small>
                        <span v-if="!hasValidDateRange" class="text-muted">
                          Selecciona las fechas en el calendario
                        </span>
                        <span v-else class="fw-medium text-success">
                          {{ formattedDateRange }}
                        </span>
                      </div>
                    </div>
                  </div>
                </form>

                <!-- Botón de hospedaje solo en pantallas grandes -->
                <div class="btn-hospedaje-container d-none d-lg-flex">
                  <button 
                    type="button" 
                    class="btn btn-lg btn-hospedaje"
                    :class="buttonClass"
                    @click="verHospedaje"
                    :disabled="!isFormValid"
                  >
                    <i class="fas fa-bed me-2"></i>
                    <span v-if="isFormValid">Ver los lugares de hospedaje</span>
                    <span v-else>{{ buttonText }}</span>
                  </button>
                </div>
              </div>

              <!-- Columna Derecha: Calendario (orden 2 en móvil) -->
              <div class="col-12 col-md-6 order-2 order-md-2">
                <Calendar 
                  v-model="selectedDateRange"
                  @rangeApplied="handleDateRangeApplied"
                  @rangeCleared="handleDateRangeCleared"
                />
                
                <!-- Botón de hospedaje en pantallas medianas y pequeñas -->
                <div class="d-grid mt-4 d-lg-none">
                  <button 
                    type="button" 
                    class="btn btn-lg px-4"
                    :class="buttonClass"
                    @click="verHospedaje"
                    :disabled="!isFormValid"
                  >
                    <i class="fas fa-bed me-2"></i>
                    <span v-if="isFormValid">Ver los lugares de hospedaje</span>
                    <span v-else>{{ buttonText }}</span>
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
        </div>
      </transition>
      <BottomNavbar />
    </div>
    </div>
</template>

<script>
import Calendar from '@/components/calendar.vue';
import BottomNavbar from '@/components/bottomnavbar.vue';
import TopNavbar from '@/components/topnavbaritinerary.vue';

export default {
  components: { Calendar, BottomNavbar, TopNavbar },
  name: 'NuevoItinerario',
  data() {
    return {
      showForm: false,
      tituloViaje: '',
      estado: '',
      estadoMenuOpen: false,
      selectedDateRange: { start: null, end: null },
      estados: [
        { value: 'cdmx', nombre: 'Ciudad de México', icon: 'fas fa-building' },
        { value: 'jalisco', nombre: 'Jalisco', icon: 'fas fa-mountain' },
        { value: 'nuevo-leon', nombre: 'Nuevo León', icon: 'fas fa-city' },
        { value: 'yucatan', nombre: 'Yucatán', icon: 'fas fa-landmark' }
      ]
    }
  },
  computed: {
    hasValidDateRange() {
      return this.selectedDateRange && 
             this.selectedDateRange.start && 
             this.selectedDateRange.end;
    },
    
    isFormValid() {
      return this.tituloViaje.trim() && 
             this.estado && 
             this.hasValidDateRange;
    },
    
    missingFields() {
      const missing = [];
      if (!this.tituloViaje.trim()) missing.push('título del viaje');
      if (!this.estado) missing.push('destino');
      if (!this.hasValidDateRange) missing.push('fechas del viaje');
      return missing;
    },
    
    buttonClass() {
      return this.isFormValid ? 'btn-primary' : 'btn-outline-secondary';
    },
    
    buttonText() {
      if (this.missingFields.length === 1) {
        return `Selecciona ${this.missingFields[0]}`;
      }
      return `Completa ${this.missingFields.length} campos`;
    },
    
    dateRangeStatusIcon() {
      return this.hasValidDateRange ? 'fas fa-check-circle' : 'far fa-calendar-alt';
    },
    
    dateRangeStatusColor() {
      return this.hasValidDateRange ? '#28a745' : '#6c757d';
    },
    
    formattedDateRange() {
      if (!this.hasValidDateRange) return '';
      
      const formatDate = (date) => {
        const months = [
          'enero', 'febrero', 'marzo', 'abril', 'mayo', 'junio',
          'julio', 'agosto', 'septiembre', 'octubre', 'noviembre', 'diciembre'
        ];
        return `${date.getDate()} ${months[date.getMonth()]}`;
      };
      
      const start = formatDate(this.selectedDateRange.start);
      const end = formatDate(this.selectedDateRange.end);
      const diffTime = Math.abs(this.selectedDateRange.end - this.selectedDateRange.start);
      const daysDifference = Math.ceil(diffTime / (1000 * 60 * 60 * 24)) + 1;
      
      return `${start} - ${end} (${daysDifference} días)`;
    }
  },
  
  mounted() {
    setTimeout(() => {
      this.showForm = true;
    }, 100);
    
    // Cerrar menú al hacer click fuera
    document.addEventListener('click', this.handleClickOutside);
  },
  
  beforeUnmount() {
    document.removeEventListener('click', this.handleClickOutside);
  },
  
  methods: {
    handleDateRangeApplied(range) {
      console.log('Rango de fechas aplicado:', range);
      this.selectedDateRange = { ...range };
      
      // Opcional: Mostrar notificación de éxito
      this.$nextTick(() => {
        console.log('Formulario válido:', this.isFormValid);
      });
    },
    
    handleDateRangeCleared() {
      console.log('Rango de fechas limpiado');
      this.selectedDateRange = { start: null, end: null };
    },
    
    handleVerItinerarios() {
      console.log('Ver itinerarios desde navbar');
    },
    
    verHospedaje() {
      if (!this.isFormValid) {
        // Mostrar mensaje específico según lo que falte
        const mensaje = this.missingFields.length === 1 
          ? `Por favor selecciona ${this.missingFields[0]}`
          : `Por favor completa: ${this.missingFields.join(', ')}`;
        
        alert(mensaje);
        return;
      }
      
      const estadoNombre = this.getEstadoNombre(this.estado);
      const viajeData = {
        titulo: this.tituloViaje,
        estado: this.estado,
        estadoNombre: estadoNombre,
        fechaInicio: this.selectedDateRange.start,
        fechaFin: this.selectedDateRange.end,
        duracionDias: Math.ceil((this.selectedDateRange.end - this.selectedDateRange.start) / (1000 * 60 * 60 * 24)) + 1
      };
      
      console.log('Datos del viaje:', viajeData);
      
      alert(`¡Buscando lugares de hospedaje!\n\n` +
            `Viaje: ${this.tituloViaje}\n` +
            `Destino: ${estadoNombre}\n` +
            `Fechas: ${this.formattedDateRange}`);
      
      this.$emit('ver-hospedaje', viajeData);
    },
    
    resetForm() {
      this.tituloViaje = '';
      this.estado = '';
      this.selectedDateRange = { start: null, end: null };
      this.estadoMenuOpen = false;
    },
    
    getEstadoNombre(value) {
      const estado = this.estados.find(e => e.value === value);
      return estado ? estado.nombre : value;
    },
    
    getEstadoIcon(value) {
      const estado = this.estados.find(e => e.value === value);
      return estado ? estado.icon : 'fas fa-map-marker-alt';
    },
    
    toggleEstadoMenu() {
      this.estadoMenuOpen = !this.estadoMenuOpen;
    },
    
    selectEstado(value) {
      this.estado = value;
      this.estadoMenuOpen = false;
    },
    
    handleClickOutside(event) {
      const inputGroup = event.target.closest('.input-group');
      const dropdownMenu = event.target.closest('.dropdown-menu');
      
      if (!inputGroup && !dropdownMenu && this.estadoMenuOpen) {
        this.estadoMenuOpen = false;
      }
    }
  }
}
</script>

<style scoped>
.fade-enter-active, .fade-leave-active {
  transition: opacity 0.6s ease;
}
.fade-enter-from, .fade-leave-to {
  opacity: 0;
}

/* Colores personalizados */
.text-primary {
  color: #1B515E !important;
}

.border-primary {
  border-color: #1B515E !important;
}

.btn-primary {
  background-color: #1B515E;
  border-color: #1B515E;
  color: white;
}

.btn-primary:hover:not(:disabled) {
  background-color: #48cd13;
  border-color: #48cd13;
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(72, 205, 19, 0.3);
}

.btn-primary:disabled {
  background-color: #6dad64;
  border-color: #6dad64;
}

/* Botón deshabilitado con estilo diferente */
.btn-outline-secondary {
  color: #1B515E;
  border-color: #1B515E;
  background-color: transparent;
}

.btn-outline-secondary:hover:not(:disabled) {
  color: #1B515E;
  background-color: #e2e6ea;
  border-color: #dae0e5;
}

.btn-outline-secondary:disabled {
  color: #abcdab;
  background-color: transparent;
  border-color: #abcdab;
  opacity: 0.65;
  cursor: not-allowed;
}

/* Indicador de estado para fechas */
.status-indicator {
  min-width: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.status-indicator i {
  font-size: 1.1rem;
}

.btn-hospedaje-container {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  margin-top: 1.5rem;
}

.btn-hospedaje {
  display: flex;
  align-items: center;
  justify-content: center;
  min-width: 280px;
  max-width: 100%;
  padding: 0.75rem 2rem;
  transition: all 0.3s ease;
}

/* Responsive adjustments para el botón */
@media (max-width: 576px) {
  .btn-hospedaje {
    min-width: 250px;
    padding: 0.5rem 1.5rem;
  }
}

@media (min-width: 768px) {
  .btn-hospedaje {
    min-width: 320px;
    padding: 0.875rem 2.5rem;
  }
}

.border-success {
  border-color: #48cd13 !important;
}

/* Efectos de hover para dropdown items */
.dropdown-item:hover {
  background-color: rgba(72, 205, 19, 0.1);
  color: #48cd13;
}

/* Rotación para la flecha */
.rotate-180 {
  transform: rotate(180deg);
}

.transition-all {
  transition: all 0.3s ease;
}

/* Focus states usando Bootstrap */
.form-control:focus,
.form-select:focus {
  border-color: #48cd13;
  box-shadow: 0 0 0 0.25rem rgba(72, 205, 19, 0.25);
}

/* Mejoras para el dropdown menu */
.dropdown-menu {
  border-radius: 0.5rem;
  max-height: 250px;
  overflow-y: auto;
}

.dropdown-menu.show {
  animation: dropdownSlide 0.3s cubic-bezier(0.68, -0.55, 0.265, 1.55);
}

@keyframes dropdownSlide {
  from {
    opacity: 0;
    transform: scaleY(0) rotateX(-60deg);
    transform-origin: top center;
  }
  to {
    opacity: 1;
    transform: scaleY(1) rotateX(0deg);
    transform-origin: top center;
  }
}

/* Responsive adjustments */
@media (max-width: 576px) {
  .btn-lg {
    padding: 0.5rem 1rem;
  }
}

/* Padding para evitar que el BottomNavbar oculte el contenido */
.pb-navbar {
  padding-bottom: 100px !important; /* Para pantallas pequeñas */
}

/* Ajustes responsivos para el padding del navbar */
@media (min-width: 800px) {
  .pb-navbar {
    padding-bottom: 130px !important; /* Para pantallas grandes */
  }
}

/* Alerta de información personalizada */
.alert-info {
  background-color: rgba(27, 81, 94, 0.1);
  color: #1B515E;
  border: 1px solid rgba(27, 81, 94, 0.2);
}
</style>