<template>
  <div class="delete-account-container d-flex flex-column justify-content-center align-items-center text-center">
    <!-- Logo -->
    <div class="mb-5 fade-in-element" style="animation-delay: 0.2s;">
      <img src="/logo-letras.png" alt="Logo Huellas por México" class="img-fluid" style="max-height: 100px;">
    </div>

    <!-- Card principal -->
    <div class="main-content-card fade-in-element" style="animation-delay: 0.4s;">
      <!-- Título principal -->
      <h1 class="mb-4 main-title fade-in-element" style="animation-delay: 0.6s;">
        ¿Estás seguro que quieres eliminar tu cuenta?
      </h1>

      <!-- Carita triste -->
      <div class="emoji-wrapper mb-4 fade-in-element" style="animation-delay: 0.8s;">
        <span class="sad-emoji">😢</span>
      </div>

      <!-- Descripción -->
      <p class="description-text mb-5 fade-in-element" style="animation-delay: 1s;">
        Lamentamos mucho que te quieras ir, juntos podemos seguir dejando
        <strong class="brand-highlight">Huellas por México</strong>
      </p>

      <!-- Botones -->
      <div class="d-flex flex-column flex-md-row justify-content-center gap-3 fade-in-element" style="animation-delay: 1.2s;">
        <button class="btn btn-delete-custom px-4 py-3" @click="showModal">
            Eliminar cuenta
        </button>
        <button class="btn btn-cancel-custom px-4 py-3" @click="cancelDelete">
          Cancelar
        </button>
      </div>
    </div>

    <!-- Modal -->
    <div class="modal fade" id="deleteModal" tabindex="-1" aria-labelledby="deleteModalLabel" aria-hidden="true" ref="deleteModal">
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content modal-custom">
          <div class="modal-header border-0 pb-2">
            <div class="w-100 text-center">
              <div class="warning-icon mb-3">⚠️</div>
              <h5 class="modal-title text-danger fw-bold mb-0" id="deleteModalLabel">Confirmar eliminación</h5>
            </div>
            <button type="button" class="btn-close position-absolute" style="top: 1rem; right: 1rem;" @click="closeModal"></button>
          </div>
          <div class="modal-body text-center py-4">
            <p class="fs-6 mb-3 confirmation-message">
              Escribe <strong class="text-danger">"Eliminar"</strong> para proceder con la eliminación de la cuenta en
              <strong class="text-primary-custom">Huellas por México</strong>
            </p>
            <div class="input-wrapper position-relative mb-3">
              <input 
                type="text" 
                class="form-control text-center input-confirmation" 
                v-model="confirmationText" 
                placeholder="Escribe 'Eliminar' aquí"
                :class="{ 'border-success': isDeleteEnabled, 'border-danger': confirmationText && !isDeleteEnabled }"
              >
              <div class="input-status" v-if="confirmationText">
                <span v-if="isDeleteEnabled" class="text-success">✓</span>
                <span v-else class="text-danger">✗</span>
              </div>
            </div>
          </div>
          <div class="modal-footer d-flex flex-column flex-md-row justify-content-center gap-3 border-0 py-3">
            <button 
              class="btn btn-danger-confirm px-4 py-2"
              :disabled="!isDeleteEnabled"
              @click="confirmDelete"
            >
              Eliminar definitivamente
            </button>
            <button class="btn btn-keep-traveling px-4 py-2" @click="closeModal">
              Seguir viajando
            </button>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
import { Modal } from 'bootstrap'

export default {
  name: 'DeleteAccountScreen',
  data() {
    return {
      confirmationText: '',
      isDeleteEnabled: false,
      modalInstance: null
    }
  },
  watch: {
    confirmationText(newVal) {
      this.isDeleteEnabled = newVal.trim().toLowerCase() === 'eliminar';
    }
  },
  mounted() {
    this.modalInstance = new Modal(this.$refs.deleteModal);
  },
  methods: {
    showModal() {
      this.confirmationText = '';
      this.isDeleteEnabled = false;
      this.modalInstance.show();
    },
    closeModal() {
      this.confirmationText = '';
      this.isDeleteEnabled = false;
      this.modalInstance.hide();
    },
    confirmDelete() {
      if (this.isDeleteEnabled) {
        console.log('Cuenta eliminada definitivamente');
        this.closeModal();
      }
    },
    cancelDelete() {
    document.body.classList.add('fade-out');
        setTimeout(() => {
            if (this.$router) {
            this.$router.push({ name: 'myaccount' });
            }
        }, 300);
    }
  }
}
</script>

<style scoped>
/* Colores principales */
.text-primary-custom {
  color: #1B515E !important;
}

/* Contenedor principal con fade */
.delete-account-container {
  min-height: 100vh;
  padding: 2rem 1rem;
  background: white;
  position: relative;
  animation: fadeIn 0.5s ease-out;
}

/* Animación de entrada de la página */
@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

.fade-out {
  animation: fadeOut 0.3s ease-in forwards;
}

@keyframes fadeOut {
  from {
    opacity: 1;
  }
  to {
    opacity: 0;
  }
}

