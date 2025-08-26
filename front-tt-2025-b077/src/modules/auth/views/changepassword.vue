<template>
  <div class="change-password-container">
    <transition name="fade" mode="out-in">
      <div v-if="showChangePassword" class="content-wrapper">
        
        <!-- Logo Section -->
        <div class="logo-section">
          <img :src="logoUrl" alt="Logo letras" class="img-fluid logo-size" />
        </div>

        <!-- Form Section -->
        <div class="form-section">
          <h4 class="text-center mb-4 sub-text">
            Cambia tu contraseña
          </h4>

          <form @submit.prevent="changePassword">
            <!-- Validar contraseña actual -->
            <div class="input-section mb-3">
              <div class="position-relative has-icon">
                <i class="fas fa-lock position-absolute icon-input"></i>
                <input
                  type="password"
                  id="currentPassword"
                  v-model="currentPassword"
                  class="form-control floating-input ps-5"
                  :class="{ 'has-content': currentPassword }"
                  @focus="setFocus('currentPassword', true)"
                  @blur="setFocus('currentPassword', false)"
                  required
                />
                <label 
                  for="currentPassword" 
                  class="floating-label position-absolute fs-6"
                  :class="{ 'active': currentPasswordFocused || currentPassword }"
                >
                  Valida tu contraseña actual
                </label>
              </div>

              <!-- Botón validar -->
              <div class="d-flex justify-content-end mt-3">
                <button 
                  type="button" 
                  class="btn btn-success"
                  @click="validateCurrentPassword"
                  :disabled="isValidating || !currentPassword"
                >
                  <i class="fas fa-check me-2"></i>Validar
                </button>
              </div>
              
              <!-- Mensaje de validación contraseña actual -->
              <transition name="message-fade">
                <div v-if="currentPasswordMessage" class="alert mt-3" :class="currentPasswordMessageType">
                  <i :class="currentPasswordMessageIcon" class="me-2"></i>{{ currentPasswordMessage }}
                </div>
              </transition>
            </div>

            <!-- Sección de nueva contraseña (visible solo después de validar) -->
            <transition name="slide-down">
              <div v-if="isCurrentPasswordValid" class="new-password-section">
                <h5 class="text-center mb-4 title">
                  Introduce tu nueva contraseña
                </h5>

                <!-- Nueva contraseña -->
                <div class="input-section mb-4">
                  <div class="position-relative has-icon">
                    <i class="fas fa-key position-absolute icon-input"></i>
                    <input
                      type="password"
                      id="newPassword"
                      v-model="newPassword"
                      class="form-control floating-input ps-5"
                      :class="{ 'has-content': newPassword }"
                      @focus="setFocus('newPassword', true)"
                      @blur="setFocus('newPassword', false)"
                      required
                    />
                    <label 
                      for="newPassword" 
                      class="floating-label position-absolute fs-6"
                      :class="{ 'active': newPasswordFocused || newPassword }"
                    >
                      Nueva contraseña
                    </label>
                  </div>
                </div>

                <!-- Confirmar nueva contraseña -->
                <div class="input-section mb-4">
                  <div class="position-relative has-icon">
                    <i class="fas fa-check-circle position-absolute icon-input"></i>
                    <input
                      type="password"
                      id="confirmPassword"
                      v-model="confirmPassword"
                      class="form-control floating-input ps-5"
                      :class="{ 
                        'has-content': confirmPassword,
                        'is-invalid': confirmPassword && !passwordsMatch,
                        'is-valid': confirmPassword && passwordsMatch
                      }"
                      @focus="setFocus('confirmPassword', true)"
                      @blur="setFocus('confirmPassword', false)"
                      required
                    />
                    <label 
                      for="confirmPassword" 
                      class="floating-label position-absolute fs-6"
                      :class="{ 'active': confirmPasswordFocused || confirmPassword }"
                    >
                      Confirmar contraseña nueva
                    </label>
                    <div v-if="confirmPassword && !passwordsMatch" class="invalid-feedback">
                      Las contraseñas no coinciden
                    </div>
                  </div>
                </div>

                <!-- Mensaje de estado del cambio de contraseña -->
                <transition name="message-fade">
                  <div v-if="changePasswordMessage" class="alert mt-3" :class="changePasswordMessageType">
                    <i :class="changePasswordMessageIcon" class="me-2"></i>{{ changePasswordMessage }}
                  </div>
                </transition>
              </div>
            </transition>

            <!-- Botones -->
            <div class="buttons-section d-flex justify-content-between mt-4">
              <button type="button" class="btn btn-outline-secondary" @click="goBack">
                <i class="fas fa-arrow-left me-2"></i>Volver
              </button>
              <button 
                type="submit" 
                class="btn btn-success"
                :disabled="!canSubmit"
                v-if="isCurrentPasswordValid"
              >
                <i class="fas fa-save me-2"></i>Cambiar contraseña
              </button>
            </div>
          </form>
        </div>

      </div>
    </transition>
  </div>
</template>

