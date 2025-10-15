<template>
  <nav class="main-navbar-container">
    <div class="navbar-content">
      <div class="d-flex align-items-center justify-content-between w-100 px-2 px-sm-3 py-2">
        <div class="menu-wrapper flex-shrink-0">
          <Hamburgermenu />
        </div>
        <div class="navbar-title flex-grow-1 text-center mx-2">
          Itinerarios generados
        </div>
        <router-link :to="{ name: 'interactive_map' }" class="map-wrapper text-decoration-none flex-shrink-0">
          <div class="d-flex flex-column align-items-center map-section">
            <i class="fa-solid fa-location-dot map-icon"></i>
            <span class="map-text">Mapa</span>
          </div>
        </router-link>
      </div>
    </div>
  </nav>
</template>

<script>
import Hamburgermenu from '@/components/hamburgermenu.vue';

export default {
  name: "MainNavbar",
  components: {
    Hamburgermenu
  },
  methods: {
    toggleMapView() {
      console.log('Navegando o alternando a la vista de mapa');
      this.$emit('view-map');
    }
  }
};
</script>

<style scoped>
.main-navbar-container {
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

.navbar-title {
  font-size: 1.15rem;
  font-weight: bold;
  color: #1b515e;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.map-wrapper {
  cursor: pointer;
  padding: 5px 8px;
  border-radius: 8px;
  transition: background-color 0.2s;
  min-width: 45px;
  display: flex;
  justify-content: center;
}

.map-wrapper:hover {
  background-color: #f0f0f0;
}

.map-section {
  gap: 2px;
}

.map-icon {
  font-size: 1.3rem;
  color: #1b515e;
}

.map-text {
  font-size: 0.75rem;
  color: #1b515e;
  font-weight: 500;
  line-height: 1;
  white-space: nowrap;
}

@media (max-width: 576px) {
  .navbar-title {
    font-size: 0.95rem;
  }
  
  .map-icon {
    font-size: 1.25rem;
  }
  
  .map-text {
    font-size: 0.7rem;
  }
}

@media (max-width: 400px) {
  .navbar-content {
    padding: 0;
  }
  
  .navbar-title {
    font-size: 0.85rem;
    margin: 0 0.5rem;
  }
  
  .map-wrapper {
    padding: 4px 6px;
    min-width: 40px;
  }
  
  .map-icon {
    font-size: 1.2rem;
  }
  
  .map-text {
    font-size: 0.65rem;
  }
}

@media (max-width: 350px) {
  .navbar-title {
    font-size: 0.75rem;
    margin: 0 0.25rem;
  }
  
  .menu-wrapper {
    min-width: 35px;
  }
  
  .map-wrapper {
    padding: 3px 5px;
    min-width: 35px;
  }
  
  .map-icon {
    font-size: 1.1rem;
  }
  
  .map-text {
    font-size: 0.6rem;
  }
}

@media (min-width: 768px) {
  .navbar-title {
    font-size: 1.25rem;
  }
  
  .map-wrapper {
    padding: 6px 10px;
  }
  
  .map-icon {
    font-size: 1.2rem;
  }
  
  .map-text {
    font-size: 0.8rem;
  }
}

@media (min-width: 1200px) {
  .navbar-title {
    font-size: 1.3rem;
  }
}
</style>