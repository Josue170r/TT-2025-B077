<template>
  <div 
    class="w-100 d-flex justify-content-center align-items-center vh-100 bg-white"
    :class="{ 'fade-out': fading }"
  >
    <img src="/fondo.png" alt="Portada" class="portada-background" />
  </div>
</template>

<script>
export default {
  name: 'Portada',
  data() {
    return {
      fading: false,
    };
  },
  mounted() {
    // Esperar un poco y luego activar la transición de fade out
    setTimeout(() => {
      this.fading = true;

      // Después de que termine la transición, hacer la redirección
      setTimeout(() => {
        this.$router.push('/login');
      }, 600); // Debe coincidir con la duración de la transición CSS
    }, 2000); // Duración que se muestra la portada antes de empezar a desvanecer
  }
}
</script>

<style scoped>
.portada-background {
  width: 100%;
  height: 100vh;
  object-fit: cover;
  object-position: center;
  transition: opacity 0.6s ease;
}

/* Clase para hacer fade out */
.fade-out {
  opacity: 0;
  transition: opacity 0.6s ease;
}
</style>