<script>
import { mapActions, mapGetters } from 'vuex';
import { getErrorDetails } from '@/utils/utils';

export default {
  name: 'ChangePasswordComponent',
  data() {
    return {
      showChangePassword: false,
      logoUrl: '/logo-letras.png',
      currentPassword: '',
      newPassword: '',
      confirmPassword: '',
      currentPasswordFocused: false,
      newPasswordFocused: false,
      confirmPasswordFocused: false,
      isCurrentPasswordValid: false,
      isNavigating: false,
      
      // Mensajes específicos para validación de contraseña actual
      currentPasswordMessage: '',
      currentPasswordMessageType: '',
      currentPasswordMessageIcon: '',
      
      // Mensajes específicos para cambio de contraseña
      changePasswordMessage: '',
      changePasswordMessageType: '',
      changePasswordMessageIcon: '',
      
      isValidating: false
    };
  },
  mounted() {
    setTimeout(() => {
      this.showChangePassword = true;
    }, 100);
  },
  computed: {
    ...mapGetters('auth', ['isLogged']),
    passwordsMatch() {
      return this.newPassword && this.confirmPassword && this.newPassword === this.confirmPassword;
    },
    canSubmit() {
      return this.isCurrentPasswordValid && 
             this.newPassword && 
             this.confirmPassword && 
             this.passwordsMatch &&
             !this.isValidating;
    }
  },
  methods: {
    ...mapActions('auth', {
      validatePassword: 'validateCurrentPassword',
      updatePassword: 'changePassword'
    }),
    
    setFocus(field, focused) {
      if (field === 'currentPassword') {
        this.currentPasswordFocused = focused;
      } else if (field === 'newPassword') {
        this.newPasswordFocused = focused;
      } else if (field === 'confirmPassword') {
        this.confirmPasswordFocused = focused;
      }
    },
    // Método del botón "Validar"
    async validateCurrentPassword() {
      if (!this.currentPassword) {
        this.isCurrentPasswordValid = false;
        this.clearCurrentPasswordMessage();
        return;
      }

      this.isValidating = true;
      
      try {
        // Llamar a la api
        await this.validatePassword({ currentPassword: this.currentPassword });
        
        this.isCurrentPasswordValid = true;
        this.showCurrentPasswordMessage("Contraseña actual validada correctamente", 'success');
        
      } catch (error) {
        this.isCurrentPasswordValid = false;
        this.showCurrentPasswordMessage("La contraseña actual es incorrecta", 'error');
        console.error('Error validating password:', error);
      } finally {
        this.isValidating = false;
      }
    },

    async changePassword() {
        if (!this.canSubmit) return;

        try {
            const data = {
            currentPassword: this.currentPassword,
            newPassword: this.newPassword,
            confirmPassword: this.confirmPassword
            };

            await this.updatePassword(data);
            
            this.showChangePasswordMessage("Contraseña cambiada exitosamente", 'success');
            setTimeout(() => {
            this.$router.push('../auth/login');
            }, 2000);

        } catch (error) {
            this.showChangePasswordMessage(getErrorDetails(error), 'error');
        }
    },

    // Mensajes
    showCurrentPasswordMessage(message, type = 'info') {
      this.currentPasswordMessage = message;
      
      if (type === 'success') {
        this.currentPasswordMessageType = 'alert-success';
        this.currentPasswordMessageIcon = 'fas fa-check-circle';
      } else if (type === 'error') {
        this.currentPasswordMessageType = 'alert-danger';
        this.currentPasswordMessageIcon = 'fas fa-times-circle';
      }
      
      // limpiar siempre en 4s
      setTimeout(() => {
        this.clearCurrentPasswordMessage();
      }, 4000);
    },

    showChangePasswordMessage(message, type = 'info') {
      this.changePasswordMessage = message;
      
      if (type === 'success') {
        this.changePasswordMessageType = 'alert-success';
        this.changePasswordMessageIcon = 'fas fa-check-circle';
      } else if (type === 'error') {
        this.changePasswordMessageType = 'alert-danger';
        this.changePasswordMessageIcon = 'fas fa-times-circle';
      }
      
      // limpiar siempre en 4s
      setTimeout(() => {
        this.clearChangePasswordMessage();
      }, 4000);
    },

    clearCurrentPasswordMessage() {
      this.currentPasswordMessage = '';
      this.currentPasswordMessageType = '';
      this.currentPasswordMessageIcon = '';
    },

    clearChangePasswordMessage() {
      this.changePasswordMessage = '';
      this.changePasswordMessageType = '';
      this.changePasswordMessageIcon = '';
    },

    goBack() {
      if (this.isNavigating) return;
      this.isNavigating = true;
      this.showChangePassword = false;

      setTimeout(() => {
        this.$router.go(-1); // Volver a la página anterior
      }, 600);
    }
  }
};
</script>

<style scoped>
/* Transiciones */
.fade-enter-active, .fade-leave-active {
  transition: opacity 0.6s ease;
}
.fade-enter-from, .fade-leave-to {
  opacity: 0;
}

