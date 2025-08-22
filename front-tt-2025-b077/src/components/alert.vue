<template>
  <v-snackbar
    v-model="show"
    :color="alertColor"
    :timeout="timeout"
    location="top right"
    variant="elevated"
    elevation="4"
    rounded="lg"
    :min-width="300"
  >
    <div class="d-flex align-center">
      <v-icon class="me-3" color="white" size="24">
        {{ icon }}
      </v-icon>
      <div>
        <div class="text-subtitle1 font-weight-medium text-white mb-1">
          {{ title }}
        </div>
        <div class="text-body-2 text-white">
          {{ message }}
        </div>
      </div>
    </div>

    <template v-slot:actions>
      <v-btn
        variant="text"
        @click="close"
        icon="fas fa-times"
        size="small"
        color="white"
      />
    </template>
  </v-snackbar>
</template>

<script>
export default {
  name: 'Alert',
  data() {
    return {
      show: true,
      message: '',
      type: 'success',
      timeout: 2500,
      customTitle: '',
      nextRoute: null
    }
  },
  computed: {
    title() {
      return this.customTitle || (this.type === 'success' ? 'Éxito' : 'Error')
    },
    icon() {
      return this.type === 'success' ? 'fas fa-check-circle' : 'fas fa-times-circle'
    },
    alertColor() {
      return this.type === 'success' ? '#388e3c' : '#d32f2f'
    }
  },
  methods: {
    close() {
      this.show = false
    }
  }
}
</script>
