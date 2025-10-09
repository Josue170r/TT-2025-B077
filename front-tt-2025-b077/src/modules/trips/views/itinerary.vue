<template>
  <NavTopIItinerary />

  <div style="padding-top: 55px;"></div>

  <div class="contenedor-cards">
    <div
      v-for="(itinerario, index) in itinerarios"
      :key="index"
      class="card-itinerario"
    >
      <img :src="itinerario.imagen" alt="Imagen del itinerario" class="imagen-itinerario" />

      <div class="info-itinerario">
        <h3>{{ itinerario.titulo }}</h3>
        <ul>
          <li>{{ itinerario.lugar }}</li>
          <li>{{ itinerario.fechas }}</li>
          <li>{{ itinerario.lugares }} lugares por visitar</li>
        </ul>
        <p class="mensaje">{{ itinerario.mensaje }}</p>
        <button class="btn-ver" @click="verItinerario(itinerario)">Ver itinerario</button>
      </div>
    </div>
  </div>

  <BottomNavbar />
</template>

<script>
import BottomNavbar from "@/components/bottomnavbar.vue";
import NavTopIItinerary from "@/components/NavTopIItinerary.vue";
// import axios from 'axios';

export default {
  components: { BottomNavbar, NavTopIItinerary },

  data() {
    return {
      itinerarios: [
        // Datos de ejemplo - se reemplazarán con datos del backend
        {
          titulo: "Mi viaje por Ciudad de México",
          lugar: "Ciudad de México",
          fechas: "14 al 22 de marzo de 2025",
          lugares: 10,
          mensaje: "¡El viaje ha comenzado, disfrútalo!",
          imagen: "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRTK9g6rwdWHXNjBfLwkHyqZxBKoHLJhWV85w&s"
        },
        {
          titulo: "Aventura en Oaxaca",
          lugar: "Oaxaca de Juárez",
          fechas: "1 al 5 de abril de 2025",
          lugares: 8,
          mensaje: "Prepárate para una experiencia cultural única.",
          imagen: "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTgmhl0umbnM4LQ4TREc1kjaYB9mAKELKt8sg&s"
        },
        {
          titulo: "Mi viaje por Ciudad de México",
          lugar: "Ciudad de México",
          fechas: "14 al 22 de marzo de 2025",
          lugares: 10,
          mensaje: "¡El viaje ha comenzado, disfrútalo!",
          imagen: "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRTK9g6rwdWHXNjBfLwkHyqZxBKoHLJhWV85w&s"
        },
        {
          titulo: "Aventura en Oaxaca",
          lugar: "Oaxaca de Juárez",
          fechas: "1 al 5 de abril de 2025",
          lugares: 8,
          mensaje: "Prepárate para una experiencia cultural única.",
          imagen: "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTgmhl0umbnM4LQ4TREc1kjaYB9mAKELKt8sg&s"
        },
        {
          titulo: "Mi viaje por Ciudad de México",
          lugar: "Ciudad de México",
          fechas: "14 al 22 de marzo de 2025",
          lugares: 10,
          mensaje: "¡El viaje ha comenzado, disfrútalo!",
          imagen: "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRTK9g6rwdWHXNjBfLwkHyqZxBKoHLJhWV85w&s"
        },
        {
          titulo: "Aventura en Oaxaca",
          lugar: "Oaxaca de Juárez",
          fechas: "1 al 5 de abril de 2025",
          lugares: 8,
          mensaje: "Prepárate para una experiencia cultural única.",
          imagen: "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTgmhl0umbnM4LQ4TREc1kjaYB9mAKELKt8sg&s"
        }
      ]
    };
  },

  // Descomenta el mounted cuando conectes el backend
  // mounted() {
  //   this.cargarItinerarios();
  // },

  methods: {
    // async cargarItinerarios() {
    //   try {
    //     const response = await axios.get('TU_URL_DEL_BACKEND/api/itinerarios');
    //     this.itinerarios = response.data;
    //   } catch (error) {
    //     console.error('Error al cargar los itinerarios:', error);
    //   }
    // },

    verItinerario(itinerario) {
      alert(`Abriendo itinerario: ${itinerario.titulo}`);
      // this.$router.push({ name: 'DetalleItinerario', params: { id: itinerario.id } });
    }
  }
};
</script>

