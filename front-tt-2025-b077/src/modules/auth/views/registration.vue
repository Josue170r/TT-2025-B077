<template>
  <div class="d-flex justify-content-center align-items-center vh-100 bg-white">
    <transition name="fade" mode="out-in">
      <div v-if="showRegister" class="container" key="register">
        <div class="row justify-content-center">
          <div class="col-12 col-lg-10">
            <div class="card shadow-lg overflow-hidden">
              <div class="row g-0 min-vh-400">
                <div
                  class="mb-0 col-12 col-md-6 order-1 order-md-2 d-flex justify-content-center align-items-center p-5 bg-white"
                >
                  <img :src="logoUrl" alt="Logo" class="img-fluid logo-size" />
                </div>

                <div
                  class="col-12 col-md-6 order-2 order-md-1 d-flex flex-column justify-content-center p-4 bg-white mt-2"
                >
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
                        class="form-control floating-input"
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
                        Nombre de usuario *
                      </label>
                    </div>

                    <!-- Nombre(s) -->
                    <div class="mb-4 position-relative has-icon">
                      <i class="fas fa-id-card position-absolute icon-input"></i>
                      <input
                        type="text"
                        id="name"
                        v-model="user.name"
                        class="form-control floating-input"
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
                        Nombre(s) *
                      </label>
                    </div>

                    <!-- Primer apellido -->
                    <div class="mb-4 position-relative has-icon">
                      <i class="fas fa-id-card position-absolute icon-input"></i>
                      <input
                        type="text"
                        id="lastName"
                        v-model="user.lastName"
                        class="form-control floating-input"
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
                        Primer apellido *
                      </label>
                    </div>

                    <!-- Segundo apellido -->
                    <div class="mb-4 position-relative has-icon">
                      <i class="fas fa-id-card position-absolute icon-input"></i>
                      <input
                        type="text"
                        id="secondLastName"
                        v-model="user.secondLastName"
                        class="form-control floating-input"
                        :class="{ 'has-content': user.secondLastName }"
                        @focus="setFocus('secondLastName', true)"
                        @blur="setFocus('secondLastName', false)"
                      />
                      <label
                        for="secondLastName"
                        class="floating-label position-absolute fs-6"
                        :class="{ active: secondLastNameFocused || !!user.secondLastName }"
                      >
                        Segundo apellido (opcional)
                      </label>
                    </div>

                    <!-- Número de teléfono -->
                    <div class="mb-4 position-relative has-icon">
                      <i class="fas fa-phone position-absolute icon-input"></i>
                      <input
                        type="tel"
                        id="phoneNumber"
                        v-model="user.phoneNumber"
                        class="form-control floating-input"
                        :class="{ 'has-content': user.phoneNumber }"
                        @focus="setFocus('phoneNumber', true)"
                        @blur="setFocus('phoneNumber', false)"
                        required
                      />
                      <label
                        for="phoneNumber"
                        class="floating-label position-absolute fs-6"
                        :class="{ active: phoneNumberFocused || !!user.phoneNumber }"
                      >
                        Número de teléfono *
                      </label>
                    </div>

                    <!-- Correo electrónico -->
                    <div class="mb-4 position-relative has-icon">
                      <i class="fas fa-envelope position-absolute icon-input"></i>
                      <input
                        type="email"
                        id="email"
                        v-model="user.email"
                        class="form-control floating-input"
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
                        Correo electrónico *
                      </label>
                    </div>

                    <!-- Contraseña -->
                    <div class="mb-2 position-relative has-icon">
                      <i class="fas fa-lock position-absolute icon-input"></i>
                      <input
                        :type="showPassword ? 'text' : 'password'"
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
                        Contraseña *
                      </label>
                      <i 
                        :class="showPassword ? 'fas fa-eye-slash' : 'fas fa-eye'" 
                        class="position-absolute eye-icon"
                        @click="showPassword = !showPassword"
                      ></i>
                    </div>

                    <!-- Validaciones de contraseña -->
                    <div class="password-requirements mb-3" v-if="user.password || passwordFocused">
                      <small class="d-block" :class="passwordValidation.minLength ? 'text-success' : 'text-danger'">
                        <i :class="passwordValidation.minLength ? 'fas fa-check-circle' : 'fas fa-times-circle'"></i>
                        Mínimo 8 caracteres
                      </small>
                      <small class="d-block" :class="passwordValidation.hasUpperCase ? 'text-success' : 'text-danger'">
                        <i :class="passwordValidation.hasUpperCase ? 'fas fa-check-circle' : 'fas fa-times-circle'"></i>
                        Al menos una letra mayúscula
                      </small>
                      <small class="d-block" :class="passwordValidation.hasLowerCase ? 'text-success' : 'text-danger'">
                        <i :class="passwordValidation.hasLowerCase ? 'fas fa-check-circle' : 'fas fa-times-circle'"></i>
                        Al menos una letra minúscula
                      </small>
                      <small class="d-block" :class="passwordValidation.hasNumber ? 'text-success' : 'text-danger'">
                        <i :class="passwordValidation.hasNumber ? 'fas fa-check-circle' : 'fas fa-times-circle'"></i>
                        Al menos un número
                      </small>
                      <small class="d-block" :class="passwordValidation.hasSpecialChar ? 'text-success' : 'text-danger'">
                        <i :class="passwordValidation.hasSpecialChar ? 'fas fa-check-circle' : 'fas fa-times-circle'"></i>
                        Al menos un carácter especial (@, #, $, %, etc.)
                      </small>
                    </div>

                    <!-- Confirmar contraseña -->
                    <div class="mb-2 position-relative has-icon">
                      <i class="fas fa-lock position-absolute icon-input"></i>
                      <input
                        :type="showConfirmPassword ? 'text' : 'password'"
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
                        Confirmar contraseña *
                      </label>
                      <i 
                        :class="showConfirmPassword ? 'fas fa-eye-slash' : 'fas fa-eye'" 
                        class="position-absolute eye-icon"
                        @click="showConfirmPassword = !showConfirmPassword"
                      ></i>
                    </div>

                    <!-- Validación de coincidencia de contraseñas -->
                    <div class="password-match mb-4" v-if="confirmPassword">
                      <small :class="passwordsMatch ? 'text-success' : 'text-danger'">
                        <i :class="passwordsMatch ? 'fas fa-check-circle' : 'fas fa-times-circle'"></i>
                        {{ passwordsMatch ? 'Las contraseñas coinciden' : 'Las contraseñas no coinciden' }}
                      </small>
                    </div>

                    <div class="d-flex justify-content-between mt-3">
                      <button type="button" class="btn btn-outline-secondary" @click="goToLogin">
                        Cancelar
                      </button>
                      <button type="submit" class="btn btn-success" :disabled="!isFormValid">Registrarse</button>
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
        password: '',
      },
      confirmPassword: '',
      showPassword: false,
      showConfirmPassword: false,
      usernameFocused: false,
      nameFocused: false,
      lastNameFocused: false,
      secondLastNameFocused: false,
      phoneNumberFocused: false,
      emailFocused: false,
      passwordFocused: false,
      confirmPasswordFocused: false,
      isNavigating: false,
    }
  },
  computed: {
    passwordValidation() {
      const password = this.user.password
      return {
        minLength: password.length >= 8,
        hasUpperCase: /[A-Z]/.test(password),
        hasLowerCase: /[a-z]/.test(password),
        hasNumber: /[0-9]/.test(password),
        hasSpecialChar: /[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]/.test(password),
      }
    },
    isPasswordValid() {
      return Object.values(this.passwordValidation).every(v => v === true)
    },
    passwordsMatch() {
      return this.user.password === this.confirmPassword && this.confirmPassword !== ''
    },
    isFormValid() {
      return this.isPasswordValid && 
             this.passwordsMatch && 
             this.user.username && 
             this.user.name && 
             this.user.lastName && 
             this.user.phoneNumber && 
             this.user.email
    }
  },
  mounted() {
    setTimeout(() => {
      this.showRegister = true
    }, 100)
  },
  methods: {
    ...mapActions('auth', {
      singUp: 'singUp',
    }),
    register() {
      if (!this.isPasswordValid) {
        this.$alert.error('La contraseña no cumple con los requisitos de seguridad')
        return
      }
      if (this.user.password !== this.confirmPassword) {
        this.$alert.error('Las contraseñas no coinciden')
        return
      }
      this.singUp(this.user)
        .then((response) => {
          this.$alert.success({
            title: 'Usuario registrado',
            text: response.data.message,
            nextRoute: 'authLogin',
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
        confirmPassword: 'confirmPasswordFocused',
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
    },
  },
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
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.6s ease;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
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

.eye-icon {
  right: 15px;
  top: 50%;
  transform: translateY(-50%);
  color: #1b515e;
  font-size: 16px;
  cursor: pointer;
  z-index: 3;
  transition: color 0.3s ease;
}

.eye-icon:hover {
  color: #35aa06;
}

.has-icon .floating-input {
  padding-left: 2.5rem !important;
  padding-right: 2.8rem !important;
}

.has-icon .floating-label {
  left: 40px !important;
  z-index: 2 !important;
}

.password-requirements {
  padding: 10px;
  background-color: #f8f9fa;
  border-radius: 8px;
  border-left: 3px solid #1b515e;
}

.password-requirements small {
  font-size: 11px;
  margin-bottom: 4px;
  transition: all 0.3s ease;
}

.password-requirements i {
  margin-right: 6px;
  font-size: 10px;
}

.password-match {
  padding: 8px 10px;
  background-color: #f8f9fa;
  border-radius: 8px;
}

.password-match small {
  font-size: 12px;
  font-weight: 500;
}

.password-match i {
  margin-right: 6px;
  font-size: 11px;
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
  border-color: #abcd9e !important;
}
.btn-success:disabled {
  opacity: 0.5;
  cursor: not-allowed;
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
</style>