<template>
  <transition name="fade">
    <div v-if="show" class="container my-5">
      <div class="header">
        <h2 class="title text-center fw-bold">Escoge tus preferencias de viaje</h2>
        <h6 class="sub-text text-center mb-4">
          Se recomendarán lugares de acuerdo a tus preferencias
        </h6>
        <hr />
      </div>

      <div class="categorias-wrapper">
        <div class="row g-3">
          <div
            v-for="(categoria, index) in categorias"
            :key="index"
            class="col-6 col-md-4 col-lg-3"
          >
            <div
              class="categoria-card d-flex flex-column justify-content-center align-items-center"
              :class="{ selected: selectedCategorias.includes(index) }"
              @click="toggleCategoria(index)"
              role="button"
              tabindex="0"
              @keydown.enter.prevent="toggleCategoria(index)"
              @keydown.space.prevent="toggleCategoria(index)"
              :style="{ backgroundImage: `url(${categoria.img})` }"
              loading="lazy"
            >
              <span class="categoria-text">
                <strong>{{ categoria.name.split(' ')[0] }}</strong>
                <span v-if="categoria.name.split(' ').length > 1">
                  <br />
                  {{ categoria.name.split(' ').slice(1).join(' ') }}
                </span>
              </span>
              <span v-if="selectedCategorias.includes(index)" class="selected-label"
                >Seleccionada ✓</span
              >
            </div>
          </div>
        </div>
      </div>

      <div class="text-center mt-4">
        <hr class="mt-0" />
        <button class="btn-confirmar" @click="confirmarSeleccion">Confirmar</button>
      </div>
    </div>
  </transition>
</template>

<script>
// import axios from "axios";

export default {
  name: 'UserPreferences',
  data() {
    return {
      show: false,
      // DATOS DE EJEMPLO - REEMPLAZAR CON API
      categorias: [
        { name: 'Museos', img: '/public/museos.webp' },
        { name: 'Parques', img: '/images/parques.webp' },
        { name: 'Restaurantes', img: '/images/restaurantes.webp' },
        { name: 'Playas', img: '/images/playas.webp' },
        { name: 'Zonas Arqueológicas', img: '/images/zonas.webp' },
        { name: 'Montañas', img: '/images/montanas.webp' },
        { name: 'Pueblos Mágicos', img: '/images/pueblos.webp' },
        { name: 'Cenotes', img: '/images/cenotes.webp' },
        { name: 'Lagunas', img: '/images/lagunas.webp' },
        { name: 'Hoteles', img: '/images/hoteles.webp' },
        { name: 'Bares', img: '/images/bares.webp' },
        { name: 'Cafeterías', img: '/images/cafeterias.webp' },
        { name: 'Centros Comerciales', img: '/images/centros.webp' },
        { name: 'Balnearios', img: '/images/balnearios.webp' },
        { name: 'Eventos Culturales', img: '/images/eventos.webp' },
      ],      
      selectedCategorias: [],
    }
  },
  mounted() {
    this.show = true
    // this.cargarCategorias()
  },
  methods: {
    /*
    async cargarCategorias() {
      try {
        const response = await axios.get('/api/categorias')
        this.categorias = response.data.map(cat => ({
          id: cat.id,
          name: cat.nombre,
          img: cat.imagen_url
        }))
      } catch (error) {
        console.error('Error al cargar categorías:', error)
      }
    },
    */
    
    toggleCategoria(index) {
      const i = this.selectedCategorias.indexOf(index)
      if (i !== -1) {
        this.selectedCategorias.splice(i, 1)
      } else {
        this.selectedCategorias.push(index)
      }
    },
    
    confirmarSeleccion() {
      this.show = false

      setTimeout(() => {
        console.log('IDs seleccionadas:', this.selectedCategorias)

        //PREFERENCIAS
        /*
        axios.post('[API]'], {
          categorias: this.selectedCategorias,
          // user_id: this.$store.state.user.id
        })
          .then((res) => {
            console.log('Preferencias guardadas:', res.data)
            // this.$router.push("home");
          })
          .catch((err) => {
            console.error('Error al guardar preferencias:', err)
            //notificación al usuario
          })
        */
      }, 500)
    },
  },
}
</script>

<style scoped>
.header {
  position: sticky;
  top: 0;
  background-color: #ffffff;
  z-index: 10;
  padding-bottom: 0px;
}

.categorias-wrapper {
  max-height: clamp(300px, 63vh, 600px);
  overflow-y: auto;
  padding-top: 10px;
}

.categorias-wrapper::-webkit-scrollbar {
  display: none;
}

.categoria-card {
  position: relative;
  border: 2px solid #abcd9e;
  border-radius: 12px;
  height: 100px;
  user-select: none;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.08);
  transition:
    transform 0.15s ease,
    border-color 0.2s ease;
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
}

.categoria-text {
  position: absolute;
  bottom: 8px;
  left: 8px;
  font-size: clamp(16px, 1vw, 20px);
  font-weight: normal;
  color: #1b515e;
  line-height: 1.1;
}
.categoria-text strong {
  font-weight: bold;
}

.categoria-card:not(.selected):hover {
  transform: translateY(-4px);
  border-color: #1b515e;
  cursor: pointer;
}

.categoria-card.selected {
  border-color: #1b515e;
}

.categoria-card.selected:hover {
  border-color: #1b515e;
  transform: translateY(-4px);
}
.selected-label {
  position: absolute;
  top: 8px;
  right: 8px;
  background-color: #abcd9e;
  color: #1b515e;
  padding: clamp(1px, 0.3vw, 3px) clamp(2px, 0.5vw, 6px);
  border-radius: 6px;
  font-size: clamp(0.5rem, 1vw, 0.75rem);
  font-weight: bold;
}

hr {
  height: 4px;
  background-color: #35aa06;
  border-color: #35aa06;
}

.title {
  color: #1b515e;
}

.sub-text {
  font-weight: 400;
  font-size: 12px;
  color: #1b515e;
  text-align: center;
}

.btn-confirmar {
  background-color: #1b515e;
  color: #ffffff;
  border: none;
  border-radius: 12px;
  padding: 10px 20px;
  font-size: 1rem;
  cursor: pointer;
  transition:
    background-color 0.2s ease,
    transform 0.15s ease;
}

.btn-confirmar:hover {
  background-color: #2a798d;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px #1b515e;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.5s ease;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
