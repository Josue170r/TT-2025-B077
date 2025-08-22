import { createRouter, createWebHistory } from 'vue-router'
import FrontPage from '@/components/front-page.vue'
import authRoutes from '@/modules/auth/router/index'


let baseRoutes = [
  {
    path: '/',
    name: 'index',
    component: FrontPage,
  },
]

const routes = baseRoutes.concat(
  authRoutes
)

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

export default router
