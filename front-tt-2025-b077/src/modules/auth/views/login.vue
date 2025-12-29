<template>
  <div class="d-flex justify-content-center align-items-center vh-100 bg-white">
    <transition name="fade" mode="out-in">
      <div v-if="showLogin" class="container">
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

                <!-- Formulario de Login -->
                <div
                  class="col-12 col-md-6 order-2 order-md-1 d-flex flex-column justify-content-center p-4 bg-white mt-2"
                >
                  <h4 class="text-center mb-2 sub-text">Iniciar sesión o registrate</h4>
                  <hr />
                  <h4 class="text-center mb-2 title mt-4">¡Te damos la bienvenida!</h4>

                  <form @submit.prevent="signIn">
                    <!-- Campo Usuario -->
                    <div class="mb-4 position-relative has-icon">
                      <i class="fas fa-user position-absolute icon-input"></i>
                      <input
                        type="text"
                        id="username"
                        v-model="username"
                        class="form-control floating-input mt-4 ps-5"
                        :class="{ 'has-content': username }"
                        @focus="setFocus('username', true)"
                        @blur="setFocus('username', false)"
                        required
                      />
                      <label
                        for="username"
                        class="floating-label position-absolute fs-6"
                        :class="{ active: usernameFocused || username }"
                      >
                        Email o usuario
                      </label>
                    </div>

                    <!-- Contraseña -->
                    <div class="mb-4 position-relative has-icon">
                      <i class="fas fa-lock position-absolute icon-input"></i>
                      <input
                        :type="showPassword ? 'text' : 'password'"
                        id="password"
                        v-model="password"
                        class="form-control floating-input ps-5 pe-5"
                        :class="{ 'has-content': password }"
                        @focus="setFocus('password', true)"
                        @blur="setFocus('password', false)"
                        required
                      />
                      <label
                        for="password"
                        class="floating-label position-absolute fs-6"
                        :class="{ active: passwordFocused || password }"
                      >
                        Contraseña
                      </label>
                      <button
                        type="button"
                        class="btn-toggle-password position-absolute"
                        @click="togglePassword"
                        tabindex="-1"
                      >
                        <i :class="showPassword ? 'fas fa-eye-slash' : 'fas fa-eye'"></i>
                      </button>
                    </div>
                    <!-- Olvidé contraseña -->
                    <div class="mt-0 mb-5 d-flex justify-content-end align-items-center">
                      <h6 class="title mb-0 me-1">¿Necesitas ayuda?</h6>
                      <a href="#" class="green-link" @click.prevent="goToForgotPassword">
                        Olvidé mi contraseña
                      </a>
                    </div>

                    <div class="d-flex justify-content-between mt-3">
                      <button type="button" class="btn btn-outline-secondary" @click="resetForm">
                        Cancelar
                      </button>
                      <button type="submit" class="btn btn-success">Iniciar sesión</button>
                    </div>
                  </form>

                  <!-- Registrarse -->
                  <div class="mt-4 mb-5 d-flex justify-content-end align-items-center">
                    <h6 class="title mb-0 me-1">No tengo una cuenta</h6>
                    <a href="#" class="green-link" @click.prevent="goToRegister"> Registrarse </a>
                  </div>
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
import { mapActions, mapMutations, mapGetters } from 'vuex'
import { getErrorDetails, getUrlVars } from '@/utils/utils'

export default {
  name: 'LoginComponent',
  data() {
    return {
      showLogin: false,
      logoUrl: '/logo-letras.png  ',
      username: '',
      password: '',
      usernameFocused: false,
      passwordFocused: false,
      isNavigating: false,
      showPassword: false,
    }
  },
  created() {
    const token = getUrlVars('token')['token']
    if (token !== undefined) {
      this.userActivation(token)
        .then((response) => {
          this.$alert.success({
            title: 'Cuenta activada',
            text: response.data.message,
            nextRoute: 'authLogin',
          })
          window.history.pushState({}, document.title, '/auth/login')
        })
        .catch((error) => {
          this.$alert.error(getErrorDetails(error))
        })
    }
  },
  mounted() {
    setTimeout(() => {
      this.showLogin = true
    }, 100)
  },
  computed: {
    ...mapGetters('auth', ['isLogged']),
  },
  methods: {
    ...mapActions('auth', {
      login: 'login',
      userActivation: 'userActivation',
      getProfile: 'getProfile',
    }),
    ...mapMutations('auth', {
      setUser: 'setUser',
    }),
    async signIn() {
      const data = {
        emailOrUsername: this.username,
        password: this.password,
      }
      this.login(data)
        .then(() => {
          this.getProfile().then((response) => {
            this.setUser(response.data)
            this.$router.push({ name: 'home' })
          })
        })
        .catch((error) => {
          this.$alert.error(getErrorDetails(error))
        })
    },
    resetForm() {
      this.username = ''
      this.password = ''
      this.usernameFocused = false
      this.passwordFocused = false
    },
    setFocus(field, focused) {
      if (field === 'username') {
        this.usernameFocused = focused
      } else if (field === 'password') {
        this.passwordFocused = focused
      }
    },
    goToRegister() {
      if (this.isNavigating) return
      this.isNavigating = true
      this.showLogin = false

      setTimeout(() => {
        this.$router.push({ name: 'authRegister' }).catch(() => {})
      }, 600)
    },
    goToForgotPassword() {
      if (this.isNavigating) return
      this.isNavigating = true
      this.showLogin = false

      setTimeout(() => {
        this.$router.push({ name: 'authForgotPassword' }).catch(() => {})
      }, 600)
    },
    togglePassword() {
      this.showPassword = !this.showPassword
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

hr {
  height: 4px;
  background-color: #35aa06;
  border-color: #35aa06;
}

.sub-text {
  font-family: 'Open Sans', sans-serif;
  font-weight: 500;
  font-size: 16px;
  color: #1b515e;
}

.title {
  font-family: 'Open Sans', sans-serif;
  font-weight: 400;
  font-size: 12px;
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
  border-color: #1b515e !important;
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

.has-icon .floating-input.has-content {
  padding-left: 2.8rem !important;
}

.has-icon .floating-input:focus.has-content {
  padding-left: 2.8rem !important;
}

.has-icon .floating-label {
  left: 40px !important;
  z-index: 2 !important;
}

.green-link {
  color: #35aa06 !important;
  font-size: 12px !important;
}
.green-link:hover {
  color: #2e9005 !important;
}

.card.shadow-lg {
  box-shadow:
    0 1rem 3rem rgba(190, 212, 89, 0.2),
    0 0.5rem 2rem rgba(51, 204, 64, 0.15) !important;
}

.card {
  border: 0.5px solid #abcd9e;
}

.btn-toggle-password {
  right: 15px;
  top: 50%;
  transform: translateY(-50%);
  background: none;
  border: none;
  color: #1b515e;
  cursor: pointer;
  z-index: 10;
  padding: 0;
  width: 20px;
  height: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.btn-toggle-password:hover {
  color: #abcd9e;
}

.btn-toggle-password:focus {
  outline: none;
  color: #1b515e;
}
</style>
