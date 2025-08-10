<template>
  <div class="d-flex justify-content-center align-items-center vh-100 bg-white">
    <transition name="fade" mode="out-in">
      <div v-if="showRegister" class="container" key="register">
        <div class="row justify-content-center">
          <div class="col-12 col-lg-10">
            <div class="card shadow-lg overflow-hidden">
              <div class="row g-0 min-vh-400">
                <!-- Logo -->
                <div class="mb-0 col-12 col-md-6 order-1 order-md-2 d-flex justify-content-center align-items-center p-5 bg-white">
                  <img :src="logoUrl" alt="Logo letras" class="img-fluid logo-size" />
                </div>

                <!-- Formulario -->
                <div class="col-12 col-md-6 order-2 order-md-1 d-flex flex-column justify-content-center p-4 bg-white mt-2">
                  <h4 class="text-center mb-2 sub-text">
                    Completa los campos para crear tu cuenta.
                  </h4>
                  <hr />
                  <form @submit.prevent="registroDatos">
                    <div class="mb-4 position-relative">
                      <input
                        type="text"
                        id="usuario"
                        v-model="usuario"
                        class="form-control floating-input mt-4"
                        :class="{ 'has-content': usuario }"
                        @focus="setFocus('usuario', true)"
                        @blur="setFocus('usuario', false)"
                        required
                      />
                      <label
                        for="usuario"
                        class="floating-label position-absolute fs-6"
                        :class="{ 'active': usuarioFocused || usuario }"
                      >
                        Nombre de usuario
                      </label>
                    </div>

                    <div class="mb-4 position-relative">
                      <input
                        type="text"
                        id="nombre"
                        v-model="nombre"
                        class="form-control floating-input mt-4"
                        :class="{ 'has-content': nombre }"
                        @focus="setFocus('nombre', true)"
                        @blur="setFocus('nombre', false)"
                        required
                      />
                      <label
                        for="nombre"
                        class="floating-label position-absolute fs-6"
                        :class="{ 'active': nombreFocused || nombre }"
                      >
                        Nombre(s)
                      </label>
                    </div>

                    <div class="mb-4 position-relative">
                      <input
                        type="text"
                        id="apellidos"
                        v-model="apellidos"
                        class="form-control floating-input mt-4"
                        :class="{ 'has-content': apellidos }"
                        @focus="setFocus('apellidos', true)"
                        @blur="setFocus('apellidos', false)"
                        required
                      />
                      <label
                        for="apellidos"
                        class="floating-label position-absolute fs-6"
                        :class="{ 'active': apellidosFocused || apellidos }"
                      >
                        Apellidos(s)
                      </label>
                    </div>

                    <div class="mb-4 position-relative">
                      <input
                        type="email"
                        id="correoElectronico"
                        v-model="correoElectronico"
                        class="form-control floating-input mt-4"
                        :class="{ 'has-content': correoElectronico }"
                        @focus="setFocus('correoElectronico', true)"
                        @blur="setFocus('correoElectronico', false)"
                        required
                      />
                      <label
                        for="correoElectronico"
                        class="floating-label position-absolute fs-6"
                        :class="{ 'active': correoElectronicoFocused || correoElectronico }"
                      >
                        Correo electrónico
                      </label>
                    </div>

                    <div class="mb-4 position-relative">
                      <input
                        type="password"
                        id="password"
                        v-model="password"
                        class="form-control floating-input"
                        :class="{ 'has-content': password }"
                        @focus="setFocus('password', true)"
                        @blur="setFocus('password', false)"
                        required
                      />
                      <label
                        for="password"
                        class="floating-label position-absolute fs-6"
                        :class="{ 'active': passwordFocused || password }"
                      >
                        Contraseña
                      </label>
                    </div>

                    <div class="mb-4 position-relative">
                      <input
                        type="password"
                        id="confirpassword"
                        v-model="confirpassword"
                        class="form-control floating-input"
                        :class="{ 'has-content': confirpassword }"
                        @focus="setFocus('confirpassword', true)"
                        @blur="setFocus('confirpassword', false)"
                        required
                      />
                      <label
                        for="confirpassword"
                        class="floating-label position-absolute fs-6"
                        :class="{ 'active': confirpasswordFocused || confirpassword }"
                      >
                        Confirme su contraseña
                      </label>
                    </div>

                    <div class="d-flex justify-content-between mt-3">
                      <button type="button" class="btn btn-outline-secondary" @click="irALogin">
                        Cancelar
                      </button>
                      <button type="submit" class="btn btn-success">
                        Registrarse
                      </button>
                    </div>
                  </form>
                  <div class="mt-4 mb-5 d-flex justify-content-end align-items-center">
                    <h6 class="title mb-0 me-1">Ya tengo una cuenta. </h6>
                    <a href="#" class="link-verde" @click.prevent="irALogin">Iniciar sesión</a>
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
export default {
  data() {
    return {
      showRegister: false,
      logoUrl: '/logo-letras.png',
      usuario: '',
      nombre: '',
      apellidos: '',
      correoElectronico: '',
      password: '',
      confirpassword: '',

      usuarioFocused: false,
      nombreFocused: false,
      apellidosFocused: false,
      correoElectronicoFocused: false,
      passwordFocused: false,
      confirpasswordFocused: false,

      isNavigating: false,
    };
  },
  mounted() {
    setTimeout(() => {
      this.showRegister = true;
    }, 100);
  },
  methods: {
    registroDatos() {
      alert(`Usuario: ${this.usuario}\nContraseña: ${this.password}`);
    },
    setFocus(field, focused) {
      this[`${field}Focused`] = focused;
    },
    cancelar() {
      this.usuario = '';
      this.nombre = '';
      this.apellidos = '';
      this.correoElectronico = '';
      this.password = '';
      this.confirpassword = '';

      this.usuarioFocused = false;
      this.nombreFocused = false;
      this.apellidosFocused = false;
      this.correoElectronicoFocused = false;
      this.passwordFocused = false;
      this.confirpasswordFocused = false;
    },
    irALogin() {
      if (this.isNavigating) return;
      this.isNavigating = true;
      this.showRegister = false;
      setTimeout(() => {
        this.$router.push('/login').catch(() => {});
      }, 600);
    }
  }
};
</script>

<style scoped>
/* Altura mínima personalizada */
.min-vh-400 {
  min-height: 400px;
}

/* Tamaño del logo responsivo */
.logo-size {
  max-height: 260px;
  object-fit: contain;
}

@media (max-width: 767.98px) {
  .logo-size {
    max-height: 120px;
  }
}

/* Floating input styles */
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

/* Transición de fade */
.fade-enter-active, .fade-leave-active {
  transition: opacity 0.6s ease;
}
.fade-enter-from, .fade-leave-to {
  opacity: 0;
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

/* Botón Cancelar: letra #1B515E, fondo #ABCD9E */
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

/* Botón Registrarse: letra #ABCD9E, fondo #1B515E */
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

.link-verde {
  color: #35aa06 !important;
  font-size: 12px !important;
}
.link-verde:hover {
  color: #2e9005 !important;
}
</style>