.slide-down-enter-active {
  transition: all 0.5s ease-out;
}
.slide-down-leave-active {
  transition: all 0.3s ease-in;
}
.slide-down-enter-from {
  transform: translateY(-20px);
  opacity: 0;
}
.slide-down-leave-to {
  transform: translateY(-10px);
  opacity: 0;
}

/* Layout sin contenedor - distribución completa */
.change-password-container {
  min-height: 100vh;
  background-color: #f8f9fa;
  display: flex;
  flex-direction: column;
}

.content-wrapper {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 2rem 1rem;
  max-width: 600px;
  margin: 0 auto;
  width: 100%;
}

/* Sección del logo */
.logo-section {
  text-align: center;
  margin-bottom: 3rem;
}

.logo-size {
  max-height: 200px;
  object-fit: contain;
}

@media (max-width: 767.98px) {
  .logo-size {
    max-height: 120px;
  }
  
  .content-wrapper {
    padding: 1rem;
  }
  
  .logo-section {
    margin-bottom: 2rem;
  }
}

/* Sección del formulario */
.form-section {
  width: 100%;
  background: white;
  border-radius: 20px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  padding: 2.5rem;
}

@media (max-width: 767.98px) {
  .form-section {
    padding: 1.5rem;
    border-radius: 15px;
  }
}

/* Secciones de input */
.input-section {
  margin-bottom: 1.5rem;
}

/* Sección de nueva contraseña */
.new-password-section {
  border-top: 2px solid #e9ecef;
  padding-top: 2rem;
  margin-top: 2rem;
}

/* Sección de botones */
.buttons-section {
  margin-top: 2rem;
  padding-top: 1rem;
}

/* Inputs flotantes */
.floating-input {
  border: solid #1B515E !important;
  border-radius: 10px !important;
  padding: 14px 10px 6px 10px !important;
  transition: all 0.3s ease;
  border-width: 0.5px !important;
}

.floating-input:focus {
  border-color: #48cd13 !important;
  box-shadow: 0 0 0 0.2rem rgba(72, 205, 19, 0.25) !important;
  border-width: 0.5px !important;
}

.floating-input.is-valid {
  border-color: #28a745 !important;
  box-shadow: 0 0 0 0.2rem rgba(40, 167, 69, 0.25) !important;
}

.floating-input.is-invalid {
  border-color: #dc3545 !important;
  box-shadow: 0 0 0 0.2rem rgba(220, 53, 69, 0.25) !important;
}

.floating-label {
  left: 12px;
  top: 11px;
  color: #1B515E;
  font-size: 12px;
  pointer-events: none;
  transition: all 0.3s ease;
  transform-origin: left top;
  background-color: white;
  padding: 0 4px;
  z-index: 1;
}

.floating-input:focus + .floating-label,
.floating-input.has-content + .floating-label,
.floating-label.active {
  transform: translateY(-20px) scale(0.85);
  color: #1B515E;
}

/* Estilos de texto */
.sub-text {
  font-family: 'Open Sans', sans-serif;
  font-weight: 600;
  font-size: 24px;
  color: #1B515E;
  margin-bottom: 2rem;
}

.title {
  font-family: 'Open Sans', sans-serif;
  font-weight: 500;
  font-size: 16px;
  color: #1B515E;
}

/* Botones */
.btn-outline-secondary {
  color: #1B515E !important;
  background-color: #ABCD9E !important;
  border-color: #ABCD9E !important;
}

.btn-outline-secondary:hover {
  color: #fff !important;
  background-color: #1B515E !important;
  border-color: #1B515E !important;
}

.btn-success {
  color: #ABCD9E !important;
  background-color: #1B515E !important;
  border-color: #1B515E !important;
}

.btn-success:hover:not(:disabled) {
  color: #1B515E !important;
  background-color: #ABCD9E !important;
  border-color: #1B515E !important;
}

.btn-success:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

/* Iconos en inputs */
.icon-input {
  left: 15px;
  top: 50%;
  transform: translateY(-50%);
  color: #1B515E;
  font-size: 14px;
  z-index: 1;
  pointer-events: none;
}

.has-icon .floating-input {
  padding-left: 2.2rem !important;
}

.has-icon .floating-label {
  left: 40px !important;
  z-index: 2 !important;
}

/* Mensajes de validación */
.invalid-feedback {
  display: block;
  width: 100%;
  margin-top: 0.25rem;
  font-size: 0.875rem;
  color: #dc3545;
}

/* Alertas */
.alert {
  border-radius: 8px;
  padding: 0.75rem 1rem;
  margin-bottom: 1rem;
  border: 1px solid transparent;
}

.alert-success {
  color: #155724;
  background-color: #d4edda;
  border-color: #c3e6cb;
}

.alert-danger {
  color: #721c24;
  background-color: #f8d7da;
  border-color: #f5c6cb;
}

.alert-warning {
  color: #856404;
  background-color: #fff3cd;
  border-color: #ffeaa7;
}

.alert-info {
  color: #0c5460;
  background-color: #d1ecf1;
  border-color: #bee5eb;
}
</style>