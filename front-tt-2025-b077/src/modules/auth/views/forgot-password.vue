<template>
  <div class="d-flex justify-content-center align-items-center vh-100 bg-white">
    <transition name="fade" mode="out-in">
      <div v-if="showForgot" class="container">
        <div class="row justify-content-center">
          <div class="col-12 col-lg-10">
            <div class="card shadow-lg overflow-hidden">
              <div class="row g-0 min-vh-400">
                
                <!-- Logo -->
                <div class="col-12 col-md-6 order-1 order-md-2 d-flex justify-content-center align-items-center p-5 bg-white">
                  <img :src="logoUrl" alt="Logo letras" class="img-fluid logo-size" />
                </div>

                <div class="col-12 col-md-6 order-2 order-md-1 d-flex flex-column justify-content-center p-4 bg-white mt-2">
                  <h4 class="text-center mb-2 sub-text">
                    Recuperar contraseña
                  </h4>
                  <h4 class="text-center mb-2 title mt-4">
                    Introduce tu correo electrónico, te enviaremos un correo de validación para cambiar tu contraseña.
                  </h4>
                  <hr />
                  
                  <form @submit.prevent="recuperarPassword">
                    <div class="mb-6 position-relative has-icon">
                      <i class="fa-solid fa-envelope position-absolute icon-input"></i>
                      <input
                        type="email"
                        id="correoRecuperacion"
                        v-model="correoRecuperacion"
                        class="form-control floating-input mt-4"
                        :class="{ 'has-content': correoRecuperacion }"
                        @focus="setFocus('correoRecuperacion', true)"
                        @blur="setFocus('correoRecuperacion', false)"
                        required
                      />
                      <label 
                        for="correoRecuperacion" 
                        class="floating-label position-absolute fs-6"
                        :class="{ 'active': correoRecuperacionFocused || correoRecuperacion }"
                      >
                        Correo electrónico
                      </label>
                    </div>

                    <div class="d-flex justify-content-between mt-3">
                      <button type="button" class="btn btn-outline-secondary" @click="volverALogin">
                        Volver
                      </button>
                      <button type="submit" class="btn btn-success">
                        Recuperar mi contraseña
                      </button>
                    </div>

                    <div 
                      v-if="mensajeRecuperacion" 
                      class="alert-success-custom mt-4 d-flex align-items-start text-justify"
                    >
                      <i class="fa-regular fa-circle-check check-icon me-2"></i>
                      <span>{{ mensajeRecuperacion }}</span>
                    </div>
                  </form>
                  
                  <div class="mt-5 mb-5 d-flex justify-content-end align-items-center">
                    <h6 class="title mb-0 me-1">No tengo una cuenta</h6>
                    <a href="#" class="green-link" @click.prevent="irARegistro">
                      Registrarse
                    </a>
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
import { mapActions } from 'vuex';

export default {
  name: 'ForgotPasswordComponent',
  data() {
    return {
      showForgot: false,
      logoUrl: '/logo-letras.png',
      correoRecuperacion: '',
      correoRecuperacionFocused: false,
      mensajeRecuperacion: '',
      isNavigating: false
    };
  },
  mounted() {
    setTimeout(() => {
      this.showForgot = true;
    }, 100);
  },
  methods: {
    ...mapActions('auth', {
      forgotPassword: 'forgotPassword'
    }),
    recuperarPassword() {
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

      if (!this.correoRecuperacion) {
        this.mensajeRecuperacion = "Por favor, ingresa tu correo electrónico.";
        return;
      }

      this.forgotPassword(this.correoRecuperacion)
        .then((response) => {
          this.$alert.success({
            title: 'Email enviado',
            text: response.data.message,
            nextRoute: 'authLogin'
          })
        })
        .catch((error) => {
          this.$alert.error(error.response.data.message)
        })
      
    },
    setFocus(field, focused) {
      if (field === 'correoRecuperacion') {
        this.correoRecuperacionFocused = focused;
      }
    },
    volverALogin() {
      if (this.isNavigating) return;
      this.isNavigating = true;
      this.showForgot = false;

      setTimeout(() => {
        this.$router.push({ name: 'authLogin' }).catch(() => {});
      }, 600);
    },
    irARegistro() {
      if (this.isNavigating) return;
      this.isNavigating = true;
      this.showForgot = false;

      setTimeout(() => {
        this.$router.push({ name: 'authRegister' }).catch(() => {});
      }, 600);
    }
  }
};
</script>

<style scoped>
.fade-enter-active, .fade-leave-active {
  transition: opacity 0.6s ease;
}
.fade-enter-from, .fade-leave-to {
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
  border-color: #1B515E !important;
}

.green-link {
  color: #35aa06 !important;
  font-size: 12px !important;
}
.green-link:hover {
  color: #2e9005 !important;
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

.alert-success-custom {
  background-color: #d4edda;
  border: 1px solid #28a745;
  color: #155724;
  padding: 10px 12px;
  border-radius: 8px;
  font-size: 13px;
  line-height: 1.4;
}

.check-icon {
  color: #28a745;
  font-weight: bold;
  font-size: 16px;
  line-height: 1;
  margin-top: 2px;
}
</style>