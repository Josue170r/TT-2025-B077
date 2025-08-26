<template>
  <nav class="navbar navbar-expand bg-light-blur">
    <div class="container-fluid justify-content-center">
      <div class="navbar-nav nav-pills-custom">
        <button 
          @click="setActiveFilter('preferencias')" 
          class="btn nav-filter-btn"
          :class="{ 'active': activeFilter === 'preferencias' }"
          type="button"
        >
          <span class="nav-text">Preferencias</span>
          <div class="active-indicator"></div>
        </button>
        
        <button 
          @click="setActiveFilter('mejores')" 
          class="btn nav-filter-btn"
          :class="{ 'active': activeFilter === 'mejores' }"
          type="button"
        >
          <span class="nav-text">Mejores</span>
          <div class="active-indicator"></div>
        </button>
        
        <button 
          @click="setActiveFilter('hoteles')" 
          class="btn nav-filter-btn"
          :class="{ 'active': activeFilter === 'hoteles' }"
          type="button"
        >
          <span class="nav-text">Hoteles</span>
          <div class="active-indicator"></div>
        </button>
        
        <button 
          @click="setActiveFilter('restaurantes')" 
          class="btn nav-filter-btn"
          :class="{ 'active': activeFilter === 'restaurantes' }"
          type="button"
        >
          <span class="nav-text">Restaurantes</span>
          <div class="active-indicator"></div>
        </button>
      </div>
    </div>
  </nav>
</template>

<script>
import { ref } from "vue";

export default {
  name: "TopNavbar",
  emits: ['filter-change'],
  setup(props, { emit }) {
    const activeFilter = ref('preferencias');
    
    const setActiveFilter = (filter) => {
      activeFilter.value = filter;
      emit('filter-change', filter);
    };
    
    return { 
      activeFilter,
      setActiveFilter
    };
  },
};
</script>

<style scoped>

.navbar-nav.nav-pills-custom {
  border-radius: 50px;
  padding: 8px 12px;
  backdrop-filter: blur(10px);
  gap: 4px;
}

.nav-filter-btn {
  border-radius: 25px !important;
  border: none !important;
  background: transparent !important;
  color: #1B515E !important;
  font-weight: 500;
  font-size: 14px;
  letter-spacing: 0.3px;
  position: relative;
  overflow: hidden;
  transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  transform: translateY(0);
  box-shadow: none !important;
}

.nav-text {
  position: relative;
  z-index: 2;
  transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
}

.active-indicator {
  position: absolute;
  bottom: 0;
  left: 50%;
  width: 0;
  height: 3px;
  background: linear-gradient(90deg, #ABCD9E, #8BB68A);
  border-radius: 2px;
  transform: translateX(-50%);
  transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
}

.nav-filter-btn::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, rgba(171, 205, 158, 0.1), rgba(139, 182, 138, 0.1));
  border-radius: 25px;
  opacity: 0;
  transform: scale(0.8);
  transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
}

.nav-filter-btn:hover {
  color: #ABCD9E !important;
  transform: translateY(-2px);
}

.nav-filter-btn:hover::before {
  opacity: 1;
  transform: scale(1);
}

.nav-filter-btn:hover .nav-text {
  transform: translateY(-1px);
}

.nav-filter-btn:hover .active-indicator {
  width: 60%;
  opacity: 0.6;
}

.nav-filter-btn.active {
  color: #ABCD9E !important;
  font-weight: 600;
}

.nav-filter-btn.active::before {
  opacity: 1;
  transform: scale(1);
  background: linear-gradient(135deg, rgba(171, 205, 158, 0.15), rgba(139, 182, 138, 0.15));
}

.nav-filter-btn.active .active-indicator {
  width: 80%;
  opacity: 1;
}

.nav-filter-btn:active {
  transform: translateY(0) scale(0.98) !important;
}

/* Responsive con Bootstrap breakpoints */
@media (max-width: 576px) {
  .navbar-nav.nav-pills-custom {
    padding: 6px 8px;
    gap: 2px;
  }
  
  .nav-filter-btn {
    padding: 8px 12px !important;
    font-size: 12px;
  }
  
  .active-indicator {
    height: 2px;
  }
}

@media (max-width: 480px) {
  .container-fluid {
    padding: 0 10px;
  }
  
  .navbar-nav.nav-pills-custom {
    padding: 2px 4px;
    gap: 1px;
  }
  
  .nav-filter-btn {
    padding: 6px 10px !important;
    font-size: 11px;
  }
}

@media (min-width: 768px) {
  .nav-filter-btn {
    padding: 12px 22px !important;
    font-size: 15px;
  }
}

@media (min-width: 992px) {
  .nav-filter-btn {
    padding: 6px 40px !important;
    font-size: 16px;
    letter-spacing: 0.6px;
  }
  
  .navbar-nav.nav-pills-custom {
    padding: 5px 10%;
    gap: 50px;
  }
  
  .active-indicator {
    height: 3px;
  }
}
</style>