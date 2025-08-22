<template>
  <div class="pb-16">
    <!-- Primera fila: hamburger menu + input + mapa distribuidos -->
    <div class="d-flex justify-content-center mt-1">
      <div class="search-container d-flex align-items-center justify-content-between bg-white rounded-pill px-4 py-2 w-75 w-md-50 w-lg-60 mt-2" style="max-width: 1100px; min-width: 420px;">
        <hamburgermenu />
        <div class="input-container flex-grow-1 mx-3">
          <Inputexplore />
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
    <p class="p-4">BUSCAR</p>
    
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
  methods: {
    handleFilterChange(filter) {
      console.log('Filtro seleccionado:', filter);
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

/* Estilos para el input con lupa a la derecha */
.input-container :deep(.form-control) {
  position: relative !important; 
  padding-right: 40px !important;
}

.input-container :deep(.form-control)::after {
  content: "\f002" !important;
  font-weight: 900 !important;
  position: absolute !important;
  right: 12px !important;
  top: 50% !important;
  transform: translateY(-50%) !important;
  color: #6c757d !important;
  pointer-events: none !important;
}

.input-container :deep(.form-control):focus::after {
  color: #1B515E;
}

/* Tablets */
@media (min-width: 769px) and (max-width: 1024px) {
  .map-text {
    font-size: 16px;
  }
  
  .map-icon {
    font-size: 1.4rem;
  }
}

/* Desktop pequeño */
@media (min-width: 1025px) and (max-width: 1440px) {
  .map-text {
    font-size: 18px;
  }
  
  .map-icon {
    font-size: 1.6rem;
  }
}

/* Desktop grande - con margen de 20px entre elementos */
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
}

hr{
  margin-top: 0;
  height: 2px;
  background-color: #35aa06;
  border-color: #b0d4a1;
}

</style>