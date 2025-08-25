<template>
  <nav class="bottom-navbar navbar fixed-bottom bg-white border-top shadow-sm">
    <ul class="nav w-100 justify-content-around">
      <li class="nav-item">
        <router-link to="/inicio" class="nav-link" :class="{ active: isActive('/inicio') }">
          <i class="fa-solid fa-magnifying-glass icon icon-large"></i>
          <span>Buscar</span>
        </router-link>
      </li>
      <li class="nav-item">
        <router-link to="/favoritos" class="nav-link" :class="{ active: isActive('/favoritos') }">
          <i class="fa-regular fa-heart icon icon-large"></i>
          <span>Favoritos</span>
        </router-link>
      </li>
      <li class="nav-item">
        <router-link to="/generaritinerario" class="nav-link" :class="{ active: isActive('/generaritinerario') }">
            <img src="/iconogenerar.ico" alt="Itinerario" class="icon-img icon-large" />
            <span>Generar</span>
        </router-link>
      </li>
      <li class="nav-item">
        <router-link to="/misviajes" class="nav-link" :class="{ active: isActive('/misviajes') }">
          <i class="fa-regular fa-map icon icon-large"></i>
          <span>Mis viajes</span>
        </router-link>
      </li>
      <li class="nav-item">
        <router-link to="/micuenta" class="nav-link" :class="{ active: isActive('/micuenta') }">
          <i class="fa-regular fa-circle-user icon icon-large"></i>
          <span>Mi cuenta</span>
        </router-link>
      </li>
    </ul>
  </nav>
</template>

<script>
import { useRoute } from "vue-router";

export default {
  name: "BottomNavbar",
  setup() {
    const route = useRoute();
    const isActive = (path) => route.path === path;
    return { isActive };
  },
};
</script>

<style scoped>
.bottom-navbar {
  display: flex;
  justify-content: center;
  align-items: center;
  transition: all 0.3s ease;
  background-color: #fff;
}

/* Links */
.bottom-navbar .nav-link {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  font-size: 11px;
  color: #1B515E;
  transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  padding: 8px;
  border-radius: 12px;
  transform: scale(1);
  position: relative;
  overflow: hidden;
}

.bottom-navbar .nav-link .icon {
  font-size: 18px;
  margin-bottom: 3px;
  transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
}

.bottom-navbar .nav-link .icon-large {
  font-size: 22px; /* base */
  margin-bottom: 3px;
  transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
}

.bottom-navbar .icon-img {
  width: 50px;
  height: 35px;
  margin-bottom: 3px;
  transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  filter: brightness(1);
}

.bottom-navbar .nav-link span {
  transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
}

.bottom-navbar .nav-link:hover {
  color: #ABCD9E;
  transform: scale(1.05);
  background-color: rgba(171, 205, 158, 0.1);
  transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
}

.bottom-navbar .nav-link:hover .icon,
.bottom-navbar .nav-link:hover .icon-large {
  transform: translateY(-2px);
}

.bottom-navbar .nav-link:hover .icon-img {
  transform: translateY(-2px) scale(1.05);
  filter: brightness(1.1);
}

.bottom-navbar .nav-link.active {
  color: #ABCD9E;
  font-weight: 600;
  transform: scale(1.05);
  background-color: rgba(171, 205, 158, 0.15);
  transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
}

.bottom-navbar .nav-link.active .icon,
.bottom-navbar .nav-link.active .icon-large {
  transform: translateY(-1px);
}

.bottom-navbar .nav-link.active .icon-img {
  transform: translateY(-1px) scale(1.02);
  filter: brightness(1.05);
}

.bottom-navbar .nav-link:active {
  transform: scale(0.98);
  transition: transform 0.1s ease;
}

/* Pantallas pequeñas */
@media (max-width: 767px) {
  .bottom-navbar {
    height: 75px;
  }
  .bottom-navbar .icon-img {
    width: 55px;
    height: 40px;
  }
  .bottom-navbar .nav-link .icon {
    font-size: 20px;
  }
  .bottom-navbar .nav-link .icon-large {
    font-size: 24px;
  }
}

/* Pantallas grandes y medianas*/
@media (min-width: 800px) {
  .bottom-navbar {
    margin-top: 50px;
    width: 50%;
    height: 90px;
    margin: 0 auto 20px auto;
    border-radius: 25px;
    box-shadow: 0 4px 15px rgba(168, 37, 37, 0.2);
    border: none;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1), box-shadow 0.3s ease;
  }
  
  .bottom-navbar:hover {
    box-shadow: 0 6px 20px rgba(168, 37, 37, 0.25);
    transform: translateY(-1px);
  }
  
  .bottom-navbar .icon-img {
    width: 70px;
    height: 50px;
  }
  .bottom-navbar .nav-link .icon {
    font-size: 30px;
  }
  .bottom-navbar .nav-link .icon-large {
    font-size: 34px;
  }
}
</style>