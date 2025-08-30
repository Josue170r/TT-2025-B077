import Preferences from "../views/preferences.vue";
import UserProfile from "../views/userprofile.vue";
import Deleteaccount from "../views/deleteaccount.vue";

export default [
    {
        path: '/user/preferences',
        name: 'preferences',
        meta: {
            title: 'Preferencias',
            rule: 'public',
        },
        component: Preferences,
    },
    {
        path: '/user/my-profile',
        name: 'myaccount',
        meta: {
            title: 'Micuenta',
            rule: 'public',
        },
        component: UserProfile,
    },
    
    {
        path: '/user/delete-account',
        name: 'deleteaccount',
        meta: {
            title: 'Borrar cuenta😭',
            rule: 'public',
        },
        component: Deleteaccount,
    },
]