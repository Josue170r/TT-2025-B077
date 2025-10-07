import Favorites from '../views/favorites.vue'
import Itinerary from '../views/itinerary.vue'
import Mytrips from '../views/mytrips.vue'
import Newtrip from '../views/newtrip.vue'
import Hotels from '../../trips/views/hotelsview.vue'

export default [
  {
    path: '/trips/favorites',
    name: 'favorites',
    meta: {
      title: 'Favoritos',
      rule: 'auth',
    },
    component: Favorites,
  },
  {
    path: '/trips/itinerary',
    name: 'itinerary',
    meta: {
      title: 'Mis itinerarios',
      rule: 'auth',
    },
    component: Itinerary,
  },

  {
    path: '/trips/my-trips',
    name: 'my-trips',
    meta: {
      title: 'Mis viajes',
      rule: 'auth',
    },
    component: Mytrips,
  },
  {
    path: '/trips/newtrip',
    name: 'new-trips',
    meta: {
      title: 'Nuevo viaje',
      rule: 'auth',
    },
    component: Newtrip,
  },
  {
    path: '/place/hotels',
    name: 'hotels',
    meta: {
      title: 'Hoteles',
      rule: 'auth',
    },
    component: Hotels,
  },
]
