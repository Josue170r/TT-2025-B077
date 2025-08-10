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
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

export default router
