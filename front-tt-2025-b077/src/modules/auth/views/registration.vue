<template>
  <div class="d-flex justify-content-center align-items-center vh-100 bg-white">
    <transition name="fade" mode="out-in">
      <div v-if="showRegister" class="container" key="register">
        <div class="row justify-content-center">
          <div class="col-12 col-lg-10">
            <div class="card shadow-lg overflow-hidden">
              <div class="row g-0 min-vh-400">
                <div class="mb-0 col-12 col-md-6 order-1 order-md-2 d-flex justify-content-center align-items-center p-5 bg-white">
                  <img :src="logoUrl" alt="Logo" class="img-fluid logo-size" />
                </div>

                <div class="col-12 col-md-6 order-2 order-md-1 d-flex flex-column justify-content-center p-4 bg-white mt-2">
                  <h4 class="text-center mb-2 sub-text">
                    Completa los campos para crear tu cuenta.
                  </h4>
                  <hr />
                  <form @submit.prevent="register">
                    <!-- Username -->
                    <div class="mb-4 position-relative has-icon">
                      <i class="fas fa-user position-absolute icon-input"></i>
                      <input
                        type="text"
                        id="username"
                        v-model="user.username"
                        class="form-control floating-input mt-4"
                        :class="{ 'has-content': user.username }"
                        @focus="setFocus('username', true)"
                        @blur="setFocus('username', false)"
                        required
                      />
                      <label
                        for="username"
                        class="floating-label position-absolute fs-6"
                        :class="{ active: usernameFocused || !!user.username }"
                      >
                        Nombre de usuario
                      </label>
                    </div>

                    <!-- Nombre(s) -->
                    <div class="mb-4 position-relative has-icon">
                      <i class="fas fa-id-card position-absolute icon-input"></i>
                      <input
                        type="text"
                        id="name"
                        v-model="user.name"
                        class="form-control floating-input mt-4"
                        :class="{ 'has-content': user.name }"
                        @focus="setFocus('name', true)"
                        @blur="setFocus('name', false)"
                        required
                      />
                      <label
                        for="name"
                        class="floating-label position-absolute fs-6"
                        :class="{ active: nameFocused || !!user.name }"
                      >
                        Nombre(s)
                      </label>
                    </div>

                    <!-- Primer apellido -->
                    <div class="mb-4 position-relative has-icon">
                      <i class="fas fa-id-card position-absolute icon-input"></i>
                      <input
                        type="text"
                        id="lastName"
                        v-model="user.lastName"
                        class="form-control floating-input mt-4"
                        :class="{ 'has-content': user.lastName }"
                        @focus="setFocus('lastName', true)"
                        @blur="setFocus('lastName', false)"
                        required
                      />
                      <label
                        for="lastName"
                        class="floating-label position-absolute fs-6"
                        :class="{ active: lastNameFocused || !!user.lastName }"
                      >
                        Primer apellido
                      </label>
                    </div>

                    <!-- Segundo apellido -->
                    <div class="mb-4 position-relative has-icon">
                      <i class="fas fa-id-card position-absolute icon-input"></i>
                      <input
                        type="text"
                        id="secondLastName"
                        v-model="user.secondLastName"
                        class="form-control floating-input mt-4"
                        :class="{ 'has-content': user.secondLastName }"
                        @focus="setFocus('secondLastName', true)"
                        @blur="setFocus('secondLastName', false)"
                      />
                      <label
                        for="secondLastName"
                        class="floating-label position-absolute fs-6"
                        :class="{ active: secondLastNameFocused || !!user.secondLastName }"
                      >
                        Segundo apellido
                      </label>
                    </div>

                    <!-- Número de teléfono -->
                    <div class="mb-4 position-relative has-icon">
                      <i class="fas fa-phone position-absolute icon-input"></i>
                      <input
                        type="tel"
                        id="phoneNumber"
                        v-model="user.phoneNumber"
                        class="form-control floating-input mt-4"
                        :class="{ 'has-content': user.phoneNumber }"
                        @focus="setFocus('phoneNumber', true)"
                        @blur="setFocus('phoneNumber', false)"
                      />
                      <label
                        for="phoneNumber"
                        class="floating-label position-absolute fs-6"
                        :class="{ active: phoneNumberFocused || !!user.phoneNumber }"
                      >
                        Número de teléfono
                      </label>
                    </div>

                    <!-- Correo electrónico -->
                    <div class="mb-4 position-relative has-icon">
                      <i class="fas fa-envelope position-absolute icon-input"></i>
                      <input
                        type="email"
                        id="email"
                        v-model="user.email"
                        class="form-control floating-input mt-4"
                        :class="{ 'has-content': user.email }"
                        @focus="setFocus('email', true)"
                        @blur="setFocus('email', false)"
                        required
                      />
                      <label
                        for="email"
                        class="floating-label position-absolute fs-6"
                        :class="{ active: emailFocused || !!user.email }"
                      >
                        Correo electrónico
                      </label>
                    </div>

                    <!-- Contraseña -->
                    <div class="mb-4 position-relative has-icon">
                      <i class="fas fa-lock position-absolute icon-input"></i>
                      <input
                        type="password"
                        id="password"
                        v-model="user.password"
                        class="form-control floating-input"
                        :class="{ 'has-content': user.password }"
                        @focus="setFocus('password', true)"
                        @blur="setFocus('password', false)"
                        required
                      />
                      <label
                        for="password"
                        class="floating-label position-absolute fs-6"
                        :class="{ active: passwordFocused || !!user.password }"
                      >
                        Contraseña
                      </label>
                    </div>

                    <!-- Confirmar contraseña -->
                    <div class="mb-4 position-relative has-icon">
                      <i class="fas fa-lock position-absolute icon-input"></i>
                      <input
                        type="password"
                        id="confirmPassword"
                        v-model="confirmPassword"
                        class="form-control floating-input"
                        :class="{ 'has-content': confirmPassword }"
                        @focus="setFocus('confirmPassword', true)"
                        @blur="setFocus('confirmPassword', false)"
                        required
                      />
                      <label
                        for="confirmPassword"
                        class="floating-label position-absolute fs-6"
                        :class="{ active: confirmPasswordFocused || !!confirmPassword }"
                      >
                        Confirmar contraseña
                      </label>
                    </div>

                    <div class="d-flex justify-content-between mt-3">
                      <button type="button" class="btn btn-outline-secondary" @click="goToLogin">
                        Cancelar
                      </button>
                      <button type="submit" class="btn btn-success">
                        Registrarse
                      </button>
                    </div>
                  </form>
                  <div class="mt-4 mb-5 d-flex justify-content-end align-items-center">
                    <h6 class="title mb-0 me-1">Ya tengo una cuenta.</h6>
                    <a href="#" class="green-link" @click.prevent="goToLogin">Iniciar sesión</a>
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
import { mapActions } from 'vuex'
import { getErrorDetails } from '@/utils/utils'

