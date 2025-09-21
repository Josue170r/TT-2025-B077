import HomeView from "../views/home_view.vue";
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
        path: '/place/description',
        name: 'description',
        meta: {
            title: 'Descripción',
            rule: 'auth',
        },
        component: SiteDescription,
    },
]