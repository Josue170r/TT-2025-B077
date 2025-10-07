<!-- eslint-disable vue/multi-word-component-names -->
<template>
  <div class="pb-16">
    <h1 class="text-center text-2xl font-bold mt-4">Bienvenido</h1>
    <p class="p-4">Contenido de la pantalla viajes</p>

    <BottomNavbar />
  </div>
</template>

<script>
import BottomNavbar from '@/components/bottomnavbar.vue'

export default {
  components: { BottomNavbar },
}
</script>
