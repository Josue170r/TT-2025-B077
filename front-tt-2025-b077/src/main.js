import { createApp } from 'vue'
import { library } from '@fortawesome/fontawesome-svg-core'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'

// Solid
import { fas } from '@fortawesome/free-solid-svg-icons'
//Regular
import { far } from '@fortawesome/free-regular-svg-icons'

import App from './App.vue'
import router from './router'

import './plugins'
import store from './store'
import { vuetify, AlertPlugin } from './plugins'

const app = createApp(App)

app.use(vuetify)
app.use(AlertPlugin)

app.use(router)
app.use(store)

app.mount('#app')
