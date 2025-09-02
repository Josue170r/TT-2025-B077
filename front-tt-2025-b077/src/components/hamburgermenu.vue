<template>
  <div class="hamburger-container">
    <v-overlay 
      v-model="isOpen" 
      class="custom-blur-overlay"
      contained
      persistent
      @click="closeMenu"
    ></v-overlay>

    <v-menu v-model="isOpen" offset-y location="bottom start" :close-on-content-click="false" nudge-bottom="8" nudge-right="0">
      <template v-slot:activator="{}">
        <button class="hamburger" :class="{ 'active': isOpen }" @click="toggleMenu">
          <span class="line"></span>
          <span class="line"></span>
          <span class="line"></span>
        </button>
      </template>

      <v-card class="menu-card" elevation="8" rounded="lg">
        <v-list nav>
          <v-list-item @click="goTo('preferences')" class="menu-item">
            <template v-slot:prepend>
              <v-icon class="menu-icon">mdi-autorenew</v-icon>
            </template>
            <v-list-item-title class="menu-text">Modificar mis preferencias</v-list-item-title>
          </v-list-item>

          <v-divider class="menu-divider"></v-divider>

          <v-list-item @click="goTo('footprint')" class="menu-item">
            <template v-slot:prepend>
              <img src="/huella.png" alt="icono huella" class="menu-image" width="20" height="20" />
            </template>
            <v-list-item-title class="menu-text">Huella de carbono por uso de la app</v-list-item-title>
          </v-list-item>

          <v-divider class="menu-divider"></v-divider>

          <v-list-item @click="goTo('help')" class="menu-item">
            <template v-slot:prepend>
              <v-icon class="menu-icon">mdi-headset</v-icon>
            </template>
            <v-list-item-title class="menu-text">Centro de ayuda</v-list-item-title>
          </v-list-item>

          <v-divider class="menu-divider"></v-divider>

          <v-list-item @click="handleLogout" class="menu-item last-item">
            <template v-slot:prepend>
              <v-icon class="menu-icon">mdi-logout</v-icon>
            </template>
            <v-list-item-title class="menu-text">Cerrar sesión</v-list-item-title>
          </v-list-item>
        </v-list>
      </v-card>
    </v-menu>
  </div>
</template>

<script>
import { mapActions } from 'vuex'

export default {
  name: "HamburgerMenu",
  emits: ['menu-opened', 'menu-closed'],
  data() {
    return {
      isOpen: false,
    };
  },
  mounted() {
    document.addEventListener('click', this.handleClickOutside);
  },
  beforeUnmount() {
    document.removeEventListener('click', this.handleClickOutside);
    this.removeBlurFromSiblings();
  },
  watch: {
    isOpen(newVal) {
      if (newVal) {
        this.applyBlurToSiblings();
        this.$emit('menu-opened');
      } else {
        this.removeBlurFromSiblings();
        this.$emit('menu-closed');
      }
    }
  },
  methods: {
    ...mapActions('auth', ['logout']),
    
    toggleMenu() {
      this.isOpen = !this.isOpen;
    },
    goTo(option) {
      console.log("Go to:", option);
      this.isOpen = false;
    },
    handleLogout() {
      this.logout();
      this.isOpen = false;
    },
    closeMenu() {
      this.isOpen = false;
    },
    handleClickOutside(event) {
      const menuContainer = this.$el;
      const blurOverlay = document.querySelector('.blur-overlay');
      
      if (this.isOpen && menuContainer && !menuContainer.contains(event.target) && !blurOverlay?.contains(event.target)) {
        this.isOpen = false;
      }
    },
    applyBlurToSiblings() {
      return;
    },
    removeBlurFromSiblings() {
      const appElement = document.querySelector('#app');
      if (appElement) {
        appElement.classList.remove('blur-siblings');
      }
      
      const blurredElements = document.querySelectorAll('.blur-siblings');
      blurredElements.forEach(el => {
        el.classList.remove('blur-siblings');
      });
    },
  },
};
</script>

<style scoped>
.custom-blur-overlay {
  backdrop-filter: blur(8px) !important;
  -webkit-backdrop-filter: blur(8px) !important;
  background: rgba(255, 255, 255, 0.1) !important;
  z-index: 3100 !important;
}

.hamburger-container {
  position: relative;
  z-index: 3200;
}

