<template>
  <div class="hamburger-container">
    <transition name="blur-fade">
      <div v-if="isOpen" class="blur-overlay" @click="closeMenu"></div>
    </transition>

    <button class="hamburger" @click="toggleMenu" :class="{ 'active': isOpen }">
      <span class="line"></span>
      <span class="line"></span>
      <span class="line"></span>
    </button>

    <transition name="fade">
      <div v-if="isOpen" class="menu">
        <ul>
          <li @click="goTo('preferencias')">
            <span class="menu-text"><font-awesome-icon :icon="['fas', 'arrows-rotate']" class="me-2" />Modificar mis preferencias</span>
            <div class="separator"></div>
          </li>
          <li @click="goTo('huella')">
            <span class="menu-text d-flex align-items-center">
                <img src="/huella.png" alt="icono huella" class="me-2" width="23" height="23" />
                Huella de carbono por uso de la app
            </span>
            <div class="separator"></div>
          </li>
          <li @click="goTo('ayuda')">
            <span class="menu-text"><i class="fa-solid fa-headset me-3"></i>Centro de ayuda</span>
            <div class="separator"></div>
          </li>
          <li @click="cerrarSesion" class="last-item">
            <span class="menu-text"><i class="fa-solid fa-right-from-bracket me-3"></i>Cerrar sesión</span>
          </li>
        </ul>
      </div>
    </transition>
  </div>
</template>

<script>
export default {
  name: "HamburgerMenu",
  emits: ['menu-opened', 'menu-closed'], // Declarar los eventos que emite el componente
  data() {
    return {
      isOpen: false,
    };
  },
  mounted() {
    // Agregar listener para clicks fuera del menú
    document.addEventListener('click', this.handleClickOutside);
  },
  beforeUnmount() {
    // Limpiar listener al destruir el componente
    document.removeEventListener('click', this.handleClickOutside);
    // Asegurar que se remueva el blur al destruir el componente
    this.removeBlurFromSiblings();
  },
  watch: {
    isOpen(newVal) {
      // Aplicar o remover blur cuando cambie el estado del menú
      if (newVal) {
        this.applyBlurToSiblings();
        // Emitir evento cuando se abre el menú
        this.$emit('menu-opened');
      } else {
        this.removeBlurFromSiblings();
        // Emitir evento cuando se cierra el menú
        this.$emit('menu-closed');
      }
    }
  },
  methods: {
    toggleMenu() {
      this.isOpen = !this.isOpen;
    },
    goTo(opcion) {
      console.log("Ir a:", opcion);
      this.isOpen = false;
    },
    cerrarSesion() {
      console.log("Cerrando sesión...");
      this.isOpen = false;
    },
    closeMenu() {
      this.isOpen = false;
    },
    handleClickOutside(event) {
      // Verificar si el click fue fuera del contenedor del menú
      const menuContainer = this.$el;
      const blurOverlay = document.querySelector('.blur-overlay');
      
      if (this.isOpen && menuContainer && !menuContainer.contains(event.target) && !blurOverlay?.contains(event.target)) {
        this.isOpen = false;
      }
    },
    applyBlurToSiblings() {
      // Encontrar el contenedor padre del menú hamburguesa
      const parent = this.$el.parentElement;
      if (!parent) return;

      // Aplicar blur a todos los elementos hermanos del contenedor
      Array.from(parent.children).forEach(child => {
        if (child !== this.$el && !child.classList.contains('blur-overlay')) {
          child.classList.add('blur-siblings');
        }
      });

      // También aplicar a elementos específicos si están en el body
      const commonSelectors = [
        'main', 'section', 'article', 'aside', 'nav:not(.hamburger-container *)',
        '.content', '.main-content', '.page-content', '.container:not(.hamburger-container *)',
        'header:not(.hamburger-container *)', 'footer'
      ];

      commonSelectors.forEach(selector => {
        const elements = document.querySelectorAll(selector);
        elements.forEach(el => {
          if (!this.$el.contains(el) && !el.contains(this.$el)) {
            el.classList.add('blur-siblings');
          }
        });
      });
    },
    removeBlurFromSiblings() {
      // Remover la clase blur-siblings de todos los elementos
      const blurredElements = document.querySelectorAll('.blur-siblings');
      blurredElements.forEach(el => {
        el.classList.remove('blur-siblings');
      });
    },
  },
};
</script>

