import { createRouter, createWebHistory } from 'vue-router'
import FrontPage from '@/components/front-page.vue'
import authRoutes from '@/modules/auth/router/index'
import coreRoutes from '@/modules/core/router/index'
import tripsRoutes from '@/modules/trips/router/index'
import userRoutes from '@/modules/user/router/index'


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

export default router
