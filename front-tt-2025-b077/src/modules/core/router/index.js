
import HomeView from "../views/home.vue";

export default [{
        path: '/core/home',
        name: 'home',
        meta: {
            title: 'Inicio',
            rule: 'public',
        },
        component: HomeView,
    },
]