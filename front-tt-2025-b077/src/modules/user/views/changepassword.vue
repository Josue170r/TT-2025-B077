<template>
  <div class="change-password-container">
    <transition name="fade" mode="out-in">
      <div v-if="showChangePassword" class="content-wrapper">
        <div class="logo-section">
          <img :src="logoUrl" alt="Logo letras" class="img-fluid logo-size" />
        </div>

        <div class="form-section">
          <h4 class="text-center mb-4 sub-text">Cambia tu contraseña</h4>

          <form @submit.prevent="submitChange">
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
                  :class="{ active: currentPasswordFocused || currentPassword }"
                >
                  Contraseña actual
                </label>
              </div>
            </div>

            <div class="input-section mb-3">
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
                  :class="{ active: newPasswordFocused || newPassword }"
                >
                  Nueva contraseña
                </label>
              </div>
            </div>

            <div class="input-section mb-3">
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
                    'is-valid': confirmPassword && passwordsMatch,
                  }"
                  @focus="setFocus('confirmPassword', true)"
                  @blur="setFocus('confirmPassword', false)"
                  required
                />
                <label
                  for="confirmPassword"
                  class="floating-label position-absolute fs-6"
                  :class="{ active: confirmPasswordFocused || confirmPassword }"
                >
                  Confirmar contraseña
                </label>
                <div v-if="confirmPassword && !passwordsMatch" class="invalid-feedback">
                  Las contraseñas no coinciden
                </div>
              </div>
            </div>

            <div class="buttons-section d-flex justify-content-between mt-4">
              <button type="button" class="btn btn-outline-secondary" @click="goBack">
                <i class="fas fa-arrow-left me-2"></i>Volver
              </button>
              <button type="submit" class="btn btn-success" :disabled="!canSubmit">
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
import { mapActions } from 'vuex'
import { getErrorDetails } from '@/utils/utils'

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
      isNavigating: false,
    }
  },
  mounted() {
    setTimeout(() => {
      this.showChangePassword = true
    }, 100)
  },
  computed: {
    passwordsMatch() {
      return this.newPassword && this.confirmPassword && this.newPassword === this.confirmPassword
    },
    canSubmit() {
      return this.currentPassword && this.newPassword && this.confirmPassword && this.passwordsMatch
    },
  },
  methods: {
    ...mapActions('user', {
      changePassword: 'changePassword',
    }),
    setFocus(field, focused) {
      if (field === 'currentPassword') this.currentPasswordFocused = focused
      else if (field === 'newPassword') this.newPasswordFocused = focused
      else if (field === 'confirmPassword') this.confirmPasswordFocused = focused
    },

    submitChange() {
      if (!this.canSubmit) return

      const data = {
        currentPassword: this.currentPassword,
        newPassword: this.newPassword,
        confirmPassword: this.confirmPassword,
      }

      this.changePassword(data)
        .then((response) => {
          this.$alert.success({
            title: 'Contraseña actualizada',
            text: response.data.message,
            nextRoute: 'myaccount',
          })
        })
        .catch((error) => {
          this.$alert.error(getErrorDetails(error))
        })
    },

    goBack() {
      if (this.isNavigating) return
      this.isNavigating = true
      this.showChangePassword = false
      setTimeout(() => {
        this.$router.go(-1)
      }, 600)
    },
  },
}
</script>

<style scoped>
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.6s ease;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

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

.input-section {
  margin-bottom: 1.5rem;
}
.buttons-section {
  margin-top: 2rem;
  padding-top: 1rem;
}

.floating-input {
  border: solid #1b515e !important;
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
  color: #1b515e;
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
  color: #1b515e;
}

.sub-text {
  font-family: 'Open Sans', sans-serif;
  font-weight: 600;
  font-size: 24px;
  color: #1b515e;
  margin-bottom: 2rem;
}

.btn-outline-secondary {
  color: #1b515e !important;
  background-color: #abcd9e !important;
  border-color: #abcd9e !important;
}
.btn-outline-secondary:hover {
  color: #fff !important;
  background-color: #1b515e !important;
  border-color: #1b515e !important;
}
.btn-success {
  color: #abcd9e !important;
  background-color: #1b515e !important;
  border-color: #1b515e !important;
}
.btn-success:hover:not(:disabled) {
  color: #1b515e !important;
  background-color: #abcd9e !important;
  border-color: #1b515e !important;
}
.btn-success:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.icon-input {
  left: 15px;
  top: 50%;
  transform: translateY(-50%);
  color: #1b515e;
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
</style>
