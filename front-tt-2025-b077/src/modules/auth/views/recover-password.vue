<template>
  <div class="d-flex justify-content-center align-items-center vh-100 bg-white">
    <transition name="fade" mode="out-in">
      <div v-if="showForm" key="password-reset" class="container">
        <div class="row justify-content-center">
          <div class="col-12 col-lg-10">
            <div class="card shadow-lg overflow-hidden">
              <div class="row g-0 min-vh-400">
                <!-- Logo -->
                <div
                  class="col-12 col-md-6 order-1 order-md-2 d-flex justify-content-center align-items-center p-5 bg-white"
                >
                  <img :src="logoUrl" alt="Logo letras" class="img-fluid logo-size" />
                </div>

                <!-- Formulario -->
                <div
                  class="col-12 col-md-6 order-2 order-md-1 d-flex flex-column justify-content-center p-4 bg-white mt-2"
                >
                  <h4 class="title text-center mb-2">Restablece tu contraseña</h4>
                  <hr />
                  <h6 class="sub-text mt-2 mb-5">Introduce tu nueva contraseña</h6>

                  <form @submit.prevent="savePassword">
                    <!-- Contraseña -->
                    <div class="mb-4 position-relative">
                      <input
                        type="password"
                        id="password"
                        v-model="passwordForm.password"
                        class="form-control floating-input"
                        :class="{ 'has-content': passwordForm.password }"
                        @focus="setFocus('password', true)"
                        @blur="setFocus('password', false)"
                        required
                      />
                      <label
                        for="password"
                        class="floating-label position-absolute fs-6"
                        :class="{ active: passwordFocused || !!passwordForm.password }"
                      >
                        Nueva contraseña
                      </label>
                    </div>
                    <!-- Confirmar contraseña -->
                    <div class="mb-4 position-relative">
                      <input
                        type="password"
                        id="confirmPassword"
                        v-model="passwordForm.confirmPassword"
                        class="form-control floating-input"
                        :class="{ 'has-content': passwordForm.confirmPassword }"
                        @focus="setFocus('confirmPassword', true)"
                        @blur="setFocus('confirmPassword', false)"
                        required
                      />
                      <label
                        for="confirmPassword"
                        class="floating-label position-absolute fs-6"
                        :class="{
                          active: confirmPasswordFocused || !!passwordForm.confirmPassword,
                        }"
                      >
                        Confirma tu contraseña
                      </label>
                    </div>
                    <!-- Mensaje de error -->
                    <div v-if="errorMessage" class="alert alert-danger py-2 px-3 mb-3">
                      {{ errorMessage }}
                    </div>
                    <!-- Mensaje de éxito -->
                    <div
                      v-if="successMessage"
                      class="alert alert-success-custom py-2 px-3 mb-3 d-flex align-items-start"
                    >
                      <i class="fa-regular fa-circle-check check-icon me-2"></i>
                      <span>{{ successMessage }}</span>
                    </div>
                    <div class="d-flex justify-content-between mt-5">
                      <button type="button" class="btn btn-outline-secondary" @click="cancelar">
                        Cancelar
                      </button>
                      <button type="submit" class="btn btn-success">Guardar contraseña</button>
                    </div>
                  </form>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
import { mapActions } from 'vuex'
import { getUrlVars } from '@/utils/utils'
import { getErrorDetails } from '@/utils/utils'

export default {
  data() {
    return {
      showForm: false,
      logoUrl: '/logo-letras.png  ',
      passwordForm: {
        password: '',
        confirmPassword: '',
        token: '',
      },
      passwordFocused: false,
      confirmPasswordFocused: false,
      errorMessage: '',
      successMessage: '',
    }
  },
  created() {
    const token = getUrlVars('token')?.token
    if (token !== undefined) {
      this.passwordForm.token = token
    }
  },
  mounted() {
    setTimeout(() => {
      this.showForm = true
    }, 100)
  },
  methods: {
    ...mapActions('auth', {
      recoverPassword: 'recoverPassword',
    }),
    setFocus(field, focused) {
      if (field === 'password') this.passwordFocused = focused
      else if (field === 'confirmPassword') this.confirmPasswordFocused = focused
    },
    cancelar() {
      this.passwordFocused = false
      this.confirmPasswordFocused = false
      this.errorMessage = ''
      this.successMessage = ''
      this.$router.push({ name: 'authLogin' })
    },
    savePassword() {
      this.recoverPassword(this.passwordForm)
        .then((response) => {
          this.$alert.success({
            message: response.data.message,
            nextRoute: 'authLogin',
          })
        })
        .catch((error) => {
          this.$alert.error(getErrorDetails(error))
        })
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

.min-vh-400 {
  min-height: 400px;
}

.logo-size {
  max-height: 260px;
  object-fit: contain;
}

@media (max-width: 767.98px) {
  .logo-size {
    max-height: 120px;
  }
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

.btn-outline-secondary {
  color: #1b515e !important;
  background-color: #abcd9e !important;
  border-color: #abcd9e !important;
}

.btn-outline-secondary:focus {
  color: #fff !important;
  background-color: #1b515e !important;
  border-color: #1b515e !important;
}

.btn-success {
  color: #abcd9e !important;
  background-color: #1b515e !important;
  border-color: #1b515e !important;
}

.btn-success:focus {
  color: #1b515e !important;
  background-color: #abcd9e !important;
  border-color: #abcd9e !important;
}

.title {
  color: #1b515e;
}

.sub-text {
  font-family: 'Open Sans', sans-serif;
  font-weight: 400;
  font-size: 12px;
  color: #1b515e;
  text-align: center;
}

hr {
  height: 4px;
  background-color: #35aa06;
  border-color: #35aa06;
}

.alert-success-custom {
  background-color: #d4edda;
  border: 1px solid #28a745;
  color: #155724;
  border-radius: 8px;
}

.check-icon {
  color: #28a745;
  font-weight: bold;
  font-size: 16px;
  line-height: 1;
  margin-top: 2px;
}
</style>
