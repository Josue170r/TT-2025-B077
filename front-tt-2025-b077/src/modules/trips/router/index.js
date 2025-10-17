import Favorites from '../views/favorites.vue'
import MyItineraries from '../views/my_itineraries.vue'
import Newtrip from '../views/newtrip.vue'
import Hotels from '../../trips/views/hotelsview.vue'
import Description_itinerary from '../views/description_itinerary.vue'

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
    component: MyItineraries,
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
  {
    path: '/place/description_itinerary',
    name: 'description-itinerary',
    meta: {
      title: 'Descripción de itinerario',
      rule: 'auth',
    },
    component: Description_itinerary,
  },
]
