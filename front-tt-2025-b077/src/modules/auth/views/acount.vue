<template>
  <div class="pb-16"> <!-- este padding evita que el contenido quede oculto -->
    <h1 class="text-center text-2xl font-bold mt-4">Bienvenido</h1>
    <p class="p-4">cuenta</p>

    <BottomNavbar />
  </div>
</template>

<script>
import BottomNavbar from "@/components/navbar.vue";

export default {
  components: { BottomNavbar }
};
</script>