.hamburger {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  width: 30px;
  height: 22px;
  background: none;
  border: none;
  cursor: pointer;
  padding: 0;
  transition: transform 0.3s ease;
  position: relative;
  z-index: 3200;
}

.hamburger:hover {
  transform: scale(1.1);
}

.hamburger.active .line:nth-child(1) {
  transform: rotate(45deg) translate(6px, 6px);
}

.hamburger.active .line:nth-child(2) {
  opacity: 0;
}

.hamburger.active .line:nth-child(3) {
  transform: rotate(-45deg) translate(6px, -6px);
}

.line {
  width: 100%;
  height: 4px;
  background: #1B515E;
  border-radius: 5px;
  transition: all 0.3s ease;
  transform-origin: center;
}

.blur-siblings {
  filter: blur(4px);
  transition: filter 0.3s ease;
  pointer-events: none;
}

.blur-fade-enter-active {
  transition: all 0.3s ease-out;
}

.blur-fade-leave-active {
  transition: all 0.2s ease-in;
}

.blur-fade-enter-from {
  opacity: 0;
  backdrop-filter: blur(0px);
  -webkit-backdrop-filter: blur(0px);
}

.blur-fade-leave-to {
  opacity: 0;
  backdrop-filter: blur(0px);
  -webkit-backdrop-filter: blur(0px);
}

.menu-card {
  min-width: 280px;
  margin-top: 8px;
  z-index: 3300 !important;
  box-shadow: 0px 8px 25px rgba(27, 81, 94, 0.15) !important;
  position: relative;
}

.menu-item {
  padding: 14px 20px !important;
  transition: all 0.2s ease;
  cursor: pointer;
}

.menu-item:hover {
  background: linear-gradient(135deg, #f0f8fa 0%, #e6f4f7 100%) !important;
  transform: translateX(2px);
}

.menu-text {
  color: #1B515E !important;
  font-weight: 500 !important;
  font-size: 15px !important;
  line-height: 1.4 !important;
  transition: all 0.2s ease !important;
}

.menu-item:hover .menu-text {
  color: #0f3a44 !important;
  font-weight: 600 !important;
}

.menu-icon {
  color: #1B515E !important;
  margin-right: 12px !important;
  transition: color 0.2s ease !important;
}

.menu-item:hover .menu-icon {
  color: #0f3a44 !important;
}

.menu-image {
  margin-right: 12px;
  transition: transform 0.2s ease;
}

.menu-item:hover .menu-image {
  transform: scale(1.1);
}

.menu-divider {
  margin: 8px 16px !important;
  background: linear-gradient(
    to right,
    transparent 0%,
    rgba(27, 81, 94, 0.1) 20%,
    rgba(27, 81, 94, 0.3) 50%,
    rgba(27, 81, 94, 0.1) 80%,
    transparent 100%
  ) !important;
  height: 1px !important;
}

.menu-item:active {
  transform: scale(0.98);
}

@media (max-width: 1024px) and (min-width: 768px) {
  .hamburger {
    width: 32px;
    height: 24px;
  }
  
  .menu-card {
    min-width: 300px;
  }
  
  .menu-text {
    font-size: 16px !important;
  }
}

@media (max-width: 767px) and (min-width: 481px) {
  .hamburger {
    width: 28px;
    height: 21px;
  }
  
  .menu-card {
    min-width: 260px;
  }
  
  .menu-text {
    font-size: 14px !important;
  }
}

@media (max-width: 480px) {
  .hamburger {
    width: 24px;
    height: 18px;
  }
  
  .line {
    height: 3px;
  }
  
  .menu-card {
    min-width: 240px;
    max-width: calc(100vw - 40px);
  }
  
  .menu-text {
    font-size: 13px !important;
  }
}

@media (min-width: 1025px) {
  .hamburger:hover {
    transform: scale(1.15);
  }
  
  .menu-card {
    min-width: 320px;
  }
  
  .menu-text {
    font-size: 16px !important;
  }
  
  .menu-item:hover {
    transform: translateX(4px);
  }
}

@media (max-height: 500px) and (orientation: landscape) {
  .menu-card {
    max-height: calc(100vh - 50px);
    overflow-y: auto;
  }
  
  .menu-text {
    font-size: 13px !important;
  }
}
</style>