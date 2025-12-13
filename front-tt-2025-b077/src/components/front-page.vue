<template>
  <div
    class="w-100 vh-100 position-relative bg-white overflow-hidden"
    :class="{ 'fade-out': fading }"
  >
    <img
      :src="windowWidth >= 992 ? '/portada2.jpg' : '/portada.jpg'"
      alt="Portada"
      class="portada-background"
    />

    <div class="overlay"></div>

    <div class="content-container text-center text-white">
      <h2 class="mb-3 fw-light animate-fade-up">Bienvenido a</h2>
      <div class="logo-box mb-3 animate-fade-down">
        <img src="/logo-letras.png  " alt="Logo letras" class="img-fluid" style="max-width: 300px" />
      </div>

      <p class="lead animate-fade-up delay-1">
        {{ fraseActual }}
      </p>

      <div class="scroll-indicator animate-fade-up delay-2">
        <i class="bi bi-chevron-down"></i>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'FrontPage',
  data() {
    return {
      fading: false,
      windowWidth: window.innerWidth,
      frases: [
        'Donde cada destino es un regalo y cada huella, un compromiso.',
        'Porque explorar México también es aprender a protegerlo',
        'Donde cada paso cuenta',
      ],
      fraseActual: '',
    }
  },
  computed: {
    ...mapGetters('auth', { isLogged: 'isLogged' }),
  },
  mounted() {
    this.fraseActual = this.frases[Math.floor(Math.random() * this.frases.length)]
    setTimeout(() => {
      this.fading = true
      setTimeout(() => {
        if (this.isLogged) {
          this.$router.push({ name: 'home' })
        } else {
          this.$router.push({ name: 'authLogin' })
        }
      }, 600)
    }, 2500)

    window.addEventListener('resize', this.handleResize)
  },
  beforeUnmount() {
    window.removeEventListener('resize', this.handleResize)
  },
  methods: {
    handleResize() {
      this.windowWidth = window.innerWidth
    },
  },
}
</script>

<style scoped>
.portada-background {
  width: 100%;
  height: 100vh;
  object-fit: cover;
  object-position: center;
  position: absolute;
  top: 0;
  left: 0;
  z-index: 0;
}

/* Overlay */
.overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(24, 63, 73, 0.5);
  z-index: 1;
}

/* Contenido centrado */
.content-container {
  position: relative;
  z-index: 2;
  top: 50%;
  transform: translateY(-50%);
  padding: 0 1rem;
}

/* Caja de logo con fondo difuminado */
.logo-box {
  background: rgba(255, 255, 255, 0.15);
  padding: 1rem 2rem;
  border-radius: 12px;
  backdrop-filter: blur(4px);
  display: inline-block;
}

/* Animaciones */
.animate-fade-down {
  opacity: 0;
  transform: translateY(-20px);
  animation: fadeDown 1s forwards;
}

.animate-fade-up {
  opacity: 0;
  transform: translateY(20px);
  animation: fadeUp 1s forwards;
}

.delay-1 {
  animation-delay: 0.3s;
}
.delay-2 {
  animation-delay: 0.6s;
}

@keyframes fadeDown {
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
@keyframes fadeUp {
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* Indicador scroll */
.scroll-indicator {
  margin-top: 2rem;
  font-size: 1.5rem;
  opacity: 0.8;
}

/* Fade out al salir */
.fade-out {
  opacity: 0;
  transition: opacity 0.6s ease;
}

@media (max-width: 768px) {
  .logo-box {
    padding: 0.5rem 1rem;
  }
}
</style>
