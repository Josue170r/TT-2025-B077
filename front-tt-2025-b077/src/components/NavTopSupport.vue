<template>
  <nav class="main-navbar-container">
    <div class="navbar-content">
      <div class="d-flex align-items-center justify-content-between w-100 px-2 px-sm-3 py-3">
        <div class="menu-wrapper flex-shrink-0">
          <Hamburgermenu />
        </div>
        <div class="navbar-title-wrapper flex-grow-1 text-center mx-2">
          <div class="navbar-title">
            Preguntas frecuentes
          </div>
        </div>
      </div>
    </div>
  </nav>
</template>

<script>
import Hamburgermenu from '@/components/hamburgermenu.vue';

export default {
  name: "FootPrintNavbar",
  components: {
    Hamburgermenu
  },
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

.navbar-title-wrapper {
  display: flex;
  flex-direction: column;
  gap: 0.15rem;
}

.navbar-title {
  font-size: 1rem;
  font-weight: 500;
  color: #1b515e;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  line-height: 1.3;
}

.navbar-subtitle {
  font-size: 0.95rem;
  font-weight: 700;
  color: #1b515e;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  line-height: 1.2;
}

@media (max-width: 576px) {
  .navbar-title {
    font-size: 0.85rem;
  }
  
  .navbar-subtitle {
    font-size: 0.8rem;
  }
}

@media (max-width: 400px) {
  .navbar-content {
    padding: 0;
  }
  
  .navbar-title {
    font-size: 0.75rem;
  }
  
  .navbar-subtitle {
    font-size: 0.7rem;
  }
  
  .navbar-title-wrapper {
    margin: 0 0.5rem;
    gap: 0.1rem;
  }
}

@media (max-width: 350px) {
  .navbar-title {
    font-size: 0.7rem;
  }
  
  .navbar-subtitle {
    font-size: 0.65rem;
  }
  
  .navbar-title-wrapper {
    margin: 0 0.25rem;
  }
  
  .menu-wrapper {
    min-width: 35px;
  }
}

@media (min-width: 768px) {
  .navbar-title {
    font-size: 1.1rem;
  }
  
  .navbar-subtitle {
    font-size: 1.05rem;
  }
  
  .navbar-title-wrapper {
    gap: 0.2rem;
  }
}
@media (min-width: 1200px) {
  .navbar-title {
    font-size: 1.15rem;
  }
  
  .navbar-subtitle {
    font-size: 1.1rem;
  }
  
  .navbar-title-wrapper {
    gap: 0.25rem;
  }
}
</style>