<template>
  <transition name="modal-fade">
    <div v-if="show" class="modal-overlay">
      <div class="modal-container">
        <div class="modal-header">
          <h5 class="modal-title">¡Configura tus preferencias!</h5>
        </div>
        <div class="modal-body">
          <div class="icon-container">
            <i class="fa-regular fa-clock"></i>
          </div>
          <p class="modal-text">
            Para brindarte la mejor experiencia, necesitas seleccionar tus preferencias de viaje.
          </p>
          <p class="modal-subtext">
            Esto nos ayudará a recomendarte los mejores lugares según tus intereses.
          </p>
        </div>
        <div class="modal-footer">
          <button class="btn-primary" @click="goToPreferences">
            Seleccionar preferencias
          </button>
        </div>
      </div>
    </div>
  </transition>
</template>

<script>
export default {
  name: 'PreferencesModal',
  props: {
    show: {
      type: Boolean,
      default: false,
    },
  },
  methods: {
    goToPreferences() {
      this.$emit('close')
      this.$router.push({ name: 'preferences' })
    },
  },
}
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.6);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
  padding: 20px;
}

.modal-container {
  background-color: white;
  border-radius: 16px;
  max-width: 450px;
  width: 100%;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
  animation: slideUp 0.3s ease;
}

@keyframes slideUp {
  from {
    transform: translateY(30px);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}

.modal-header {
  padding: 24px 24px 16px;
  border-bottom: 2px solid #abcd9e;
}

.modal-title {
  font-size: 1.5rem;
  font-weight: bold;
  color: #1b515e;
  margin: 0;
  text-align: center;
}

.modal-body {
  padding: 24px;
  text-align: center;
}

.icon-container {
  margin-bottom: 20px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.icon-container i {
  font-size: 4rem;
  color: #1b515e;
}

.modal-text {
  font-size: 1rem;
  color: #1b515e;
  margin-bottom: 12px;
  line-height: 1.5;
}

.modal-subtext {
  font-size: 0.9rem;
  color: #6c757d;
  margin: 0;
  line-height: 1.4;
}

.modal-footer {
  padding: 16px 24px 24px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.btn-primary {
  background-color: #1b515e;
  color: white;
  border: none;
  border-radius: 12px;
  padding: 14px 24px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s ease;
  width: 100%;
}

.btn-primary:hover {
  background-color: #2a798d;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(27, 81, 94, 0.3);
}

.btn-secondary:hover {
  background-color: #f8f9fa;
  border-color: #1b515e;
}

.modal-fade-enter-active,
.modal-fade-leave-active {
  transition: opacity 0.3s ease;
}

.modal-fade-enter-from,
.modal-fade-leave-to {
  opacity: 0;
}

@media (max-width: 576px) {
  .modal-container {
    margin: 0 16px;
  }

  .modal-title {
    font-size: 1.3rem;
  }

  .modal-text {
    font-size: 0.95rem;
  }
}
</style>