export default {
  data() {
    return {
      showRegister: false,
      logoUrl: '/logo-letras.png',
      user: {
        email: '',
        username: '',
        name: '',
        lastName: '',
        secondLastName: '',
        phoneNumber: '',
        password: ''
      },
      confirmPassword: '',
      usernameFocused: false,
      nameFocused: false,
      lastNameFocused: false,
      secondLastNameFocused: false,
      phoneNumberFocused: false,
      emailFocused: false,
      passwordFocused: false,
      confirmPasswordFocused: false,
      isNavigating: false
    }
  },
  mounted() {
    setTimeout(() => {
      this.showRegister = true
    }, 100)
  },
  methods: {
    ...mapActions('auth', {
      singUp: 'singUp'
    }),
    register() {
      if (this.user.password !== this.confirmPassword) {
        this.$alert.error('Las contraseñas no conciden')
        return
      }
      this.singUp(this.user)
        .then((response) => {
          this.$alert.success({
            title: 'Usuario registrado',
            text: response.data.message,
            nextRoute: 'authLogin'
          })
        })
        .catch((error) => {
          this.$alert.error(getErrorDetails(error))
        })
    },
    setFocus(field, focused) {
      const map = {
        username: 'usernameFocused',
        name: 'nameFocused',
        lastName: 'lastNameFocused',
        secondLastName: 'secondLastNameFocused',
        phoneNumber: 'phoneNumberFocused',
        email: 'emailFocused',
        password: 'passwordFocused',
        confirmPassword: 'confirmPasswordFocused'
      }
      const key = map[field]
      if (key) this[key] = focused
    },
    goToLogin() {
      if (this.isNavigating) return
      this.isNavigating = true
      this.showRegister = false
      setTimeout(() => {
        this.$router.push({ name: 'authLogin' }).catch(() => {})
      }, 600)
    }
  }
}
</script>

<style scoped>
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
.fade-enter-active, .fade-leave-active {
  transition: opacity 0.6s ease;
}
.fade-enter-from, .fade-leave-to {
  opacity: 0;
}

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

hr{
  height: 4px;
  background-color: #35aa06;
  border-color: #35aa06;
}
.sub-text {
  font-family: 'Open Sans', sans-serif;
  font-weight: 500;
  font-size: 16px;
  color: #1B515E;
}
.title {
  font-family: 'Open Sans', sans-serif;
  font-weight: 400;
  font-size: 12px;
  color: #1B515E;
}
.btn-outline-secondary {
  color: #1B515E !important;
  background-color: #ABCD9E !important;
  border-color: #ABCD9E !important;
}
.btn-outline-secondary:focus {
  color: #fff !important;
  background-color: #1B515E !important;
  border-color: #1B515E !important;
}
.btn-success {
  color: #ABCD9E !important;
  background-color: #1B515E !important;
  border-color: #1B515E !important;
}
.btn-success:focus {
  color: #1B515E !important;
  background-color: #ABCD9E !important;
  border-color: #ABCD9E !important;
}
.green-link {
  color: #35aa06 !important;
  font-size: 12px !important;
}
.green-link:hover {
  color: #2e9005 !important;
}
</style>
