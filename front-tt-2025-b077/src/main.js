
import { createApp } from 'vue'
import { library } from '@fortawesome/fontawesome-svg-core'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { faCircleCheck } from '@fortawesome/free-solid-svg-icons'

import App from './App.vue'
import router from './router'

import './plugins'
import store from './store'
import { vuetify, AlertPlugin } from './plugins'

const app = createApp(App)
library.add(faCircleCheck)
app.component('FontAwesomeIcon', FontAwesomeIcon)

app.use(vuetify)
app.use(AlertPlugin)

app.use(router)
app.use(store)

app.mount('#app')
