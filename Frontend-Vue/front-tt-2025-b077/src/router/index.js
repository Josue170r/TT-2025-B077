import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    component: () => import('@/views/portada.vue'),
  },
  {
    path: '/login',
    component: () => import('@/views/login.vue')
  },
  {
    path: '/crearcuenta',
    component: () => import('@/views/registration.vue')
  },
  {
    path: '/re-contrasena',
    component: () => import('@/views/reset-password.vue')
  },
  {
    path: '/preferencias',
    component: () => import('@/views/preferences.vue')
  },
  {
    path: '/inicio',
    component: () => import('@/views/home.vue')
  },
  {
    path: '/favoritos',
    component: () => import('@/views/favorites.vue')
  },
  {
    path: '/generaritinerario',
    component: () => import('@/views/itinerary.vue')
  },
  {
    path: '/misviajes',
    component: () => import('@/views/mytrips.vue')
  },
  {
    path: '/micuenta',
    component: () => import('@/views/acount.vue')
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

export default router