<style scoped>
.contenedor-cards {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
  padding: 20px;
  padding-bottom: 80px; /* Espacio para el BottomNavbar */
}

.card-itinerario {
  display: flex;
  background: #fff;
  border-radius: 15px;
  box-shadow: 0 4px 10px rgba(0,0,0,0.1);
  overflow: hidden;
  width: 100%;
  max-width: 500px;
}

.imagen-itinerario {
  width: 40%;
  object-fit: cover;
  min-height: 200px;
}

.info-itinerario {
  padding: 15px;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.info-itinerario h3 {
  color: #1a3c40;
  margin-bottom: 8px;
  font-size: 1.1rem;
}

.info-itinerario ul {
  list-style: none;
  padding: 0;
  margin: 0 0 10px 0;
}

.info-itinerario li {
  color: #555;
  font-size: 14px;
  margin-bottom: 4px;
}

.mensaje {
  color: #777;
  font-size: 13px;
  margin-bottom: 12px;
  flex-grow: 1;
}

.btn-ver {
  background-color: #1a3c40;
  color: white;
  border: none;
  border-radius: 8px;
  padding: 8px 16px;
  cursor: pointer;
  transition: background 0.3s;
  font-size: 14px;
  width: 100%;
}

.btn-ver:hover {
  background-color: #0e2325;
}

/* Tablets (768px - 1023px) */
@media (min-width: 768px) {
  .contenedor-cards {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 20px;
    padding: 30px 20px;
    padding-bottom: 100px;
    max-width: 1200px;
    margin: 0 auto;
  }

  .card-itinerario {
    max-width: 100%;
  }

  .imagen-itinerario {
    width: 45%;
    min-height: 220px;
  }

  .info-itinerario {
    padding: 20px;
    position: relative;
  }

  .info-itinerario h3 {
    font-size: 1.2rem;
    margin-bottom: 12px;
  }

  .info-itinerario li {
    font-size: 14px;
    margin-bottom: 5px;
  }

  .mensaje {
    font-size: 13px;
    margin-bottom: 35px;
  }

  .btn-ver {
    padding: 6px 14px;
    font-size: 13px;
    width: auto;
    position: absolute;
    bottom: 15px;
    right: 15px;
  }
}

/* Pantallas grandes (1024px+) */
@media (min-width: 1024px) {
  .contenedor-cards {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 25px;
    padding: 40px 40px;
    padding-bottom: 120px;
    max-width: 1400px;
    margin: 0 auto;
  }

  .card-itinerario {
    max-width: 100%;
  }

  .imagen-itinerario {
    width: 280px;
    min-height: 250px;
  }

  .info-itinerario {
    padding: 25px;
    position: relative;
  }

  .info-itinerario h3 {
    font-size: 1.4rem;
    margin-bottom: 15px;
  }

  .info-itinerario li {
    font-size: 15px;
    margin-bottom: 6px;
  }

  .mensaje {
    font-size: 14px;
    margin-bottom: 40px;
  }

  .btn-ver {
    padding: 7px 16px;
    font-size: 14px;
    width: auto;
    position: absolute;
    bottom: 20px;
    right: 20px;
  }
}

/* Móviles pequeños (max 480px) */
@media (max-width: 480px) {
  .contenedor-cards {
    padding: 15px 10px;
    padding-bottom: 70px;
    gap: 15px;
  }

  .card-itinerario {
    flex-direction: column;
    max-width: 100%;
  }

  .imagen-itinerario {
    width: 100%;
    height: 180px;
    min-height: unset;
  }

  .info-itinerario {
    padding: 12px;
  }

  .info-itinerario h3 {
    font-size: 1rem;
    margin-bottom: 8px;
  }

  .info-itinerario li {
    font-size: 13px;
    margin-bottom: 3px;
  }

  .mensaje {
    font-size: 12px;
    margin-bottom: 10px;
  }

  .btn-ver {
    padding: 8px 14px;
    font-size: 13px;
    width: 100%;
  }
}
</style>