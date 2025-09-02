import { createRouter, createWebHistory } from 'vue-router'
import FrontPage from '@/components/front-page.vue'
import authRoutes from '@/modules/auth/router/index'
import coreRoutes from '@/modules/core/router/index'
import tripsRoutes from '@/modules/trips/router/index'
import userRoutes from '@/modules/user/router/index'
import store from '@/store'


let baseRoutes = [
  {
    path: '/',
    name: 'index',
    component: FrontPage,
  },
]

const routes = baseRoutes.concat(
  authRoutes, coreRoutes, tripsRoutes, userRoutes
)

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})


router.beforeEach((to, from, next) => {
  document.title = to.meta.title || 'Huellas por México'

  const isLogged = store.getters['auth/isLogged']
  const isPublic = to.matched.some(r => r.meta?.rule === 'public')

  if (!isLogged && !isPublic) {
    return next({ name: 'authLogin' })
  }

  if (isLogged && (to.name === 'authLogin' || to.name === 'authRegister')) {
    return next({ name: 'home' })
  }

  next()
})

export default router