/* Fade-in escalonado para elementos */
.fade-in-element {
  opacity: 0;
  animation: fadeInUp 0.6s ease-out forwards;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* Logo simple */
.logo-enhanced {
  transition: transform 0.3s ease;
}

.logo-enhanced:hover {
  transform: scale(1.05);
}

/* Card principal */
.main-content-card {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 20px;
  padding: 3rem 2rem;
  box-shadow: 
    0 10px 30px rgba(27, 81, 94, 0.1),
    0 0 0 1px rgba(255, 255, 255, 0.5);
  border: 1px solid rgba(255, 255, 255, 0.3);
  max-width: 600px;
  width: 100%;
  position: relative;
  backdrop-filter: blur(10px);
}

.main-content-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: linear-gradient(90deg, #1B515E 0%, #ABCD9E 100%);
  border-radius: 20px 20px 0 0;
}

/* Título principal */
.main-title {
  color: #1B515E;
  font-weight: 700;
  font-size: 2rem;
  line-height: 1.3;
  margin-bottom: 2rem;
}

/* Emoji con animación simple */
.emoji-wrapper {
  position: relative;
  display: inline-block;
}

.sad-emoji {
  font-size: 3.5rem;
  display: inline-block;
  animation: float 3s ease-in-out infinite;
}

/* Descripción */
.description-text {
  font-size: 1.1rem;
  color: #666;
  line-height: 1.6;
}

.brand-highlight {
  color: #1B515E;
  background: linear-gradient(135deg, #1B515E 0%, #ABCD9E 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

/* Botones sin animaciones extras */
.btn {
  font-weight: 600;
  border-radius: 12px;
  transition: all 0.3s ease;
  min-width: 160px;
}

.btn-delete-custom {
  background: transparent;
  color: #1B515E;
  border: none;
  text-decoration: underline;
  font-size: 1.1rem;
  padding: 0.5rem 1rem;
}

.btn-delete-custom:hover {
  color: #0d3840;
  text-decoration: underline;
  text-decoration-thickness: 2px;
  background: transparent;
  transform: none;
  box-shadow: none;
}

.btn-cancel-custom {
  background: linear-gradient(135deg, #ABCD9E 0%, #9bc18c 100%);
  color: #1B515E;
  border: 2px solid #ABCD9E;
}

.btn-cancel-custom:hover {
  background: linear-gradient(135deg, #9bc18c 0%, #8bb87a 100%);
  color: #1B515E;
  transform: translateY(-1px);
  box-shadow: 0 4px 8px rgba(171, 205, 158, 0.3);
}

/* Modal personalizado */
.modal-custom {
  border: none;
  border-radius: 16px;
  box-shadow: 0 20px 40px rgba(0,0,0,0.15);
  background: white;
}

.modal-header {
  background: linear-gradient(135deg, #fff5f5 0%, #ffeaea 100%);
  border-radius: 16px 16px 0 0;
  padding: 2rem 2rem 1rem;
}

.warning-icon {
  font-size: 2.5rem;
  animation: shake 0.8s infinite;
}

.confirmation-message {
  color: #333;
  line-height: 1.6;
}

.input-wrapper {
  max-width: 300px;
  margin: 0 auto;
}

.input-confirmation {
  border-radius: 10px;
  padding: 0.75rem 1rem;
  border: 2px solid #dee2e6;
  transition: all 0.3s ease;
}

.input-confirmation:focus {
  border-color: #1B515E;
  box-shadow: 0 0 0 0.2rem rgba(27, 81, 94, 0.25);
}

.input-status {
  position: absolute;
  right: 15px;
  top: 50%;
  transform: translateY(-50%);
  font-size: 1.2rem;
}

/* Botones del modal */
.btn-danger-confirm {
  background: linear-gradient(135deg, #dc3545 0%, #c82333 100%);
  color: white;
  border: none;
}

.btn-danger-confirm:hover:not(:disabled) {
  background: linear-gradient(135deg, #c82333 0%, #bd2130 100%);
  color: white;
}

.btn-danger-confirm:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.btn-keep-traveling {
  background: linear-gradient(135deg, #ABCD9E 0%, #9bc18c 100%);
  color: #1B515E;
  border: 2px solid #ABCD9E;
}

.btn-keep-traveling:hover {
  background: linear-gradient(135deg, #9bc18c 0%, #8bb87a 100%);
  color: #1B515E;
}

@keyframes float {
  0%, 100% {
    transform: translateY(0px);
  }
  50% {
    transform: translateY(-8px);
  }
}

@keyframes shake {
  0%, 100% { transform: translateX(0); }
  25% { transform: translateX(-3px); }
  75% { transform: translateX(3px); }
}

/* Responsive */
@media (max-width: 768px) {
  .main-content-card {
    padding: 2rem 1.5rem;
    margin: 1rem;
  }
  
  .main-title {
    font-size: 1.6rem;
  }
  
  .sad-emoji {
    font-size: 3rem;
  }
  
  .btn {
    min-width: 40%;
  }
}

@media (max-width: 576px) {
  .delete-account-container {
    padding: 1rem;
  }
  
  .main-content-card {
    padding: 2rem 1rem;
  }
  
  .modal-header, .modal-body {
    padding: 1.5rem;
  }
  .btn {
    min-width: 40%;
  }
}
</style>