<style scoped>
.blur-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(255, 255, 255, 0.09);
  backdrop-filter: blur(8px);
  -webkit-backdrop-filter: blur(8px); /* Safari */
  z-index: 150; 
  cursor: pointer;
}

.hamburger-container .hamburger {
  position: relative;
  z-index: 200;
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

.hamburger-container {
  position: relative;
  z-index: 200;
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

/* Menú desplegable */
.menu {
  position: absolute;
  top: 45px;
  left: 0;
  background: white;
  border-radius: 12px;
  box-shadow: 0px 8px 25px rgba(27, 81, 94, 0.15);
  width: 280px;
  overflow: hidden;
  backdrop-filter: blur(10px);
  z-index: 160;
}

.menu ul {
  list-style: none;
  margin: 0;
  padding: 8px 0;
}

.menu li {
  position: relative;
  cursor: pointer;
  transition: all 0.2s ease;
}

.menu-text {
  display: block;
  padding: 14px 20px;
  color: #1B515E;
  font-weight: 500;
  font-size: 15px;
  line-height: 1.4;
  transition: all 0.2s ease;
}

/* Estilos para los íconos */
.menu-text i,
.menu-text .fa-solid {
  width: 18px;
  color: #1B515E;
  transition: color 0.2s ease;
}

.menu-text img {
  transition: transform 0.2s ease;
}

.menu li:hover .menu-text i,
.menu li:hover .menu-text .fa-solid {
  color: #0f3a44;
}

.menu li:hover .menu-text img {
  transform: scale(1.1);
}

.separator {
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 70%;
  height: 1px;
  background: linear-gradient(
    to right,
    transparent 0%,
    rgba(27, 81, 94, 0.1) 20%,
    rgba(27, 81, 94, 0.3) 50%,
    rgba(27, 81, 94, 0.1) 80%,
    transparent 100%
  );
}

.menu li:hover {
  background: linear-gradient(135deg, #f0f8fa 0%, #e6f4f7 100%);
  transform: translateX(2px);
}

.menu li:hover .menu-text {
  color: #0f3a44;
  font-weight: 600;
}

.menu li:active {
  transform: scale(0.98);
}

/* Animación del menú */
.fade-enter-active {
  transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
}

.fade-leave-active {
  transition: all 0.2s ease-out;
}

.fade-enter-from {
  opacity: 0;
  transform: translateY(-10px) scale(0.95);
}

.fade-leave-to {
  opacity: 0;
  transform: translateY(-5px) scale(0.98);
}

/* Tablets (768px - 1024px) */
@media (max-width: 1024px) and (min-width: 768px) {
  .hamburger {
    width: 32px;
    height: 24px;
  }
  
  .menu {
    width: 300px;
    top: 50px;
  }
  
  .menu-text {
    font-size: 16px;
    padding: 16px 24px;
  }
}

/* Móviles grandes (481px - 767px) */
@media (max-width: 767px) and (min-width: 481px) {
  .hamburger {
    width: 28px;
    height: 21px;
  }
  
  .menu {
    width: 260px;
    top: 45px;
    border-radius: 10px;
  }
  
  .menu-text {
    font-size: 14px;
    padding: 13px 18px;
  }
  
  .separator {
    width: 65%;
  }
}

/* Pantallas grandes (más de 1024px) */
@media (min-width: 1025px) {
  .hamburger:hover {
    transform: scale(1.15);
  }
  
  .menu {
    width: 320px;
    box-shadow: 0px 10px 30px rgba(27, 81, 94, 0.12);
  }
  
  .menu-text {
    font-size: 16px;
    padding: 16px 24px;
  }
  
  .menu li:hover {
    transform: translateX(4px);
  }
  
  .separator {
    width: 75%;
  }
}

/* Orientación landscape en móviles */
@media (max-height: 500px) and (orientation: landscape) {
  .menu {
    top: 35px;
    max-height: calc(100vh - 50px);
    overflow-y: auto;
  }
  
  .menu-text {
    padding: 10px 18px;
    font-size: 13px;
  }
}

</style>