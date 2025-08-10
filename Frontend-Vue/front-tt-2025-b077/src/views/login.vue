<template>
  <div class="d-flex justify-content-center align-items-center vh-100 bg-white">
    <transition name="fade" mode="out-in">
      <div v-if="showLogin" :key="viewMode" class="container">
        <div class="row justify-content-center">
          <div class="col-12 col-lg-10">
            <div class="card shadow-lg overflow-hidden">
              <div class="row g-0 min-vh-400">
                
                <!-- Logo -->
                <div class="col-12 col-md-6 order-1 order-md-2 d-flex justify-content-center align-items-center p-5 bg-white">
                  <img :src="logoUrl" alt="Logo letras" class="img-fluid logo-size" />
                </div>

                <!-- Formulario principal -->
                <div class="col-12 col-md-6 order-2 order-md-1 d-flex flex-column justify-content-center p-4 bg-white mt-2">

                  <!-- LOGIN -->
                  <div v-if="viewMode === 'login'">
                    <h4 class="text-center mb-2 sub-text">
                      Iniciar sesión o registrate
                    </h4>
                    <hr />
                    <h4 class="text-center mb-2 title mt-4">
                      ¡Te damos la bienvenida!
                    </h4>

                    <form @submit.prevent="iniciarSesion">
                      <!-- Campo Usuario -->
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
                          Nombre de usuario o correo electrónico
                        </label>
                      </div>

                      <!-- Campo Contraseña -->
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

                      <!-- Olvidé contraseña -->
                      <div class="mt-0 mb-5 d-flex justify-content-end align-items-center">
                        <h6 class="title mb-0 me-1">¿Necesitas ayuda?</h6>
                        <a href="#" class="link-verde" @click.prevent="viewMode = 'forgot'">
                          Olvidé mi contraseña
                        </a>
                      </div>

                      <div class="d-flex justify-content-between mt-3">
                        <button type="button" class="btn btn-outline-secondary" @click="cancelar">
                          Cancelar
                        </button>
                        <button type="submit" class="btn btn-success">
                          Iniciar sesión
                        </button>
                      </div>
                    </form>

                    <!-- Registrarse: ahora usa método para animar antes de navegar -->
                    <div class="mt-4 mb-5 d-flex justify-content-end align-items-center">
                      <h6 class="title mb-0 me-1">No tengo una cuenta</h6>
                      <a href="#" class="link-verde" @click.prevent="irARegistro">
                        Registrarse
                      </a>
                    </div>
                  </div>

                  <!-- RECUPERAR CONTRASEÑA -->
                  <div v-else-if="viewMode === 'forgot'">
                    <h4 class="text-center mb-2 sub-text">
                      Recuperar contraseña
                    </h4>
                    <hr />
                    <form @submit.prevent="recuperarContrasena">
                      <div class="mb-4 position-relative">
                        <input
                          type="email"
                          id="email"
                          v-model="email"
                          class="form-control floating-input mt-4"
                          :class="{ 'has-content': email }"
                          @focus="setFocus('email', true)"
                          @blur="setFocus('email', false)"
                          required
                        />
                        <label 
                          for="email" 
                          class="floating-label position-absolute fs-6"
                          :class="{ 'active': emailFocused || email }"
                        >
                          Correo electrónico
                        </label>
                      </div>

                      <div class="d-flex justify-content-between mt-3">
                        <button type="button" class="btn btn-outline-secondary" @click="viewMode = 'login'">
                          Volver
                        </button>
                        <button type="submit" class="btn btn-success">
                          Enviar
                        </button>
                      </div>
                    </form>
                  </div>

                </div>
              </div>
            </div>
          </div>
        </div>
      </div> <!-- cierre v-if showLogin -->
    </transition>
  </div>
</template>

<script>
export default {
  data() {
    return {
      showLogin: false,
      viewMode: 'login', // 'login' o 'forgot'
      logoUrl: '/logo-letras.png',
      usuario: '',
      password: '',
      usuarioFocused: false,
      passwordFocused: false,
      email: '',
      emailFocused: false,
      isNavigating: false // evita doble click en "Registrarse"
    };
  },
  mounted() {
    // Mostrar con pequeño delay para activar transición de entrada
    setTimeout(() => {
      this.showLogin = true;
    }, 100);
  },
  methods: {
    iniciarSesion() {
      // Si quieres que el login también haga fade antes de navegar,
      // podemos replicar la lógica de irARegistro aquí.
      alert(`Usuario: ${this.usuario}\nContraseña: ${this.password}`);
    },
    recuperarContrasena() {
      alert(`Se enviará un correo a: ${this.email}`);
      this.viewMode = 'login';
    },
    cancelar() {
      this.usuario = '';
      this.password = '';
      this.usuarioFocused = false;
      this.passwordFocused = false;
    },
    setFocus(field, focused) {
      if (field === 'usuario') {
        this.usuarioFocused = focused;
      } else if (field === 'password') {
        this.passwordFocused = focused;
      } else if (field === 'email') {
        this.emailFocused = focused;
      }
    },
    irARegistro() {
      if (this.isNavigating) return;
      this.isNavigating = true;

      // Disparamos la animación de salida del contenedor
      this.showLogin = false;

      // Esperamos a que termine la transición CSS (0.6s)
      setTimeout(() => {
        // Navegamos a la ruta de registro
        this.$router.push('/registro').catch(() => {});
        // No necesitamos resetear isNavigating porque el componente se destruye al cambiar de ruta
      }, 600); // coincide con .fade-enter-active (0.6s)
    }
  }
};
</script>

<style scoped>
/* Transición de fade (principal) */
.fade-enter-active, .fade-leave-active {
  transition: opacity 0.6s ease;
}
.fade-enter-from, .fade-leave-to {
  opacity: 0;
}

/* (Si tienes transición interna, mantenla igual si quieres) */
/* ...si usas fade-inner, lo puedes mantener; no lo incluí aquí porque tu request pedía
   que no se quiten transiciones existentes. Si quieres, lo vuelvo a añadir. */

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

.link-verde {
  color: #35aa06 !important;
  font-size: 12px !important;
}
.link-verde:hover {
  color: #2e9005 !important;
}
</style>
