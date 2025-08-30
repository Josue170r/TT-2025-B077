import Favorites from "../views/favorites.vue";
import Itinerary from "../views/itinerary.vue";
import Mytrips from "../views/mytrips.vue";


export default [
    {
        path: '/trips/favorites',
        name: 'favorites',
        meta: {
            title: 'Favoritos',
            rule: 'public',
        },
        component: Favorites,
    },
    {
        path: '/trips/itinerary',
        name: 'itinerary',
        meta: {
            title: 'Mis itinerarios',
            rule: 'public',
        },
        component: Itinerary,
    },

    {
        path: '/trips/my-trips',
        name: 'my-trips',
        meta: {
            title: 'Mis viajes',
            rule: 'public',
        },
        component: Mytrips,
    },
]