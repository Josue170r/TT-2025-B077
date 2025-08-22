import LoginView from "../views/login.vue";
import RegistrationView from "../views/registration.vue";
import ForgotPasswordView from "../views/forgot-password.vue";
import RecoverPasswordView from "../views/recover-password.vue";

export default [
    {
        path: '/auth/login',
        name: 'authLogin',
        meta: {
            title: 'Iniciar Sesión',
            rule: 'public',
        },
        component: LoginView,
    },
    {
        path: '/auth/signup',
        name: 'authRegister',
        meta: {
            title: 'Crear cuenta',
            rule: 'public',
        },
        component: RegistrationView,
    },
    {
        path: '/auth/forgot-password',
        name: 'authForgotPassword',
        meta: {
            title: 'Recuperar contraseña',
            rule: 'public',
        },
        component: ForgotPasswordView,
    },
    {
        path: '/password_reset',
        name: 'passwordReset',
        meta: {
            title: 'Coloque sus nueva contraseña',
            rule: 'public',
        },
        component: RecoverPasswordView,
    },
]