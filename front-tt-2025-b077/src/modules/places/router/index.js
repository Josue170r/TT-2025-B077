import SiteDescription from "../views/site_description.vue";

export default [{
        path: '/place/site_description',
        name: 'site_description',
        meta: {
            title: 'Descripcion del lugar',
            rule: 'sites',
        },
        component: SiteDescription,
    },
]