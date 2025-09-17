<template>
  <header>
    <nav class="navbar navbar-expand-lg navbar-light bg-white shadow-sm fixed-top" role="navigation" aria-label="Navegación principal">
      <div class="container-fluid px-3 px-md-4">
        
        <!-- Menú Hamburguesa (Izquierda) -->
        <div class="navbar-brand-left">
          <HamburgerMenu />
        </div>
        
        <!-- Título Central -->
        <div class="navbar-title-center flex-grow-1 text-center mx-3">
          <h5 class="navbar-title mb-0 text-primary fw-bold d-none d-md-block">
            Genera un viaje personalizado por México
          </h5>
          <!-- Versión móvil más corta -->
          <h6 class="navbar-title mb-0 text-primary fw-bold d-block d-md-none">
           Genera un viaje por México
          </h6>
        </div>
        
        <!-- Icono Lista (Derecha) -->
        <div class="navbar-brand-right">
          <button 
            @click="verItinerarios"
            class="btn btn-outline-primary btn-navbar-icon"
            type="button"
            :title="tooltipText"
            aria-label="Ver mis itinerarios creados"
          >
          <i class="fa-solid fa-list-check " aria-hidden="true"></i>
            <span class="d-none d-lg-inline-block ms-2">Itinerarios</span>
          </button>
        </div>
        
      </div>
    </nav>
  </header>
  <div class="navbar-spacer"></div>
</template>

<script>
import HamburgerMenu from './hamburgermenu.vue';

export default {
  name: 'TopNavbar',
  components: {
    HamburgerMenu
  },
  emits: ['ver-itinerarios'],
  computed: {
    tooltipText() {
      return 'Ver mis itinerarios creados';
    }
  },
  methods: {
    verItinerarios() {
      console.log('Navegando a la lista de itinerarios');
      
    }
  }
}
</script>

<style scoped>
/* Navbar principal */
.navbar {
  background: linear-gradient(135deg, #ffffff 0%, #f8f9fa 100%);
  border-bottom: 2px solid rgba(27, 81, 94, 0.1);
  padding: 0.75rem 0;
  min-height: 70px;
  z-index: 1040;
}

/* Contenedor principal del navbar */
.container-fluid {
  display: flex;
  align-items: center;
  justify-content: space-between;
  max-width: 1400px;
  margin: 0 auto;
}

/* Secciones del navbar */
.navbar-brand-left,
.navbar-brand-right {
  flex: 0 0 auto;
  display: flex;
  align-items: center;
}

.navbar-title-center {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 50px;
}

/* Estilos del título */
.navbar-title {
  color: #1B515E !important;
  font-weight: 700 !important;
  letter-spacing: -0.5px;
  line-height: 1.2;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
}

/* Colores personalizados */
.text-primary {
  color: #1B515E !important;
}

/* Botón de icono personalizado */
.btn-navbar-icon {
  color: #1B515E;
  border-color: #1B515E;
  background-color: transparent;
  border-radius: 10px;
  padding: 0.5rem 0.75rem;
  transition: all 0.3s ease;
  border-width: 1.5px;
  font-weight: 500;
  display: flex;
  align-items: center;
  min-height: 44px;
}

.btn-navbar-icon:hover {
  background-color: #1B515E;
  border-color: #1B515E;
  color: white;
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(27, 81, 94, 0.2);
}

.btn-navbar-icon:focus {
  background-color: #1B515E;
  border-color: #1B515E;
  color: white;
  box-shadow: 0 0 0 0.2rem rgba(27, 81, 94, 0.25);
}

/* Espaciador para el navbar fijo */
.navbar-spacer {
  height: 70px;
  flex-shrink: 0;
}

/* Pantallas extra pequeñas */
@media (max-width: 575px) {
  .navbar {
    padding: 0.5rem 0;
    min-height: 60px;
  }
  
  .navbar-spacer {
    height: 60px;
  }
  
  .container-fluid {
    padding-left: 1rem;
    padding-right: 1rem;
  }
  
  .navbar-title {
    font-size: 0.95rem;
  }
  
  .btn-navbar-icon {
    padding: 0.4rem 0.6rem;
    min-height: 40px;
  }
  
  .btn-navbar-icon i {
    font-size: 1.1rem !important;
  }
}

/* Pantallas pequeñas */
@media (min-width: 576px) and (max-width: 767px) {
  .navbar-title {
    font-size: 1rem;
  }
}

/* Pantallas medianas */
@media (min-width: 768px) and (max-width: 991px) {
  .navbar-title {
    font-size: 1.1rem;
  }
  
  .container-fluid {
    padding-left: 2rem;
    padding-right: 2rem;
  }
}

/* Pantallas grandes */
@media (min-width: 992px) {
  .navbar-title {
    font-size: 1.25rem;
  }
  
  .container-fluid {
    padding-left: 3rem;
    padding-right: 3rem;
  }
  
  .btn-navbar-icon {
    padding: 0.6rem 1rem;
  }
}

/* Pantallas extra grandes */
@media (min-width: 1200px) {
  .navbar-title {
    font-size: 1.3rem;
  }
}

/* Animación suave para cambios de layout */
.navbar-title,
.btn-navbar-icon {
  transition: all 0.3s ease;
}

/* Mejora visual para el botón en estados activos */
.btn-navbar-icon:active {
  transform: translateY(0);
  box-shadow: 0 1px 3px rgba(27, 81, 94, 0.3);
}

/* Asegura que el HamburgerMenu tenga el espaciado correcto */
.navbar-brand-left {
  min-width: 44px;
}

/* Mejoras para accesibilidad */
.btn-navbar-icon:focus-visible {
  outline: 2px solid #1B515E;
  outline-offset: 2px;
}

/* Prevenir text overflow en títulos largos */
.navbar-title-center {
  overflow: hidden;
}

.navbar-title {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 100%;
}
</style>