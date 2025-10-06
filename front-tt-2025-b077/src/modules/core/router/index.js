import HomeView from "../views/home_view.vue";
import Hotels from "../views/hotels.vue";
import Interactive_map from "../views/interactive_map.vue";
import SiteDescription from "../views/site_description.vue";

export default [
    {
        path: '/core/home',
        name: 'home',
        meta: {
            title: 'Inicio',
            rule: 'auth',
        },
        component: HomeView,
    },
    {
        path: '/place/site_description',
        name: 'site_description',
        meta: {
            title: 'Descripcion del lugar',
            rule: 'sites',
        },
        component: SiteDescription,
    },
    {
        path: '/place/map',
        name: 'interactive_map',
        meta: {
            title: 'Mapa interactivo',
            rule: 'sites',
        },
        component: Interactive_map,
    },
    {
        path: '/place/hotels',
        name: 'hotels',
        meta: {
            title: 'Hoteles',
            rule: 'sites',
        },
        component: Hotels,
    },
]