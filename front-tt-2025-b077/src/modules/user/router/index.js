import Preferences from '../views/preferences.vue'
import UserProfile from '../views/userprofile.vue'
import Deleteaccount from '../views/deleteaccount.vue'
import ChangePassword from '../views/changepassword.vue'
import CarbonFootPrint from '../views/CarbonFootPrint.vue'

export default [
  {
    path: '/user/preferences',
    name: 'preferences',
    meta: {
      title: 'Preferencias',
      rule: 'auth',
    },
    component: Preferences,
  },
  {
    path: '/user/my-profile',
    name: 'myaccount',
    meta: {
      title: 'Mi cuenta',
      rule: 'auth',
    },
    component: UserProfile,
  },

  {
    path: '/user/delete-account',
    name: 'deleteaccount',
    meta: {
      title: 'Borrar cuenta',
      rule: 'auth',
    },
    component: Deleteaccount,
  },
  {
    path: '/user/change-password',
    name: 'changepassword',
    meta: {
      title: 'Cambia tu contraseña',
      rule: 'auth',
    },
    component: ChangePassword,
  },
  {
    path: '/user/carbon-foot-print',
    name: 'carbonfootprint',
    meta: {
      title: 'Tu huella de carbono',
      rule: 'auth',
    },
    component: CarbonFootPrint,
  },
]
